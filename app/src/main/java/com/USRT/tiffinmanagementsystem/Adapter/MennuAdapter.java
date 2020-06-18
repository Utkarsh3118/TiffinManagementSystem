package com.USRT.tiffinmanagementsystem.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
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

import com.USRT.tiffinmanagementsystem.Activity.Vendor.MenuList;
import com.USRT.tiffinmanagementsystem.Model.Menu;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class MennuAdapter extends RecyclerView.Adapter<MennuAdapter.ViewHolder> {
    List<Menu> scname_data = new ArrayList<Menu>();
    Context context;

    public MennuAdapter(Context c, List<Menu> scname_data) {
        this.scname_data=scname_data;
        context=c;
    }

    @Override
    public MennuAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_vendor_menu, viewGroup, false);
        return new MennuAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(MennuAdapter.ViewHolder viewHolder, final int i) {
        final Menu subCategory = scname_data.get(i);

        viewHolder.title.setText("Name : "+subCategory.getItem_name());
        viewHolder.subtitle.setText("Rs."+subCategory.getPrice());//"Address : "+
        viewHolder.qty.setText("Qty - "+subCategory.getQuantity());//"Mobile : "+

        if (subCategory.getType().equals("Veg")){
            viewHolder.type.setText(subCategory.getType());
            viewHolder.type.setTextColor(Color.GREEN);
        }else
        {
            viewHolder.type.setText(subCategory.getType());
            viewHolder.type.setTextColor(Color.RED);
        }

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
                        String item_id = subCategory.getItem_id().toString();
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference("Vendor").child("Menu").child(item_id);

                        //removing artist
                        dR.removeValue();

                        Toast.makeText(context, "Vendor Deleted", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, MenuList.class));

                    }
                });
                AlertDialog dialog = builder.create();

                dialog.show();

            }
        });
/*
       */

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Intent intent = new Intent(context, ViewVendor.class);
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


                context.startActivity(intent);*/
            }
        });

    }

    @Override
    public int getItemCount() {
        return scname_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,subtitle,qty,type;
        ImageView image;
        ImageButton img_btn_delete;

        public ViewHolder(View view) {
            super(view);
            context = itemView.getContext();

            image=(ImageView) view.findViewById(R.id.image);
            title=(TextView) view.findViewById(R.id.title);
            subtitle=(TextView) view.findViewById(R.id.subtitle);
            qty=(TextView) view.findViewById(R.id.qty);
            type=(TextView) view.findViewById(R.id.type);
            img_btn_delete=(ImageButton) view.findViewById(R.id.img_btn_delete);
        }
    }
}

