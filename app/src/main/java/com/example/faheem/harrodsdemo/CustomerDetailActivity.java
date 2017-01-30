package com.example.faheem.harrodsdemo;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class CustomerDetailActivity extends AppCompatActivity {

    private EditText customerId;
    private EditText latitude;
    private EditText longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.customer_detail);

        customerId = (EditText)findViewById(R.id.customerID);
        latitude = (EditText)findViewById(R.id.latitudeID);
        longitude = (EditText)findViewById(R.id.longitudeid);

    }

    public void saveDetails(View view){
        SharedPreferences sharedPreferences = getSharedPreferences("customerDetail.txt",MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString("customerId",customerId.getText().toString());
        edit.putString("latitude",latitude.getText().toString());
        edit.putString("longitude",longitude.getText().toString());
        edit.apply();
        edit.commit();
        finish();
    }


}
