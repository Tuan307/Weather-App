package com.example.weatherapp.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.weatherapp.Model.NextDay;
import com.example.weatherapp.R;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NextDayWeatherAdapter extends RecyclerView.Adapter<NextDayWeatherAdapter.ViewHolder> {
    private ArrayList<NextDay> arrayList;

    public NextDayWeatherAdapter(ArrayList<NextDay> arrayList) {
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.allweekitem,parent,false);
        return new NextDayWeatherAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NextDay nextDay = arrayList.get(position);
        holder.txtmin.setText(nextDay.getMin()+"°c");
        holder.txtmax.setText(nextDay.getMax()+"°c");
        Picasso.get().load("https:".concat(nextDay.getImg())).into(holder.img);
        holder.txtchance.setText(nextDay.getRainning()+"%");
        try {
            String s = nextDay.getTime();
            Date date= new SimpleDateFormat("yyyy-MM-dd").parse(s);
            String dayOfWeek = new SimpleDateFormat("EEEE", Locale.ENGLISH).format(date);
            holder.txtday.setText(dayOfWeek);

        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txtmin,txtmax,txtday,txtchance;
        ImageView img;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtmin = itemView.findViewById(R.id.txtmin);
            txtmax = itemView.findViewById(R.id.txtmax);
            txtday = itemView.findViewById(R.id.txtdayinweek);
            txtchance = itemView.findViewById(R.id.txtchancerainweek);
            img = itemView.findViewById(R.id.imgiconweek);
        }
    }
}
