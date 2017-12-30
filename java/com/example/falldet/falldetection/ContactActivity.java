package com.example.damera.falldetection;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by SJADHDAV on 04-19-2017.
 */

public class ContactActivity extends Activity {
    DatabaseHelper DB;
    EditText editName, editPhoneno;
    Button btnAddData;
    Button btnretrievedata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        DB = new DatabaseHelper(this);
        editName = (EditText) findViewById(R.id.editText_name);
        editPhoneno = (EditText) findViewById(R.id.editText_phoneno);
        btnAddData = (Button) findViewById(R.id.button_add);
        btnretrievedata = (Button) findViewById(R.id.button6);
        Log.d("Contact Activity", "Activity Created");
        InsertData();
        Retrievedata();
    }

    public void InsertData() {
        btnAddData.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        boolean isInserted = DB.insertData(editName.getText().toString(),
                                editPhoneno.getText().toString());
                        if (isInserted == true)
                            Toast.makeText(ContactActivity.this, "Contact Insertion Successful!!", Toast.LENGTH_LONG).show();
                        else
                            Toast.makeText(ContactActivity.this, "Adding Contact Failed", Toast.LENGTH_LONG).show();
                    }
                }
        );
    }

    public void Retrievedata() {

        btnretrievedata.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Cursor cursor1 = DB.SelectData();
                        if (cursor1.getCount() == 0) {
                            Toast.makeText(getApplicationContext(),"There Is No Data To Be Fetched", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        StringBuffer retrieve = new StringBuffer();
                        while (cursor1.moveToNext()) {
                            retrieve.append("Id :" + cursor1.getString(0) + "\n");
                            retrieve.append("Name :" + cursor1.getString(1) + "\n");
                            retrieve.append("Phone Number :" + cursor1.getString(2) + "\n\n");
                        }
                        Toast.makeText(ContactActivity.this, "\nCONTACTS IN DATABASE\n" + retrieve.toString(), Toast.LENGTH_LONG).show();

                    }
                }
        );
    }


    public void gotocActivity(View v) {
        Intent intent = new Intent(this, AccActivity.class);
        startActivity(intent);

    }

}
