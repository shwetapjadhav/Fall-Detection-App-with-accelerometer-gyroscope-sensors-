package com.example.damera.falldetection;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d("Main Activity", "Activity Created");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.menu_load) {
            Toast.makeText(getApplicationContext(), "App Loaded Successfully", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_save) {
            Toast.makeText(getApplicationContext(), "App Saved Successfully", Toast.LENGTH_SHORT).show();
        }
        if (id == R.id.menu_new) {
            Toast.makeText(getApplicationContext(), "New App Successful", Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }



    public void gotoActivity(View v) {
        Intent intent = new Intent(this, ContactActivity.class);
        startActivity(intent);

    }

}
