package com.USRT.tiffinmanagementsystem.Activity.Customer;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.USRT.tiffinmanagementsystem.Model.CartModel;
import com.USRT.tiffinmanagementsystem.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Checkout extends AppCompatActivity {

    TextView tiffin_qty,tot_price,item_name,content_quantity,quantity,item_total_price,paid_amount;
    ImageView image;
    EditText ed_date,ed_time,edt_note;
    Button bt_Confirm;
    //DatabaseReference databaseReference;
    List<CartModel> data;
    ProgressDialog progressDialog;
    FirebaseDatabase database;
    DatabaseReference myRef;
    String cart_id,item_image,todaysdate,time;
    String user_id,vendor_id,item_id,item_namee,item_price,item_qty,quantityy,item_total,type;
    Calendar myCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);

        myCalendar = Calendar.getInstance();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);


        final Intent intent = this.getIntent();
        cart_id=intent.getStringExtra("cart_id");
        item_image=intent.getStringExtra("item_image");
        item_id=intent.getStringExtra("item_id");
        user_id=intent.getStringExtra("user_id");
        vendor_id=intent.getStringExtra("vendor_id");
        item_namee=intent.getStringExtra("item_name");
        item_price=intent.getStringExtra("item_price");
        item_qty=intent.getStringExtra("item_qty");
        quantityy=intent.getStringExtra("quantity");
        item_total=intent.getStringExtra("item_total");
        type=intent.getStringExtra("type");


        data = new ArrayList<>();
        //databaseReference = FirebaseDatabase.getInstance().getReference().child("CartOrder").child();

        tiffin_qty=findViewById(R.id.tiffin_qty);
        tot_price=findViewById(R.id.tot_price);
        item_name=findViewById(R.id.item_name);
        content_quantity=findViewById(R.id.content_quantity);
        quantity=findViewById(R.id.quantity);
        item_total_price=findViewById(R.id.item_total_price);
        edt_note=findViewById(R.id.edt_note);
        image=findViewById(R.id.image);
        ed_date=findViewById(R.id.ed_date);
        ed_time=findViewById(R.id.ed_time);

        paid_amount=findViewById(R.id.paid_amount);

        bt_Confirm=findViewById(R.id.bt_Confirm);


        tiffin_qty.setText(quantityy);
        tot_price.setText(item_total);
        item_name.setText(item_namee);
        content_quantity.setText(item_qty);
        quantity.setText(quantityy);
        item_total_price.setText(item_total);
        paid_amount.setText(item_total);


        byte[] decodedString = Base64.decode(item_image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(decodedByte);


        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
         todaysdate = df.format(c);

        ed_date.setText(todaysdate);

        ed_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(Checkout.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ed_time.setText(selectedHour + ":" + selectedMinute);
                        time = selectedHour + ":" + selectedMinute;
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });


        bt_Confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder11 = new AlertDialog.Builder(Checkout.this);
                builder11.setMessage("Are you sure you want to Place Order?");
                builder11.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog11, int id) {
                        dialog11.dismiss();
                    }
                });
                builder11.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog11, int id) {
                        dialog11.dismiss();

                        AlertDialog.Builder builder = new AlertDialog.Builder(Checkout.this);
                        builder.setMessage("Select Your Payment Type");
                        builder.setCancelable(false);
                        builder.setPositiveButton("Online", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.dismiss();
                                Intent i;
                                PackageManager manager = getPackageManager();
                                try {
                                    i = manager.getLaunchIntentForPackage("com.phonepe.app");
                                    if (i == null)
                                        throw new PackageManager.NameNotFoundException();
                                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                                    startActivity(i);
                                } catch (PackageManager.NameNotFoundException e) {

                                }

                            }
                        });
                        builder.setNegativeButton("Cash on Delivery", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();

                                progressDialog.show();
                                myRef = FirebaseDatabase.getInstance().getReference();
                                String newid = myRef.push().getKey();
                                HashMap<String, Object> UserValue = new HashMap<>();

                                UserValue.put("order_id", newid);
                                UserValue.put("cart_id", cart_id);
                                UserValue.put("menu_id",item_id);
                                UserValue.put("user_id", user_id);
                                UserValue.put("vendor_id", vendor_id);
                                UserValue.put("item_name", item_namee);
                                UserValue.put("price", item_price);
                                UserValue.put("content", item_qty);
                                UserValue.put("quantity", quantityy);
                                UserValue.put("total_price", item_total);
                                UserValue.put("type",type );
                                UserValue.put("image", item_image);
                                UserValue.put("date",todaysdate );
                                UserValue.put("time",time );
                                UserValue.put("note",edt_note.getText().toString() );
                                UserValue.put("delivery_status","pending" );
                                myRef.child("PlaceOrder").child(newid).setValue(UserValue);
                                AlertDialog.Builder builder1 = new AlertDialog.Builder(Checkout.this);
                                builder1.setMessage("Order Place Successfully !!");
                                builder1.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog1, int id) {

                                        Intent i= new Intent(Checkout.this, ViewAllVendors.class);
                                        startActivity(i);
                                        finish();

                                        dialog1.dismiss();
                                    }
                                });
                                AlertDialog dialog1 = builder1.create();
                                dialog1.show();


                                //Toast.makeText(Checkout.this, "Success", Toast.LENGTH_SHORT).show();
                                progressDialog.dismiss();

                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                });
                AlertDialog dialog11 = builder11.create();
                dialog11.show();

            }
        });

    }

   /* @Override
    protected void onResume() {
        super.onResume();
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        CartModel model = snapshot.getValue(CartModel.class);
                        data.add(model);



                        Log.d("da", model.getItem_name().toString());
                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }*/

    @Override
    public void onBackPressed() {
        Intent i =new Intent(Checkout.this,Vendor_Menu.class);
        startActivity(i);
        finish();
    }
}
