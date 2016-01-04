package com.topad.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topad.R;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.LoginBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Md5;
import com.topad.util.Utils;
import com.topad.view.customviews.PickDatePopwindow;
import com.topad.view.customviews.TitleView;
import com.topad.view.interfaces.IDatePick;

/**
 * ${todo}<完善资料>
 *
 * @author ouruiqiang
 * @data: on 15/11/2 16:35
 */
public class CompleteInforActivity extends BaseActivity implements View.OnClickListener, IDatePick {
    private static final String LTAG = CompleteInforActivity.class.getSimpleName();
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

    LinearLayout mainlayout;
    TextView tv_bithday;
    TextView gerenjianjie,xuanzezhiye, shenfenyanzheng;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_complete_infor;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mainlayout = (LinearLayout) findViewById(R.id.scroll_layout);
        mTitleView = (TitleView) findViewById(R.id.title);
        mBTLogin = (Button) findViewById(R.id.btn_login);
        tv_bithday = (TextView) findViewById(R.id.tv_bithday);
        tv_bithday.setOnClickListener(this);
        mBTLogin.setOnClickListener(this);
        gerenjianjie = (TextView) findViewById(R.id.gerenjianjie);
        xuanzezhiye = (TextView) findViewById(R.id.xuanzezhiye);
        shenfenyanzheng = (TextView) findViewById(R.id.shenfenyanzheng);
        gerenjianjie.setOnClickListener(this);
        xuanzezhiye.setOnClickListener(this);
        shenfenyanzheng.setOnClickListener(this);
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
        mTitleView.setTitle("完善资料");
        mTitleView.setLeftVisiable(true);
        mTitleView.setRightVisiable(false);
        mTitleView.setLeftClickListener(new TitleRightOnClickListener());
    }

    @Override
    public void setDate(String dateString) {//选择日期
        tv_bithday.setText(dateString);
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
        switch (v.getId()) {
            case R.id.gerenjianjie://个人简介


                break;
            case R.id.shenfenyanzheng://身份验证
                Intent intent = new Intent(mContext, IdentityAutherActivity.class);
                startActivity(intent);

                break;
            case R.id.xuanzezhiye://选择职业
                 intent = new Intent(mContext, IdentityAutherActivity.class);
                startActivity(intent);

                break;
            case R.id.btn_login://确认保存


                break;
            case R.id.tv_bithday://出生日期
                PickDatePopwindow datePick = new PickDatePopwindow(mContext);
                datePick.registeDatePick(this);
                datePick.showAtLocation(mainlayout,
                        Gravity.BOTTOM, 0, 0);

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
}
