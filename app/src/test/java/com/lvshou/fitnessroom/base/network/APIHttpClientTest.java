package com.lvshou.fitnessroom.base.network;

import org.junit.Test;

import java.util.List;

/**
 * Created by je on 8/31/17.
 */

public class APIHttpClientTest
{

    @Test
    public void postAPI() throws Exception
    {
        APIResponse<List> apiResponse = APIHttpClient.post("","",APIResponse.class);
    }
}
