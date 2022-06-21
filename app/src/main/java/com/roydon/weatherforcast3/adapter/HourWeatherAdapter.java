package com.roydon.weatherforcast3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roydon.weatherforcast3.R;
import com.roydon.weatherforcast3.bean.HoursWeatherBean;
import com.roydon.weatherforcast3.utils.WeatherImgUtil;

import java.util.List;

public class HourWeatherAdapter extends RecyclerView.Adapter<HourWeatherAdapter.HourViewHolder> {
    private Context mContext;
    private List<HoursWeatherBean> mHoursWeatherBeans;

    public HourWeatherAdapter(Context context, List<HoursWeatherBean> hoursWeatherBeans) {
        mContext = context;
        this.mHoursWeatherBeans = hoursWeatherBeans;
    }

    @NonNull
    @Override
    public HourViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.hour_item_layout, parent, false);
        HourViewHolder hourViewHolder = new HourViewHolder(view);
        return hourViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull HourViewHolder holder, int position) {
        HoursWeatherBean hoursweatherBean = mHoursWeatherBeans.get(position);

        holder.tvTem.setText(hoursweatherBean.getTem()+"℃");
        holder.tvHours.setText(hoursweatherBean.getHours().substring(0,2)+":00");
        holder.ivWeather.setImageResource(WeatherImgUtil.getImgResOfWeather(hoursweatherBean.getWeaImg()));
    }

    @Override
    public int getItemCount() {

        return (mHoursWeatherBeans == null) ? 0 : mHoursWeatherBeans.size();
    }


    class HourViewHolder extends RecyclerView.ViewHolder {

        TextView tvHours, tvTem;
        ImageView ivWeather;

        public HourViewHolder(@NonNull View itemView) {
            super(itemView);

            tvHours = itemView.findViewById(R.id.tv_hours);
            tvTem = itemView.findViewById(R.id.tv_tem);
            ivWeather = itemView.findViewById(R.id.iv_weather);
        }


    }


}
