package com.clearbin.app;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.camerakit.CameraKit;
import com.camerakit.CameraKitView;
import com.camerakit.type.CameraFlash;

import java.io.File;
import java.io.FileOutputStream;

public class MainActivity extends AppCompatActivity {

    private CameraKitView cameraView;
    private ImageView flashView;
    private int flashMode = CameraKit.FLASH_OFF;
    private boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cameraView = (CameraKitView) findViewById(R.id.cameraView);

        Typeface customFont = Typeface.createFromAsset(getAssets(),"fonts/Oswald-Regular.ttf");

        ((TextView) findViewById(R.id.titleText)).setTypeface(customFont);
        ((TextView) findViewById(R.id.hintText)).setTypeface(customFont);

        ((ImageView) findViewById(R.id.shutter)).setOnClickListener(view -> cameraView.captureImage((cameraKitView, capturedImage) -> {
            Log.d("CAPTURE", "TTTTTTTTTT");
            // Play shutter audio
            MediaActionSound mSound = new MediaActionSound();
            mSound.playWithStreamVolume(MediaActionSound.SHUTTER_CLICK, (Context) MainActivity.this, AudioManager.STREAM_MUSIC, 0.5f);

            // Go to Result activity with captured data
            gotoResult(capturedImage);
        }));

        flashView = (ImageView) findViewById(R.id.flash);
        flashView.setImageResource(R.drawable.flash_off);
        cameraView.setFlash(flashMode);

        flashView.setOnClickListener(view -> {
            toggleFlash();
        });
    }

    private void toggleFlash() {
        if (flashMode == CameraKit.FLASH_OFF) {
            flashMode = CameraKit.FLASH_ON;
            flashView.setImageResource(R.drawable.flash_on);
        } else if (flashMode == CameraKit.FLASH_ON) {
            flashMode = CameraKit.FLASH_OFF;
            flashView.setImageResource(R.drawable.flash_off);
        }

        cameraView.setFlash(flashMode);
    }

    private String saveTempImage(byte[] capturedImage) {

        File savedPhoto = new File(getCacheDir(), "photo.jpg");
        try {
            FileOutputStream outputStream = new FileOutputStream(savedPhoto.getPath());
            outputStream.write(capturedImage);
            outputStream.close();

            return savedPhoto.getAbsolutePath();
        } catch (java.io.IOException e) {
            e.printStackTrace();

            return "";
        }
    }

    private void gotoResult(byte[] capturedImage) {
        String imagePath = saveTempImage(capturedImage);

        Intent intent = new Intent(MainActivity.this, ResultActivity.class);
        intent.putExtra(ResultActivity.IMAGE_PATH_PARAM, imagePath);
        startActivity(intent);
    }

    @Override
    protected void onStart() {
        super.onStart();
        cameraView.onStart();
    }

    @Override
    protected void onStop() {
        cameraView.onStop();
        super.onStop();
    }

    @Override
    protected void onPause() {
        cameraView.onPause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        cameraView.onResume();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        cameraView.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}