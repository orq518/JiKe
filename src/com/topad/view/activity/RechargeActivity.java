package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<充值页面>
 *
 * @author lht
 * @data: on 15/7/31 16:22
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = RechargeActivity.class.getSimpleName();
    /** 上下文对象 **/
    private Context mContext;
    /** title布局 **/
    private TitleView mTitle;
    /** 金额 **/
    private EditText mEtMoney;
    /** 取消 **/
    private ImageView mIvClose;
    /** 下一步 **/
    private Button mBtNext;

    /** 支付宝返回用户ID **/
    private String mSellerId;
    /** 订单号 **/
    private String mOutTradeNo;
    /** 传入支付的项目名称（钱包充值、保证金、xxxx项目款托管） **/
    private String mMoney;
    /** 金额 **/
    private String mSubject;
    /** 传入系统的userid |支付类型|其它参数
     支付类型说明：1、钱包充值 2、交纳保证金 3、项目款托管
     其它参数说明：支付类型3项目款托管时传入needid,支付类型1和2传入0 **/
    private String mBody;

    @Override
    public int setLayoutById() {
        mContext = this;
        return  R.layout.activity_recharge;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {

        mTitle = (TitleView) findViewById(R.id.title);
        mEtMoney = (EditText) findViewById(R.id.et_recharge_money);
        mIvClose = (ImageView) findViewById(R.id.iv_clear);
        mBtNext = (Button) findViewById(R.id.bt_recharge_next_step);

        // 设置顶部布局
        mTitle.setTitle("支付宝支付");
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
                    setNextBtnState(true);
                } else {
                    setNextBtnState(false);
                }

                if (!Utils.isEmpty(money)) {// 有内容
                    mIvClose.setVisibility(View.VISIBLE);
                } else {
                    mIvClose.setVisibility(View.GONE);
                }
            }
        });

        // 设置焦点监听
        mEtMoney.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus && !Utils.isEmpty(mEtMoney.getText().toString())) {// 此处为得到焦点时的处理内容，并且有内容
                    mIvClose.setVisibility(View.VISIBLE);
                } else {// 此处为失去焦点时的处理内容
                    mIvClose.setVisibility(View.GONE);
                }
            }
        });

        // 注册监听
        mBtNext.setOnClickListener(this);
        mIvClose.setOnClickListener(this);

        // 设置充值
        setNextBtnState(false);
    }

    @Override
    public void initData() {
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            mSellerId = intent.getStringExtra("seller_id");
            mOutTradeNo = intent.getStringExtra("out_trade_no");
            mMoney = intent.getStringExtra("total_fee");
            mSubject = intent.getStringExtra("subject");
            mBody = intent.getStringExtra("body");
        }
        if(!Utils.isEmpty(mMoney)){
            mEtMoney.setText(mMoney);
        }

    }

    /**
     * 点击事件
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 清除
            case R.id.iv_clear:{
                mEtMoney.setText("");
            }
            break;
            // 下一步
            case R.id.bt_recharge_next_step:
                if (Utils.isEmpty(mMoney)){
                    Utils.showToast(this, "充值金额不能为空");
                    return;
                }
                if (Float.parseFloat(mMoney) < 1){
                    Utils.showToast(this, "充值金额最低1元");
                    return;
                }
                if (Double.parseDouble(mMoney) >= 1000000000){
                    Utils.showToast(this, "超出充值限额，请重新输入金额");
                    return;
                }

                // 拼接url
                StringBuffer sb = new StringBuffer();
                sb.append(Constants.getCurrUrl()).append(Constants.URL_ALI_RETUREN).append("?");
                String url = sb.toString();
                RequestParams rp = new RequestParams();
                rp.add("seller_id", mSellerId);
                rp.add("out_trade_no", mOutTradeNo);
                rp.add("total_fee", mEtMoney.getText().toString());
                rp.add("subject", mSubject);
                rp.add("body", mBody);

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
