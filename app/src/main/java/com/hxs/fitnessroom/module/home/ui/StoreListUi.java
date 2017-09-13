package com.hxs.fitnessroom.module.home.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.util.image.ImageLoader;
import com.hxs.fitnessroom.widget.AdapterWrapper.LoadMoreAdapterWrapper;

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
        //recyclerView.addItemDecoration(ViewUitl.getRecyclerViewItemDecorationOneRow());

    }

    public void setLoadmoremListener(LoadMoreAdapterWrapper.RequestToLoadMoreListener requestToLoadMoreListener)
    {
        recyclerView.setAdapter(new StoreRecyclerViewAdapter());
    }

    public void addStoreList(List<StoreBean> loadMoreData)
    {
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

    static class StoreViewHolder extends RecyclerView.ViewHolder
    {
        private final ImageView store_image;
        private final TextView store_address;
        private final TextView store_distance;
        private final TextView store_name;

        public StoreViewHolder(View itemView)
        {
            super(LayoutInflater.from(itemView.getContext())
                    .inflate(R.layout.store_main_item, (ViewGroup) itemView,false));

            store_image = (ImageView) this.itemView.findViewById(R.id.store_image);
            store_address = (TextView) this.itemView.findViewById(R.id.store_address);
            store_distance = (TextView) this.itemView.findViewById(R.id.store_distance);
            store_name = (TextView) this.itemView.findViewById(R.id.store_name);
        }

        public void bindData(StoreBean storeBean)
        {
            store_name.setText(storeBean.name);
            store_distance.setText(storeBean.distance);
            store_address.setText(storeBean.address);
            ImageLoader.load(storeBean.img,store_image);
        }

    }








}
