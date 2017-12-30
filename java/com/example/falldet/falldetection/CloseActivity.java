package com.example.damera.falldetection;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by DAMERA on 05-05-2017.
 */

public class CloseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close);
        Log.d("Close Activity", "Close Activity Created");
    }

}
