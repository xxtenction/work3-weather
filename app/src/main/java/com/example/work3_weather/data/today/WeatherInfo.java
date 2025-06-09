package com.example.work3_weather.data.today;

import com.google.gson.annotations.SerializedName;

public class WeatherInfo {
    @SerializedName("location")
    private LocationInfo locationInfo;

    @SerializedName("now")
    private WeatherNow WeatherNow;

    @SerializedName("last_update")
    private  String lastUpdate;

    public LocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(LocationInfo pLocationInfo) {
        locationInfo = pLocationInfo;
    }

    public WeatherNow getXinZhiWeatherNow() {
        return WeatherNow;
    }

    public void setXinZhiWeatherNow(WeatherNow pWeatherNow) {
        WeatherNow = pWeatherNow;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String pLastUpdate) {
        lastUpdate = pLastUpdate;
    }

    @Override
    public String toString() {
        return "WeatherInfo{" +
                "locationInfo=" + locationInfo +
                ", xinZhiWeatherNow=" + WeatherNow +
                ", lastUpdate='" + lastUpdate + '\'' +
                '}';
    }
}
