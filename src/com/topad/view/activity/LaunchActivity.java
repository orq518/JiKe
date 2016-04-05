package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.LoginBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Md5;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.Utils;

/**
 * ${todo}<闪屏页>
 *
 * @author lht
 * @data: on 15/12/08 15:06
 */
public class LaunchActivity extends BaseActivity {
    private Context mCtx;
    private ImageView mImageV;

    /** 页面显示时间 **/
    private int time = 3000;

    @Override
    public int setLayoutById() {
        return R.layout.activity_launch;

    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mCtx = LaunchActivity.this;
        mImageV = (ImageView) findViewById(R.id.Imagev_launch);

    }

    @Override
    public void initData() {
        //统一一定时间后finish；
        mImageV.postDelayed(new Runnable() {
            @Override
            public void run() {
                finishThis();
            }
        }, time);

    }

    private void finishThis() {
        // 是否登录
        if (((TopADApplication)getApplication()).isLogin()) {
            startActivity(new Intent(mCtx, MainActivity.class));


        }else{
            startActivity(new Intent(mCtx, LoginActivity.class));
        }

        finish();
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
