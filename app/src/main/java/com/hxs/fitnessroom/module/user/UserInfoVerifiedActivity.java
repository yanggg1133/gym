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
import com.hxs.fitnessroom.base.baseclass.BaseAsyncTask;
import com.hxs.fitnessroom.base.baseclass.HXSUser;
import com.hxs.fitnessroom.base.network.APIResponse;
import com.hxs.fitnessroom.module.user.model.UserAccountModel;
import com.hxs.fitnessroom.module.user.ui.UserInfoVerifiedUi;
import com.hxs.fitnessroom.util.ToastUtil;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.util.image.ImageUtil;

import java.util.ArrayList;

import me.iwf.photopicker.PhotoPicker;

/**
 * 用户实名认证
 * Created by je on 9/26/17.
 */

public class UserInfoVerifiedActivity extends BaseActivity implements View.OnClickListener
{
    private final int Request_Code_Body = 1;//人与身份证 图片
    private final int Request_Code_Idcard = 2;//身份证 图片

    private String mBodyAndIDcardImagePath = "";
    private String mIDcardImagePath = "";

    private String mRealname;
    private String mIDcard;
    private String mBodyAndIDcardImageUrl = "";
    private String mIDcardImageUrl = "";

    private UserInfoVerifiedUi mUserInfoVerifiedUi;

    public static Intent getNewIntent(Context context)
    {
        return new Intent(context,UserInfoVerifiedActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_info_verified_activity);
        mUserInfoVerifiedUi = new UserInfoVerifiedUi(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.action_verified: //认证
                submit();
                break;
            case R.id.phone_1_view://上传人证合照
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setPreviewEnabled(false)
                        .start(this, Request_Code_Body);
                break;
            case R.id.phone_2_view://上传证件正面照
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(false)
                        .setPreviewEnabled(false)
                        .start(this, Request_Code_Idcard);
                break;
        }
    }

    /**
     * 提交认证
     */
    private void submit()
    {
        if(ValidateUtil.isEmpty(mUserInfoVerifiedUi.getRealnameText())
                || ValidateUtil.isEmpty(mUserInfoVerifiedUi.getIdcardText()))
        {
            return;
        }
        mRealname = mUserInfoVerifiedUi.getRealnameText();
        mIDcard = mUserInfoVerifiedUi.getIdcardText();

        if(ValidateUtil.isEmpty(mBodyAndIDcardImagePath))
        {
            ToastUtil.toastShort("请上传手持证照");
            return ;
        }
        if(ValidateUtil.isEmpty(mIDcardImagePath))
        {
            ToastUtil.toastShort("请上传证件正面照");
            return;
        }
        mUserInfoVerifiedUi.getLoadingView().showByNullBackground();
        upLoadBodyAndIDcard();
    }

    /**
     * 上传完成证照到的回调
     */
    private void upLoadSuccess()
    {

        new SaveRealnameTask().go(this);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        /**
         * 人证合照
         */
        if (resultCode == RESULT_OK && requestCode == Request_Code_Body)
        {
            if (data != null)
            {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                mBodyAndIDcardImagePath = photos.get(0);
                mUserInfoVerifiedUi.setBodyAndIdCardImage(mBodyAndIDcardImagePath);
            }
        }
        /**
         * 证照
         */
        else if(resultCode == RESULT_OK && requestCode == Request_Code_Idcard)
        {
            if (data != null)
            {
                ArrayList<String> photos = data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                mIDcardImagePath = photos.get(0);
                mUserInfoVerifiedUi.setIDcardImage(mIDcardImagePath);
            }

        }
    }




    /**
     * 上传人证合照
     */
    private void upLoadBodyAndIDcard()
    {
        ImageUtil.uploadImg(this, mBodyAndIDcardImagePath, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>()
        {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result)
            {
                mBodyAndIDcardImageUrl = request.getObjectKey();
                upLoadIDcard();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException)
            {
                ToastUtil.toastShort("证照上传失败,请重新再试");
                mUserInfoVerifiedUi.getLoadingView().hide();
            }
        });

    }
    /**
     * 上传证件照
     */
    private void upLoadIDcard()
    {
        ImageUtil.uploadImg(this, mIDcardImagePath, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>()
        {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result)
            {
                mIDcardImageUrl = request.getObjectKey();
                upLoadSuccess();
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientException, ServiceException serviceException)
            {
                ToastUtil.toastShort("证照上传失败,请重新再试");
                mUserInfoVerifiedUi.getLoadingView().hide();
            }
        });

    }

    /**
     * 提交实名数据
     */
    class SaveRealnameTask extends BaseAsyncTask
    {
        @Override
        protected APIResponse doWorkBackground() throws Exception
        {
            return UserAccountModel.saveRealname(mRealname,mIDcard,mBodyAndIDcardImageUrl,mIDcardImageUrl);
        }

        @Override
        protected void onError(@Nullable Exception e)
        {
            super.onError(e);
            mUserInfoVerifiedUi.getLoadingView().hide();
        }

        @Override
        protected void onSuccess(APIResponse data)
        {
            HXSUser.updateUserInfoAsync();
            finish();
        }
    }

}
