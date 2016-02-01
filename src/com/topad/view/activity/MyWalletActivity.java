package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.alipay.PayDemoActivity;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.LoginBean;
import com.topad.bean.MyWalletBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Md5;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<我的钱包页面>
 *
 * @author lht
 * @data: on 15/12/7 13:58
 */
public class MyWalletActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = MyWalletActivity.class.getSimpleName();
    // 上下文
    private Context mContext;
    // 顶部布局
    private TitleView mTitleView;
    // 余额
    private TextView mTVMoney;
    // 提现
    private Button mBTCash;
    // 充值
    private Button mBTRecharge;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_my_wallet;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        ;
        mTVMoney = (TextView) findViewById(R.id.tv_money);
        ;
        mBTCash = (Button) findViewById(R.id.btn_cash);
        mBTRecharge = (Button) findViewById(R.id.btn_recharge);

        mBTCash.setOnClickListener(this);
        mBTRecharge.setOnClickListener(this);
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
        mTitleView.setTitle("我的钱包");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        getData();
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
                Intent intentcash = new Intent(mContext, WithDrawCashActivity.class);
                intentcash.putExtra("money", mTVMoney + "");// 余额
                intentcash.putExtra("aliaccount", "");// 支付宝账户
                startActivity(intentcash);
                break;

            // 充值
            case R.id.btn_recharge:
                Intent intentrecharge = new Intent(mContext, RechargeActivity.class);
                intentrecharge.putExtra("seller_id", "");// 支付宝返回用户ID
                intentrecharge.putExtra("out_trade_no", "");// 订单号
                intentrecharge.putExtra("total_fee", "");// 金额
                intentrecharge.putExtra("subject", "");// 传入支付的项目名称
                intentrecharge.putExtra("body", "");// 传入系统的userid |支付类型|其它参数
                startActivity(intentrecharge);
                break;

            default:
                break;
        }
    }

    /**
     * 获取数据
     */
    private void getData() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_WALLET_GETMONEY).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());

        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                MyWalletBean bean = (MyWalletBean) t;
                if (bean != null && !Utils.isEmpty(bean.getMoney())) {
                    mTVMoney.setText("￥" + bean.getMoney());
                }

            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, "status = " + status + "\n"
                        + "msg = " + msg);
            }
        }, MyWalletBean.class);

    }
}
