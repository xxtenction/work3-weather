package com.example.work3_weather.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class WeatherDBHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "weather.db";
    private static final int DATABASE_VERSION = 1;

    // 天气历史记录表
    public static final String TABLE_WEATHER_HISTORY = "weather_history";
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_TEMPERATURE = "temperature";
    public static final String COLUMN_WEATHER = "weather";
    public static final String COLUMN_UPDATE_TIME = "update_time";
    public static final String COLUMN_QUERY_TIME = "query_time";

    // 创建天气历史记录表的SQL语句
    private static final String CREATE_WEATHER_HISTORY_TABLE = 
        "CREATE TABLE " + TABLE_WEATHER_HISTORY + " (" +
        COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
        COLUMN_CITY + " TEXT NOT NULL, " +
        COLUMN_TEMPERATURE + " TEXT, " +
        COLUMN_WEATHER + " TEXT, " +
        COLUMN_UPDATE_TIME + " TEXT, " +
        COLUMN_QUERY_TIME + " INTEGER" +
        ")";

    public WeatherDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_WEATHER_HISTORY_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // 如果需要升级数据库，可以在这里添加升级逻辑
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_WEATHER_HISTORY);
        onCreate(db);
    }
} 