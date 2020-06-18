package com.USRT.tiffinmanagementsystem.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.USRT.tiffinmanagementsystem.R;

public class MainActivity extends AppCompatActivity {

 View lyt_admin,lyt_vendor,lyt_customer,lyt_deliveryboy;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lyt_admin=findViewById(R.id.lyt_admin);
        lyt_vendor=findViewById(R.id.lyt_vendor);
        lyt_customer=findViewById(R.id.lyt_customer);
        lyt_deliveryboy=findViewById(R.id.lyt_deliveryboy);

        lyt_admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,Login.class);
                i.putExtra("usertype","admin");
                startActivity(i);
                finish();
            }
        });
        lyt_vendor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,Login.class);
                i.putExtra("usertype","vendor");
                startActivity(i);
                finish();
            }
        });
        lyt_customer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,Login.class);
                i.putExtra("usertype","customer");
                startActivity(i);
                finish();
            }
        });
        lyt_deliveryboy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i= new Intent(MainActivity.this,Login.class);
                i.putExtra("usertype","deliveryboy");
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    public void onBackPressed() {
        Exit();
    }

    public void Exit() {
        new android.app.AlertDialog.Builder(this)
                .setIcon(R.mipmap.ic_launcher)
                .setTitle(getString(R.string.app_name))
                .setMessage("Are you sure,want to Exit from App?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent i = new Intent();
                        i.setAction(Intent.ACTION_MAIN);
                        i.addCategory(Intent.CATEGORY_HOME);
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();

                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

}
