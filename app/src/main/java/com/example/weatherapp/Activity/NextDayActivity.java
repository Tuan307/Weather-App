package com.example.weatherapp.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Adapter.NextDayWeatherAdapter;
import com.example.weatherapp.Model.NextDay;
import com.example.weatherapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class NextDayActivity extends AppCompatActivity {
    private ImageView imgback;
    private RecyclerView recyclerView;
    private NextDayWeatherAdapter nextDayWeatherAdapter;
    private ArrayList<NextDay> nextDayArrayList;
    private String city;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_next_day);
        addControls();
        Intent intent = getIntent();
        city = intent.getStringExtra("city");
        addEvents();
    }

    private void addEvents() {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=f0bf7031f44b468592e52234220902&q="+city+"&days=7&aqi=yes&alerts=no";
        RequestQueue requestQueue = Volley.newRequestQueue(NextDayActivity.this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONObject jsonObject = response.getJSONObject("forecast");
                    JSONArray jsonArray = jsonObject.getJSONArray("forecastday");
                    for(int i = 0 ;i<jsonArray.length();i++)
                    {

                        JSONObject jsonObj= jsonArray.getJSONObject(i);
                        String time = jsonObj.getString("date");
                        String max = jsonObj.getJSONObject("day").getString("maxtemp_c");
                        String min = jsonObj.getJSONObject("day").getString("mintemp_c");
                        String img = jsonObj.getJSONObject("day").getJSONObject("condition").getString("icon");
                        String chance = jsonObj.getJSONObject("day").getString("daily_chance_of_rain");
                        nextDayArrayList.add(new NextDay(time,max,min,img,chance));
                    }
                    nextDayWeatherAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
                Log.e("AAA","ERROR");
            }
        });
        requestQueue.add(jsonObjectRequest);
        imgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(NextDayActivity.this,MainActivity.class));
                finish();
            }
        });
    }

    private void addControls() {
        imgback = findViewById(R.id.imgback);
        nextDayArrayList = new ArrayList<>();
        recyclerView = findViewById(R.id.recyallweek);
        nextDayWeatherAdapter = new NextDayWeatherAdapter(nextDayArrayList);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(nextDayWeatherAdapter);
    }
}