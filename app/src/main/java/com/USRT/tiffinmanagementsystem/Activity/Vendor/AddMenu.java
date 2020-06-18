package com.USRT.tiffinmanagementsystem.Activity.Vendor;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.AutoCompleteTextView;
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

public class AddMenu extends AppCompatActivity {

    ImageView img_photo;
    FloatingActionButton fab_camera;
    AutoCompleteTextView edit_item_name,edit_price,edit_quantity;
    Button btn_add_menu;

    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    private DatabaseReference myRef;

    int REQUEST_CODE_PICKER_PHOTO=112;
    Boolean isimage=false;
    String upload_image,vendor_id;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CharSequence[] values = {" Veg "," Non-veg "};
    AlertDialog alertDialog1;

    String item_type;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_menu);

        initToolbar();
        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait..");

        img_photo=findViewById(R.id.img_photo);
        fab_camera=findViewById(R.id.fab_camera);
        edit_item_name=findViewById(R.id.edit_item_name);
        edit_price=findViewById(R.id.edit_price);
        edit_quantity=findViewById(R.id.edit_quantity);

        btn_add_menu=findViewById(R.id.btn_add_menu);


        AlertDialog.Builder builder = new AlertDialog.Builder(AddMenu.this);
        builder.setTitle("Select Type of Item?");
        builder.setCancelable(false);
        builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                switch(item)
                {
                    case 0:
                        item_type="Veg";
                        break;
                    case 1:
                        item_type="Non-veg";

                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();


        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
         vendor_id = sharedPreferences.getString("vendor_id", null);

        fab_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                launchImagePicker(REQUEST_CODE_PICKER_PHOTO);
            }
        });

        btn_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String name = edit_item_name.getText().toString();
                String  price = edit_price.getText().toString();
                String quantity = edit_quantity.getText().toString();

                if (TextUtils.isEmpty(name)) {
                    edit_item_name.setError("Cannot be empty.");
                    edit_item_name.requestFocus();
                }
                if (TextUtils.isEmpty(price)) {
                    edit_price.setError("Cannot be empty.");
                    edit_price.requestFocus();
                }
                if (TextUtils.isEmpty(quantity)) {
                    edit_quantity.setError("Cannot be empty.");
                    edit_quantity.requestFocus();
                }else {

                    mProgressDialog.show();
                    myRef = FirebaseDatabase.getInstance().getReference();
                    String newid = myRef.push().getKey();
                    HashMap<String, Object> UserValue = new HashMap<>();
                    UserValue.put("item_id", newid);
                    UserValue.put("vendor_id", vendor_id);
                    UserValue.put("item_name", name);
                    UserValue.put("price", price);
                    UserValue.put("quantity", quantity);
                    UserValue.put("photo", upload_image);
                    UserValue.put("type", item_type);

                    myRef.child("Vendor").child("Menu").child(newid).setValue(UserValue);
                    startActivity(new Intent(AddMenu.this, MenuList.class));
                    finish();


                    Toast.makeText(AddMenu.this, "Success", Toast.LENGTH_SHORT).show();
                    mProgressDialog.dismiss();
                }
            }
        });


    }



    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Menu");
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
        Intent i = new Intent(AddMenu.this, MenuList.class );
        startActivity(i);
        finish();
    }


}
