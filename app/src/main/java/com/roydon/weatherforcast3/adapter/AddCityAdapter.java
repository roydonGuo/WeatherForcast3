package com.roydon.weatherforcast3.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.roydon.weatherforcast3.R;
import com.roydon.weatherforcast3.bean.CityBean;

import java.util.List;

public class AddCityAdapter extends RecyclerView.Adapter<AddCityAdapter.AddViewHolder>{

    private Context mContext;
    private List<CityBean> mCityBeans;

    public AddCityAdapter(Context context, List<CityBean> cityBeans) {
        mContext = context;
        this.mCityBeans = cityBeans;
    }

    @NonNull
    @Override
    public AddViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.added_city, parent, false);
        AddViewHolder cityViewHolder = new AddViewHolder(view);
        return cityViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddViewHolder holder, int position) {
        CityBean cityBean = mCityBeans.get(position);

        holder.cityCity.setText(cityBean.getName());
        holder.cityTem.setText(cityBean.getTem());
    }

    @Override
    public int getItemCount() {
        return (mCityBeans == null) ? 0 : mCityBeans.size();
    }

    class AddViewHolder extends RecyclerView.ViewHolder {

        TextView cityCity,cityTem;

        public AddViewHolder(@NonNull View itemView) {
            super(itemView);

            cityCity=itemView.findViewById(R.id.city_city);
            cityTem = itemView.findViewById(R.id.city_tem);

            //绑定点击事件
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

//                    ToastUtil.showShotToast(mContext, "data==" + mProvinceBeans.get(getLayoutPosition()));
                    if (onItemClickListener!=null) {
                        onItemClickListener.onItemClick(view,getLayoutPosition());
                    }

                }
            });
            //长按事件
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    onItemClickListener.onItemLongClick(view,getLayoutPosition());
                    return true;

                }
            });

        }
    }

    /**
     * 点击响应事件
     */
    public interface OnItemClickListener {

        /**
         * 当RecyclerView某个被点击的时候回调
         * @param view 点击item的视图
         * @param position 点击得到的数据
         */
        public void onItemClick(View view, int position);
        public void onItemLongClick(View view,int position);

    }

    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }



}
