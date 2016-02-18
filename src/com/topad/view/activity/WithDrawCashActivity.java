package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<提现页面>
 *
 * @author lht
 * @data: on 15/7/31 16:22
 */
public class WithDrawCashActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = WithDrawCashActivity.class.getSimpleName();
    /** 上下文对象 **/
    private Context mContext;
    /** title布局 **/
    private TitleView mTitle;
    /** 账户名 **/
    private EditText mTVName;
    /** 余额 **/
    private TextView mTVTip;
    /** 超额 **/
    private TextView mTVChao;
    /** 金额 **/
    private EditText mEtMoney;
    /** 全部 **/
    private TextView mTVAll;

    /** 下一步 **/
    private Button mBtNext;

    /** 余额 **/
    private String mMoney;
    /** 支付宝账户 **/
    private String mAliaccount;

    @Override
    public int setLayoutById() {
        mContext = this;
        return  R.layout.activity_withdraw_cash;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {

        mTitle = (TitleView) findViewById(R.id.title);
        mTVName = (EditText) findViewById(R.id.tv_name);
        mEtMoney = (EditText) findViewById(R.id.et_cash_money);
        mTVTip = (TextView) findViewById(R.id.tv_tip);
        mTVAll = (TextView) findViewById(R.id.tv_all);
        mTVChao = (TextView) findViewById(R.id.tv_chao_e);

        mBtNext = (Button) findViewById(R.id.bt_next_step);

        // 设置顶部布局
        mTitle.setTitle("提现");
        mTitle.setLeftClickListener(new TitleLeftOnClickListener());

        // 设置身份证
        mEtMoney.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mEtMoney);
                if (!Utils.isEmpty(data)) {
                    mMoney = data;
                }
                // 显示小数点后两位
                String temp = arg0.toString();
                int d = temp.indexOf(".");
                if (d < 0) return;
                if (temp.length() - d - 1 > 2) {
                    arg0.delete(d + 3, d + 4);
                } else if (d == 0) {
                    arg0.delete(d, d + 1);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                String money = getData(mEtMoney);
                if (!Utils.isEmpty(money) && money.length() > 0) {
                    if (Double.parseDouble(money) >= 1000) {
                        mTVTip.setVisibility(View.GONE);
                        mTVAll.setVisibility(View.GONE);
                        mTVChao.setVisibility(View.VISIBLE);
                        setNextBtnState(false);
                    }else{
                        mTVTip.setVisibility(View.VISIBLE);
                        mTVAll.setVisibility(View.VISIBLE);
                        mTVChao.setVisibility(View.GONE);
                        setNextBtnState(true);
                    }

                } else {
                    setNextBtnState(false);
                }
            }
        });

        // 注册监听
        mBtNext.setOnClickListener(this);
        mTVAll.setOnClickListener(this);

        // 设置充值
        setNextBtnState(false);
    }

    @Override
    public void initData() {
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            mMoney = intent.getStringExtra("money");
            mAliaccount = intent.getStringExtra("aliaccount");
        }

        if(!Utils.isEmpty(mMoney)){
            mTVTip.setText("零钱余额" + mMoney + "");
        }
//        if(!Utils.isEmpty(mAliaccount)){
//            mTVName.setText(mAliaccount);
//        }
    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 全部提现
            case R.id.tv_all:

                break;

            // 下一步
            case R.id.bt_next_step:
                if (Utils.isEmpty(mEtMoney.getText().toString())){
                    Utils.showToast(this, "提现金额不能为空");
                    return;
                }
                if (Float.parseFloat(mEtMoney.getText().toString()) < 1){
                    Utils.showToast(this, "提现金额最低1元");
                    return;
                }
                if (Utils.isEmpty( mTVName.getText().toString())){
                    Utils.showToast(this, "请输入支付宝账号");
                    return;
                }

                // 拼接url
                StringBuffer sb = new StringBuffer();
                sb.append(Constants.getCurrUrl()).append(Constants.URL_GET_MONEY).append("?");
                String url = sb.toString();
                RequestParams rp = new RequestParams();
                rp.add("userid", TopADApplication.getSelf().getUserId());
                rp.add("getmoney", mEtMoney.getText().toString());
                rp.add("aliaccount", mTVName.getText().toString());

                postWithLoading(url, rp, false, new HttpCallback() {
                    @Override
                    public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                        BaseBean login = (BaseBean) t;
                        if (login != null) {
                            Toast.makeText(mContext, "提现成功", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(BaseBean base) {
                        int status = base.getStatus();// 状态码
                        String msg = base.getMsg();// 错误信息
                        ToastUtil.show(mContext, "status = " + status + "\n"
                                + "msg = " + msg);
                    }
                }, BaseBean.class);

                break;
        }
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

    /**
     * 设置下一步按钮
     *
     * @param flag
     */
    private void setNextBtnState(boolean flag) {
        if (mBtNext == null)
            return;
        mBtNext.setEnabled(flag);
        mBtNext.setClickable(flag);
    }

    /**
     * 去除EditText的空格
     * @param et
     * @return
     */
    public String getData(EditText et) {
        String s = et.getText().toString();
        return s.replaceAll(" ", "");
    }
}
