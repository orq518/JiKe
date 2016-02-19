package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.alipay.AliPayUtil;
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
    // 余额
    private String money;

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
        mTVMoney = (TextView) findViewById(R.id.tv_money);
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
                intentcash.putExtra("money", money);
                intentcash.putExtra("aliaccount", AliPayUtil.SELLER);
                startActivity(intentcash);
                break;

            // 充值
            case R.id.btn_recharge:
                Intent intentrecharge = new Intent(mContext, RechargeActivity.class);
                intentrecharge.putExtra("subject", "钱包充值");
                intentrecharge.putExtra("body", TopADApplication.getSelf().getUserId()+"|1|0");
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
                    money = bean.getMoney();
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
