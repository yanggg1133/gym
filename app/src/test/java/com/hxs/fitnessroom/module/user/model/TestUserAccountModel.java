package com.hxs.fitnessroom.module.user.model;

import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.pay.model.entity.UserAccountBean;
import com.hxs.fitnessroom.module.user.model.entity.RealnameBean;

import org.junit.Test;

/**
 * Created by je on 17/09/17.
 */

public class TestUserAccountModel {
    @Test
    public void testUserAccountModel(){
        APIResponse<UserAccountBean> gymUserAccount = UserAccountModel.getGymUserAccount(UserAccountModel.FROMPAGE_WALLET);
        System.out.println(gymUserAccount.data.balance + " " + gymUserAccount.data.status);
    }
    @Test
    public void testGetRealname(){
        APIResponse<RealnameBean> realnameBean = UserAccountModel.getRealname();

    }
}
