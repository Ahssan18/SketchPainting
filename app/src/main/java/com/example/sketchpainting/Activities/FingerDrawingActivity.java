package com.example.sketchpainting.Activities;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.sketchpainting.Java.MyCanvas;
import com.example.sketchpainting.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

public class FingerDrawingActivity extends AppCompatActivity implements View.OnClickListener {
/*finger drawn activity*/
    MyCanvas myCanvas;
    private ImageView iv_back, iv_eraser, iv_undo, iv_redo, iv_indicator, iv_delete, iv_share, iv_download;
    private InterstitialAd mInterstitialAd;
    private SeekBar seekBar;
    private AdView adView;
    private LinearLayout linearCanvas, linearRed, linearleaf, linearPink, linearEraser,linearSkyblue,linearBlue,linearBrown,linearPurpule,linearBlack,linearMehron,
            linearOrange,linearYellow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finger_drawing);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);

        boolean tabletSize = getResources().getBoolean(R.bool.isTab);
        if (tabletSize) {
            this.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
        }
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });
        getSupportActionBar().hide();
        init();
        listener();
        linearCanvas.addView(myCanvas);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }


    private void listener() {
        iv_back.setOnClickListener(this);
        linearPink.setOnClickListener(this);
        linearRed.setOnClickListener(this);
        linearleaf.setOnClickListener(this);
        linearBlack.setOnClickListener(this);
        linearBlue.setOnClickListener(this);
        linearBrown.setOnClickListener(this);
        linearYellow.setOnClickListener(this);
        linearSkyblue.setOnClickListener(this);
        linearOrange.setOnClickListener(this);
        linearPurpule.setOnClickListener(this);
        linearMehron.setOnClickListener(this);
        linearEraser.setOnClickListener(this);
        iv_undo.setOnClickListener(this);
        iv_redo.setOnClickListener(this);
        iv_download.setOnClickListener(this);
        iv_share.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
        iv_eraser.setOnClickListener(this);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                // Toast.makeText(FingerDrawingActivity.this, ""+i, Toast.LENGTH_SHORT).show();
                MyCanvas.selectedStroke = i;
                LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(i, i);
                iv_indicator.setLayoutParams(layoutParams);

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        seekBar.setProgress(30);

    }

    private void init() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.Interstial_Ad_Unit_Id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        adView = findViewById(R.id.adView);
        myCanvas = new MyCanvas(this, "finger_drawing", -1);
        linearCanvas = findViewById(R.id.linearCanvas);
        iv_back = findViewById(R.id.iv_back);
        linearRed = findViewById(R.id.linearRed);
        iv_back = findViewById(R.id.iv_back);
        linearBlue = findViewById(R.id.linearBlue);
        linearPink = findViewById(R.id.linearPink);
        linearleaf= findViewById(R.id.linearLeaf);
        linearEraser = findViewById(R.id.linearWhite);
        linearBrown = findViewById(R.id.linearBrown);
        linearSkyblue = findViewById(R.id.linearSkyblue);
        linearBlack = findViewById(R.id.linearBlack);
        linearPurpule = findViewById(R.id.linearPurpule);
        linearMehron = findViewById(R.id.linearMehroon);
        linearOrange = findViewById(R.id.linearOrange);
        linearYellow = findViewById(R.id.linearYellow);
        iv_undo = findViewById(R.id.iv_undo);
        iv_redo = findViewById(R.id.iv_redo);
        iv_delete = findViewById(R.id.iv_delete);
        iv_eraser = findViewById(R.id.iv_eraser);
        iv_share = findViewById(R.id.iv_share);
        iv_download = findViewById(R.id.iv_download);
        seekBar = findViewById(R.id.seekbar);
        iv_indicator = findViewById(R.id.iv_indicator);
    }

    @Override
    public void onBackPressed() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
        super.onBackPressed();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.iv_back:
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("Are you sure,You wanted to Exit");
                alertDialogBuilder.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                onBackPressed();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                break;
            case R.id.iv_eraser:
                Toast.makeText(this, "Erase", Toast.LENGTH_SHORT).show();
                MyCanvas.selectedColor = Color.WHITE;
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite));
                unchooseColor();
                chooseColor(linearEraser);
                break;
            case R.id.linearWhite:
                MyCanvas.selectedColor = Color.WHITE;
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorWhite));
                unchooseColor();
                chooseColor(linearEraser);
                break;
            case R.id.linearYellow:
                MyCanvas.selectedColor = Color.YELLOW;
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorYellow));
                unchooseColor();
                chooseColor(linearYellow);
                break;
            case R.id.linearLeaf:
                MyCanvas.selectedColor = ContextCompat.getColor(this,R.color.colorleaf);
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorleaf));
                unchooseColor();
                chooseColor(linearleaf);
                break;

            case R.id.linearRed:
                MyCanvas.selectedColor = Color.RED;
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorRed));
                unchooseColor();
                chooseColor(linearRed);
                break;
            case R.id.linearOrange:
                MyCanvas.selectedColor =ContextCompat.getColor(this,R.color.colororange);
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colororange));
                unchooseColor();
                chooseColor(linearOrange);

                break;

            case R.id.linearBlack:
                MyCanvas.selectedColor = ContextCompat.getColor(this, R.color.colorblack);
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorblack));
                unchooseColor();
                chooseColor(linearBlack);
                break;

            case R.id.linearMehroon:
                MyCanvas.selectedColor = ContextCompat.getColor(this, R.color.colormehroon);
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colormehroon));
                unchooseColor();
                chooseColor(linearMehron);
                break;
            case R.id.linearBlue:
                MyCanvas.selectedColor =ContextCompat.getColor(this, R.color.colorBlue);
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorBlue));
                unchooseColor();
                chooseColor(linearBlue);
                break;
            case R.id.linearSkyblue:
                MyCanvas.selectedColor = ContextCompat.getColor(this,R.color.colorskyblue);
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorskyblue));
                unchooseColor();
                chooseColor(linearSkyblue);
                break;
            case R.id.linearPurpule:
                MyCanvas.selectedColor = ContextCompat.getColor(this,R.color.colorPurpule);
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorPurpule));
                unchooseColor();
                chooseColor(linearPurpule);
                break;
            case R.id.linearBrown:
                MyCanvas.selectedColor = ContextCompat.getColor(this,R.color.colorbrown);
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorbrown));
                unchooseColor();
                chooseColor(linearBrown);
                break;
            case R.id.linearPink:
                MyCanvas.selectedColor =ContextCompat.getColor(this, R.color.colorpink);
                iv_indicator.setColorFilter(ContextCompat.getColor(this, R.color.colorpink));
                unchooseColor();
                chooseColor(linearPink);
                break;
            case R.id.iv_undo:
                try {
                    for (int i = 0; i < 20; i++) {
                        myCanvas.onClickUndo();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_redo:
                try {
                    for (int i = 0; i < 20; i++) {
                        myCanvas.onClickRedo();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.iv_download:
                Dexter.withActivity(this).
                        withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    saveToGallary(linearCanvas);
                                    Toast.makeText(FingerDrawingActivity.this, "Downloaded", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                break;
            case R.id.iv_delete:
                AlertDialog.Builder delete = new AlertDialog.Builder(this);
                delete.setMessage("Are you sure,You wanted to Reset Drawing");
                delete.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                myCanvas.deleteAll();
                            }
                        });

                delete.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //finish();
                    }
                });

                AlertDialog alert = delete.create();
                alert.show();
                break;
            case R.id.iv_share:
                Dexter.withActivity(this).withPermissions(Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {

                                    StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                                    StrictMode.setVmPolicy(builder.build());
                                    Intent share = new Intent(Intent.ACTION_SEND);
                                    share.setType("iv_image/jpeg");
                                    share.putExtra(Intent.EXTRA_STREAM, saveToGallary(linearCanvas));
                                    startActivity(Intent.createChooser(share, "Share Image"));


                                } else {
                                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                break;
        }
    }

    private void unchooseColor() {
        linearEraser.setGravity(Gravity.CENTER);
        linearleaf.setGravity(Gravity.CENTER);
        linearPink.setGravity(Gravity.CENTER);
        linearBlack.setGravity(Gravity.CENTER);
        linearBlue.setGravity(Gravity.CENTER);
        linearBrown.setGravity(Gravity.CENTER);
        linearSkyblue.setGravity(Gravity.CENTER);
        linearYellow.setGravity(Gravity.CENTER);
        linearOrange.setGravity(Gravity.CENTER);
        linearMehron.setGravity(Gravity.CENTER);
        linearPurpule.setGravity(Gravity.CENTER);
        linearRed.setGravity(Gravity.CENTER);
    }

    private void chooseColor(LinearLayout linearLayout) {
        boolean tabletSize = getResources().getBoolean(R.bool.isTab);
        if (tabletSize) {
            linearLayout.setGravity(Gravity.TOP);
        }else
        {
            linearLayout.setGravity(Gravity.RIGHT);
        }

    }

    public Bitmap viewToBitmap(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }

    private Uri saveToGallary(View view) {
        Bitmap bitmap;
        bitmap = viewToBitmap(view);
        FileOutputStream fileOutputStream = null;
        File sdcard = Environment.getExternalStorageDirectory();
        File directory = new File(sdcard.getAbsolutePath() + "/Sketch painting");
        if (!directory.exists())
            directory.mkdir();
        //Toast.makeText(this, "Directory made", Toast.LENGTH_SHORT).show();
        String filename = String.format("%d.jpg", System.currentTimeMillis());
        File output = new File(directory, filename);
        try {
            fileOutputStream = new FileOutputStream(output);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(output);
            intent.setData(uri);
            this.sendBroadcast(intent);
            return uri;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

}
