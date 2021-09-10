package com.clearbin.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kaopiz.kprogresshud.KProgressHUD;

import java.util.Random;

public class ResultActivity extends AppCompatActivity {

    public static final String IMAGE_PATH_PARAM = "IMAGE_PATH_PARAM";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String imagePath = getIntent().getStringExtra(IMAGE_PATH_PARAM);
        if (!TextUtils.isEmpty(imagePath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);
        }

        KProgressHUD hud = KProgressHUD.create(ResultActivity.this)
            .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
            .setLabel("Please wait...")
            .setCancellable(true)
            .setAnimationSpeed(1)
            .setDimAmount(0.3f)
            .show();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        hud.dismiss();

                        // Mock
                        Random r = new Random();
                        if (r.nextInt(10) < 8) {
                            String[] mats = {"Plastic Bottle", "Wood", "Paper Cup", "Steel Can"};

                            ResultFragment fragment = ResultFragment.newInstance(mats[r.nextInt(mats.length)], r.nextBoolean());
                            fragment.show(getSupportFragmentManager(), "result_dialog_fragment");
                        } else {
                            NoResultFragment fragment = NoResultFragment.newInstance();
                            fragment.show(getSupportFragmentManager(), "no_result_dialog_fragment");
                        }
                    }
                }, 4000);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        //No call for super(). Bug on API Level > 11
    }
}