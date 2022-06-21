package com.roydon.weatherforcast3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.roydon.weatherforcast3.adapter.FutureWeatherAdapter;
import com.roydon.weatherforcast3.adapter.HourWeatherAdapter;
import com.roydon.weatherforcast3.bean.CityBean;
import com.roydon.weatherforcast3.bean.DayWeatherBean;
import com.roydon.weatherforcast3.bean.WeatherBean;
import com.roydon.weatherforcast3.db.DBUtils;
import com.roydon.weatherforcast3.utils.NetworkUtil;
import com.roydon.weatherforcast3.utils.ToastUtil;
import com.roydon.weatherforcast3.utils.WeatherImgUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    TextView tvCity, tvTime, tvWeather, tvWeek, tvTem, tvTemLowHigh, tvWin, tvAir,tv_tips;
    ImageView ivWeather;//天气图标
    ImageView ivAdd;//添加城市事件
    ImageView ivMore;//城市管理

    private String[] mCities;

    private DayWeatherBean dayWeather;

    String nowCity = "";

    private HourWeatherAdapter mHourAdapter;
    private FutureWeatherAdapter mWeatherAdapter;

    private RecyclerView rlvHourWeather;
    private RecyclerView rlvFutureWeather;

    DBUtils dbUtils = new DBUtils(MainActivity.this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
        moreView();



    }





    private void initView() {
        /**
         * 注册
         */
        tvCity = (TextView) findViewById(R.id.tv_city);
        tvTime = (TextView) findViewById(R.id.tv_time);
        tvWeather = (TextView) findViewById(R.id.tv_weather);
        tvWeek = (TextView) findViewById(R.id.tv_week);
        tvTem = (TextView) findViewById(R.id.tv_tem);
        tvTemLowHigh = (TextView) findViewById(R.id.tv_tem_low_high);
        tvWin = (TextView) findViewById(R.id.tv_win);
        tvAir = (TextView) findViewById(R.id.tv_air);
        tv_tips=(TextView)findViewById(R.id.tv_tips);

        rlvHourWeather = findViewById(R.id.rlv_hour_weather);
        rlvFutureWeather = findViewById(R.id.rlv_future_weather);

        ivWeather = (ImageView) findViewById(R.id.iv_weather);

        getWeather(nowCity);


    }
    private void moreView() {
        ivAdd = (ImageView) findViewById(R.id.iv_add);
        ivMore = (ImageView) findViewById(R.id.iv_more);

        ivMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (null == dbUtils.getCityByName(tvCity.getText().toString()).getName()) {
                    //不存在就插入
                    dbUtils.insertData(tvCity.getText().toString(),tvTem.getText().toString(),tvTime.getText().toString());
                }
                else {
                    //存在就更新
                    dbUtils.updateByName(tvCity.getText().toString(),tvTem.getText().toString(),tvTime.getText().toString());
//                dbUtils.insertData("西安","30°C","2022-06-19 21:23:35");
//                dbUtils.delCityByName("商丘");
                    List<CityBean> list = dbUtils.getAllCity();
                    Log.d("?????getAllCity", "allList<CityBean>>>>" + list.toString());

//                    ToastUtil.showLongToast(MainActivity.this, "当前位置:" + tvCity.getText());
                    Intent intent = new Intent(MainActivity.this, CityManagerActivity.class);
//                intent.putExtra("nowCity", tvCity.getText());
                    Log.d("?????getCity", "<CityBean>>>>" + tvCity.getText());
//                Log.d("?????getCity", "<CityBean>>>>" + dbUtils.getCityByName(tvCity.getText().toString()));
                    startActivityForResult(intent, 200);


                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 200) {
            String dt = data.getExtras().getString("selectedCity");
            tvCity.setText(dt);
            nowCity=dt;
            getWeather(nowCity);
            ToastUtil.showLongToast(this,nowCity+"天气更新😘~");
        }

    }


    private void getWeather(String cityName) {
        // 开启子线程，请求网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                // 请求网络
                String weatherJson = NetworkUtil.getWeatherByCity(cityName);
                // 使用handler将数据传递给主线程
                Message message = Message.obtain();
                message.what = 0;
                message.obj = weatherJson;
                mHandler.sendMessage(message);
            }
        }).start();

    }

    private Handler mHandler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                String weather = (String) msg.obj;
                Log.d("Main", "api天气数据>>>" + weather);
                if (TextUtils.isEmpty(weather)) {
                    Log.d("Main", "api天气数据为空");
                    Toast.makeText(MainActivity.this, "天气数据为空！", Toast.LENGTH_LONG).show();
                    return;
                }
                Gson gson = new Gson();
                WeatherBean weatherBean = gson.fromJson(weather, WeatherBean.class);
                if (weatherBean != null) {
                    Log.d("Main", "weatherBean>>>" + weatherBean.toString());
                }


                /**
                 * 小时天气
                 */
                hourDataShow(weatherBean);


            }

        }
    };

    private void hourDataShow(WeatherBean weatherBean) {
        if (weatherBean == null) {
            return;
        }

        dayWeather = weatherBean.getData().get(0);//当天天气
        if (dayWeather == null) {
            return;
        }
        tvCity.setText(weatherBean.getCity());
        tvTime.setText(weatherBean.getUpdate_time());


        /**
         * 当天天气
         */
        tvWeather.setText(dayWeather.getWea());
        tvTem.setText(dayWeather.getTem());
        tvTemLowHigh.setText(dayWeather.getTem2() + "/" + dayWeather.getTem1());
        tvWeek.setText(dayWeather.getWeek());
        tvWin.setText(dayWeather.getWin()[0] + dayWeather.getWin_speed());
        tvAir.setText( dayWeather.getAir() + " | " + dayWeather.getAir_level());
        tv_tips.setText("👒："+dayWeather.getAir_tips());
        ivWeather.setImageResource(WeatherImgUtil.getImgResOfWeather(dayWeather.getWea_img()));


        /**
         * 每小时温度
         */
        mHourAdapter = new HourWeatherAdapter(this, dayWeather.getHoursWeatherBeanList());
        rlvHourWeather.setAdapter(mHourAdapter);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rlvHourWeather.setLayoutManager(layoutManager);

        /**
         * 未来七天天气
         */
        List<DayWeatherBean> futureWeather = weatherBean.getData();
        futureWeather.remove(0);//除去当天天气
        mWeatherAdapter = new FutureWeatherAdapter(this, futureWeather);
        rlvFutureWeather.setAdapter(mWeatherAdapter);
        rlvFutureWeather.setLayoutManager(new LinearLayoutManager(this));

    }



}