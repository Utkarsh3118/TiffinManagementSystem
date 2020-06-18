package com.USRT.tiffinmanagementsystem.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.USRT.tiffinmanagementsystem.Model.PlaceOrder;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<PlaceOrder> items = new ArrayList<>();

    private Context ctx;
    private OrderAdapter.OnItemClickListener mOnItemClickListener;
    /*int hours;
    int min = 0;
    int days;
    long difference;*/


    public void setOnItemClickListener(final OrderAdapter.OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }



    public OrderAdapter(Context context, List<PlaceOrder> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        TextView item_name,delivery_status,quantity,item_total_price,time;
        ImageView image;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image=(ImageView) v.findViewById(R.id.image);
            item_name=(TextView) v.findViewById(R.id.item_name);
            delivery_status=(TextView) v.findViewById(R.id.delivery_status);
            quantity=(TextView) v.findViewById(R.id.quantity);
            item_total_price=(TextView) v.findViewById(R.id.item_total_price);
            time=(TextView) v.findViewById(R.id.time);

            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_vendor_order, parent, false);
        vh = new OrderAdapter.OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OrderAdapter.OriginalViewHolder) {
            final OrderAdapter.OriginalViewHolder view = (OrderAdapter.OriginalViewHolder) holder;

            final PlaceOrder subCategory = items.get(position);

            view.item_name.setText("Name : "+subCategory.getItem_name());
            view.quantity.setText(subCategory.getQuantity());//"Address : "+
            view.item_total_price.setText("Rs."+subCategory.getTotal_price());//"Mobile : "+
            view.time.setText(subCategory.getTime());//"Mobile : "+

            if (subCategory.getDelivery_status().equals("Pending")){
                view.delivery_status.setText(subCategory.getDelivery_status());
                view.delivery_status.setTextColor(Color.YELLOW);
            }else
            {
                view.delivery_status.setText(subCategory.getDelivery_status());
                view.delivery_status.setTextColor(Color.GREEN);
            }

            String img=subCategory.getImage();
            byte[] decodedString = Base64.decode(img, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            view.image.setImageBitmap(decodedByte);



            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });


        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, PlaceOrder obj, int pos);
    }


}
