package com.macyer.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.SerializationService;
import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by Lenovo on 2017/12/22.
 */

@Route(path = "/service/json")
public class JsonServiceImpl implements SerializationService {
    
    private Gson gson = new Gson();
    
    @Override
    public void init(Context context) {

    }

    @Override
    public <T> T json2Object(String text, Class<T> clazz) {
        return gson.fromJson(text,clazz);
    }

    @Override
    public String object2Json(Object instance) {
        return gson.toJson(instance);
    }

    @Override
    public <T> T parseObject(String input, Type clazz) {
        return gson.fromJson(input,clazz);
    }
}