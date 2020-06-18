package com.USRT.tiffinmanagementsystem.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.CountDownTimer;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.USRT.tiffinmanagementsystem.Model.Vendor;
import com.USRT.tiffinmanagementsystem.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class AllVendorsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Vendor> items = new ArrayList<>();

    private Context ctx;
    private OnItemClickListener mOnItemClickListener;
    /*int hours;
    int min = 0;
    int days;
    long difference;*/


    public void setOnItemClickListener(final OnItemClickListener mItemClickListener) {
        this.mOnItemClickListener = mItemClickListener;
    }



    public AllVendorsAdapter(Context context, List<Vendor> items) {
        this.items = items;
        ctx = context;
    }

    public class OriginalViewHolder extends RecyclerView.ViewHolder {
        public ImageView image;
        public TextView edt_vname,edt_mobile,edt_vaddress,edt_timer;
        public View lyt_parent;

        public OriginalViewHolder(View v) {
            super(v);
            image = (ImageView) v.findViewById(R.id.image);
            edt_vname = (TextView) v.findViewById(R.id.edt_vname);
            edt_mobile = (TextView) v.findViewById(R.id.edt_mobile);
            edt_vaddress = (TextView) v.findViewById(R.id.edt_vaddress);
            edt_timer = (TextView) v.findViewById(R.id.edt_timer);

            lyt_parent = (View) v.findViewById(R.id.lyt_parent);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder vh;
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_view_all_vendors, parent, false);
        vh = new OriginalViewHolder(v);
        return vh;
    }

    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof OriginalViewHolder) {
            final OriginalViewHolder view = (OriginalViewHolder) holder;

            final Vendor p = items.get(position);

            view.edt_vname.setText(p.getVname());
            view.edt_mobile.setText(p.getVphone());
            view.edt_vaddress.setText(p.getVaddress());
           // view.edt_timer.setText(p.getLunchtime()  +"dineer : "+p.getDinnertime());


            String time = p.getLunchtime();//"02:30"; //mm:ss
            String[] units = time.split(":"); //will break the string up into an array
            int hours = Integer.parseInt(units[0]); //first element
            int minutes = Integer.parseInt(units[1]); //second element
            int duration = 60 * hours + minutes; //add up our values

            String dtime = p.getDinnertime();//"02:30"; //mm:ss
            String[] dunits = dtime.split(":"); //will break the string up into an array
            int dhours = Integer.parseInt(dunits[0]); //first element
            int dminutes = Integer.parseInt(dunits[1]); //second element
            int dduration = 60 * dhours + dminutes; //add up our values

           // Current Time
            Calendar calender = Calendar.getInstance();
            calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
            Log.e("time",calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE) +  ":" + calender.getActualMinimum(Calendar.SECOND));
            System.out.println(calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE) +  ":" + calender.getActualMinimum(Calendar.SECOND));

            final int hours1=calender.get(Calendar.HOUR_OF_DAY);
            int minutes1=calender.get(Calendar.MINUTE);
            int duration1 = 60 * hours1 + minutes1; //add up our values
            Log.e("======= min1", " :: " + duration1);
            Log.e("======= min", " :: " + duration);

            int minutes_lunch = duration-duration1;
           final int minutes_dinner =  dduration-duration1;
            Log.e("======= minutes_lunch", " :: " + minutes_lunch);
            Log.e("======= minutes_dinner", " :: " + minutes_dinner);
            new CountDownTimer(60*minutes_lunch*1000, 1000) {
                public void onTick(long millisUntilFinished) {
                    long millis = millisUntilFinished;
                    //Convert milliseconds into hour,minute and seconds
                    String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                    view.edt_timer.setText("lunch : "+hms);//set text
                }
                public void onFinish() {
                    //view.edt_timer.setText("TIME'S UP!!");
                            if(hours1>13) {
                                new CountDownTimer(60 * minutes_dinner * 1000, 1000) {
                                    public void onTick(long millisUntilFinished) {
                                        long millis = millisUntilFinished;
                                        //Convert milliseconds into hour,minute and seconds
                                        String hms = String.format("%02d:%02d:%02d", TimeUnit.MILLISECONDS.toHours(millis), TimeUnit.MILLISECONDS.toMinutes(millis) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millis)), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));
                                        view.edt_timer.setText("dinner : " + hms);//set text
                                    }

                                    public void onFinish() {
                                        view.edt_timer.setText("TIME'S UP!!");
                                        //Update_Assessment(); //On finish change timer text
                                    }
                                }.start();
                            }else{
                                view.edt_timer.setText("TIME'S UP!!");
                            }
                }
            }.start();

            byte[] decodedString = Base64.decode(p.getPhoto(), Base64.DEFAULT);
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
        void onItemClick(View view, Vendor obj, int pos);
    }

    public int difftime() {
        int hours;
        int min = 0;
        int days;
        long difference;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");

            Calendar calender = Calendar.getInstance();
            calender.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));

            Date date1 = simpleDateFormat.parse( calender.get(Calendar.HOUR_OF_DAY) + ":" + calender.get(Calendar.MINUTE));
            Date date2 = simpleDateFormat.parse("04:00 PM");
            //Date date1 = simpleDateFormat.parse("08:00 AM");
            //Date date2 = simpleDateFormat.parse("04:00 PM");

            difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60 * 24));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            Log.e("======= Hours", " :: " + hours);
            Log.e("======= min", " :: " + min);
            return min;
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        return min;
    }


}
