package com.hxs.fitnessroom.module.sports;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.SystemClock;
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
import com.hxs.fitnessroom.module.pay.PayDepositActivity;
import com.hxs.fitnessroom.module.pay.PayRechargeActivity;
import com.hxs.fitnessroom.module.pay.mode.UserAccountModel;
import com.hxs.fitnessroom.module.pay.mode.entity.UserAccountBean;
import com.hxs.fitnessroom.module.sports.model.QRCodeModel;
import com.hxs.fitnessroom.module.sports.model.entity.QRCodeBean;
import com.hxs.fitnessroom.module.sports.ui.SportsMainUi;
import com.hxs.fitnessroom.module.user.HXSUser;
import com.hxs.fitnessroom.module.user.LoginActivity;
import com.hxs.fitnessroom.util.DialogUtil;
import com.hxs.fitnessroom.util.LogUtil;
import com.hxs.fitnessroom.util.ScanCodeUtil;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.util.VariableUtil;
import com.hxs.fitnessroom.widget.dialog.ConfirmDialog;

import fitnessroom.hxs.com.codescan.CameraUtil;

/**
 * 运动主入口界面
 * Created by je on 9/2/17.
 */

public class SportsMainFragment extends BaseFragment implements View.OnClickListener
{
    private final int RequestCode_Login = 10;
    private final int RequestCode_Scan_OpenDoor = 11;//扫码开门
    private final int RequestCode_Pay_Deposit = 12;//押金充值
    private final int RequestCode_Pay_Recharge = 13;//余额充值
    private final int RequestCode_action_scan_code = 14;//进入健身房后的所有扫码行为

    private SportsMainUi mSportsMainUi;
    private UserAccountBean mUserAccountBean;

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
    public void onStart()
    {
        super.onStart();
        mSportsMainUi.onStart();
    }

    @Override
    public void onStop()
    {
        super.onStop();
        mSportsMainUi.onStop();
    }

    private boolean isScaning = false;//防止同一时间多次点击扫描二维码
    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.start_scan:
                startSport();
                break;
            case R.id.sport_action_opendoor_text: //扫描结算开门
            case R.id.sport_action_lockers_text://扫描储物柜
            case R.id.sport_action_readmill_text://扫描器械
            case R.id.sport_action_shop_text://扫描售货机
                if(!isScaning)
                {
                    ScanCodeUtil.startScanCode(this,RequestCode_action_scan_code);
                    isScaning = true;
                }
                break;

        }
    }

    /************************************************************************************************************
     *** 健身房流程控制 ********************************************************************************************
     ************************************************************************************************************/

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
            step2_checkDeposit();
        else
        {
            startActivityForResult(LoginActivity.getNewIntent(getContext(), LoginActivity.VALUE_TYPE_LOGIN), RequestCode_Login);
        }
    }

    /**
     * 第二步 检查押金/余额/及上次消费情况
     */
    private void step2_checkDeposit()
    {
        if (mUserAccountBean == null)//初始查询，查询帐户情况
        {
            new QueryAccountTask().execute(getBaseActivity(), mSportsMainUi);
        } else//初始查询完成后，判断数据
        {
            //用户已在健身房内还未出来
            if(UserAccountBean.DoorStatus_USING == mUserAccountBean.doorStatus)
            {
                step5_start_using();
                return;
            }

            switch (mUserAccountBean.status)
            {
                case UserAccountBean.AccountStatus_NoDeposit://未交押金
                    error_not_deposit();
                    return;
                case UserAccountBean.AccountStatus_NORMAL://正常
                    //继续往下判断
                    break;
                case UserAccountBean.AccountStatus_BlackList://黑名单
                    error_not_deposit();
                    return;
            }


            double mincost = VariableUtil.stringToDouble(mUserAccountBean.mincost);//最低消费
            double balance = VariableUtil.stringToDouble(mUserAccountBean.balance);//余额
            if (balance >= 0d && balance < mincost)
            {
                error_insufficient_balance();//余额不满足最低消费
                return;
            }
            if (0d > balance)
            {
                error_order_is_not_settled();//有未结算的消费
                return;
            }

            //去扫码开门
            step3_scan_open_door();
        }
    }


    /**
     * 第三步 扫码开门
     */
    private void step3_scan_open_door()
    {
        if (CameraUtil.isCameraCanUse() && !isScaning)
        {
            ScanCodeUtil.startScanCode(this,RequestCode_Scan_OpenDoor);
            isScaning = true;
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
        } else
        {
            mSportsMainUi.getLoadingView().show();
            new OpenDoorAsyncTask(openDoorCode).execute(getBaseActivity(),mSportsMainUi);
        }
    }

    /**
     * 第5步 已开门，开始计算使用状态
     */
    private void step5_start_using()
    {
        mSportsMainUi.startSport();
    }




    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        isScaning = false;
        switch (requestCode)
        {
            case RequestCode_Login:
                if (resultCode == Activity.RESULT_OK)
                {
                    step2_checkDeposit();
                }
                break;
            case RequestCode_Scan_OpenDoor:
                step4_handler_opendoor_code(ScanCodeUtil.getResultScanCode(resultCode,data));
                break;
            case RequestCode_Pay_Deposit:
                if (resultCode == Activity.RESULT_OK)
                {
                    mUserAccountBean.status = UserAccountBean.AccountStatus_NORMAL;
                    step2_checkDeposit();
                }
            case RequestCode_Pay_Recharge:
                if (resultCode == Activity.RESULT_OK)
                {
                    double amount = data.getDoubleExtra(PayRechargeActivity.RESULT_AMOUNT, 0d);
                    mUserAccountBean.balance = String.valueOf(VariableUtil.stringToDouble(mUserAccountBean.balance) + amount);
                    LogUtil.dClass("最新余额：" + mUserAccountBean.balance);
                    step2_checkDeposit();
                }
                break;
            case RequestCode_action_scan_code://进入健身房后的所有扫码行为
                String code = ScanCodeUtil.getResultScanCode(resultCode,data);
                if(null == code)
                {
                    ToastUtil.toastShort("扫码失败，请重新再试");
                }
                else
                {
                    LogUtil.dClass(code);
                    new ActionScanCodeTask(code).execute(getBaseActivity(),mSportsMainUi);
                }
                break;
        }
    }

    /************************************************************************************************************
     *** 所有操作异常处理 ********************************************************************************************
     ************************************************************************************************************/

    /**
     * 有未结算的订单
     */
    private void error_order_is_not_settled()
    {
        DialogUtil.showConfirmDialog("你有未结算的订单", "取消", "去充值",
                getFragmentManager(),
                new ConfirmDialog.OnDialogCallbackAdapter()
                {
                    @Override
                    public void onConfirm()
                    {
                        startActivityForResult(PayRechargeActivity.getNewIntent(getBaseActivity()), RequestCode_Pay_Recharge);
                    }

                    @Override
                    public void onCancel()
                    {
                    }
                });
    }

    /**
     * 未交押金
     */
    private void error_not_deposit()
    {
        DialogUtil.showConfirmDialog("你尚未交押金\n暂时无法使用健身房", "取消", "去缴费",
                getFragmentManager(),
                new ConfirmDialog.OnDialogCallbackAdapter()
                {
                    @Override
                    public void onConfirm()
                    {
                        startActivityForResult(PayDepositActivity.getNewIntent(getBaseActivity()), RequestCode_Pay_Deposit);
                    }

                    @Override
                    public void onCancel()
                    {
                    }
                });
    }

    /**
     * 余额不足
     */
    private void error_insufficient_balance()
    {
        DialogUtil.showConfirmDialog("你的帐户余额\n不满足最低消费余额", "取消", "去充值",
                getFragmentManager(),
                new ConfirmDialog.OnDialogCallbackAdapter()
                {
                    @Override
                    public void onConfirm()
                    {
                        startActivityForResult(PayRechargeActivity.getNewIntent(getBaseActivity()), RequestCode_Pay_Recharge);
                    }

                    @Override
                    public void onCancel()
                    {
                    }
                });
    }


    /****************************************************************************************************
     * 服务端请求 ****************************************************************************************
     ****************************************************************************************************/

    /**
     * 扫码开门
     */
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
        protected void onAPIError(APIResponse apiResponse)
        {
            if (APIResponse.error_order_is_not_settled.equals(apiResponse.code))
            {
                error_order_is_not_settled();
            } else if (APIResponse.error_insufficient_balance.equals(apiResponse.code))
            {
                error_insufficient_balance();
            } else if (APIResponse.error_not_deposit.equals(apiResponse.code))
            {
                error_not_deposit();
            } else
            {
                super.onAPIError(apiResponse);
            }
        }


        @Override
        protected void onSuccess(APIResponse data)
        {
            mSportsMainUi.getLoadingView().hide();
            step5_start_using();
        }
    }

    /**
     * 查询用户帐户情况
     */
    class QueryAccountTask extends BaseAsyncTask
    {
        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return UserAccountModel.getGymUserAccount();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            APIResponse<UserAccountBean> userAccount = data;

            mUserAccountBean = userAccount.data;
            if (null == mUserAccountBean)
                throw new IllegalArgumentException("状态数据不正确");
            step2_checkDeposit();
        }
    }


    /**
     * 在健身房内的所有扫码行为
     */
    class ActionScanCodeTask extends BaseAsyncTask
    {
        private String code;

        public ActionScanCodeTask(String code)
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
            APIResponse<QRCodeBean> userAccount = data;
        }
    }
}
