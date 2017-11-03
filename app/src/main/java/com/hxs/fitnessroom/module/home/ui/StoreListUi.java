package com.hxs.fitnessroom.module.home.ui;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxs.fitnessroom.BuildConfig;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.network.ConstantsApiUrl;
import com.hxs.fitnessroom.module.home.StoreReserveActivity;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.module.home.widget.StoreInfoView;
import com.hxs.fitnessroom.module.web.WebActivity;
import com.hxs.fitnessroom.util.ValidateUtil;

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
        findViewByIdAndSetClick(R.id.county_item_select_onclick_view);
        findViewByIdAndSetClick(R.id.city_item_select_onclick_view);

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

    public void setAdapter(RecyclerView.Adapter adapter)
    {
//        recyclerView.setAdapter(new StoreRecyclerViewAdapter());
        recyclerView.setAdapter(adapter);
    }



    public void addStoreList(List<StoreBean> loadMoreData)
    {
        if(null == loadMoreData)
        {
            storeBeanList.clear();
            recyclerView.getAdapter().notifyDataSetChanged();
        }
        else
        {
            storeBeanList.addAll(loadMoreData);
        }
    }

    private List<StoreBean> storeBeanList = new ArrayList<>();

    public class StoreRecyclerViewAdapter extends RecyclerView.Adapter
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
        private StoreBean storeBean;

        public StoreViewHolder(View itemView)
        {
            super(new StoreInfoView(itemView.getContext()));
            this.itemView.setOnClickListener(this);
        }

        public void bindData(StoreBean storeBean)
        {
            this.storeBean = storeBean;
            ((StoreInfoView)this.itemView).setStoreBean(storeBean);
        }

        @Override
        public void onClick(View v)
        {
            if(BuildConfig.DEBUG)
            {
                itemView.getContext().startActivity(StoreReserveActivity.getNewIntent(itemView.getContext(),storeBean.uid));
            }
            else
            {
                WebActivity.gotoWeb(v.getContext(), ConstantsApiUrl.H5_gymDetail.getH5Url(storeBean.uid));
            }
        }
    }








}
