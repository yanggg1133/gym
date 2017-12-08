package com.hxs.fitnessroom.module.user.model;

import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.user.LoginActivity;

import org.junit.Test;

/**
 * Created by je on 9/5/17.
 */
public class TestLoginModel
{

    @Test
    public void testSendSMS() throws Exception
    {
        APIResponse apiResponse = LoginModel.sendSMS("17097660375", LoginActivity.VALUE_TYPE_LOGIN);
        System.out.print(apiResponse.code + apiResponse.msg);
    }


}
