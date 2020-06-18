package com.USRT.tiffinmanagementsystem.Activity.Vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.USRT.tiffinmanagementsystem.R;

import java.util.HashMap;

public class AddDeliveryBoy extends AppCompatActivity {

    AutoCompleteTextView edit_name,edit_address,edit_city,edit_email,edit_mobile,edit_password;
    Button btn_add_cust;
    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    private DatabaseReference myRef;

    String email,mobile,city,name,address,vendor_id,pass;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_delivery_boy);

        initToolbar();

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait..");

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        vendor_id = sharedPreferences.getString("vendor_id", null);

        edit_name=findViewById(R.id.edit_cust_name);
        edit_address=findViewById(R.id.edit_cust_address);
        edit_city=findViewById(R.id.edit_cust_city);
        edit_email=findViewById(R.id.edit_cust_email);
        edit_mobile=findViewById(R.id.edit_cust_mobile);
        edit_password=findViewById(R.id.edit_password);
        btn_add_cust=findViewById(R.id.btn_add_cust);

        btn_add_cust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                email = edit_email.getText().toString();
                mobile = edit_mobile.getText().toString();
                name = edit_name.getText().toString();
                address = edit_address.getText().toString();
                city = edit_city.getText().toString();
               pass= edit_password.getText().toString();

                if (TextUtils.isEmpty(mobile)) {
                    edit_mobile.setError("Cannot be empty.");
                    edit_mobile.requestFocus();
                }
                if (TextUtils.isEmpty(city)) {
                    edit_city.setError("Cannot be empty.");
                    edit_city.requestFocus();
                }

                if (TextUtils.isEmpty(city)) {
                    edit_city.setError("Cannot be empty.");
                    edit_city.requestFocus();
                }
                if (TextUtils.isEmpty(pass)) {
                    edit_password.setError("Cannot be empty.");
                    edit_password.requestFocus();
                }
                if (TextUtils.isEmpty(address)) {
                    edit_address.setError("Cannot be empty.");
                    edit_address.requestFocus();
                } else if (mobile.length() < 10) {
                    edit_mobile.setError("Mobile should atleast have 10 digits.");
                    //  Toast.makeText(AddBus.this, "Password should atleast have 6 digits or characters.", Toast.LENGTH_SHORT).show();
                    edit_mobile.requestFocus();
                }  else {

                    mProgressDialog.show();
                    myRef = FirebaseDatabase.getInstance().getReference();
                    String newid = myRef.push().getKey();
                    HashMap<String, Object> UserValue = new HashMap<>();
                    //bus_number,busname,start_point,start_time,last_point,last_time,amount_km,route,conductor_mobile,password;
                    UserValue.put("boyid", newid);
                    UserValue.put("vendor_id", vendor_id);
                    UserValue.put("name", name);
                    UserValue.put("address", address);
                    UserValue.put("city", city);
                    UserValue.put("mobile", mobile);
                    UserValue.put("email", email);
                    UserValue.put("password", pass);

                    myRef.child("DeliveryBoy").child(newid).setValue(UserValue);
                    startActivity(new Intent(AddDeliveryBoy.this, DeliveryBoyList.class));
                    finish();
                    Toast.makeText(AddDeliveryBoy.this, "Success", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                }
            }
        });

    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Delivery Boy");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Tools.setSystemBarColor(this, R.color.colorPrimary);
    }
    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddDeliveryBoy.this,VendorDashboard.class));
        finish();
    }
}
