package com.hxs.fitnessroom.module.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseCallBack;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.module.user.model.entity.UserBean;
import com.hxs.fitnessroom.module.user.ui.UserInfoUi;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.util.image.ImageUtil;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

/**
 * 个人信息界面
 * Created by je on 9/16/17.
 */

public class UserInfoActivity extends BaseActivity implements View.OnClickListener
{

    private UserInfoUi mUserInfoUi;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_activity);
        mUserInfoUi = new UserInfoUi(this);
        mUserInfoUi.setTitle("我的资料");
        mUserInfoUi.setBackAction(true);
        registerUserUpdateBroadcastReceiver();
        HXSUser.updateUserInfoAsync();
    }

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context, UserInfoActivity.class);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.user_avatar: //头像
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setPreviewEnabled(false)
                        .start(this, PhotoPicker.REQUEST_CODE);
                break;
            case R.id.user_nickname: //昵称
                startActivity(UserNicknameActivity.getNewIntent(v.getContext()));
                break;
            case R.id.user_realname: //实名认证
            case R.id.user_authenticate: //实名认证
                startActivity(UserInfoVerifiedActivity.getNewIntent(v.getContext()));
                break;
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * 头像选择回调
         */
        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE)
        {
            if (data != null)
            {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                upLoadHeadImage(photos.get(0));
            }
        }
    }

    /**
     * 上传头像
     * @param imagePath
     */
    private void upLoadHeadImage(String imagePath)
    {
        mUserInfoUi.getLoadingView().showByNullBackground();

        ImageUtil.uploadImg(this, imagePath, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>()
        {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result)
            {
                /**
                 * 向后台接交新的头像地址
                 */
                UserBean userBean = new UserBean();
                userBean.head_img = request.getObjectKey();
                HXSUser.saveUserInfoAsync(userBean, new BaseCallBack()
                {
                    @Override
                    public void onSuccess(Object o)
                    {
                        if (!isDestroyed())
                        {
                            mUserInfoUi.getLoadingView().hide();
                            mUserInfoUi.updateHeadImg();
                        }
                    }

                    @Override
                    public void onFailure(Object o)
                    {
                        mUserInfoUi.getLoadingView().hide();
                        ToastUtil.toastShort("头像上传失败");
                    }
                });
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException)
            {
                mUserInfoUi.getLoadingView().hide();
                ToastUtil.toastShort("头像上传失败");
            }
        });

    }

    @Override
    public void onUserUpdate()
    {
        super.onUserUpdate();
        mUserInfoUi.updateUserInfo();
    }
}
