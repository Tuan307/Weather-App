package com.example.weatherapp.Model;

public class NextDay {
    private String time;
    private String max;
    private String min;
    private String img;
    private String rainning;

    public NextDay() {
    }

    public NextDay(String time, String max, String min, String img, String rainning) {
        this.time = time;
        this.max = max;
        this.min = min;
        this.img = img;
        this.rainning = rainning;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getMax() {
        return max;
    }

    public void setMax(String max) {
        this.max = max;
    }

    public String getMin() {
        return min;
    }

    public void setMin(String min) {
        this.min = min;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getRainning() {
        return rainning;
    }

    public void setRainning(String rainning) {
        this.rainning = rainning;
    }
}
