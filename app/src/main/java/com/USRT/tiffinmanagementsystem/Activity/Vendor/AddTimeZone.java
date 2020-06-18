package com.USRT.tiffinmanagementsystem.Activity.Vendor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.USRT.tiffinmanagementsystem.Model.TimeZone;
import com.USRT.tiffinmanagementsystem.Model.Vendor;
import com.USRT.tiffinmanagementsystem.R;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.Calendar;
import java.util.List;

public class AddTimeZone extends AppCompatActivity {

    EditText edit_lunchtimezone,edit_dinnertimezone;
    Button bt_save;

    ProgressDialog mProgressDialog;
    FirebaseAuth mAuth;
    private DatabaseReference myRef;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    List<TimeZone> data;
    String vendor_id;
    String time_zone,dinner_time,lunch_time;
    String vname,vmobile,vemail,vpasssword,vaddress,vage,vlocation,vlatitude,vlongitude,vphoto;
    double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_time_zone);

        initToolbar();


        mAuth = FirebaseAuth.getInstance();
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setMessage("Please wait..");

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        vendor_id = sharedPreferences.getString("vendor_id", null);
        vname = sharedPreferences.getString("vname", null);
        vmobile = sharedPreferences.getString("vmobile", null);
        vemail = sharedPreferences.getString("vemail", null);
        vpasssword = sharedPreferences.getString("vpasssword", null);
        vaddress = sharedPreferences.getString("vaddress", null);
        vage = sharedPreferences.getString("vage", null);
        vlocation= sharedPreferences.getString("vlocation", null);
        vlatitude = sharedPreferences.getString("vlatitude", null);
        vlongitude =sharedPreferences.getString("vlongitude", null);
        vphoto = sharedPreferences.getString("vphoto", null);

        latitude = Double.parseDouble(vlatitude);
        longitude = Double.parseDouble(vlongitude);

        edit_lunchtimezone=findViewById(R.id.edit_lunchtimezone);
        edit_dinnertimezone=findViewById(R.id.edit_dinnertimezone);
        bt_save=findViewById(R.id.bt_save);

       getdata();

        edit_lunchtimezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTimePickerDark((EditText) v);
            }
        });
        edit_dinnertimezone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogTimePickerDark1((EditText) v);
            }
        });
        bt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               String lunchtime = edit_lunchtimezone.getText().toString();
                String dinnertime = edit_dinnertimezone.getText().toString();

                if (TextUtils.isEmpty(lunchtime)) {
                    edit_lunchtimezone.setError("Cannot be empty.");
                    edit_lunchtimezone.requestFocus();
                }
                if (TextUtils.isEmpty(dinnertime)) {
                    edit_dinnertimezone.setError("Cannot be empty.");
                    edit_dinnertimezone.requestFocus();
                }

                  else {
                    if (vendor_id != null) {

                         mProgressDialog.show();

                        myRef= FirebaseDatabase.getInstance().getReference("Vendor").child("data").child(vendor_id);

                        //updating artist

                        Vendor busitem = new Vendor(vendor_id,vname,vmobile,vemail,vpasssword,vaddress,vage,vlocation,latitude,longitude,vphoto,lunchtime,dinnertime);
                        myRef.setValue(busitem);
                        Toast.makeText(getApplicationContext(), "TimeZone Updated", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                        Intent i =new Intent(AddTimeZone.this, VendorDashboard.class);
                        startActivity(i);
                        finish();

                    }

                    /*if(time_zone != null){
                        mProgressDialog.show();

                        myRef= FirebaseDatabase.getInstance().getReference().child("TimeZone").child(time_zone);

                        //updating artist
                        TimeZone item = new TimeZone(time_zone,vendor_id,lunchtime,dinnertime);
                        myRef.setValue(item);
                        Toast.makeText(getApplicationContext(), "TimeZone Details Updated", Toast.LENGTH_LONG).show();
                        mProgressDialog.dismiss();
                        Intent i =new Intent(AddTimeZone.this, VendorDashboard.class);
                        startActivity(i);
                        finish();
                    }else {
                        mProgressDialog.show();
                        myRef = FirebaseDatabase.getInstance().getReference();
                        String newid = myRef.push().getKey();
                        HashMap<String, Object> UserValue = new HashMap<>();
                        //bus_number,busname,start_point,start_time,last_point,last_time,amount_km,route,conductor_mobile,password;
                        UserValue.put("timezoneid", newid);
                        UserValue.put("vendor_id", vendor_id);
                        UserValue.put("lunchtime", lunchtime);
                        UserValue.put("dinnertime", dinnertime);

                        myRef.child("TimeZone").child(newid).setValue(UserValue);

                        startActivity(new Intent(AddTimeZone.this, VendorDashboard.class));
                        finish();
                        Toast.makeText(AddTimeZone.this, "Success", Toast.LENGTH_SHORT).show();
                        mProgressDialog.dismiss();


                    }
*/


                }
            }
        });
    }
    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add TimeZone");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Tools.setSystemBarColor(this, R.color.colorPrimary);
    }

    private void dialogTimePickerDark(final EditText bt) {
        Calendar cur_calender = Calendar.getInstance();
        TimePickerDialog datePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                ((TextView) findViewById(R.id.edit_lunchtimezone)).setText(hourOfDay + ":" + minute);
            }
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);
        //set dark light
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Timepickerdialog");
    }

    private void dialogTimePickerDark1(final EditText bt) {
        Calendar cur_calender = Calendar.getInstance();
        TimePickerDialog datePicker = TimePickerDialog.newInstance(new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
                ((TextView) findViewById(R.id.edit_dinnertimezone)).setText(hourOfDay + ":" + minute);
            }
        }, cur_calender.get(Calendar.HOUR_OF_DAY), cur_calender.get(Calendar.MINUTE), true);
        //set dark light
        datePicker.setThemeDark(true);
        datePicker.setAccentColor(getResources().getColor(R.color.colorPrimary));
        datePicker.show(getFragmentManager(), "Timepickerdialog");
    }



    @Override
    public void onBackPressed() {
        Intent i = new Intent(AddTimeZone.this, VendorDashboard.class );
        startActivity(i);
        finish();
    }
    public void getdata(){
         myRef = FirebaseDatabase.getInstance().getReference("Vendor");
        Query key = myRef.child("data").orderByChild("vendor_id").equalTo(vendor_id);
        key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        TimeZone model = snapshot.getValue(TimeZone.class);
                       // data.add(model);

                       // Log.e("da", model.getDinnertime().toString());

                        dinner_time=model.getDinnertime().toString();
                        lunch_time= model.getLunchtime().toString();
                        time_zone= model.getVendor_id().toString();
                        edit_lunchtimezone.setText(lunch_time);
                        edit_dinnertimezone.setText(dinner_time);

                    }


                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
