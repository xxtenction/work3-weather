package com.example.work3_weather.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.work3_weather.R;
import com.example.work3_weather.data.daily.DailyInfo;

import java.util.List;


public class FutureWeatherAdapter extends RecyclerView.Adapter<FutureWeatherAdapter.WeatherViewHolder> {

    private Context mContext;
    private List<DailyInfo> mWeatherBeans;

    public FutureWeatherAdapter(Context context, List<DailyInfo> weatherBeans) {
        mContext = context;
        this.mWeatherBeans = weatherBeans;
    }

    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.weather_item, parent, false);
        WeatherViewHolder weatherViewHolder = new WeatherViewHolder(view);
        return weatherViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        DailyInfo weatherBean = mWeatherBeans.get(position);

        holder.tvWeather.setText("白天："+weatherBean.getText_day() + "\n夜晚："+ weatherBean.getText_night());
        String lHigh = weatherBean.getHigh(); // 最高温度
        String lLow = weatherBean.getLow(); // 最低温度
        holder.tvTem.setText(lLow + "°C");
        holder.tvTemLowHigh.setText(lLow + "°C~" + lHigh + "°C");
        String lWindSpeed = weatherBean.getWind_speed(); // 风力
        String windowDirection = weatherBean.getWind_direction(); // 风向
        holder.tvWin.setText(windowDirection + "风，风力：" + lWindSpeed);

        String lHumidity = weatherBean.getHumidity(); // 湿度
        holder.tvAir.setText("湿度："+ lHumidity);

        holder.tvTem.setText(weatherBean.getLow());

        holder.tvDate.setText(weatherBean.getDate());

        holder.ivWeather.setImageResource(getImgResOfWeather(weatherBean.getCode_day()));
    }

    @Override
    public int getItemCount() {
        return (mWeatherBeans == null) ? 0 : mWeatherBeans.size();
    }


    class WeatherViewHolder extends RecyclerView.ViewHolder {

        TextView tvWeather, tvTem, tvTemLowHigh, tvWin, tvAir, tvDate;
        ImageView ivWeather;

        public WeatherViewHolder(@NonNull View itemView) {
            super(itemView);

            tvWeather = itemView.findViewById(R.id.tv_weather);
            tvAir = itemView.findViewById(R.id.tv_air);
            tvTem = itemView.findViewById(R.id.tv_tem);
            tvTemLowHigh = itemView.findViewById(R.id.tv_tem_low_high);
            tvWin = itemView.findViewById(R.id.tv_win);
            ivWeather = itemView.findViewById(R.id.iv_weather);
            tvDate = itemView.findViewById(R.id.tv_date);
        }


    }

    private int getImgResOfWeather(String weaCode) {
        // xue、lei、shachen、wu、bingbao、yun、yu、yin、qing
        int result = 0;
        switch (weaCode) {
            case "0":
                result = R.drawable.ic_0_2x;
                break;
            case "1":
                result = R.drawable.ic_1_2x;
                break;
            case "2":
                result = R.drawable.ic_2_2x;
                break;
            case "3":
                result = R.drawable.ic_3_2x;
                break;
            case "4":
                result = R.drawable.ic_4_2x;
                break;
            case "5":
                result = R.drawable.ic_5_2x;
                break;
            case "6":
                result = R.drawable.ic_6_2x;
                break;
            case "7":
                result = R.drawable.ic_7_2x;
                break;
            case "8":
                result = R.drawable.ic_8_2x;
                break;
            case "9":
                result = R.drawable.ic_9_2x;
                break;
            case "10":
                result = R.drawable.ic_10_2x;
                break;
            case "11":
                result = R.drawable.ic_11_2x;
                break;
            case "12":
                result = R.drawable.ic_12_2x;
                break;
            case "13":
                result = R.drawable.ic_13_2x;
                break;
            case "14":
                result = R.drawable.ic_14_2x;
                break;
            case "15":
                result = R.drawable.ic_15_2x;
                break;
            case "16":
                result = R.drawable.ic_16_2x;
                break;
            case "17":
                result = R.drawable.ic_17_2x;
                break;
            case "18":
                result = R.drawable.ic_18_2x;
                break;
            case "19":
                result = R.drawable.ic_19_2x;
                break;
            case "20":
                result = R.drawable.ic_20_2x;
                break;
            case "21":
                result = R.drawable.ic_21_2x;
                break;
            case "22":
                result = R.drawable.ic_22_2x;
                break;
            case "23":
                result = R.drawable.ic_23_2x;
                break;
            case "24":
                result = R.drawable.ic_24_2x;
                break;
            case "25":
                result = R.drawable.ic_25_2x;
                break;
            case "26":
                result = R.drawable.ic_26_2x;
                break;
            case "27":
                result = R.drawable.ic_27_2x;
                break;
            case "28":
                result = R.drawable.ic_28_2x;
                break;
            case "29":
                result = R.drawable.ic_29_2x;
                break;
            case "30":
                result = R.drawable.ic_30_2x;
                break;
            case "31":
                result = R.drawable.ic_31_2x;
                break;
            case "32":
                result = R.drawable.ic_32_2x;
                break;
            case "33":
                result = R.drawable.ic_33_2x;
                break;
            case "34":
                result = R.drawable.ic_34_2x;
                break;
            case "35":
                result = R.drawable.ic_35_2x;
                break;
            case "36":
                result = R.drawable.ic_36_2x;
                break;
            case "37":
                result = R.drawable.ic_37_2x;
                break;
            case "38":
                result = R.drawable.ic_38_2x;
                break;
            default:
                result = R.drawable.ic_0_2x;
                break;
        }
        return result;
    }
}
