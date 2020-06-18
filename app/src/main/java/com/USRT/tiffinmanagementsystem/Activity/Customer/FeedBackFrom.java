package com.USRT.tiffinmanagementsystem.Activity.Customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.USRT.tiffinmanagementsystem.R;

import java.util.HashMap;

public class FeedBackFrom extends AppCompatActivity {

    EditText edit_comment;
    RatingBar rating_bar;
    AppCompatButton bt_submit;

    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference myRef;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
String user_id;

   // private TextView tvRateCount,tvRateMessage;

    private float ratedValue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back_from);

        edit_comment=findViewById(R.id.edit_comment);
        rating_bar=findViewById(R.id.rating_bar);
        bt_submit=findViewById(R.id.bt_submit);

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
         user_id = sharedPreferences.getString("user_id", null);


        rating_bar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {

            @Override

            public void onRatingChanged(RatingBar ratingBar, float rating,

                                        boolean fromUser) {

                ratedValue = ratingBar.getRating();

              /*  tvRateCount.setText("Your Rating : "

                        + ratedValue + "/5.");*/

            }

        });

        bt_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.show();
                myRef = FirebaseDatabase.getInstance().getReference();
                String newid = myRef.push().getKey();
                HashMap<String, Object> UserValue = new HashMap<>();

                UserValue.put("feed_id", newid);
                UserValue.put("user_id", user_id);
                UserValue.put("comment", edit_comment.getText().toString());
                UserValue.put("rate_val", ratedValue);

                myRef.child("Feedback").child(newid).setValue(UserValue);
                Toast.makeText(FeedBackFrom.this, "Success", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();


            }
        });


    }

    @Override
    public void onBackPressed() {
        Intent i =new Intent(FeedBackFrom.this,ViewAllVendors.class);
        startActivity(i);
        finish();
    }
}
