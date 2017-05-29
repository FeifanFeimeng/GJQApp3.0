package com.ustc.gjqapp.bean.WeatherBean;

import java.util.List;

/**
 * Created by MaLei on 2017/3/19 0019.
 * Email:ml1995@mail.ustc.edu.cn
 */

public class Results {
    private String currentCity;
    private String pm25;
    private List<Weather> weather_data;
    private List<IndexData> index;

    public String getCurrentCity() {
        return currentCity;
    }

    public void setCurrentCity(String currentCity) {
        this.currentCity = currentCity;
    }

    public String getPM25() {
        return pm25;
    }

    public void setPM25(String pm25) {
        this.pm25 = pm25;
    }

    public List<Weather> getWeather_data() {
        return weather_data;
    }

    public void setWeather_data(List<Weather> weatherData) {
        weather_data = weatherData;
    }

    public List<IndexData> getIndext() {
        return index;
    }

    public void setIndex(List<IndexData> indexData) {
        index = indexData;
    }
}
