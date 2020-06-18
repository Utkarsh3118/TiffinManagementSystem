package com.USRT.tiffinmanagementsystem.Activity.Vendor;

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
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.USRT.tiffinmanagementsystem.Adapter.MennuAdapter;
import com.USRT.tiffinmanagementsystem.Model.Menu;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class MenuList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    List<Menu> Items = new ArrayList<>();
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private MennuAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int flag=0;
Button btn_add_menu;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String vendor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_list);


        initToolbar();

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        vendor_id = sharedPreferences.getString("vendor_id", null);

        btn_add_menu= findViewById(R.id.btn_add_menu);

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(MenuList.this);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(MenuList.this));
        Items=new ArrayList<>();
        mAdapter=new MennuAdapter(this,Items);
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

        btn_add_menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuList.this, AddMenu.class );
                startActivity(i);
               // finish();
            }
        });

    }



    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Tools.setSystemBarColor(this, R.color.colorPrimary);
    }
    private void loadJSON(){
        swipeRefreshLayout.setRefreshing(true);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vendor");
        Query key = databaseReference.child("Menu").orderByChild("vendor_id").equalTo(vendor_id);
        key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Menu model = snapshot.getValue(Menu.class);

                    if (Items.size() > 0) {
                        for (int i = 0; i < Items.size(); i++) {
                            if (model.getItem_id().equals(Items.get(i).getItem_id())) {
                                flag = 1;
                                Log.d("data2", model.getItem_name().toString());
                            }
                        }
                        if (flag == 1) {
                        } else {
                            flag = 0;

                            Items.add(model);
                            Log.d("data", model.getItem_name().toString());
                            Log.d("data", model.getVendor_id().toString());

                        }
                    } else {

                        Items.add(model);
                        Log.d("data11", model.getPrice().toString());
                        Log.d("data11", model.getQuantity().toString());

                    }
                    // list.add(model);
                    HashSet<Menu> hashSet = new HashSet<Menu>();
                    hashSet.addAll(Items);
                    Items.clear();
                    Items.addAll(hashSet);
                    Log.d("size1", "" + Items.size());

                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                    Log.d("daaa", model.getItem_name().toString());
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
    public void onBackPressed() {
        Intent i = new Intent(MenuList.this, VendorDashboard.class );
        startActivity(i);
        finish();
    }

}
