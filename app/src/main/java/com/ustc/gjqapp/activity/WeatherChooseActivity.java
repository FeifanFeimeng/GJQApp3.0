package com.ustc.gjqapp.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.ustc.gjqapp.R;

public class WeatherChooseActivity extends Activity {
    private EditText searchText;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_weathersearch);
        searchButton = (Button) findViewById(R.id.searchBtn);
        searchText = (EditText) findViewById(R.id.editWeather);
        searchButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                String citynameString = searchText.getText().toString().trim();
                Intent intent = new Intent(WeatherChooseActivity.this, WeatherActivity.class);
                intent.putExtra("cityname", citynameString);
                setResult(RESULT_OK,intent);
                //startActivity(intent);
                finish();
            }
        });

    }

}
