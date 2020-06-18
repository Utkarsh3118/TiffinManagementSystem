package com.USRT.tiffinmanagementsystem.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.USRT.tiffinmanagementsystem.Model.Feedback;
import com.USRT.tiffinmanagementsystem.R;

import java.util.ArrayList;
import java.util.List;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHolder> {
    List<Feedback> scname_data = new ArrayList<Feedback>();
    Context context;

    public FeedBackAdapter(Context c, List<Feedback> scname_data) {
        this.scname_data=scname_data;
        context=c;
    }

    @Override
    public FeedBackAdapter.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_feedback, viewGroup, false);
        return new FeedBackAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(FeedBackAdapter.ViewHolder viewHolder, final int i) {
        final Feedback subCategory = scname_data.get(i);

        viewHolder.title.setText(subCategory.getComment() );//"Comment : "+
      //"Address : "+
        viewHolder.rating_bar.setRating(subCategory.getRate_val());


        if(subCategory.getRate_val()<1){

            viewHolder.ratingval.setText(""+subCategory.getRate_val()+"   (ohh ho...)");

        }else if(subCategory.getRate_val()<2){

            viewHolder.ratingval.setText(""+subCategory.getRate_val()+"   (Ok.)");

        }else if(subCategory.getRate_val()<3){

            viewHolder.ratingval.setText(""+subCategory.getRate_val()+"   (Not bad.)");

        }else if(subCategory.getRate_val()<4){

            viewHolder.ratingval.setText(""+subCategory.getRate_val()+"   (Nice)");

        }else if(subCategory.getRate_val()<5){

            viewHolder.ratingval.setText(""+subCategory.getRate_val()+"   ( Very Nice)");

        }else if(subCategory.getRate_val()==5){

            viewHolder.ratingval.setText(""+subCategory.getRate_val()+"   ( Thank you..!!!)");
        }
    }

    @Override
    public int getItemCount() {
        return scname_data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title,ratingval;//txtcomp,
        RatingBar rating_bar;

        public ViewHolder(View view) {
            super(view);
            context = itemView.getContext();

            title=(TextView) view.findViewById(R.id.title);
            rating_bar=(RatingBar) view.findViewById(R.id.rating_bar);
            ratingval=(TextView) view.findViewById(R.id.ratingval);

        }
    }
}

