package com.hxs.fitnessroom.module.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.home.model.StoreModel;
import com.hxs.fitnessroom.module.home.model.entity.AreaBean;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.module.home.ui.StoreListUi;
import com.hxs.fitnessroom.module.home.widget.AreaSelectDialogFragment;
import com.hxs.fitnessroom.util.LocationUtil;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.widget.adapterwrapper.LoadMoreAdapterWrapper;
import com.hxs.fitnessroom.widget.LoadingView;

import java.util.List;


/**
 * 首页界面
 * Created by je on 9/2/17.
 */

public class StoreListFragment extends BaseFragment implements LoadingView.OnReloadListener, View.OnClickListener
{

    private WorkAsyncTask mWorkAsyncTask;
    private StoreListUi mStoreListUi;

    private List<AreaBean> mAreas;
    private int mCurrentCityIndex = 0;
    private int mCurrentCountyIndex = -1;
    private String mCurrentUserSelectCity = "";
    private String mCurrentUserSelectCounty = "";
    private int mPageIndex = 1;

    private LoadMoreAdapterWrapper.RequestToLoadMoreListener mRequestToLoadMoreListener = new LoadMoreAdapterWrapper.RequestToLoadMoreListener()
    {
        @Override
        public void onLoadMoreRequested()
        {
            ++mPageIndex;
            doWork();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.store_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mStoreListUi = new StoreListUi(this);
        mStoreListUi.setTitle("健身房");
        mStoreListUi.setLoadmoremListener(mRequestToLoadMoreListener);
        mStoreListUi.setOnclick(this);
        mStoreListUi.getLoadingView().show();
        doWork();
    }

    private void doWork()
    {
        if (null == mWorkAsyncTask)
        {
            mWorkAsyncTask = new WorkAsyncTask();
            mWorkAsyncTask.execute(getBaseActivity());
        }
    }

    @Override
    public void onReload()
    {
        mPageIndex = 1;
        doWork();
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.city_item_select:
                AreaSelectDialogFragment.show(getFragmentManager(), AreaSelectDialogFragment.SELECT_TYPE_CITY,
                        mCurrentCityIndex, mCurrentCountyIndex, mAreas,
                        new AreaSelectDialogFragment.OnSelectCallBack()
                        {
                            @Override
                            public void onSelect(int cityIndex, int countyIndex)
                            {
                                mCurrentCityIndex = cityIndex;
                                mCurrentCountyIndex = -1;
                                mCurrentUserSelectCity = mAreas.get(cityIndex).city;
                                mCurrentUserSelectCounty = "";
                                mStoreListUi.setCityAndCountyName(mCurrentUserSelectCity,mCurrentUserSelectCounty);
                                mStoreListUi.getLoadingView().showByNullBackground();
                                mPageIndex = 1;
                                doWork();
                            }
                        });
                break;
            case R.id.county_item_select:
                AreaSelectDialogFragment.show(getFragmentManager(), AreaSelectDialogFragment.SELECT_TYPE_COUNTY,
                        mCurrentCityIndex, mCurrentCountyIndex, mAreas,
                        new AreaSelectDialogFragment.OnSelectCallBack()
                        {
                            @Override
                            public void onSelect(int cityIndex, int countyIndex)
                            {
                                mCurrentCountyIndex = countyIndex;
                                mCurrentUserSelectCounty = mCurrentCountyIndex == -1 ? "" : mAreas.get(mCurrentCityIndex).county[mCurrentCountyIndex];
                                mStoreListUi.setCityAndCountyName(mCurrentUserSelectCity,mCurrentUserSelectCounty);
                                mStoreListUi.getLoadingView().showByNullBackground();
                                mPageIndex = 1;
                                doWork();
                            }
                        });
                break;
        }
    }


    /**
     * 查询城市列表及门店地址
     */
    final class WorkAsyncTask extends BaseAsyncTask
    {
        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            if(mAreas == null)
            {
                APIResponse<List<AreaBean>> areas = StoreModel.areaList(LocationUtil.getLastLocationPoints());
                if (areas.isSuccess())
                {
                    mAreas = areas.data;
                    mCurrentUserSelectCity=mAreas.get(0).city;
                }

            }
            return StoreModel.storeList(mCurrentUserSelectCity + mCurrentUserSelectCounty, LocationUtil.getLastLocationPoints(), mPageIndex);
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            mStoreListUi.getLoadingView().showNetworkError();
            mWorkAsyncTask = null;
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            APIResponse<List<StoreBean>> stores = data;
            if (ValidateUtil.isNotEmpty(stores.data))
            {
                mStoreListUi.addStoreList(stores.data);
            }
            if(ValidateUtil.isNotEmpty(mAreas))
            {
                mStoreListUi.setCityAndCountyName(mCurrentUserSelectCity,mCurrentUserSelectCounty);
            }
            mWorkAsyncTask = null;
            mStoreListUi.getLoadingView().hide();
        }
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if (null != mWorkAsyncTask && !mWorkAsyncTask.isCancelled())
        {
            mWorkAsyncTask.cancel(true);
            mWorkAsyncTask = null;
        }
    }


}
