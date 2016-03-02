package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.bean.BaseBean;
import com.topad.bean.IsCompanyBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<提示公司认证>
 *
 * @author lht
 * @data: on 15/10/30 14:10
 */
public class IsCompanyActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = IsCompanyActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** 关闭 **/
    private Button mBtClose;
    /** 上传营业执照 **/
    private Button mBtUp;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_is_company;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        mBtClose = (Button) findViewById(R.id.bt_close);
        mBtUp = (Button) findViewById(R.id.bt_up);

        mBtClose.setOnClickListener(this);
        mBtUp.setOnClickListener(this);
    }

    @Override
    public void initData() {
        mTitleView.setTitle("提示");
        mTitleView.setLeftIcon(R.drawable.btn_top_close);
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
    }

    /**
     * 顶部布局--左按钮事件监听
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
        Intent intent;
        switch (v.getId()) {
            // 关闭
            case R.id.bt_close:
                finish();
                break;
            // 上传营业执照
            case R.id.bt_up:
                intent = new Intent(IsCompanyActivity.this, MediaReoeaseUploadPicActivity.class);
                intent.putExtra("is_company", "1");
                intent.putExtra("picurl", "");
                startActivity(intent);
                finish();
                break;
            default:
                break;
        }
    }
}
