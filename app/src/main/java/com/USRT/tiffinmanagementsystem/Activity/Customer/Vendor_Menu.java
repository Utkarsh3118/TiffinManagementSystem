package com.USRT.tiffinmanagementsystem.Activity.Customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.USRT.tiffinmanagementsystem.Adapter.VendorMenuAdapter;
import com.USRT.tiffinmanagementsystem.Model.Menu;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

public class Vendor_Menu extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener{

    private View parent_view;

    List<Menu> Items = new ArrayList<>();
    ProgressDialog progressDialog;
    private RecyclerView recyclerView;
    private VendorMenuAdapter mAdapter;
    private SwipeRefreshLayout swipeRefreshLayout;
    FirebaseDatabase database;
    DatabaseReference myRef;
    int flag=0;
    String vendor_id,user_id;
    String item_id,item_name,item_price,item_image,item_qty;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    int minteger = 1;
    String item_total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor__menu);
        parent_view = findViewById(R.id.parent_view);
        initToolbar();

        sharedPreferences = getApplicationContext().getSharedPreferences("Mydata", MODE_PRIVATE);
        sharedPreferences.edit();
        user_id = sharedPreferences.getString("user_id", null);


        final Intent intent = this.getIntent();
        vendor_id=intent.getStringExtra("vendor_id");

        database = FirebaseDatabase.getInstance();
        progressDialog = new ProgressDialog(this);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);


        swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(Vendor_Menu.this);
        recyclerView.hasFixedSize();
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);

        Items=new ArrayList<>();
        mAdapter=new VendorMenuAdapter(this,Items);
        recyclerView.setAdapter(mAdapter);

        // on item list clicked
        mAdapter.setOnItemClickListener(new VendorMenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Menu obj, int position) {
                //Snackbar.make(parent_view, "Item " + obj.getItem_name() + " clicked", Snackbar.LENGTH_SHORT).show();

                showCustomDialog(obj);

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
        getSupportActionBar().setTitle(" Menu");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent i = new Intent(Vendor_Menu.this, ViewAllVendors.class );
            startActivity(i);
            finish();
        }
        else {
            Toast.makeText(getApplicationContext(), item.getTitle(), Toast.LENGTH_SHORT).show();
        }
        return super.onOptionsItemSelected(item);
    }


    private void loadJSON(){
        swipeRefreshLayout.setRefreshing(true);

        progressDialog.setMessage("Loading...");
        progressDialog.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        Query key = databaseReference.child("Vendor").child("Menu").orderByChild("vendor_id").equalTo(vendor_id);
        key.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    final Menu model = snapshot.getValue(Menu.class);

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
                            Log.d("data", model.getQuantity().toString());
                            Log.d("data", model.getPrice().toString());

                        }
                    } else {

                        Items.add(model);
                        Log.d("data11", model.getPrice().toString());
                        Log.d("data11", model.getType().toString());

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



    private void showCustomDialog(final Menu p) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature( Window.FEATURE_NO_TITLE); // before
        dialog.setContentView(R.layout.dialog_add_to_cart);
        dialog.setCancelable(true);

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;

        item_id = String.valueOf( p.getItem_id() );
        item_name = p.getItem_name();
        item_qty = p.getQuantity();
        item_price = p.getPrice();
        item_image = p.getPhoto();

        final String type = p.getType();
        if(type.equals( "Veg" )){
            ((TextView) dialog.findViewById(R.id.type)).setTextColor( Color.parseColor("#4CAF50"));//green
            ((TextView) dialog.findViewById(R.id.type)).setText(p.getType());
        }else{
            ((TextView) dialog.findViewById(R.id.type)).setTextColor( Color.parseColor("#F44336"));//red
            ((TextView) dialog.findViewById(R.id.type)).setText(p.getType());
        }

        ((TextView) dialog.findViewById(R.id.title)).setText(p.getItem_name());

        ((TextView) dialog.findViewById(R.id.price)).setText("Rs." +p.getPrice());
        ((TextView) dialog.findViewById(R.id.intial_qty)).setText("Initial Qty :"+p.getQuantity());

       final TextView txt_quantity = ((TextView) dialog.findViewById(R.id.quantity));
        final TextView txt_total_price = ((TextView) dialog.findViewById(R.id.item_total_price));

        item_total=p.getPrice();
        txt_total_price.setText("Rs. " + item_total  );

         ((ImageButton) dialog.findViewById(R.id.decrease)).setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 if(minteger > 1){
                     minteger = minteger - 1;

                     txt_quantity.setText("" +minteger);
                     int new_price = Integer.parseInt( item_price );
                     item_total = String.valueOf( (new_price * minteger) );
                     txt_total_price.setText("Rs. " + item_total  );
                 }else{
                     minteger=1;
                     txt_quantity.setText("" +minteger);
                     int new_price = Integer.parseInt( item_price );
                     item_total = String.valueOf( (new_price * minteger) );
                     txt_total_price.setText("Rs. " + item_total  );
                 }

             }
         });
        ((ImageButton) dialog.findViewById(R.id.increase)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                minteger = minteger + 1;
                // display(minteger);
                txt_quantity.setText("" +minteger);
                int new_price = Integer.parseInt( item_price );
                item_total = String.valueOf( (new_price * minteger) );
                txt_total_price.setText( "Rs. "+item_total  );
            }
        });

        byte[] decodedString = Base64.decode(item_image, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        ((ImageView) dialog.findViewById(R.id.image)).setImageBitmap(decodedByte);


        ((ImageButton) dialog.findViewById(R.id.bt_close)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        ((AppCompatButton) dialog.findViewById(R.id.bt_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressDialog.show();
                myRef = FirebaseDatabase.getInstance().getReference();
                String newid = myRef.push().getKey();
                HashMap<String, Object> UserValue = new HashMap<>();
                UserValue.put("cart_id", newid);
                UserValue.put("menu_id", item_id);
                UserValue.put("user_id", user_id);
                UserValue.put("vendor_id", vendor_id);
                UserValue.put("item_name", item_name);
                UserValue.put("price", item_price);
                UserValue.put("content", item_qty);
                UserValue.put("quantity", txt_quantity.getText().toString());
                UserValue.put("total_price", item_total);
                UserValue.put("type",type );

                myRef.child("CartOrder").child(newid).setValue(UserValue);
                Intent i= new Intent(Vendor_Menu.this, Checkout.class);
                i.putExtra("cart_id",newid);
                i.putExtra("item_image",item_image);
                i.putExtra("item_id",item_id);
                i.putExtra("user_id",user_id);
                i.putExtra("vendor_id",vendor_id);
                i.putExtra("item_name",item_name);
                i.putExtra("item_price",item_price);
                i.putExtra("item_qty",item_qty);
                i.putExtra("quantity",txt_quantity.getText().toString());
                i.putExtra("item_total",item_total);
                i.putExtra("type",type);
                startActivity(i);
                finish();


               // Toast.makeText(Vendor_Menu.this, "Success", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();



            }
        });

        dialog.show();
        dialog.getWindow().setAttributes(lp);


    }


    @Override
    public void onBackPressed() {
        Intent i = new Intent(Vendor_Menu.this, ViewAllVendors.class );
        startActivity(i);
        finish();
    }

}
