package com.hxs.fitnessroom.module.home;

import android.content.Context;
import android.content.Intent;
import android.net.rtp.RtpStream;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.home.model.StoreModel;
import com.hxs.fitnessroom.module.home.model.entity.StoreReserveBean;
import com.hxs.fitnessroom.module.home.ui.StoreReserveUi;
import com.hxs.fitnessroom.module.home.widget.StoreReserveSelectTimeView;
import com.hxs.fitnessroom.module.user.UserReserveActivity;
import com.hxs.fitnessroom.util.DialogUtil;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.widget.dialog.ConfirmDialog;

import java.util.List;

/**
 * 预约成功界面
 * Created by shaojunjie on 17-10-30.
 */

public class StoreReserveSuccessActivity extends BaseActivity implements View.OnClickListener
{
    private static final String KEY_STROE_NAME = "KEY_STROE_NAME";
    private static final String KEY_TIME = "KEY_TIME";
    private static final String KEY_MONEY = "KEY_MONEY";


    private BaseUi mUi;
    private TextView store_name_text;
    private TextView time_text;
    private TextView money_text;

    public static Intent getNewIntent(Context context, String storeName,String time,String money)
    {
        Intent intent = new Intent(context, StoreReserveSuccessActivity.class);
        intent.putExtra(KEY_STROE_NAME, storeName);
        intent.putExtra(KEY_TIME, time);
        intent.putExtra(KEY_MONEY, money);
        return intent;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.store_reserve_success_activity);
        mUi = new BaseUi(this);
        mUi.setTitle("预约成功");
        mUi.setBackAction(true);



        store_name_text = mUi.findViewById(R.id.store_name_text);
        time_text = mUi.findViewById(R.id.time_text);
        money_text = mUi.findViewById(R.id.money_text);

        store_name_text.setText(getIntent().getStringExtra(KEY_STROE_NAME));
        time_text.setText(getIntent().getStringExtra(KEY_TIME));
        money_text.setText("￥"+getIntent().getStringExtra(KEY_MONEY));

        mUi.findViewByIdAndSetClick(R.id.goto_user_reserve_button);

    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.goto_user_reserve_button:
                startActivity(UserReserveActivity.getNewIntent(StoreReserveSuccessActivity.this));
                finish();
                break;
        }
    }
}
