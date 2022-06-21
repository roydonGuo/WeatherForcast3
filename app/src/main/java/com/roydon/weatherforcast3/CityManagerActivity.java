package com.roydon.weatherforcast3;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.roydon.weatherforcast3.adapter.AddCityAdapter;
import com.roydon.weatherforcast3.bean.CityBean;
import com.roydon.weatherforcast3.db.DBUtils;
import com.roydon.weatherforcast3.utils.ToastUtil;

import java.util.List;

public class CityManagerActivity extends AppCompatActivity {

    private AddCityAdapter addCityAdapter;
    private RecyclerView rvAddedCity;

    private List<CityBean> cityDbBeanList;
    DBUtils dbUtils= new DBUtils(CityManagerActivity.this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.city_manager);


        initView();
    }


    public void initView() {

        rvAddedCity = findViewById(R.id.city_manager);

        cityDbBeanList = dbUtils.getAllCity();
        Log.d("cityList",">>>>>数据库中的数据》》》》》》"+cityDbBeanList);
        addCityAdapter = new AddCityAdapter(CityManagerActivity.this,cityDbBeanList);

//        rvAddedCity.setLayoutManager(new LinearLayoutManager(this));
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rvAddedCity.setLayoutManager(layoutManager);

        addCityAdapter.setOnItemClickListener(new AddCityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
//                ToastUtil.showShotToast(CityManagerActivity.this, cityDbBeanList.get(position).getName());
                Intent intent = new Intent(CityManagerActivity.this,MainActivity.class);
                intent.putExtra("selectedCity",cityDbBeanList.get(position).getName());
//                startActivity(intent);

                setResult(200,intent);
                finish();

//                ToastUtil.showShotToast(CityManagerActivity.this,"finish");

            }

            @Override
            public void onItemLongClick(View view, int position) {
                Log.d("onItemLongClick","2222222222222点击了长按事件onItemLongClick>>>>>>"+position+cityDbBeanList.get(position).getName());
                AlertDialog.Builder builder=new AlertDialog.Builder(CityManagerActivity.this);
                builder.setTitle("⛔删除");
                builder.setMessage("确定删除城市："+cityDbBeanList.get(position).getName()+"吗？");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        boolean b = dbUtils.delCityByName(cityDbBeanList.get(position).getName());
                        Log.d("dbUtils.delCityByName","是否删除此城市》》》------"+b+"------");
                        cityDbBeanList.remove(position);
                        ToastUtil.showShotToast(CityManagerActivity.this,"删除成功");
                        addCityAdapter.notifyDataSetChanged();
                    }
                });
//                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialogInterface, int i) {
//                        ToastUtil.showShotToast(CityManagerActivity.this,"点了No");
//                    }
//                });
                builder.setNeutralButton("cancel",null);
                builder.create().show();
            }

        });
        rvAddedCity.setAdapter(addCityAdapter);

    }

}
