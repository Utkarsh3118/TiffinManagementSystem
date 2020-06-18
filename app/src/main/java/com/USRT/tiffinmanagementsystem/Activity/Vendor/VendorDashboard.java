package com.USRT.tiffinmanagementsystem.Activity.Vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.USRT.tiffinmanagementsystem.Activity.MainActivity;
import com.USRT.tiffinmanagementsystem.Model.Vendor;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class VendorDashboard extends AppCompatActivity {

    List<Vendor> Items = new ArrayList<>();
  //  ProgressDialog progressDialog;
    int flag=0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    LinearLayout lyt_addmenu,lyt_addtimezone,lyt_vieworder,lyt_adddeliveryboy;
String vendor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_dashboard);
        initToolbar();


        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        vendor_id = sharedPreferences.getString("vendor_id", null);


        lyt_addmenu=findViewById(R.id.lyt_addmenu);
        lyt_addtimezone=findViewById(R.id.lyt_addtimezone);
        lyt_vieworder=findViewById(R.id.lyt_vieworder);
        lyt_adddeliveryboy=findViewById(R.id.lyt_adddeliveryboy);

        Items=new ArrayList<>();
        loadJSON();

        lyt_addmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(VendorDashboard.this, MenuList.class);
                startActivity(i);
            }
        });
        lyt_addtimezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(VendorDashboard.this, AddTimeZone.class);
                startActivity(i);
            }
        });
        lyt_vieworder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(VendorDashboard.this, OrderList.class);
                startActivity(i);
            }
        });
        lyt_adddeliveryboy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(VendorDashboard.this, DeliveryBoyList.class);
                startActivity(i);
            }
        });
    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_vendor, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        // Activate the navigation drawer toggle

        if (id == R.id.action_logout) {
            sharedPreferences = getApplicationContext().getSharedPreferences( "Mydata", MODE_PRIVATE );
            editor = sharedPreferences.edit();
            editor.remove( "vuser_name" );
            editor.remove( "vpassword" );
            editor.commit();
                   /* Intent i9 = new Intent( MainActivity.this, LoginSimpleLight.class );
                    startActivity( i9 );
                    finish();*/
            Intent i = new Intent(VendorDashboard.this, MainActivity.class );
         //   i.putExtra("usertype","vendor");
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.action_add_menu) {
            Intent i = new Intent(VendorDashboard.this, AddMenu.class );
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected( item );
    }

    private void loadJSON(){


        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vendor");
        Query key = databaseReference.child("data").orderByChild("vendor_id").equalTo(vendor_id);
        key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Vendor model = snapshot.getValue(Vendor.class);

                    if (Items.size() > 0) {
                        for (int i = 0; i < Items.size(); i++) {
                            if (model.getVendor_id().equals(Items.get(i).getVendor_id())) {
                                flag = 1;
                                Log.d("data2", model.getVname().toString());
                            }
                        }
                        if (flag == 1) {
                        } else {
                            flag = 0;

                            Items.add(model);
                            Log.d("data", model.getVphone().toString());
                            Log.d("data", model.getVaddress().toString());

                        }
                    } else {

                        Items.add(model);
                        Log.d("data11", model.getVaddress().toString());
                        Log.d("data11", model.getVemail().toString());

                    }
                    // list.add(model);
                    HashSet<Vendor> hashSet = new HashSet<Vendor>();
                    hashSet.addAll(Items);
                    Items.clear();
                    Items.addAll(hashSet);
                    Log.d("size1", "" + Items.size());


                    sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
                    editor = sharedPreferences.edit();
                    editor.putString("vname", model.getVname());
                    editor.putString("vmobile", model.getVphone());
                    editor.putString("vemail", model.getVemail());
                    editor.putString("vpasssword", model.getVpassword());
                    editor.putString("vaddress", model.getVaddress());
                    editor.putString("vage", model.getVage());
                    editor.putString("vlocation", model.getLocation());
                    editor.putString("vlatitude", String.valueOf(model.getLatitude()));
                    editor.putString("vlongitude", String.valueOf(model.getLongitude()));
                    editor.putString("vphoto", model.getPhoto());
                    editor.commit();

                    Log.d("daaa", model.getVemail().toString());
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


       // progressDialog.dismiss();


    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(VendorDashboard.this, MainActivity.class );
        startActivity(i);
        finish();
    }

}
