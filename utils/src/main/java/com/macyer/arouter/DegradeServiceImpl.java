package com.macyer.arouter;

import android.content.Context;

import com.alibaba.android.arouter.facade.Postcard;
import com.alibaba.android.arouter.facade.annotation.Route;
import com.alibaba.android.arouter.facade.service.DegradeService;

/**
 * Created by Lenovo on 2017/12/22.
 */
@Route(path = "/xxx/xxx")
public class DegradeServiceImpl implements DegradeService {

    private Context context;
    
    @Override
    public void onLost(Context context, Postcard postcard) {
        
    }

    @Override
    public void init(Context context) {
        this.context = context;
    }
}
