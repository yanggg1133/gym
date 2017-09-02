package com.lvshou.fitnessroom.base.network;

import com.google.gson.reflect.TypeToken;

import org.junit.Test;

/**
 * Created by je on 8/31/17.
 */

public class APIHttpClientTest
{

    @Test
    public void postAPI() throws Exception
    {
        APIResponse<Bean> apiResponse = APIHttpClient.postForm(
                ConstantsApiUrl.TestUrl.getAPIRootUrl(),
                ParamsBuilder.buildFormParam().putParam("api", 1),
                new TypeToken<APIResponse<Bean>>() {}.getType());
        if (!apiResponse.isSuccess())
        {
            throw new Exception("请求返回错误");
        }
    }

    static class Bean
    {


    }
}
