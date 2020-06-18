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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.USRT.tiffinmanagementsystem.Adapter.VendorOrderAdapter;
import com.USRT.tiffinmanagementsystem.Model.PlaceOrder;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class OrderList extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    List<PlaceOrder> Items = new ArrayList<>();
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private VendorOrderAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int flag=0;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String vendor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        initToolbar();

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        vendor_id = sharedPreferences.getString("vendor_id", null);

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(OrderList.this);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(OrderList.this));
        Items=new ArrayList<>();

        mAdapter=new VendorOrderAdapter(this,Items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new VendorOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PlaceOrder subCategory, int position) {
                //Snackbar.make(parent_view, "Item " + obj.getVname() + " clicked", Snackbar.LENGTH_SHORT).show();
                Intent intent=new Intent(OrderList.this, ViewOrder.class);
                intent.putExtra("order_id",subCategory.getOrder_id());
                intent.putExtra("vendor_id",subCategory.getVendor_id());
                intent.putExtra("user_id",subCategory.getUser_id());
                intent.putExtra("name",subCategory.getItem_name());
                intent.putExtra("quantity",subCategory.getQuantity());
                intent.putExtra("image",subCategory.getImage());
                intent.putExtra("content",subCategory.getContent());
                intent.putExtra("note",subCategory.getNote());
                intent.putExtra("price",subCategory.getPrice());
                intent.putExtra("date",subCategory.getDate());
                intent.putExtra("delivery_status",subCategory.getDelivery_status());
                intent.putExtra("type",subCategory.getType());
                intent.putExtra("time",subCategory.getTime());
                intent.putExtra("total_price",subCategory.getTotal_price());
                intent.putExtra("status",subCategory.getDelivery_status());
                startActivity(intent);
                finish();
            }
        });
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
        getSupportActionBar().setTitle("Vendor Orders");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        // Tools.setSystemBarColor(this, R.color.colorPrimary);
    }
    private void loadJSON(){
        swipeRefreshLayout.setRefreshing(true);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query key = databaseReference.child("PlaceOrder").orderByChild("vendor_id").equalTo(vendor_id);
        key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    PlaceOrder model = snapshot.getValue(PlaceOrder.class);

                    if (Items.size() > 0) {
                        for (int i = 0; i < Items.size(); i++) {
                            if (model.getOrder_id().equals(Items.get(i).getOrder_id())) {
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
                    HashSet<PlaceOrder> hashSet = new HashSet<PlaceOrder>();
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
        Intent i = new Intent(OrderList.this, VendorDashboard.class );
        startActivity(i);
        finish();
    }
}
