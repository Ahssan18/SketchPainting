package com.example.sketchpainting.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.os.Handler;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.sketchpainting.R;

public class SplashActivity extends AppCompatActivity {

    TextView tvTitle;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_splash);
        init();
        gradientText();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent main=new Intent(getApplicationContext(),MainActivity.class);
                startActivity(main);
            }
        },1000);

    }

    private void gradientText() {
        Shader shader = new LinearGradient(0,0,0,tvTitle.getLineHeight(),getResources().getColor(R.color.colorYellow), getResources().getColor(R.color.colorRed), Shader.TileMode.REPEAT);
        tvTitle.getPaint().setShader(shader);
    }

    private void init() {
        tvTitle=findViewById(R.id.tvTitle);
    }
}
