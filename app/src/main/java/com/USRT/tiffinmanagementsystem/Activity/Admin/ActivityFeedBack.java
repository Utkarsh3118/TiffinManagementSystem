package com.USRT.tiffinmanagementsystem.Activity.Admin;

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
import android.view.View;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.USRT.tiffinmanagementsystem.Adapter.FeedBackAdapter;
import com.USRT.tiffinmanagementsystem.Model.Feedback;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ActivityFeedBack extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

        private View parent_view;

        List<Feedback> Items = new ArrayList<>();
        ProgressDialog progressDialog;
        private RecyclerView recyclerView;
        private FeedBackAdapter mAdapter;
        private SwipeRefreshLayout swipeRefreshLayout;
        FirebaseDatabase database;
        DatabaseReference myRef;
        int flag=0;


        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_back);

        parent_view = findViewById(R.id.parent_view);

        initToolbar();

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(ActivityFeedBack.this);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        Items=new ArrayList<>();
        mAdapter=new FeedBackAdapter(this,Items);
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
        //toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("View All Feedbacks");
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

    }



    private void loadJSON(){
        swipeRefreshLayout.setRefreshing(true);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query key = databaseReference.child("Feedback").orderByChild("feed_id");
        key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Feedback model = snapshot.getValue(Feedback.class);

                    if (Items.size() > 0) {
                        for (int i = 0; i < Items.size(); i++) {
                            if (model.getFeed_id().equals(Items.get(i).getFeed_id())) {
                                flag = 1;
                                Log.d("data2", model.getComment().toString());
                            }
                        }
                        if (flag == 1) {
                        } else {
                            flag = 0;

                            Items.add(model);
                            Log.d("data", model.getComment().toString());
                            Log.d("data", String.valueOf(model.getRate_val()));

                        }
                    } else {

                        Items.add(model);
                        Log.d("data11", String.valueOf(model.getRate_val()));
                        Log.d("data11", model.getUser_id().toString());

                    }
                    // list.add(model);
                    HashSet<Feedback> hashSet = new HashSet<Feedback>();
                    hashSet.addAll(Items);
                    Items.clear();
                    Items.addAll(hashSet);
                    Log.d("size1", "" + Items.size());
                    mAdapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(false);

                    Log.d("daaa", model.getComment().toString());


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
        Intent i = new Intent(ActivityFeedBack.this, AdminDashboard.class );
        startActivity(i);
        finish();
    }


}
