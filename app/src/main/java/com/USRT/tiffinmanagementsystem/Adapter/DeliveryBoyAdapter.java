package com.USRT.tiffinmanagementsystem.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.USRT.tiffinmanagementsystem.Activity.Vendor.DeliveryBoyList;
import com.USRT.tiffinmanagementsystem.Model.DeliveryBoy;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class DeliveryBoyAdapter extends RecyclerView.Adapter<DeliveryBoyAdapter.ViewHolder> {
    List<DeliveryBoy> scname_data = new ArrayList<DeliveryBoy>();
    Context context;

    public DeliveryBoyAdapter(Context c, List<DeliveryBoy> scname_data) {
        this.scname_data=scname_data;
        context=c;
    }

    @Override
    public DeliveryBoyAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_delivery_boy, viewGroup, false);
        return new DeliveryBoyAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(DeliveryBoyAdapter.ViewHolder viewHolder, final int i) {
        final DeliveryBoy subCategory = scname_data.get(i);

        viewHolder.title.setText("Name : "+subCategory.getName() );
        viewHolder.subtitle.setText(subCategory.getCity());//"Address : "+
        viewHolder.mobile.setText(subCategory.getMobile());//"Mobile : "+


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
                        String boyid = subCategory.getBoyid().toString();
                        DatabaseReference dR = FirebaseDatabase.getInstance().getReference().child("DeliveryBoy").child(boyid);

                        //removing artist
                        dR.removeValue();

                        Toast.makeText(context, "DeliveryBoy Deleted", Toast.LENGTH_LONG).show();
                        context.startActivity(new Intent(context, DeliveryBoyList.class));

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
             /*   Intent intent = new Intent(context, ViewVendor.class);
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
        TextView title,subtitle,mobile;//txtcomp,
        ImageButton img_btn_delete;

        public ViewHolder(View view) {
            super(view);
            context = itemView.getContext();

            title=(TextView) view.findViewById(R.id.title);
            subtitle=(TextView) view.findViewById(R.id.subtitle);
            mobile=(TextView) view.findViewById(R.id.mobile);
            img_btn_delete=(ImageButton) view.findViewById(R.id.img_btn_delete);
        }
    }
}
