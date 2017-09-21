package com.hxs.fitnessroom.module.pay;

import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.model.DepositModel;
import com.hxs.fitnessroom.module.pay.model.entity.DepositBean;

import org.junit.Test;

import java.util.List;

/**
 * Created by je on 17/09/17.
 */

public class TestDepositModel {
    @Test
    public void testDepositModel(){
        APIResponse<DepositBean> deposit = DepositModel.deposit();
        System.out.println(deposit.data.toString());
    }
}
