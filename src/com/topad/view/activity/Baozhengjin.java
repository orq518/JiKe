package com.topad.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<身份认证>
 *
 * @author ouruiqiang
 * @data: on 15/11/2 16:35
 */
public class Baozhengjin extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = Baozhengjin.class.getSimpleName();
    /**
     * 上下文
     **/
    private Activity mContext;
    /**
     * 顶部布局
     **/
    private TitleView mTitleView;
    Button join_bt;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_baozhengjin;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        join_bt = (Button) findViewById(R.id.join_bt);
        join_bt.setOnClickListener(this);
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
        mTitleView.setTitle("客户保障-保证完成");
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
        switch (v.getId()) {
            case R.id.join_bt:
                Intent intentrecharge = new Intent(mContext, RechargeActivity.class);
                intentrecharge.putExtra("subject", "交纳保证金");
                intentrecharge.putExtra("maxmoney", 100000.0f);
                intentrecharge.putExtra("minmoney", 1000.0f);
                intentrecharge.putExtra("subject", "交纳保证金");
                intentrecharge.putExtra("body", TopADApplication.getSelf().getUserId()+"|2|0");
                startActivity(intentrecharge);
                break;

            default:
                break;
        }
    }


}
