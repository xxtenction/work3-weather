package com.example.work3_weather.data.daily;

public class DailyLocationInfo {

    private String id;
    private String name;
    private String country;
    private String path;
    private String timezone;
    private String timezone_offset;
    public void setId(String id) {
        this.id = id;
    }
    public String getId() {
        return id;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    public void setCountry(String country) {
        this.country = country;
    }
    public String getCountry() {
        return country;
    }

    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }
    public String getTimezone() {
        return timezone;
    }

    public void setTimezone_offset(String timezone_offset) {
        this.timezone_offset = timezone_offset;
    }
    public String getTimezone_offset() {
        return timezone_offset;
    }

}