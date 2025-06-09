package com.example.work3_weather.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.work3_weather.data.today.WeatherNow;

public class WeatherDBManager {
    private static final String TAG = "WeatherDBManager";
    private WeatherDBHelper dbHelper;
    private SQLiteDatabase database;

    public WeatherDBManager(Context context) {
        dbHelper = new WeatherDBHelper(context);
    }

    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    // 保存天气记录
    public long saveWeatherRecord(String city, WeatherNow weatherNow, String updateTime) {
        ContentValues values = new ContentValues();
        values.put(WeatherDBHelper.COLUMN_CITY, city);
        values.put(WeatherDBHelper.COLUMN_TEMPERATURE, weatherNow.getTemperature());
        values.put(WeatherDBHelper.COLUMN_WEATHER, weatherNow.getText());
        values.put(WeatherDBHelper.COLUMN_UPDATE_TIME, updateTime);
        values.put(WeatherDBHelper.COLUMN_QUERY_TIME, System.currentTimeMillis());

        return database.insert(WeatherDBHelper.TABLE_WEATHER_HISTORY, null, values);
    }

    // 获取最近的天气记录
    public WeatherRecord getLatestWeatherRecord(String city) {
        String[] columns = {
            WeatherDBHelper.COLUMN_TEMPERATURE,
            WeatherDBHelper.COLUMN_WEATHER,
            WeatherDBHelper.COLUMN_UPDATE_TIME,
            WeatherDBHelper.COLUMN_QUERY_TIME
        };

        String selection = WeatherDBHelper.COLUMN_CITY + " = ?";
        String[] selectionArgs = {city};
        String orderBy = WeatherDBHelper.COLUMN_QUERY_TIME + " DESC";
        String limit = "1";

        Cursor cursor = database.query(
            WeatherDBHelper.TABLE_WEATHER_HISTORY,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            orderBy,
            limit
        );

        WeatherRecord record = null;
        if (cursor != null && cursor.moveToFirst()) {
            record = new WeatherRecord();
            record.temperature = cursor.getString(cursor.getColumnIndex(WeatherDBHelper.COLUMN_TEMPERATURE));
            record.weather = cursor.getString(cursor.getColumnIndex(WeatherDBHelper.COLUMN_WEATHER));
            record.updateTime = cursor.getString(cursor.getColumnIndex(WeatherDBHelper.COLUMN_UPDATE_TIME));
            record.queryTime = cursor.getLong(cursor.getColumnIndex(WeatherDBHelper.COLUMN_QUERY_TIME));
            cursor.close();
        }

        return record;
    }

    // 检查缓存是否有效（默认30分钟）
    public boolean isCacheValid(String city) {
        WeatherRecord record = getLatestWeatherRecord(city);
        if (record == null) {
            return false;
        }

        long currentTime = System.currentTimeMillis();
        long cacheTime = record.queryTime;
        // 30分钟 = 30 * 60 * 1000 毫秒
        return (currentTime - cacheTime) < 30 * 60 * 1000;
    }

    // 清除指定城市的缓存
    public int clearCityCache(String city) {
        String whereClause = WeatherDBHelper.COLUMN_CITY + " = ?";
        String[] whereArgs = {city};
        return database.delete(WeatherDBHelper.TABLE_WEATHER_HISTORY, whereClause, whereArgs);
    }

    // 清除所有缓存
    public void clearAllCache() {
        database.delete(WeatherDBHelper.TABLE_WEATHER_HISTORY, null, null);
    }

    // 天气记录数据类
    public static class WeatherRecord {
        public String temperature;
        public String weather;
        public String updateTime;
        public long queryTime;
    }
} 