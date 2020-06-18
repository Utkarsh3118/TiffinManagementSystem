package com.USRT.tiffinmanagementsystem.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.USRT.tiffinmanagementsystem.Model.Vendor;
import com.USRT.tiffinmanagementsystem.R;

public class ViewVendor extends AppCompatActivity {

    EditText edit_id,edit_name,edit_email,edit_phone,edit_address,edit_age,edit_location,edit_password;
    ImageView img_photo;
    Button bt_update;
    String vendor_id,name,mobile,email,address,age,password,location,photo;
    double latitude,longitude;

    FirebaseAuth mAuth;
    ProgressDialog mProgressDialog;
    private DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vendor);

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait..");


        edit_id=findViewById(R.id.edit_id);
        edit_name=findViewById(R.id.edit_name);
        edit_email=findViewById(R.id.edit_email);
        edit_phone=findViewById(R.id.edit_phone);
        edit_address=findViewById(R.id.edit_address);
        edit_age=findViewById(R.id.edit_age);
        edit_location=findViewById(R.id.edit_location);
        edit_password=findViewById(R.id.edit_password);

        img_photo=findViewById(R.id.img_photo);

        bt_update=findViewById(R.id.bt_update);

        Intent intent = getIntent();
        vendor_id = intent.getExtras().getString("vendor_id");
        name = intent.getExtras().getString("name");
        mobile = intent.getExtras().getString("mobile");
        email = intent.getExtras().getString("email");
        address = intent.getExtras().getString("address");
        age = intent.getExtras().getString("age");
        password = intent.getExtras().getString("password");
        location = intent.getExtras().getString("location");
        latitude = intent.getExtras().getDouble("latitude");
        longitude= intent.getExtras().getDouble("longitude");
        photo= intent.getExtras().getString("photo");

        edit_id.setText(vendor_id);
        edit_name.setText(name);
        edit_address.setText(address);
        edit_age.setText(age);
        edit_email.setText(email);
        edit_phone.setText(mobile);
        edit_location.setText(location);
        edit_password.setText(password);


        byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img_photo.setImageBitmap(decodedByte);

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (vendor_id != null) {

                    //shop_id = edit_shopid.getText().toString();
                    name = edit_name.getText().toString();
                    address = edit_address.getText().toString();
                    age = edit_age.getText().toString();
                    mobile = edit_phone.getText().toString();
                    email = edit_email.getText().toString();
                    location = edit_location.getText().toString();
                    password = edit_password.getText().toString();

                    mProgressDialog.show();

                    myRef= FirebaseDatabase.getInstance().getReference("Vendor").child("data").child(vendor_id);

                    //updating artist
                    Vendor busitem = new Vendor(vendor_id,name,mobile,email,password,address,age,location,latitude,longitude,photo);
                    myRef.setValue(busitem);
                    Toast.makeText(getApplicationContext(), "Vendor Details Updated", Toast.LENGTH_LONG).show();
                    mProgressDialog.dismiss();
                    Intent i =new Intent(ViewVendor.this, AdminDashboard.class);
                    startActivity(i);
                    finish();

                }
            }
        });



    }

    @Override
    public void onBackPressed() {
        Intent i =new Intent(ViewVendor.this, AdminDashboard.class);
        startActivity(i);
        finish();
    }

}
