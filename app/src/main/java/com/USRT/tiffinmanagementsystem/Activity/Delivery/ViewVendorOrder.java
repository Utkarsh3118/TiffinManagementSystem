package com.USRT.tiffinmanagementsystem.Activity.Delivery;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.USRT.tiffinmanagementsystem.Class.Constants;
import com.USRT.tiffinmanagementsystem.Class.MySingleton;
import com.USRT.tiffinmanagementsystem.Model.UserModel;
import com.USRT.tiffinmanagementsystem.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ViewVendorOrder extends AppCompatActivity {
    ImageView img_photo;
    EditText edit_id,edit_itemname,edit_price,edit_qty,edit_content,edit_address,edit_date,edit_time,edit_note,edit_status;
    Button bt_update;
    //CharSequence[] values = {"Pending","Complete"};
    CharSequence[] values = {"Delivered"};
    AlertDialog alertDialog1;
    String status;

    FirebaseAuth mAuth;
    ProgressDialog mProgressDialog;
    private DatabaseReference myRef;
    String order_id,vendor_id,user_id,name,quantity,image,content,note,price,date,delivery_status,type,time,total_price;

    DatabaseReference databaseRef;
    List<UserModel> data_cust;
    String token,boy_name;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_vendor_order);

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
        boy_name = sharedPreferences.getString("boy_name", null);


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


        byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        img_photo.setImageBitmap(decodedByte);

        if (delivery_status.equals("Pending")){
            edit_status.setText(delivery_status);
            edit_status.setTextColor(Color.YELLOW);
        }else
        {
            edit_status.setText(delivery_status);
            edit_status.setTextColor(Color.GREEN);
        }

        edit_status.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewVendorOrder.this);
                builder.setTitle("Select Status");
                builder.setCancelable(false);
                builder.setSingleChoiceItems(values, -1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        switch(item)
                        {
                           /* case 0:
                                edit_status.setText("Pending");
                                status="Pending";
                                break;*/
                            case 0:

                                edit_status.setText("Delivered");
                                edit_status.setTextColor(Color.GREEN);
                                status="Delivered";

                                break;
                        }
                        alertDialog1.dismiss();
                    }
                });
                alertDialog1 = builder.create();
                alertDialog1.show();
            }
        });

        bt_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String status= edit_status.getText().toString();
                mProgressDialog.show();

                myRef = FirebaseDatabase.getInstance().getReference();

                //updating artist
                myRef.child("PlaceOrder").child(order_id).child("delivery_status").setValue(status)
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                               /**/
                                mProgressDialog.dismiss();
                                DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
                                Query key = ref.child("Customer").child("user").orderByChild("user_id").equalTo(user_id);
                                key.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                            UserModel model = appleSnapshot.getValue(UserModel.class);
                                            token = model.getToken();
                                            Log.d("tokenss", "" + model.getToken());

                                                    ///notification code
                                                    JSONObject notification = new JSONObject();
                                                    JSONObject notifcationBody = new JSONObject();
                                                    try {
                                                        notifcationBody.put("title", "Delivery Notification");
                                                        notifcationBody.put("message", "your tiffin order delivered successfully by " + boy_name);
                                                        notification.put("to", token);
                                                        notification.put("data", notifcationBody);
                                                    } catch (JSONException e) {
                                                        Log.e("error", "onCreate: " + e.getMessage());
                                                    }
                                                    sendNotification(notification);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Log.e("position", "onCancelled", databaseError.toException());
                                    }
                                });

                                Toast.makeText(getApplicationContext(), "Order Status Updated", Toast.LENGTH_LONG).show();
                                Intent i =new Intent(ViewVendorOrder.this, OwnVendor_Orders.class);
                                startActivity(i);
                                finish();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                mProgressDialog.dismiss();
                                Toast.makeText(ViewVendorOrder.this, "Status Not Change Successful", Toast.LENGTH_SHORT).show();

                            }
                        });

            }
        });
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

    private void sendNotification(JSONObject notification) {
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Constants.FCM_API, notification,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                Log.i("responce", "onResponse: " + response.toString());

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(ViewVendorOrder.this, "Request error", Toast.LENGTH_LONG).show();
                                Log.i("error", "onErrorResponse: Didn't work  " + error.getMessage());
                            }
                        }) {
                    @Override
                    public Map<String, String> getHeaders() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("Authorization", Constants.serverkey);
                        params.put("Content-Type", Constants.contentType);
                        return params;
                    }
                };
                MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
            }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(ViewVendorOrder.this, OwnVendor_Orders.class );
        startActivity(i);
        finish();
    }
        }

