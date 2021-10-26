package com.clearbin.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kaopiz.kprogresshud.KProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.Random;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ResultActivity extends AppCompatActivity {

    public static final String IMAGE_PATH_PARAM = "IMAGE_PATH_PARAM";

    public static final String API_DETECT_URL = "https://clearbin-bk.herokuapp.com/detect";
    public static final String NO_DETECTED_MSG = "No object detected.";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        String imagePath = getIntent().getStringExtra(IMAGE_PATH_PARAM);
        if (!TextUtils.isEmpty(imagePath)) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);

            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(bitmap);

            KProgressHUD hud = KProgressHUD.create(ResultActivity.this)
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait...")
                    .setCancellable(true)
                    .setAnimationSpeed(1)
                    .setDimAmount(0.3f)
                    .show();

            new DetectImageTask().execute(bitmap, hud);
        }

//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        hud.dismiss();
//
//                        // Mock
//                        Random r = new Random();
//                        if (r.nextInt(10) < 8) {
//                            String[] mats = {"Plastic Bottle", "Wood", "Paper Cup", "Steel Can"};
//
//                            ResultFragment fragment = ResultFragment.newInstance(mats[r.nextInt(mats.length)], r.nextBoolean());
//                            fragment.show(getSupportFragmentManager(), "result_dialog_fragment");
//                        } else {
//                            NoResultFragment fragment = NoResultFragment.newInstance();
//                            fragment.show(getSupportFragmentManager(), "no_result_dialog_fragment");
//                        }
//                    }
//                }, 4000);
//            }
//        });
    }

    private String encode2base64(Bitmap bitmap) {
        String encoded = "data:image/jpeg;base64,";
        if (bitmap != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            encoded += Base64.encodeToString(byteArray, Base64.DEFAULT);
        }
        return encoded;
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


    private class DetectImageTask extends AsyncTask<Object,  Integer, Long> {
        @Override
        protected Long doInBackground(Object... objects) {
            Bitmap bitmap = (Bitmap) objects[0];

            KProgressHUD hud = (KProgressHUD) objects[1];

            OkHttpClient client = new OkHttpClient();
            try {
                JSONObject reqJsonObject = new JSONObject();
                reqJsonObject.put("imgb64", encode2base64(bitmap));

                RequestBody requestJsonBody = RequestBody.create(
                        reqJsonObject.toString(),
                        MediaType.parse("application/json")
                );
                Request postRequest = new Request.Builder()
                        .url(API_DETECT_URL)
                        .post(requestJsonBody)
                        .build();

                Response response = client.newCall(postRequest).execute();

                JSONObject resJsonObject = new JSONObject(response.body().string());
                Log.d("MESSAGE", resJsonObject.getString("message"));
                Log.d("PRED_TIME", resJsonObject.getString("pred_time"));
                Log.d("CONFIDENCE", resJsonObject.getString("confidence"));
                Log.d("CLUSTER", resJsonObject.getString("cluster"));
                Log.d("CLUSTER_NAME", resJsonObject.getString("cluster_name"));
                Log.d("MATERIALS", resJsonObject.getString("materials"));

                hud.dismiss();

                if (NO_DETECTED_MSG.equals(resJsonObject.getString("message"))) {
                    NoResultFragment fragment = NoResultFragment.newInstance();
                    fragment.show(getSupportFragmentManager(), "no_result_dialog_fragment");
                } else {
                    ResultFragment fragment = ResultFragment.newInstance(resJsonObject.getString("cluster_name"), true);
                    fragment.show(getSupportFragmentManager(), "result_dialog_fragment");
                }
            } catch (IOException | JSONException e) {
                e.printStackTrace();
                hud.dismiss();
            }

            return null;
        }
    }
}