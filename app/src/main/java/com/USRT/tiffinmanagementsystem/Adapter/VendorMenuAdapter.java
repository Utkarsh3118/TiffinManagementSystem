package com.USRT.tiffinmanagementsystem.Adapter;


import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.USRT.tiffinmanagementsystem.Model.Menu;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class VendorMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

private List<Menu> items = new ArrayList<>();
        Bitmap bmp;
private Context ctx;
private OnItemClickListener mOnItemClickListener;
private OnMoreButtonClickListener onMoreButtonClickListener;

public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
        }

public void setOnMoreButtonClickListener(final OnMoreButtonClickListener onMoreButtonClickListener) {
        this.onMoreButtonClickListener = onMoreButtonClickListener;
        }

public VendorMenuAdapter(Context context, List<Menu> items) {
        this.items = items;
        ctx = context;
        }

public class OriginalViewHolder extends RecyclerView.ViewHolder {
    public ImageView image;
    public TextView title,type;
    public TextView initial_quantity,price;
    //public ImageButton more;
    public View lyt_parent;

    public OriginalViewHolder(View v) {
        super(v);
        image = (ImageView) v.findViewById(R.id.image);
        title = (TextView) v.findViewById( R.id.title);
        type = (TextView) v.findViewById( R.id.type);
        initial_quantity = (TextView) v.findViewById( R.id.initial_quantity);
        price = (TextView) v.findViewById(R.id.price);
        //more = (ImageButton) v.findViewById(R.id.more);
        lyt_parent = (View) v.findViewById(R.id.lyt_parent);
    }
}

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_menu_items, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final Menu p = items.get(position);
            view.title.setText(p.getItem_name());
            String type = p.getType();
            if(type.equals( "Veg" )){
                view.type.setBackgroundColor( Color.parseColor("#9CCC65"));//green
                view.type.setText(p.getType() );
            }else{
                view.type.setBackgroundColor( Color.parseColor("#FF7043"));//red
                view.type.setText(p.getType() );
            }


            view.initial_quantity.setText("Qty :"+p.getQuantity());
            view.price.setText("Rs."+p.getPrice());
            final String image=p.getPhoto();
            Log.e("Image stud",image);
            byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            view.image.setImageBitmap(decodedByte);

            //Tools.displayImageOriginal(ctx, view.image, p.image);
            view.lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        mOnItemClickListener.onItemClick(view, items.get(position), position);
                    }
                }
            });

          /*  view.more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onMoreButtonClickListener == null) return;
                    onMoreButtonClick(view, p);
                }
            });*/
        }
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

public interface OnItemClickListener {
    void onItemClick(View view, Menu obj, int pos);
}

public interface OnMoreButtonClickListener {
    void onItemClick(View view, Menu obj, MenuItem item);
}


}
