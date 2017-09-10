package com.hxs.fitnessroom.module.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.home.model.StoreModel;
import com.hxs.fitnessroom.module.home.model.entity.AreaBean;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;
import com.hxs.fitnessroom.module.home.ui.StoreListUi;
import com.hxs.fitnessroom.util.LocationUtil;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.widget.AdapterWrapper.LoadMoreAdapterWrapper;

import java.util.List;


/**
 * 首页界面
 * Created by je on 9/2/17.
 */

public class StoreListFragment extends BaseFragment
{

    private WorkAsyncTask mWorkAsyncTask;
    private StoreListUi mStoreListUi;
    private LoadMoreAdapterWrapper.RequestToLoadMoreListener mRequestToLoadMoreListener = new LoadMoreAdapterWrapper.RequestToLoadMoreListener()
    {
        @Override
        public void onLoadMoreRequested()
        {

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
        mStoreListUi.setLoadmoremListener(mRequestToLoadMoreListener);
        TextView textView = new TextView(getContext());

        doWork();
    }

    private void doWork()
    {
        if(null == mWorkAsyncTask)
        {
            mWorkAsyncTask = new WorkAsyncTask();
            mWorkAsyncTask.execute(getBaseActivity());
        }
    }

    private List<AreaBean> mAreas;

    private String mCurrentUserSelectCity = "";
    private String mCurrentUserSelectCounty = "";
    private int mPageIndex = 1;
    /**
     * 查询城市列表及门店地址
     */
    final class WorkAsyncTask extends BaseAsyncTask
    {
        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            APIResponse<List<AreaBean>> areas = StoreModel.areaList(LocationUtil.getLastLocationPoints());
            if(areas.isSuccess())
            {
                mAreas = areas.data;
            }
            return StoreModel.storeList(mCurrentUserSelectCity+mCurrentUserSelectCounty,LocationUtil.getLastLocationPoints(),mPageIndex);
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            APIResponse<List<StoreBean>> stores = data;
            if(ValidateUtil.isNotEmpty(stores.data))
            {
                mStoreListUi.addStoreList(stores.data);
            }
        }

        @Override
        protected void onCancelled()
        {
            super.onCancelled();
            mWorkAsyncTask = null;
        }
    }


    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        if(null != mWorkAsyncTask && !mWorkAsyncTask.isCancelled())
        {
            mWorkAsyncTask.cancel(true);
        }
    }
}
