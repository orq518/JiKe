package com.topad.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.AdProductBean;
import com.topad.bean.BaseBean;
import com.topad.bean.CaseBean;
import com.topad.bean.RegisterBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.Md5;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

import java.util.ArrayList;

/**
 * ${todo}<添加产品页>
 *
 * @author lht
 * @data: on 15/11/2 18:06
 */
public class AddProductActivity extends BaseActivity implements View.OnClickListener{
    private static final String LTAG = AddProductActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** 产品类别 **/
    private LinearLayout mLYClass;
    /** 产品类别 **/
    private TextView mTVClass;
    /** 产品名称 **/
    private EditText mETName;
    /** 产品价格 **/
    private EditText mETOffer;
    /** 产品详情 **/
    private EditText mETDetails;
    /** 添加案例 **/
    private ImageView mIVAdd;
    /** 提交 **/
    private Button mBTAdd;

    /** 职业分类1 **/
    private String type1;
    /** 职业分类2 **/
    private String type2;
    /** 产品名称 **/
    private String servicename;
    /** 出售价格 **/
    private String price;
    /** 产品简介 **/
    private String intro;

    /** 案例实体 **/
    /** 数据源 **/
    private ArrayList<CaseBean> caseList = new ArrayList<CaseBean>();
    final int CASE = 1;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_add_product;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        mLYClass = (LinearLayout) findViewById(R.id.ly_product_class);
        mTVClass = (TextView) findViewById(R.id.tv_product_class);
        mETName = (EditText) findViewById(R.id.et_product_name);
        mETOffer = (EditText) findViewById(R.id.et_product_offer);
        mETDetails = (EditText) findViewById(R.id.et_product_details);
        mIVAdd = (ImageView) findViewById(R.id.iv_add_item);
        mBTAdd = (Button) findViewById(R.id.btn_add);

        mLYClass.setOnClickListener(this);
        mIVAdd.setOnClickListener(this);
        mBTAdd.setOnClickListener(this);
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
        mTitleView.setTitle("我的服务产品设计方案");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        setNextBtnState(false);

        // 产品名称
        mETName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mETName);
                if (!Utils.isEmpty(data)) {
                    servicename = data;
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
                String name = getData(mETName);
                String offer = getData(mETOffer);
                String details = getData(mETDetails);

                if (!Utils.isEmpty(name) && name.length() > 0
                        && !Utils.isEmpty(offer) && offer.length() > 0
                        && !Utils.isEmpty(details) && details.length() > 0
                        && !Utils.isEmpty(mTVClass.getText().toString())) {
                    setNextBtnState(true);
                } else {
                    setNextBtnState(false);
                }
            }
        });

        // 产品价格
        mETOffer.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mETOffer);
                if (!Utils.isEmpty(data)) {
                    price = data;
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
                String name = getData(mETName);
                String offer = getData(mETOffer);
                String details = getData(mETDetails);

                if (!Utils.isEmpty(name) && name.length() > 0
                        && !Utils.isEmpty(offer) && offer.length() > 0
                        && !Utils.isEmpty(details) && details.length() > 0
                        && !Utils.isEmpty(mTVClass.getText().toString())) {
                    setNextBtnState(true);
                } else {
                    setNextBtnState(false);
                }
            }
        });

        // 产品详情
        mETDetails.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mETDetails);
                if (!Utils.isEmpty(data)) {
                    intro = data;
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
                String name = getData(mETName);
                String offer = getData(mETOffer);
                String details = getData(mETDetails);

                if (!Utils.isEmpty(name) && name.length() > 0
                        && !Utils.isEmpty(offer) && offer.length() > 0
                        && !Utils.isEmpty(details) && details.length() > 0
                        && !Utils.isEmpty(mTVClass.getText().toString())) {
                    setNextBtnState(true);
                } else {
                    setNextBtnState(false);
                }
            }
        });
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
            // 产品类别
            case R.id.ly_product_class:
                intent = new Intent(mContext, ProductClassListActivity.class);
                startActivity(intent);

                IntentFilter filter = new IntentFilter();
                filter.addAction(Constants.BROADCAST_ACTION_PRODUCT_CLASS);
                registerReceiver(broadcastReceiver, filter);
                break;

            // 添加案例
            case R.id.iv_add_item:
                intent = new Intent(mContext, AddCaseActivity.class);
                startActivityForResult(intent, CASE);
                break;

            // 提交
            case R.id.btn_add:

                // 拼接url
                StringBuffer sb = new StringBuffer();
                sb.append(Constants.getCurrUrl()).append(Constants.URL_ADD_PRODUCT).append("?");
                String url = sb.toString();
                RequestParams rp=new RequestParams();
                rp.add("userid", TopADApplication.getSelf().getUserId());
                rp.add("type1", type1);
                rp.add("type2", type2);
                rp.add("servicename", servicename);
                rp.add("price", price);
                rp.add("intro", intro);
                rp.add("token", TopADApplication.getSelf().getToken());


                postWithLoading(url, rp, false, new HttpCallback() {
                    @Override
                    public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {

                    }

                    @Override
                    public void onFailure(BaseBean base) {
                        int status = base.getStatus();// 状态码
                        String msg = base.getMsg();// 错误信息

//                        if(status == 10001){// 验证码不存在
//
//                        }else if(status == 10002){// 验证码过期失效（大于30分钟）
//
//                        }else if(status == 10003){// 手机号已经被注册
//
//                        }

                        LogUtil.d(LTAG, "status = " + status + "\n" + "msg = " + msg);
                        ToastUtil.show(mContext, msg);
                    }
                }, BaseBean.class, true);
                break;

            default:
                break;
        }
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
        if (mBTAdd == null)
            return;
        mBTAdd.setEnabled(flag);
        mBTAdd.setClickable(flag);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        }

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (Constants.BROADCAST_ACTION_PRODUCT_CLASS.equals(action)) { //我的产品类别
                String str = intent.getStringExtra("media_class");
                String typea = intent.getStringExtra("type1");
                String typeb = intent.getStringExtra("type2");
                if (!Utils.isEmpty(str) && !Utils.isEmpty(typea) && !Utils.isEmpty(typeb)) {
                    // 媒体类型
                    mTVClass.setVisibility(View.VISIBLE);
                    mTVClass.setText(str);

                    type1 = typea;
                    type2 = typeb;
                }
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CASE:
                if (data != null) {
                    Bundle buddle = data.getExtras();
                    CaseBean caseBean = (CaseBean) buddle.getSerializable("data");
                    caseList.add(caseBean);
                }
                break;
            default:
                break;

        }
    }
}
