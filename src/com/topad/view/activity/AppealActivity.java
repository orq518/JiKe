package com.topad.view.activity;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.topad.R;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<申诉页面>
 *
 * @author lht
 * @data: on 15/12/7 13:58
 */
public class AppealActivity extends BaseActivity implements View.OnClickListener{
    private static final String LTAG = AppealActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_appeal;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);;

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
        mTitleView.setTitle("申诉");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
    }

    /**
     * 顶部布局--按钮事件监听
     */
    public class TitleLeftOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 提现
            case R.id.btn_cash:

                break;
            default:
                break;
        }
    }
}
