package com.USRT.tiffinmanagementsystem.Activity.Admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.nguyenhoanglam.imagepicker.activity.ImagePicker;
import com.nguyenhoanglam.imagepicker.activity.ImagePickerActivity;
import com.nguyenhoanglam.imagepicker.model.Image;
import com.USRT.tiffinmanagementsystem.R;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class AddVendor extends AppCompatActivity {

    ImageView img_photo;
    AppCompatEditText edit_name,edit_phone,edit_email,edit_address,edit_password,edit_age,edit_location;
    Button btn_add_vendor;
    FloatingActionButton fab_camera;

    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    private DatabaseReference myRef;

    String name,mobile,email,address,password,age,location,dinnertime="0:0",lunchtime="0:0";

    int REQUEST_CODE_PICKER_PHOTO=112;
    Boolean isimage=false;
    String upload_image;

    String value;
    double latitude,longitude,dist;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_vendor);

        initToolbar();
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait..");

        edit_name=findViewById(R.id.edit_name);
        edit_phone=findViewById(R.id.edit_phone);
        edit_email=findViewById(R.id.edit_email);
        edit_address=findViewById(R.id.edit_address);
        edit_password=findViewById(R.id.edit_password);
        edit_age=findViewById(R.id.edit_age);
        edit_location=findViewById(R.id.edit_location);

        img_photo=findViewById(R.id.img_photo);
        btn_add_vendor=findViewById(R.id.btn_add_vendor);
        fab_camera=findViewById(R.id.fab_camera);

        final Intent intent = this.getIntent();
        value=intent.getStringExtra("value");
        String data = intent.getStringExtra("x");
        String d=intent.getStringExtra("y");
        latitude=intent.getDoubleExtra("latitude",0);
        longitude=intent.getDoubleExtra("longitude",0);
        dist = intent.getDoubleExtra("dist",0);

        dinnertime="0:0";
        lunchtime="0:0";

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        String name1 = sharedPreferences.getString("namee", null);
        String phone1 = sharedPreferences.getString("phonee", null);
        String email1 = sharedPreferences.getString("emaile", null);
        String password1 = sharedPreferences.getString("passworde", null);
        String address1 = sharedPreferences.getString("addresse", null);
        String age1 = sharedPreferences.getString("agee", null);
        edit_name.setText(name1);
        edit_phone.setText(phone1);
        edit_email.setText(email1);
        edit_address.setText(address1);
        edit_password.setText(password1);
        edit_age.setText(age1);

        String data1=(data+d);
        edit_location.setText(value);

        edit_location.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                editor = sharedPreferences.edit();
                editor.putString("namee", edit_name.getText().toString());
                editor.putString("phonee", edit_phone.getText().toString());
                editor.putString("emaile", edit_email.getText().toString());
                editor.putString("passworde", edit_password.getText().toString());
                editor.putString("addresse", edit_address.getText().toString());
                editor.putString("agee", edit_age.getText().toString());
                editor.commit();


                Intent i= new Intent(AddVendor.this, MapsActivity.class);
                startActivity(i);
            }
        });
        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImagePicker(REQUEST_CODE_PICKER_PHOTO);
            }
        });

        btn_add_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                name = edit_name.getText().toString();
                mobile = edit_phone.getText().toString();
                email = edit_email.getText().toString();
                address = edit_address.getText().toString();
                password = edit_password.getText().toString();
                age = edit_age.getText().toString();
                location = edit_location.getText().toString();


                if (TextUtils.isEmpty(email)) {
                    edit_email.setError("Cannot be empty.");
                    edit_email.requestFocus();
                }
                if (TextUtils.isEmpty(mobile)) {
                    edit_phone.setError("Cannot be empty.");
                    edit_phone.requestFocus();
                }
                if (TextUtils.isEmpty(password)) {
                    edit_password.setError("Cannot be empty.");
                    edit_password.requestFocus();
                } else if (password.length() < 6) {
                    edit_password.setError("Password should atleast have 6 digits or characters.");
                    //  Toast.makeText(AddBus.this, "Password should atleast have 6 digits or characters.", Toast.LENGTH_SHORT).show();
                    edit_password.requestFocus();
                }
                if (TextUtils.isEmpty(name)) {
                    edit_name.setError("Cannot be empty.");
                    edit_name.requestFocus();
                }
                if (TextUtils.isEmpty(age)) {
                    edit_age.setError("Cannot be empty.");
                    edit_age.requestFocus();
                }
                if (TextUtils.isEmpty(address)) {
                    edit_address.setError("Cannot be empty.");
                    edit_address.requestFocus();
                }
                if (TextUtils.isEmpty(location)) {
                    edit_location.setError("Cannot be empty.");
                    edit_location.requestFocus();
                }
                if (upload_image != null && !upload_image.isEmpty() && !upload_image.equals("null")) {


                    mProgressDialog.show();
                    myRef = FirebaseDatabase.getInstance().getReference();
                    String newid = myRef.push().getKey();
                    HashMap<String, Object> UserValue = new HashMap<>();
                    //bus_number,busname,start_point,start_time,last_point,last_time,amount_km,route,conductor_mobile,password;
                    UserValue.put("vendor_id", newid);
                    UserValue.put("vname", name);
                    UserValue.put("vphone", mobile);
                    UserValue.put("vemail", email);
                    UserValue.put("vpassword", password);
                    UserValue.put("vaddress", address);
                    UserValue.put("vage", age);
                    UserValue.put("location", location);
                    UserValue.put("latitude", latitude);
                    UserValue.put("longitude", longitude);
                    UserValue.put("photo", upload_image);
                    UserValue.put("lunchtime", lunchtime);
                    UserValue.put("dinnertime", dinnertime);

                    myRef.child("Vendor").child("data").child(newid).setValue(UserValue);
                    startActivity(new Intent(AddVendor.this, AdminDashboard.class));
                    finish();

                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.remove("namee");
                    editor.remove("phonee");
                    editor.remove("emaile");
                    editor.remove("passworde");
                    editor.remove("addresse");
                    editor.remove("agee");
                    editor.commit();


                    Toast.makeText(AddVendor.this, "Success", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();

                } else {
                    Toast.makeText(getApplicationContext(), "Please Select Image !!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Vendor");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        } else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }



    public void launchImagePicker(int REQUEST_CODE_PICKER){


        try {
            ImagePicker.create(this)
                    .folderMode(true) // folder mode (false by default)
                    .folderTitle("Folder") // folder selection title
                    .imageTitle("Tap to select") // image selection title
                    .single() // single mode
                    .multi() // multi mode (default mode)
                    .limit(1) // max images can be selected (999 by default)
                    .showCamera(true) // show camera or not (true by default)
                    .imageDirectory("Camera") // directory name for captured image  ("Camera" folder by default)
                    //.origin(images) // original selected images, used in multi mode
                    .start(REQUEST_CODE_PICKER); // st
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Tag","Inside UA::launchImagePicker(): "+e.getMessage());
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICKER_PHOTO && resultCode == RESULT_OK && data != null) {

            ArrayList<Image> myImagesList = data.getParcelableArrayListExtra(ImagePickerActivity.INTENT_EXTRA_SELECTED_IMAGES);
            try {
                Log.d("Tag", "+ On :" + myImagesList.get(0).getName());
                Log.d("Tag", "+ On :" + myImagesList.get(0).getPath());
                upload_image = getBase64(myImagesList.get(0).getPath());
                isimage = true;

                Bitmap tempBitmap = getBitmapFromPath(myImagesList.get(0).getPath());
                if (tempBitmap != null) {
                    img_photo.setImageBitmap(tempBitmap);
                }

            } catch (IOException e) {
                Log.d("Tag", "Inside UA::onActivityResult(): " + e.getMessage());
                e.printStackTrace();
            }

        }

    }
    public Bitmap getBitmapFromPath(String path){
        Bitmap bitmapImage=null;

        try {
            bitmapImage = BitmapFactory.decodeFile(path);
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("Tag","Inside getBitmapFromPath() IA "+e.getMessage());
        }

        return  bitmapImage;
    }
    public String getBase64(String path) throws IOException {
        String encodedImage = null;

        try
        {
            Bitmap bm = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm = resize(bm, 600, 600);
            bm.compress(Bitmap.CompressFormat.JPEG, 50, baos); //bm is the bitmap object
            byte[] b = baos.toByteArray();
            encodedImage = Base64.encodeToString(b, Base64.DEFAULT);

        } catch (Exception e)
        {
            e.printStackTrace();
            Log.d("Tag","Inside getBase64(): "+e.getMessage());
        }

        Log.d("Mytag","getBase64 :"+encodedImage);
        return encodedImage;

    }

    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }
    @Override
    public void onBackPressed() {
        Intent i =new Intent(AddVendor.this, AdminDashboard.class);
        startActivity(i);
        finish();
    }

}
