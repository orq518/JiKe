package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topad.R;
import com.topad.alipay.AliPayInterface;
import com.topad.alipay.AliPayUtil;
import com.topad.alipay.PayResult;
import com.topad.alipay.PayResultDetail;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<充值页面>
 *
 * @author lht
 * @data: on 15/7/31 16:22
 */
public class RechargeActivity extends BaseActivity implements View.OnClickListener,AliPayInterface {
    private static final String LTAG = RechargeActivity.class.getSimpleName();
    /**
     * 上下文对象
     **/
    private Context mContext;
    /**
     * title布局
     **/
    private TitleView mTitle;
    /**
     * 金额
     **/
    private EditText mEtMoney;
    /**
     * 取消
     **/
    private ImageView mIvClose;
    /**
     * 下一步
     **/
    private Button mBtNext;

    /**
     * 充值金额
     **/
    private String mMoney;
    String subject, body;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_recharge;
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
            subject=getIntent().getStringExtra("subject");
            body=getIntent().getStringExtra("body");
        }

    }

    /**
     * 点击事件
     *
     * @param view
     */
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            // 清除
            case R.id.iv_clear: {
                mEtMoney.setText("");
            }
            break;
            // 下一步
            case R.id.bt_recharge_next_step:
                if (Utils.isEmpty(mMoney)) {
                    Utils.showToast(this, "充值金额不能为空");
                    return;
                }
                if (Float.parseFloat(mMoney) < 0) {
                    Utils.showToast(this, "充值金额最低0.01元");
                    return;
                }
                if (Double.parseDouble(mMoney) >= 1000000000) {
                    Utils.showToast(this, "超出充值限额，请重新输入金额");
                    return;
                }
                AliPayUtil aliPayUtil=new AliPayUtil(this);
                aliPayUtil.aliPay(RechargeActivity.this,subject,body,mMoney);

                break;
        }
    }

    @Override
    public void payResult(PayResult payResult) {
        /**
         * 同步返回的结果必须放置到服务端进行验证（验证的规则请看https://doc.open.alipay.com/doc2/
         * detail.htm?spm=0.0.0.0.xdvAU6&treeId=59&articleId=103665&
         * docType=1) 建议商户依赖异步通知
         */
        String resultInfo = payResult.getResult();// 同步返回需要验证的信息
        Log.d("ouou", "返回结果：" + resultInfo);
        String resultStatus = payResult.getResultStatus();

        // 判断resultStatus 为“9000”则代表支付成功，具体状态码代表含义可参考接口文档
        if (TextUtils.equals(resultStatus, "9000")) {
            Toast.makeText(mContext, "支付成功",
                    Toast.LENGTH_SHORT).show();
            finish();
//            PayResultDetail resultDetail=new PayResultDetail(resultInfo);
//            // 拼接url
//            StringBuffer sb = new StringBuffer();
//            sb.append(Constants.getCurrUrl()).append(Constants.URL_ALI_RETUREN).append("?");
//            String url = sb.toString();
//            RequestParams rp = new RequestParams();
//            rp.add("seller_id", resultDetail.getseller_id());
//            rp.add("out_trade_no", resultDetail.getout_trade_no());
//            rp.add("total_fee", resultDetail.gettotal_fee());
//            rp.add("subject", resultDetail.getsubject());
//            rp.add("body", resultDetail.getbody());
//
//            postWithLoading(url, rp, false, new HttpCallback() {
//                @Override
//                public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
//                    BaseBean login = (BaseBean) t;
//                    if (login != null) {
//                        Toast.makeText(mContext, "成功", Toast.LENGTH_SHORT).show();
//                        finish();
//                    }
//                }
//
//                @Override
//                public void onFailure(BaseBean base) {
//                    int status = base.getStatus();// 状态码
//                    String msg = base.getMsg();// 错误信息
//                    ToastUtil.show(mContext, "status = " + status + "\n"
//                            + "msg = " + msg);
//                }
//            }, BaseBean.class);
        } else {
            // 判断resultStatus 为非"9000"则代表可能支付失败
            // "8000"代表支付结果因为支付渠道原因或者系统原因还在等待支付结果确认，最终交易是否成功以服务端异步通知为准（小概率状态）
            if (TextUtils.equals(resultStatus, "8000")) {
                Toast.makeText(mContext, "支付结果确认中",
                        Toast.LENGTH_SHORT).show();

            } else {
                // 其他值就可以判断为支付失败，包括用户主动取消支付，或者系统返回的错误
                Toast.makeText(mContext, "支付失败",
                        Toast.LENGTH_SHORT).show();

            }
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
     *
     * @param et
     * @return
     */
    public String getData(EditText et) {
        String s = et.getText().toString();
        return s.replaceAll(" ", "");
    }
}
