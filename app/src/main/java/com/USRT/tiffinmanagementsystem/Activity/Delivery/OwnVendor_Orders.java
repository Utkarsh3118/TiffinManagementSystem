package com.USRT.tiffinmanagementsystem.Activity.Delivery;

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
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.USRT.tiffinmanagementsystem.Activity.MainActivity;
import com.USRT.tiffinmanagementsystem.Adapter.VendorOrderAdapter;
import com.USRT.tiffinmanagementsystem.Model.PlaceOrder;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class OwnVendor_Orders extends AppCompatActivity  implements SwipeRefreshLayout.OnRefreshListener{

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
    String bvendor_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_own_vendor__orders);


        initToolbar();

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        bvendor_id = sharedPreferences.getString("bvendor_id", null);


        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(OwnVendor_Orders.this);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new LinearLayoutManager(OwnVendor_Orders.this));
        Items=new ArrayList<>();
        mAdapter=new VendorOrderAdapter(this,Items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new VendorOrderAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, PlaceOrder subCategory, int position) {
                //Snackbar.make(parent_view, "Item " + obj.getVname() + " clicked", Snackbar.LENGTH_SHORT).show();
                Intent intent=new Intent(OwnVendor_Orders.this, ViewVendorOrder.class);
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
        Query key = databaseReference.child("PlaceOrder").orderByChild("vendor_id").equalTo(bvendor_id);
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
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delivery, menu);
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
            editor.remove( "duser_name" );
            editor.remove( "dpassword" );
            editor.commit();
                   /* Intent i9 = new Intent( MainActivity.this, LoginSimpleLight.class );
                    startActivity( i9 );
                    finish();*/
            Intent i = new Intent(OwnVendor_Orders.this, MainActivity.class );
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

        return super.onOptionsItemSelected( item );
    }
    @Override
    public void onRefresh() {
        loadJSON();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent(OwnVendor_Orders.this, MainActivity.class );
        startActivity(i);
        finish();
    }


}
