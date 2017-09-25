package com.hxs.fitnessroom.module.home.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.util.image.ImageLoader;
import com.hxs.fitnessroom.widget.adapterwrapper.LoadMoreAdapterWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页门店地址的UI操作类
 * Created by je on 9/6/17.
 */

public class StoreListUi extends BaseUi
{


    private final TextView city_item_select;
    private final TextView county_item_select;
    private final RecyclerView recyclerView;

    public StoreListUi(BaseFragment baseFragment)
    {
        super(baseFragment);
        city_item_select = findViewById(R.id.city_item_select);
        county_item_select = findViewById(R.id.county_item_select);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getBaseActivity()));

    }

    public void setOnclick(View.OnClickListener onclick)
    {
        city_item_select.setOnClickListener(onclick);
        county_item_select.setOnClickListener(onclick);
    }

    public void setCityAndCountyName(String cityName,String countyName)
    {
        city_item_select.setText(cityName);
        county_item_select.setText(ValidateUtil.isEmpty(countyName) ? "全部门店" : countyName);
    }

    public void setLoadmoremListener(LoadMoreAdapterWrapper.RequestToLoadMoreListener requestToLoadMoreListener)
    {
        recyclerView.setAdapter(new StoreRecyclerViewAdapter());
//        recyclerView.setAdapter(new LoadMoreAdapterWrapper(new StoreRecyclerViewAdapter(),requestToLoadMoreListener));
    }

    public void addStoreList(List<StoreBean> loadMoreData)
    {
        storeBeanList.clear();
        storeBeanList.addAll(loadMoreData);
        recyclerView.getAdapter().notifyDataSetChanged();
    }

    private List<StoreBean> storeBeanList = new ArrayList<>();

    class StoreRecyclerViewAdapter extends RecyclerView.Adapter
    {
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
        {
            return new StoreViewHolder(parent);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
        {
            if(holder instanceof StoreViewHolder)
            {
                ((StoreViewHolder)holder).bindData(storeBeanList.get(position));
            }
        }

        @Override
        public int getItemCount()
        {
            return storeBeanList.size();
        }
    }

    static class StoreViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        private final ImageView store_image;
        private final TextView store_address;
        private final TextView store_distance;
        private final TextView store_name;
        private StoreBean storeBean;

        public StoreViewHolder(View itemView)
        {
            super(LayoutInflater.from(itemView.getContext())
                    .inflate(R.layout.store_main_item, (ViewGroup) itemView,false));

            store_image = (ImageView) this.itemView.findViewById(R.id.store_image);
            store_address = (TextView) this.itemView.findViewById(R.id.store_address);
            store_distance = (TextView) this.itemView.findViewById(R.id.store_distance);
            store_name = (TextView) this.itemView.findViewById(R.id.store_name);
            this.itemView.setOnClickListener(this);
        }

        public void bindData(StoreBean storeBean)
        {
            this.storeBean = storeBean;
            store_name.setText(storeBean.name);
            store_distance.setText(storeBean.distance);
            store_address.setText(storeBean.address);
            if(ValidateUtil.isEmpty(storeBean.img))
                Glide.with(itemView.getContext()).clear(store_image);
            else
                ImageLoader.load(storeBean.img,store_image);

        }

        @Override
        public void onClick(View v)
        {
            WebActivity.gotoWeb(v.getContext(), ConstantsApiUrl.H5_gymDetail.getH5Url(storeBean.uid),"健身房详情");
        }
    }








}
