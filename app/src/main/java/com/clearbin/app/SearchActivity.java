package com.clearbin.app;

import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Typeface customFont = Typeface.createFromAsset(getAssets(),"fonts/Oswald-Regular.ttf");

        EditText editSearch = (EditText) findViewById(R.id.editItemName);
        editSearch.setTypeface(customFont);
        editSearch.setBackgroundResource(R.drawable.button_border);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        this.finish();
    }
}