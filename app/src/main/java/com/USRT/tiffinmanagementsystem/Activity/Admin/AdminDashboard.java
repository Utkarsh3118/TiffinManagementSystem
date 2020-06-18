package com.USRT.tiffinmanagementsystem.Activity.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.USRT.tiffinmanagementsystem.Activity.MainActivity;
import com.USRT.tiffinmanagementsystem.Adapter.VendorAdapter;
import com.USRT.tiffinmanagementsystem.Model.Vendor;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class AdminDashboard extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    List<Vendor> Items = new ArrayList<>();
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private VendorAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int flag=0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        initToolbar();
        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(AdminDashboard.this);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(AdminDashboard.this));
        Items=new ArrayList<>();
        mAdapter=new VendorAdapter(this,Items);
        recyclerView.setAdapter(mAdapter);

        // show loader and fetch messages
        swipeRefreshLayout.post(
                new Runnable() {
                    @Override
                    public void run() {
                        loadJSON();
                    }
                }
        );



    }


    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Vendors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Tools.setSystemBarColor(this, R.color.colorPrimary);
    }
    private void loadJSON(){
        swipeRefreshLayout.setRefreshing(true);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vendor");
        Query key = databaseReference.child("data").orderByChild("vendor_id");//.equalTo(user_id);
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

                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                    Log.d("daaa", model.getVemail().toString());
                }
                swipeRefreshLayout.setRefreshing(false);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        progressDialog.dismiss();


    }

    @Override
    public void onRefresh() {
        loadJSON();
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            editor.remove( "user_name" );
            editor.remove( "password" );
            editor.commit();
                   /* Intent i9 = new Intent( MainActivity.this, LoginSimpleLight.class );
                    startActivity( i9 );
                    finish();*/
            Intent i = new Intent(AdminDashboard.this, MainActivity.class );
           // i.putExtra("usertype","admin");
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.action_add_vendor) {
            Intent i = new Intent(AdminDashboard.this, AddVendor.class );
            startActivity(i);
            finish();
            return true;
        }
        if (id == R.id.action_see_feedback) {
            Intent i = new Intent(AdminDashboard.this, ActivityFeedBack.class );
            startActivity(i);
            finish();
            return true;
        }

        return super.onOptionsItemSelected( item );
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(AdminDashboard.this, MainActivity.class );
        startActivity(i);
        finish();
    }


}
