package com.example.work3_weather.data.daily;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DailyResponse {

    @SerializedName("results")
    private List<DailyWeatherInfo> results;

    @SerializedName("status")
    private String status = "";

    @SerializedName("status_code")
    private String statusCode = "";

    public List<DailyWeatherInfo> getResults() {
        return results;
    }

    public void setResults(List<DailyWeatherInfo> pResults) {
        results = pResults;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String pStatus) {
        status = pStatus;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String pStatusCode) {
        statusCode = pStatusCode;
    }

    @Override
    public String toString() {
        return "XinZhiDailyResponse{" +
                "results=" + results +
                ", status='" + status + '\'' +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}
