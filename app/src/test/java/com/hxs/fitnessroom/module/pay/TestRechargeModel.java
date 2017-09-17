package com.hxs.fitnessroom.module.pay;

import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.mode.RechargeModel;
import com.hxs.fitnessroom.module.pay.mode.entity.TopupAmountBean;

import org.junit.Test;

import java.util.List;

/**
 * Created by je on 17/09/17.
 */

public class TestRechargeModel {
    @Test
    public void testRechargeListModel(){
        APIResponse<List<TopupAmountBean>> listAPIResponse = RechargeModel.rechargeList();
        System.out.println(listAPIResponse.data.toString());
    }
}
