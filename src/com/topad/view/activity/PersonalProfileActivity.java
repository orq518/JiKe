package com.topad.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.topad.R;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<完善资料>
 *
 * @author ouruiqiang
 * @data: on 15/11/2 16:35
 */
public class PersonalProfileActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = PersonalProfileActivity.class.getSimpleName();
    /**
     * 上下文
     **/
    private Activity mContext;
    /**
     * 顶部布局
     **/
    private TitleView mTitleView;
    /**
     * 登录
     **/
    private Button mBTLogin;
    EditText et_personal_profile;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_personal_profile;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        et_personal_profile= (EditText) findViewById(R.id.et_personal_profile);
        mBTLogin = (Button) findViewById(R.id.btn_ok);
        mBTLogin.setOnClickListener(this);
        // 设置登录按钮
        setNextBtnState(false);
    }

    @Override
    public void initData() {
        showView();
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        mTitleView.setTitle("个人简介");
        mTitleView.setLeftVisiable(true);
        mTitleView.setRightVisiable(false);
        mTitleView.setLeftClickListener(new TitleRightOnClickListener());
    }


    /**
     * 顶部布局--注册按钮事件监听
     */
    public class TitleRightOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.btn_ok://确认保存


                break;

            default:
                break;
        }
    }


    /**
     * 去除EditText的空格
     *
     * @param et
     * @return
     */
    public String getData(EditText et) {
        String s = et.getText().toString();
        return s.replaceAll(" ", "");
    }

    /**
     * 设置下一步按钮
     *
     * @param flag
     */
    private void setNextBtnState(boolean flag) {
        if (mBTLogin == null)
            return;
        mBTLogin.setEnabled(flag);
        mBTLogin.setClickable(flag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

}
