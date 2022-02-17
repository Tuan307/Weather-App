package com.example.weatherapp.Model;

public class WeatherTVModel {
    private String time;
    private String temper;
    private String icon;
    private String windspeed;

    public WeatherTVModel(String time, String temper, String icon, String windspeed) {
        this.time = time;
        this.temper = temper;
        this.icon = icon;
        this.windspeed = windspeed;
    }

    public WeatherTVModel() {
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemper() {
        return temper;
    }

    public void setTemper(String temper) {
        this.temper = temper;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindspeed() {
        return windspeed;
    }

    public void setWindspeed(String windspeed) {
        this.windspeed = windspeed;
    }
}
