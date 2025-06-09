package com.example.work3_weather.data.daily;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyWeatherInfo {

    @SerializedName("location")
    private DailyLocationInfo locationInfo;

    @SerializedName("daily")
    private List<DailyInfo> dailyWeather;

    public DailyLocationInfo getLocationInfo() {
        return locationInfo;
    }

    public void setLocationInfo(DailyLocationInfo pLocationInfo) {
        locationInfo = pLocationInfo;
    }

    public List<DailyInfo> getDailyWeather() {
        return dailyWeather;
    }

    public void setDailyWeather(List<DailyInfo> pDailyWeather) {
        dailyWeather = pDailyWeather;
    }

    @Override
    public String toString() {
        return "DailyWeatherInfo{" +
                "locationInfo=" + locationInfo +
                ", dailyWeather=" + dailyWeather +
                '}';
    }
}
