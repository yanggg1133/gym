package com.hxs.fitnessroom.module.user.ui;

import android.support.design.widget.TextInputLayout;
import android.text.InputFilter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxs.fitnessroom.R;
import com.hxs.fitnessroom.base.baseclass.BaseActivity;
import com.hxs.fitnessroom.base.baseclass.BaseUi;
import com.hxs.fitnessroom.util.ValidateUtil;
import com.hxs.fitnessroom.util.ViewUtil;
import com.hxs.fitnessroom.util.image.ImageLoader;

/**
 * 用户实名认证 UI操作类
 * Created by je on 9/26/17.
 */

public class UserInfoVerifiedUi extends BaseUi
{
    private TextInputLayout realname_text_layout;
    private TextInputLayout idcard_text_layout;
    private EditText realname_text;
    private EditText idcard_text;
    private ImageView phone_1_view;
    private ImageView phone_2_view;
    private TextView action_verified;

    public UserInfoVerifiedUi(BaseActivity baseActivity)
    {
        super(baseActivity);
        setTitle("实名认证");
        setBackAction(true);
        initView();
    }

    private void initView()
    {
        realname_text_layout = findViewById(R.id.realname_text_layout);
        realname_text = findViewById(R.id.realname_text);
        idcard_text_layout = findViewById(R.id.idcard_text_layout);
        idcard_text = findViewById(R.id.idcard_text);
        phone_1_view = findViewByIdAndSetClick(R.id.phone_1_view);
        phone_2_view = findViewByIdAndSetClick(R.id.phone_2_view);
        action_verified = findViewByIdAndSetClick(R.id.action_verified);
        initEditText();
    }

    private void initEditText()
    {
        realname_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        idcard_text.setFilters(new InputFilter[]{new InputFilter.LengthFilter(18)});
        realname_text.addTextChangedListener(new ViewUtil.ClearErrorTextWatcher(realname_text_layout));
        idcard_text.addTextChangedListener(new ViewUtil.ClearErrorTextWatcher(idcard_text_layout));
    }

    public String getRealnameText()
    {
        if(ValidateUtil.isEmpty(realname_text.getText().toString()))
        {
            realname_text.requestFocus();
            realname_text_layout.setError("请填写姓名");
            return null;
        }
        return realname_text.getText().toString();
    }

    public String getIdcardText()
    {
        if(!ValidateUtil.isIDcard(idcard_text.getText().toString()))
        {
            idcard_text.requestFocus();
            idcard_text_layout.setError("请填写正确的身份证号");
            return null;
        }
        return idcard_text.getText().toString();
    }


    public void setBodyAndIdCardImage(String filePath)
    {
        ImageLoader.load(filePath,phone_1_view);
    }
    public void setIDcardImage(String filePath)
    {
        ImageLoader.load(filePath,phone_2_view);
    }



}
