package com.USRT.tiffinmanagementsystem.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.USRT.tiffinmanagementsystem.Activity.Admin.AdminDashboard;
import com.USRT.tiffinmanagementsystem.Model.Vendor;
import com.USRT.tiffinmanagementsystem.R;
import com.USRT.tiffinmanagementsystem.Activity.Admin.ViewVendor;

import java.util.ArrayList;
import java.util.List;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder> {
    List<Vendor> scname_data = new ArrayList<Vendor>();
    Context context;

    public VendorAdapter(Context c, List<Vendor> scname_data) {
        this.scname_data=scname_data;
        context=c;
    }

    @Override
    public VendorAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vendors, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(VendorAdapter.ViewHolder viewHolder, final int i) {
        final Vendor subCategory = scname_data.get(i);

        viewHolder.title.setText("Name : "+subCategory.getVname() +" \n "+ "ID : "+subCategory.getVendor_id());
        viewHolder.subtitle.setText(subCategory.getVaddress());//"Address : "+
        viewHolder.mobile.setText(subCategory.getVphone());//"Mobile : "+

        String img=subCategory.getPhoto();
        byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        viewHolder.image.setImageBitmap(decodedByte);


        viewHolder.img_btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);

                builder.setMessage("Are you sure you want to delete?");
// Add the buttons
                builder.setPositiveButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // mOnArrayItemClick.setOnArrayItemClickListener(position, mFilterLIst.get(position).getEquipment_id());            }
                        String vendor_id = subCategory.getVendor_id().toString();
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Vendor").child("data").child(vendor_id);

                        //removing artist
                        dR.removeValue();

                        Toast.makeText(context, "Vendor Deleted", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, AdminDashboard.class));

                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();

            }
        });
/*
        if (subCategory.getStatus().equals("in process")){
            viewHolder.txtstatus.setTextColor(Color.RED);
        }else
        {
            viewHolder.txtstatus.setTextColor(Color.GREEN);
        }*/

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ViewVendor.class);
                intent.putExtra("vendor_id",subCategory.getVendor_id());
                intent.putExtra("name",subCategory.getVname());
                intent.putExtra("mobile",subCategory.getVphone());
                intent.putExtra("email",subCategory.getVemail());
                intent.putExtra("address",subCategory.getVaddress());
                intent.putExtra("age",subCategory.getVage());
                intent.putExtra("password",subCategory.getVpassword());
                intent.putExtra("location",subCategory.getLocation());
                intent.putExtra("latitude",subCategory.getLatitude());
                intent.putExtra("longitude",subCategory.getLongitude());
                intent.putExtra("photo",subCategory.getPhoto());


                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return scname_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,subtitle,mobile;//txtcomp,
        ImageView image;
        ImageButton img_btn_delete;

        public ViewHolder(View view) {
            super(view);
            context = itemView.getContext();

            image=(ImageView) view.findViewById(R.id.image);
            title=(TextView) view.findViewById(R.id.title);
            subtitle=(TextView) view.findViewById(R.id.subtitle);
            mobile=(TextView) view.findViewById(R.id.mobile);
            img_btn_delete=(ImageButton) view.findViewById(R.id.img_btn_delete);
        }
    }
}
