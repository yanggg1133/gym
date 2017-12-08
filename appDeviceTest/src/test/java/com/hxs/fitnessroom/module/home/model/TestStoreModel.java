package com.hxs.fitnessroom.module.home.model;

import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.home.model.entity.AreaBean;
import com.hxs.fitnessroom.module.home.model.entity.StoreBean;

import org.junit.Test;

import java.util.List;

/**
 * Created by je on 9/5/17.
 */
public class TestStoreModel
{

    @Test
    public void testStoreList() throws Exception
    {
        APIResponse<List<StoreBean>> apiResponse = StoreModel.storeList(null,null,1);
        System.out.println(apiResponse.data.get(0).name);
    }


    @Test
    public void testAreaList() throws Exception
    {
        APIResponse<List<AreaBean>> apiResponse = StoreModel.areaList(null);
        apiResponse.data.get(0);
    }

}
