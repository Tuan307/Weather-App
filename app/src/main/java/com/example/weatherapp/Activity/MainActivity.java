package com.example.weatherapp.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.weatherapp.Adapter.WeatherRVAdapter;
import com.example.weatherapp.Model.WeatherTVModel;
import com.example.weatherapp.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RelativeLayout rlhome;
    private ProgressBar progressBar;
    private TextView citynametxt, tempertxt, conditiontxt, txtrain, txtnext;
    private TextInputEditText cityedt;
    private ImageView imgicon, imgback, imgseach, backIV;
    private RecyclerView recyweather;
    private ArrayList<WeatherTVModel> arrayList;
    private WeatherRVAdapter weatherRVAdapter;
    private LocationManager locationManager;
    private int PERMISSION_CODE = 1;
    private String cityname;
    private FusedLocationProviderClient fusedLocationProviderClient;

    //°c
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        addControls();
        addEvents();
    }

    private void addEvents() {
        getWeatherInfo(cityname);
        imgseach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = cityedt.getText().toString();
                cityname = city;
                if (city.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Please enter city name", Toast.LENGTH_LONG).show();
                } else {
                    citynametxt.setText(city);
                    getWeatherInfo(city);
                }
            }
        });
        txtnext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NextDayActivity.class);
                intent.putExtra("city", cityname);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("A", "Permission granted");
            } else {
                Toast.makeText(getApplicationContext(), "Please provide permissions", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void getWeatherInfo(String city) {
        String url = "https://api.weatherapi.com/v1/forecast.json?key=f0bf7031f44b468592e52234220902&q=" + city + "&days=7&aqi=yes&alerts=no";
        citynametxt.setText(city);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                progressBar.setVisibility(View.GONE);
                rlhome.setVisibility(View.VISIBLE);
                arrayList.clear();
                try {
                    String loca = response.getJSONObject("location").getString("country");
                    citynametxt.setText(city + "\n" + loca);
                    String temper = response.getJSONObject("current").getString("temp_c");
                    tempertxt.setText(temper + "°c");
                    int isDay = response.getJSONObject("current").getInt("is_day");
                    String condition = response.getJSONObject("current").getJSONObject("condition").getString("text");
                    String icon = response.getJSONObject("current").getJSONObject("condition").getString("icon");
                    Picasso.get().load("https:".concat(icon)).into(imgicon);
                    conditiontxt.setText(condition);
                    if (isDay == 1) {
                        // morning https://wallpaperaccess.com/full/1442152.jpg
                        Picasso.get().load("https://wallpaperaccess.com/full/1442152.jpg").into(backIV);
                    } else {
                        //night https://i.pinimg.com/originals/09/a4/bc/09a4bcf20053b5d4f919d2ac1efc4e73.jpg\
                        //https://i.pinimg.com/originals/a7/3c/eb/a73ceb211689466ebd331402b414b7d1.jpg
                        //https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcShdXlDEirPoAQgcA2VLjfAf2Iyi6gSs9s5nw&usqp=CAU
                        //https://images.unsplash.com/photo-1489549132488-d00b7eee80f1?ixlib=rb-1.2.1&ixid=MnwxMjA3fDB8MHxleHBsb3JlLWZlZWR8MTl8fHxlbnwwfHx8fA%3D%3D&w=1000&q=80
                        Picasso.get().load("https://www.enwallpaper.com/wp-content/uploads/2021/02/crop-25.jpg").into(backIV);
                    }

                    JSONObject jsonObject = response.getJSONObject("forecast");
                    JSONArray jsonArray = jsonObject.getJSONArray("forecastday");
                    JSONObject currjson = jsonArray.getJSONObject(0);
                    String changerain = currjson.getJSONObject("day").getString("daily_chance_of_rain");
                    txtrain.setText(changerain + "%");
                    JSONArray hourArray = currjson.getJSONArray("hour");
                    for (int i = 0; i < hourArray.length(); i++) {
                        JSONObject jsonObject1 = hourArray.getJSONObject(i);
                        String time = jsonObject1.getString("time");
                        String tem_c = jsonObject1.getString("temp_c");
                        String img = jsonObject1.getJSONObject("condition").getString("icon");
                        String text = jsonObject1.getJSONObject("condition").getString("text");
                        String wind = jsonObject1.getString("wind_kph");
                        arrayList.add(new WeatherTVModel(time, tem_c, img, wind));
                    }
                    weatherRVAdapter.notifyDataSetChanged();
                } catch (JSONException jsonException) {
                    jsonException.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Please enter valid city name...", Toast.LENGTH_LONG).show();
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    private String getCityName(double longti, double lati) {
        String name = "Not found";
        Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
        try {
            List<Address> addressList = gcd.getFromLocation(lati, longti, 10);
            for (Address address : addressList) {
                if (address != null) {
                    String cityn = address.getLocality();
                    if (cityn != null && !cityn.equals("")) {
                        name = cityn;
                    } else {
                        Log.e("City", "NONE");
                        //Toast.makeText(getApplicationContext(),"City not found",Toast.LENGTH_LONG).show();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return name;
    }

    private void addControls() {
        rlhome = findViewById(R.id.rlvhome);
        backIV = findViewById(R.id.backIV);
        progressBar = findViewById(R.id.idPBLoading);
        citynametxt = findViewById(R.id.txtcityname);
        tempertxt = findViewById(R.id.txttemper);
        conditiontxt = findViewById(R.id.txtcondition);
        cityedt = findViewById(R.id.tiedtcity);
        imgicon = findViewById(R.id.imgicon);
        imgseach = findViewById(R.id.imgsearch);
        recyweather = findViewById(R.id.recyweather);
        arrayList = new ArrayList<>();
        weatherRVAdapter = new WeatherRVAdapter(this, arrayList);
        recyweather.setHasFixedSize(true);
        recyweather.setAdapter(weatherRVAdapter);
        txtrain = findViewById(R.id.txtchanerain);
        txtnext = findViewById(R.id.txtweatherallweek);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(MainActivity.this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        }
        else
        {
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSION_CODE);
        }

    }

    @SuppressLint("MissingPermission")
    private void getLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
        {
            fusedLocationProviderClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    Location location = task.getResult();
                    if(location!=null)
                    {
                        cityname = getCityName(location.getLongitude(),location.getLatitude());
                    }
                    else
                    {
                        com.google.android.gms.location.LocationRequest locationRequest = new com.google.android.gms.location.LocationRequest()
                                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                                .setInterval(1000)
                                .setFastestInterval(1000)
                                .setNumUpdates(1);
                        LocationCallback locationCallback = new LocationCallback() {
                            @Override
                            public void onLocationResult(@NonNull LocationResult locationResult) {
                                Location location1 = locationResult.getLastLocation();
                                cityname = getCityName(location1.getLongitude(),location1.getLatitude());
                            }
                        };
                        fusedLocationProviderClient.requestLocationUpdates(locationRequest,locationCallback, Looper.myLooper());
                        Toast.makeText(getApplicationContext(),"None",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}