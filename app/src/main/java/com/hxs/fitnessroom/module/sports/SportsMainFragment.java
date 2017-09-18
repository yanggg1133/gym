package com.hxs.fitnessroom.module.sports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.activity.CaptureActivity;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.BaseFragment;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.sports.model.QRCodeModel;
import com.hxs.fitnessroom.module.sports.model.entity.QRCodeBean;
import com.hxs.fitnessroom.module.sports.ui.SportsMainUi;
import com.hxs.fitnessroom.module.user.HXSUser;
import com.hxs.fitnessroom.module.user.LoginActivity;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.ValidateUtil;

import fitnessroom.hxs.com.codescan.CameraUtil;

/**
 * 运动主入口界面
 * Created by je on 9/2/17.
 */

public class SportsMainFragment extends BaseFragment implements View.OnClickListener
{
    private final int RequestCode_Login = 10;
    private final int RequestCode_Scan_OpenDoor = 11;//扫码开门

    private SportsMainUi mSportsMainUi;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.sports_main_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        mSportsMainUi = new SportsMainUi(this);
        mSportsMainUi.setTitle("运动");
    }


    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.start_scan:
                startSport();
                break;
        }
    }

    /**
     * 开始扫描 使用健身房流程
     */
    private void startSport()
    {
        step1_checkLogin();
    }

    /**
     * 第一步 检查登陆
     */
    private void step1_checkLogin()
    {
        if (HXSUser.isLogin())
            step3_scan_open_door();
        else
            startActivityForResult(LoginActivity.getNewIntent(getContext(), LoginActivity.VALUE_TYPE_LOGIN), RequestCode_Login);
    }

    /**
     * 第二步 检查押金/余额/及上次消费情况
     */
    private void step2_checkDeposit()
    {
//        DialogUtil.showConfirmDialog("你尚未交押金\n暂时无法使用健身房", "取消", "去缴费",
//                getFragmentManager(),
//                new ConfirmDialog.OnDialogCallbackAdapter()
//                {
//                    @Override
//                    public void onConfirm()
//                    {
//                        if(CameraUtil.isCameraCanUse()){
//                            Intent intent = new Intent(getActivity(), CaptureActivity.class);
//                            startActivityForResult(intent, 10);
//                        }else{
//                            Toast.makeText(getContext(),"请打开此应用的摄像头权限！",Toast.LENGTH_SHORT).show();
//                        }
//                        LogUtil.dClass("onConfirm");
//                    }
//                });

    }


    /**
     * 第三步 扫码开门
     */
    private void step3_scan_open_door()
    {
        if (CameraUtil.isCameraCanUse())
        {
            Intent intent = new Intent(getActivity(), CaptureActivity.class);
            startActivityForResult(intent, RequestCode_Scan_OpenDoor);
        } else
        {
            Toast.makeText(getContext(), "请打开此应用的摄像头权限！", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 第四步 处理开门二维码
     */
    private void step4_handler_opendoor_code(String openDoorCode)
    {
        if (ValidateUtil.isEmpty(openDoorCode))
        {
            Toast.makeText(getContext(), "二维码异常，请尝试重新扫描！", Toast.LENGTH_SHORT).show();
        }
        else
        {
            mSportsMainUi.getLoadingView().show();
            new OpenDoorAsyncTask(openDoorCode).execute(getBaseActivity());
        }
    }

    /**
     * 第5步 已开门，开始计算使用状态
     */
    private void step5_start_using()
    {

    }

    /**
     * 有未结算的订单
     */
    private void error_order_is_not_settled()
    {

    }

    /**
     * 未交押金
     */
    private void error_not_deposit()
    {

    }

    /**
     * 余额不足
     */
    private void error_insufficient_balance()
    {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode)
        {
            case RequestCode_Login:
                if (resultCode == Activity.RESULT_OK)
                {
                    step2_checkDeposit();
                }
                break;
            case RequestCode_Scan_OpenDoor:
                if (resultCode == CaptureActivity.RESULT_CODE_QR_SCAN
                        && null != data)
                {
                    step4_handler_opendoor_code(data.getStringExtra(CaptureActivity.INTENT_EXTRA_KEY_QR_SCAN));
                }
                break;
        }
    }




    class OpenDoorAsyncTask extends BaseAsyncTask
    {
        private String code;
        public OpenDoorAsyncTask(String code)
        {
            this.code = code;
        }

        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return QRCodeModel.deQRCode(code);
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            mSportsMainUi.getLoadingView().hide();
            step5_start_using();
        }
    }
}
