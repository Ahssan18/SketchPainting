package com.example.sketchpainting.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sketchpainting.BuildConfig;
import com.example.sketchpainting.R;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView iv_touchfill,iv_paintraise,iv_finderpaint;
    AdView adView;
    private InterstitialAd mInterstitialAd;
    private Button btn_rate,btn_share;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void listener() {
        iv_paintraise.setOnClickListener(this);
        iv_touchfill.setOnClickListener(this);
        iv_finderpaint.setOnClickListener(this);
        btn_share.setOnClickListener(this);
        btn_rate.setOnClickListener(this);

        mInterstitialAd.setAdListener(new AdListener()
        {
            @Override
            public void onAdClosed() {
                super.onAdClosed();
                finish();
            }


        });
    }

    private void init() {
        adView=findViewById(R.id.adView);
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.Interstial_Ad_Unit_Id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        iv_paintraise=findViewById(R.id.iv_paintrise);
        iv_finderpaint=findViewById(R.id.iv_fingerpaint);
        iv_touchfill=findViewById(R.id.iv_touchfill);
        btn_rate=findViewById(R.id.btn_rate);
        btn_share=findViewById(R.id.btn_share);
    }


    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id)
        {
            case R.id.iv_paintrise:
                Intent paintraise=new Intent(getApplicationContext(),SketchesDataActivity.class);
                paintraise.putExtra("refrence","paintraise");
                startActivity(paintraise);
                break;
                case R.id.iv_touchfill:
                Intent touchfill=new Intent(getApplicationContext(),SketchesDataActivity.class);
                touchfill.putExtra("refrence","touchfill");
                startActivity(touchfill);
                break;
                case R.id.iv_fingerpaint:
                Intent fingerpaint=new Intent(getApplicationContext(),FingerDrawingActivity.class);
                fingerpaint.putExtra("type","fingerpaint");
                startActivity(fingerpaint);
                break;
                case R.id.btn_rate:
                    Uri uri = Uri.parse("market://details?id=" + getPackageName());
                    Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
                    try {
                        startActivity(myAppLinkToMarket);
                    } catch (ActivityNotFoundException e) {
                        Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
                    }
                break;
                case R.id.btn_share:
                    try {
                        Intent shareIntent = new Intent(Intent.ACTION_SEND);
                        shareIntent.setType("text/plain");
                        shareIntent.putExtra(Intent.EXTRA_SUBJECT,R.string.app_name);
                        String shareMessage= "\nLet me recommend you this application\n\n";
                        shareMessage = shareMessage + "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID +"\n\n";
                        shareIntent.putExtra(Intent.EXTRA_TEXT, shareMessage);
                        startActivity(Intent.createChooser(shareIntent, "choose one"));
                    } catch(Exception e) {
                        //e.toString();
                    }
                    break;
        }
    }


    @Override
    public void onBackPressed() {
        if(mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }
        finish();
    }
}
