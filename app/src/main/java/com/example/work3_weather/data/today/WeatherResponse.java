package com.example.work3_weather.data.today;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class WeatherResponse {
    @SerializedName("results")
    private List<WeatherInfo> results;

    @SerializedName("status")
    private String status = "";

    @SerializedName("status_code")
    private  String statusCode = "";

    public List<WeatherInfo> getResults() {
        return results;
    }

    public void setResults(List<WeatherInfo> pResults) {
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
        return "WeatherResponse{" +
                "results=" + results +
                ", status='" + status + '\'' +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }
}
