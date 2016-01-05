package com.topad.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topad.R;
import com.topad.view.customviews.PickDatePopwindow;
import com.topad.view.customviews.TitleView;
import com.topad.view.interfaces.IDatePick;

/**
 * ${todo}<身份认证>
 *
 * @author ouruiqiang
 * @data: on 15/11/2 16:35
 */
public class IdentityAutherActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = IdentityAutherActivity.class.getSimpleName();
    /**
     * 上下文
     **/
    private Activity mContext;
    /**
     * 顶部布局
     **/
    private TitleView mTitleView;
    TextView shenfenzheng,biyezheng,mingpian;
    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_identity_auth;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        shenfenzheng=(TextView) findViewById(R.id.shenfenzheng);
        biyezheng=(TextView) findViewById(R.id.biyezheng);
        mingpian=(TextView) findViewById(R.id.mingpian);
        shenfenzheng.setOnClickListener(this);
        biyezheng.setOnClickListener(this);
        mingpian.setOnClickListener(this);
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
        mTitleView.setTitle("身份认证");
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
        Intent  mIntent=new Intent(mContext,UploadPicActivity.class);
        switch (v.getId()) {
            case R.id.shenfenzheng://
                mIntent.putExtra("title","上传身份证");
                mIntent.putExtra("type",0);
                break;
            case R.id.biyezheng://
                mIntent.putExtra("title","上传毕业证");
                mIntent.putExtra("type",1);

                break;
            case R.id.mingpian://确认保存
                mIntent.putExtra("title","上传名片");
                mIntent.putExtra("type",2);

                break;

            default:
                break;
        }
        startActivity(mIntent);
    }



}
