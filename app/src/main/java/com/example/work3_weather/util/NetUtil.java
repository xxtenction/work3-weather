package com.example.work3_weather.util;

import android.util.Log;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class NetUtil {

    public static final String KEY = "S1clWMLJPOmfFg4Jo";
    public static final String WEATHER_NOW_URL = "https://api.seniverse.com/v3/weather/now.json?key=" + KEY + "&language=zh-Hans&unit=c&location=";
    public static final String WEATHER_DAILY_URL = "https://api.seniverse.com/v3/weather/daily.json?key=" + KEY + "&language=zh-Hans&unit=c&location=";

    private static final String BASE_URL = "https://api.seniverse.com/v3/weather/";

    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    /**
     * 获取当天的天气
     *
     * @return
     */
    public static void getWeatherToday(String city, OnWeatherListener listener) {
        String url = buildUrl("now.json", city);
        sendRequest(url, listener);
    }

    /**
     * 获取未来5天的天气
     */
    public static void getWeatherDaily(String city, OnWeatherListener listener) {
        String url = buildUrl("daily.json", city) + "&start=0&days=5";
        sendRequest(url, listener);
    }

    /**
     * 构建完整的API请求URL
     */
    private static String buildUrl(String endpoint, String city) {
        return BASE_URL + endpoint +
                "?key=" + KEY +
                "&location=" + city +
                "&language=zh-Hans&unit=c";
    }

    /**
     * 发送网络请求
     */
    private static void sendRequest(String url, final OnWeatherListener listener) {
        Log.d("NetUtil", "请求URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("NetUtil", "请求失败: " + e.getMessage(), e);
                if (listener != null) {
                    listener.onError("网络请求失败: " + e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().string();
                    Log.d("NetUtil", "请求成功: " + result);
                    if (listener != null) {
                        listener.onSuccess(result);
                    }
                } else {
                    Log.e("NetUtil", "请求失败，状态码: " + response.code());
                    if (listener != null) {
                        listener.onError("请求失败，状态码: " + response.code());
                    }
                }
            }
        });
    }

    /**
     * 发送GET请求（支持缓存）
     */
    public static void doGet(String url, final OnSuccessListener listener) {
        Log.d("NetUtil", "请求URL: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("NetUtil", "请求失败: " + e.getMessage(), e);
                if (listener != null) {
                    listener.onError("网络请求失败: " + e.getMessage());
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (response.isSuccessful() && response.body() != null) {
                    String result = response.body().string();
                    Log.d("NetUtil", "请求成功: " + result);
                    if (listener != null) {
                        listener.onSuccess(result);
                    }
                } else {
                    Log.e("NetUtil", "请求失败，状态码: " + response.code());
                    if (listener != null) {
                        listener.onError("请求失败，状态码: " + response.code());
                    }
                }
            }
        });
    }

    /**
     * 天气请求回调接口
     */
    public interface OnWeatherListener {
        void onSuccess(String result);
        void onError(String error);
    }

    /**
     * 通用请求回调接口
     */
    public interface OnSuccessListener {
        void onSuccess(String result);
        void onError(String error);
    }
}