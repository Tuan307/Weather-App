package com.example.weatherapp.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Model.WeatherTVModel;
import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WeatherRVAdapter extends RecyclerView.Adapter<WeatherRVAdapter.WeatherRVAdapter1> {
    private Context context;
    private ArrayList<WeatherTVModel> arrayList;

    public WeatherRVAdapter(Context context, ArrayList<WeatherTVModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public WeatherRVAdapter1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.weather_item_layout,parent,false);
        return new WeatherRVAdapter.WeatherRVAdapter1(view);
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherRVAdapter1 holder, int position) {
        WeatherTVModel weatherTVModel = arrayList.get(position);
        if(weatherTVModel == null)
        {
            Log.e("null object","True");
        }
        else
        {
            String imgurl = weatherTVModel.getIcon();
            Picasso.get().load("https:".concat(imgurl)).into(holder.imgcondition);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyydd-MM-dd hh:mm");
            SimpleDateFormat simpleDateFormat1 = new SimpleDateFormat("hh:mm aa");

                Log.d("AAA",weatherTVModel.getTime());
                holder.timetv.setText((weatherTVModel.getTime()));
            holder.tempertv.setText(weatherTVModel.getTemper()+"°c");
           // holder.windtv.setText(weatherTVModel.getWindspeed()+"\n"+"Km/h");
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class WeatherRVAdapter1 extends RecyclerView.ViewHolder {
        private TextView windtv,tempertv,timetv;
        private ImageView imgcondition;
        public WeatherRVAdapter1(@NonNull View itemView) {
            super(itemView);
//            windtv = itemView.findViewById(R.id.TVwindspeed);
            tempertv = itemView.findViewById(R.id.TVtemperature);
            timetv = itemView.findViewById(R.id.TVtime);
            imgcondition = itemView.findViewById(R.id.IMGcondition);
        }
    }
}
