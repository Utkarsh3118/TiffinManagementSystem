package com.USRT.tiffinmanagementsystem.Activity.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
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
import com.USRT.tiffinmanagementsystem.Adapter.AllVendorsAdapter;
import com.USRT.tiffinmanagementsystem.Model.Vendor;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ViewAllVendors extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private View parent_view;

    List<Vendor> Items = new ArrayList<>();
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private AllVendorsAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int flag=0;


    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_vendors);
        parent_view = findViewById(R.id.parent_view);

        initToolbar();

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(ViewAllVendors.this);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        Items=new ArrayList<>();
        mAdapter=new AllVendorsAdapter(this,Items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new AllVendorsAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Vendor obj, int position) {
                //Snackbar.make(parent_view, "Item " + obj.getVname() + " clicked", Snackbar.LENGTH_SHORT).show();
                Intent in=new Intent(ViewAllVendors.this,Vendor_Menu.class);
                in.putExtra("vendor_id",obj.getVendor_id());
                startActivity(in);
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
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("All Vendors");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_customer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_add_feedback) {
            Intent in=new Intent(ViewAllVendors.this,FeedBackFrom.class);
            startActivity(in);
            finish();
        }
        if (item.getItemId() == R.id.action_logout) {
            sharedPreferences = getApplicationContext().getSharedPreferences( "Mydata", MODE_PRIVATE );
            editor = sharedPreferences.edit();
            editor.remove( "cuser_name" );
            editor.remove( "cpassword" );
            editor.commit();
                   /* Intent i9 = new Intent( MainActivity.this, LoginSimpleLight.class );
                    startActivity( i9 );
                    finish();*/
            Intent i = new Intent(ViewAllVendors.this, MainActivity.class );
            //i.putExtra("usertype","customer");
            i.setAction(Intent.ACTION_MAIN);
            i.addCategory(Intent.CATEGORY_HOME);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(i);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


        private void loadJSON(){
            swipeRefreshLayout.setRefreshing(true);

            progressDialog.setMessage("Loading...");
            progressDialog.show();
            DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference("Vendor");
            Query key = databaseReference.child("data").orderByChild("vendor_id");
            key.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        final Vendor model = snapshot.getValue(Vendor.class);

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
                                Log.d("data", model.getVaddress().toString());
                                Log.d("data", model.getVendor_id().toString());

                            }
                        } else {

                            Items.add(model);
                            Log.d("data11", model.getVphone().toString());
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

                        Log.d("daaa", model.getVname().toString());


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
            Intent i = new Intent(ViewAllVendors.this, MainActivity.class );
            startActivity(i);
            finish();
        }



}
