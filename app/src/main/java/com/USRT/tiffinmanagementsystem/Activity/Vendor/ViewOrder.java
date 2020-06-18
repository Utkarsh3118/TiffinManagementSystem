package com.USRT.tiffinmanagementsystem.Activity.Vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.USRT.tiffinmanagementsystem.Model.UserModel;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class ViewOrder extends AppCompatActivity {

    ImageView img_photo;
    EditText edit_id,edit_itemname,edit_price,edit_qty,edit_content,edit_address,edit_date,edit_time,edit_note,edit_status;
    Button bt_update;

    FirebaseAuth mAuth;
    ProgressDialog mProgressDialog;
    private DatabaseReference myRef;
    String order_id,vendor_id,user_id,name,quantity,image,content,note,price,date,delivery_status,type,time,total_price;

    DatabaseReference databaseRef;
    List<UserModel> data_cust;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_order);

        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait..");


        img_photo=findViewById(R.id.img_photo);

        edit_id=findViewById(R.id.edit_id);
        edit_itemname=findViewById(R.id.edit_itemname);
        edit_price=findViewById(R.id.edit_price);
        edit_qty=findViewById(R.id.edit_qty);
        edit_content=findViewById(R.id.edit_content);
        edit_address=findViewById(R.id.edit_address);
        edit_date=findViewById(R.id.edit_date);
        edit_time=findViewById(R.id.edit_time);
        edit_note=findViewById(R.id.edit_note);
        edit_status=findViewById(R.id.edit_status);
        bt_update=findViewById(R.id.bt_update);

        Intent intent = getIntent();
        order_id = intent.getExtras().getString("order_id");
        vendor_id = intent.getExtras().getString("vendor_id");
        user_id= intent.getExtras().getString("user_id");
        name = intent.getExtras().getString("name");
        quantity = intent.getExtras().getString("quantity");
        image = intent.getExtras().getString("image");
        content = intent.getExtras().getString("content");
        note = intent.getExtras().getString("note");
        price = intent.getExtras().getString("price");
        date = intent.getExtras().getString("date");
        delivery_status = intent.getExtras().getString("delivery_status");
        type= intent.getExtras().getString("type");
        time= intent.getExtras().getString("time");
        total_price= intent.getExtras().getString("total_price");

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        vendor_id = sharedPreferences.getString("vendor_id", null);


        databaseRef = FirebaseDatabase.getInstance().getReference("Customer").child("user");
        data_cust = new ArrayList<>();

        edit_id.setText(order_id);
        edit_itemname.setText(name);
        edit_price.setText(total_price);
        edit_qty.setText(quantity);
        edit_content.setText(content);
        //edit_address.setText("adress");
        edit_date.setText(date);
        edit_time.setText(time);
        edit_note.setText(note);
        if (delivery_status.equals("Pending")){
            edit_status.setText(delivery_status);
            edit_status.setTextColor(Color.YELLOW);
        }else
        {
            edit_status.setText(delivery_status);
            edit_status.setTextColor(Color.GREEN);
        }

        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img_photo.setImageBitmap(decodedByte);



    }


    @Override
    protected void onResume() {
        super.onResume();

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        UserModel model1 = snapshot.getValue(UserModel.class);
                        data_cust.add(model1);

                        Log.e("cust da", model1.getMobile().toString());

                        edit_address.setText( model1.getAddress());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewOrder.this, OrderList.class );
        startActivity(i);
        finish();
    }
}
