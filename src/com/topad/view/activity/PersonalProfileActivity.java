package com.topad.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.MyInfoBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

import java.util.List;

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
    String myIntro;

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
        myIntro= getIntent().getStringExtra("myintro");
        mTitleView = (TitleView) findViewById(R.id.title);
        et_personal_profile = (EditText) findViewById(R.id.et_personal_profile);
        mBTLogin = (Button) findViewById(R.id.btn_ok);
        mBTLogin.setOnClickListener(this);
        if(!Utils.isEmpty(myIntro)){
            et_personal_profile.setText(myIntro);
        }
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
                if (!Utils.isEmpty(et_personal_profile.getText().toString())) {
                    updateMyIntro();
                } else {
                    ToastUtil.show(mContext, "简介不能为空");
                }
                break;

            default:
                break;
        }
    }

    /**
     * 更新个人简介
     */
    public void updateMyIntro() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.UPDATE_INTRO).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("intro", et_personal_profile.getText().toString());
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                ToastUtil.show(mContext, ((BaseBean) t).getMsg());
                finish();
            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, msg);
            }
        }, MyInfoBean.class);
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
