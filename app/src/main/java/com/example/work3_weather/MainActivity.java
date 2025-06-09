package com.example.work3_weather;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.work3_weather.adapter.FutureWeatherAdapter;
import com.example.work3_weather.data.daily.DailyInfo;
import com.example.work3_weather.data.daily.DailyResponse;
import com.example.work3_weather.data.daily.DailyWeatherInfo;
import com.example.work3_weather.data.today.LocationInfo;
import com.example.work3_weather.data.today.WeatherInfo;
import com.example.work3_weather.data.today.WeatherNow;
import com.example.work3_weather.data.today.WeatherResponse;
import com.example.work3_weather.util.NetUtil;
import com.example.work3_weather.util.WeatherDBManager;
import com.google.gson.Gson;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final int CODE_TODAY = 0;
    private final int CODE_DAILY = 1;

    private AppCompatSpinner mSpinner;
    private ArrayAdapter<String> mSpAdapter;
    private String[] mCities;

    private TextView tvWeather, tvTem, tvTemLowHigh, tvWin, tvHumidity;
    private EditText etCity;
    private ImageView ivWeather, ivSearch;
    private RecyclerView rlvFutureWeather;
    private FutureWeatherAdapter mWeatherAdapter;

    private WeatherNow nowWeather;
    private LocationInfo locationInfo;
    private String updateTime;

    private WeatherDBManager dbManager;

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            try {
                if (msg.what == CODE_TODAY) {
                    String weather = (String) msg.obj;
                    Gson gson = new Gson();
                    WeatherResponse weatherResponse = gson.fromJson(weather, WeatherResponse.class);
                    updateUiOfWeather(weatherResponse);
                } else if (msg.what == CODE_DAILY) {
                    String weather = (String) msg.obj;
                    Gson gson = new Gson();
                    DailyResponse weatherDailyResponse = gson.fromJson(weather, DailyResponse.class);
                    updateDailyWeatherUI(weatherDailyResponse);
                }
            } catch (Exception e) {
                Toast.makeText(MainActivity.this, "更新天气失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        // 初始化数据库管理器
        dbManager = new WeatherDBManager(this);
        dbManager.open();
        
        initView();
        initEvent();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbManager != null) {
            dbManager.close();
        }
    }

    private void initView() {
        tvWeather = findViewById(R.id.tv_weather);
        tvTem = findViewById(R.id.tv_tem);
        tvTemLowHigh = findViewById(R.id.tv_tem_low_high);
        tvWin = findViewById(R.id.tv_win);
        tvHumidity = findViewById(R.id.tv_humidity);
        etCity = findViewById(R.id.et_city);
        ivWeather = findViewById(R.id.iv_weather);
        ivSearch = findViewById(R.id.iv_search);
        rlvFutureWeather = findViewById(R.id.rlv_future_weather);
        mSpinner = findViewById(R.id.sp_city);
        mCities = getResources().getStringArray(R.array.cities);
        mSpAdapter = new ArrayAdapter<>(this, R.layout.sp_item_layout, mCities);
        mSpinner.setAdapter(mSpAdapter);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCity = mCities[position];
                getWeatherNow(selectedCity);
                getDailyWeather(selectedCity);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
    }

    private void initEvent() {
        ivSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String city = etCity.getText().toString().trim();
                if (city == null || city.isEmpty()) {
                    Toast.makeText(MainActivity.this, "请输入查询的城市名", Toast.LENGTH_SHORT).show();
                    return;
                }
                getWeatherNow(city);
                getDailyWeather(city);
                etCity.setText("");
            }
        });
    }

    private void updateUiOfWeather(WeatherResponse weatherBean) {
        Log.i(TAG, "updateUiOfWeekWeather: "+ weatherBean);
        if (weatherBean == null || !weatherBean.getStatusCode().isEmpty()) {
            Log.e(TAG, "updateUiOfWeekWeather: error!");
            Toast.makeText(MainActivity.this, "更新天气失败", Toast.LENGTH_SHORT).show();
            return;
        }
        List<WeatherInfo> lResults = weatherBean.getResults();
        if (lResults == null || lResults.isEmpty()) {
            Log.e(TAG, "updateUiOfWeekWeather: error, lResults is null or empty");
            return;
        }
        nowWeather = lResults.get(0).getXinZhiWeatherNow();
        locationInfo = lResults.get(0).getLocationInfo();
        updateTime = lResults.get(0).getLastUpdate();
        if (nowWeather == null) {
            Log.e(TAG, "updateUiOfWeekWeather: error, nowWeather is null");
            return;
        }
        String lPath = locationInfo.getPath(); // 地区
        tvTem.setText(nowWeather.getTemperature() + "°C\n(" + lPath +")");
        tvWeather.setText(nowWeather.getText() + "\n(" + updateTime + ")");
        ivWeather.setImageResource(getImgResOfWeather(nowWeather.getCode()));

        // 保存到数据库
        if (nowWeather != null && locationInfo != null) {
            dbManager.saveWeatherRecord(
                locationInfo.getPath(),
                nowWeather,
                updateTime
            );
        }
    }

    private void getWeatherNow(String selectedCity) {
        // 检查缓存是否有效
        if (dbManager.isCacheValid(selectedCity)) {
            WeatherDBManager.WeatherRecord record = dbManager.getLatestWeatherRecord(selectedCity);
            if (record != null) {
                // 使用缓存数据更新UI
                updateUIWithCache(record);
                return;
            }
        }

        // 如果缓存无效，则发起网络请求
        NetUtil.doGet(NetUtil.WEATHER_NOW_URL + selectedCity, new NetUtil.OnSuccessListener() {
            @Override
            public void onSuccess(String result) {
                Message msg = Message.obtain();
                msg.what = CODE_TODAY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "获取天气失败：" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUIWithCache(WeatherDBManager.WeatherRecord record) {
        tvTem.setText(record.temperature + "°C");
        tvWeather.setText(record.weather + "\n(" + record.updateTime + ")");
        // 更新其他UI元素...
        Toast.makeText(this, "使用缓存数据", Toast.LENGTH_SHORT).show();
    }

    private void getDailyWeather(String selectedCity) {
        NetUtil.doGet(NetUtil.WEATHER_DAILY_URL + selectedCity + "&start=0&days=5", new NetUtil.OnSuccessListener() {
            @Override
            public void onSuccess(String result) {
                Message msg = Message.obtain();
                msg.what = CODE_DAILY;
                msg.obj = result;
                mHandler.sendMessage(msg);
            }

            @Override
            public void onError(String error) {
                Toast.makeText(MainActivity.this, "获取天气预报失败：" + error, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateDailyWeatherUI(DailyResponse dailyWeather) {
        Log.i(TAG, "updateDailyWeatherUI: " + dailyWeather);
        if (dailyWeather == null || !dailyWeather.getStatusCode().isEmpty()) {
            Log.e(TAG, "updateDailyWeatherUI: dailyWeather null");
            return;
        }

        List<DailyWeatherInfo> lResults = dailyWeather.getResults();
        if (lResults == null || lResults.isEmpty()) {
            Log.e(TAG, "updateDailyWeatherUI: lResults is null");
            return;
        }
        DailyWeatherInfo dailyWeatherInfo = lResults.get(0);
        if (dailyWeatherInfo == null) {
            Log.e(TAG, "updateDailyWeatherUI: dailyWeatherInfo is null");
            return;
        }
        List<DailyInfo> dailyWeatherList = dailyWeatherInfo.getDailyWeather();
        if (dailyWeatherList == null || dailyWeatherList.isEmpty()) {
            Log.e(TAG, "updateDailyWeatherUI: dailyWeatherList is null");
            return;
        }
        DailyInfo todayWeather = dailyWeatherList.get(0);
        String lHigh = todayWeather.getHigh(); // 最高温度
        String lLow = todayWeather.getLow(); // 最低温度
        tvTemLowHigh.setText(lLow + "°C~" + lHigh + "°C");
        String lWindSpeed = todayWeather.getWind_speed(); // 风力
        String windowDirection = todayWeather.getWind_direction(); // 风向
        tvWin.setText(windowDirection + "风，风力：" + lWindSpeed);

        String lHumidity = todayWeather.getHumidity(); // 湿度
        tvHumidity.setText("湿度："+ lHumidity);

        // 未来天气的展示
        mWeatherAdapter = new FutureWeatherAdapter(this, dailyWeatherList);
        rlvFutureWeather.setAdapter(mWeatherAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rlvFutureWeather.setLayoutManager(layoutManager);
        Toast.makeText(this, "天气更新成功！", Toast.LENGTH_SHORT).show();
    }

    private int getImgResOfWeather(String weaCode) {
        try {
            int code = Integer.parseInt(weaCode);

            // 晴天类
            if (code == 0 || code == 32) {
                return R.drawable.ic_0_2x; // 晴天
            }

            // 多云类
            if (code == 1 || code == 2 || code == 33) {
                return R.drawable.ic_1_2x; // 多云
            }

            // 阴天类
            if (code == 3) {
                return R.drawable.ic_3_2x; // 阴天
            }

            // 雨天类（小雨、中雨、大雨、暴雨等）
            if (code >= 4 && code <= 10) {
                return R.drawable.ic_4_2x; // 雨天通用图标
            }

            // 雪天类（小雪、中雪、大雪、暴雪等）
            if (code >= 11 && code <= 17) {
                return R.drawable.ic_11_2x; // 雪天通用图标
            }

            // 冰雹类
            if (code == 18) {
                return R.drawable.ic_18_2x; // 冰雹
            }

            // 雾天类
            if (code == 19 || code == 20) {
                return R.drawable.ic_19_2x; // 雾天
            }

            // 霾类
            if (code == 21) {
                return R.drawable.ic_21_2x; // 霾
            }

            // 沙尘暴类
            if (code == 22 || code == 23) {
                return R.drawable.ic_22_2x; // 沙尘暴
            }

            // 高温/低温类
            if (code == 24 || code == 25) {
                return R.drawable.ic_24_2x; // 温度异常
            }

            // 天气预警类
            if (code >= 26 && code <= 31) {
                return R.drawable.ic_26_2x; // 预警图标
            }

            // 特殊天气类
            if (code == 34 || code == 35) {
                return R.drawable.ic_34_2x; // 特殊天气
            }

            // 高温天气
            if (code == 36) {
                return R.drawable.ic_36_2x; // 高温
            }

            // 雷暴类
            if (code == 37 || code == 38) {
                return R.drawable.ic_37_2x; // 雷暴
            }

        } catch (NumberFormatException e) {
            Log.e(TAG, "WeatherCode 格式错误: " + weaCode, e);
        }

        // 默认返回晴天图标
        return R.drawable.ic_0_2x;
    }

    private static final String TAG = "WeatherActivity";
}