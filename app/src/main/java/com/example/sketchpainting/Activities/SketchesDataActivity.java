package com.example.sketchpainting.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.sketchpainting.Adapters.AdapterSketches;
import com.example.sketchpainting.Models.ModelSketch;
import com.example.sketchpainting.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;

import java.util.ArrayList;
import java.util.List;

import static android.content.pm.ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;

public class SketchesDataActivity extends AppCompatActivity implements View.OnClickListener {

    public static List<ModelSketch> sketchList;
    private RecyclerView recyclerViewSketch;
    private ImageView iv_back;
    private InterstitialAd mInterstitialAd;
    private AdView adView;
    public static String ref=null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_sketches_data);
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        try {
            ref=getIntent().getExtras().getString("refrence");
            Log.d("reference_",ref);
        } catch (Exception e) {
            e.printStackTrace();
        }

        init();
        setData();
        listener();
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);
    }

    private void listener() {
        iv_back.setOnClickListener(this);
    }

    private void init() {
        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.Interstial_Ad_Unit_Id));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());
        adView=findViewById(R.id.adView);
        recyclerViewSketch = findViewById(R.id.recycle_sketches);
        iv_back=findViewById(R.id.iv_back);
        sketchList = new ArrayList<>();
    }

    private void setData() {

        int[] img = {
                R.drawable.apple,
                R.drawable.banana, R.drawable.cherry, R.drawable.grapes, R.drawable.lemon, R.drawable.mango,
                R.drawable.orange, R.drawable.strawberry, R.drawable.lemon_slice,
                R.drawable.bird, R.drawable.giraffe, R.drawable.elephant, R.drawable.tortoise,
                R.drawable.whale_fish, R.drawable.pig, R.drawable.dog, R.drawable.goat, R.drawable.hen,
                R.drawable.owl, R.drawable.sea_horse, R.drawable.spider, R.drawable.tiger, R.drawable.doll_1,
                R.drawable.doll_2, R.drawable.doll_3,
                R.drawable.doll_4, R.drawable.doll_5,
                R.drawable.doll_6
        };
        for (int value : img) {
            ModelSketch modelSketch = new ModelSketch(value);
            sketchList.add(modelSketch);
        }
        boolean tabletSize = getResources().getBoolean(R.bool.isTab);
        if (tabletSize) {
            this.setRequestedOrientation(SCREEN_ORIENTATION_LANDSCAPE);
            recyclerViewSketch.setLayoutManager(new GridLayoutManager(this, 4));
        } else {
            // do something else
            recyclerViewSketch.setLayoutManager(new GridLayoutManager(this, 2));
        }
        AdapterSketches adapterSketches = new AdapterSketches(this, sketchList);
        recyclerViewSketch.setAdapter(adapterSketches);
    }

    @Override
    public void onClick(View view) {
        int id=view.getId();
        switch (id)
        {
            case R.id.iv_back:
                onBackPressed();
            break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if(mInterstitialAd.isLoaded())
        {
            mInterstitialAd.show();
        }
    }
}
