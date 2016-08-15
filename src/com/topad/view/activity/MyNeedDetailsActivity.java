package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.alipay.AliPayInterface;
import com.topad.alipay.AliPayUtil;
import com.topad.alipay.PayResult;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.GrabSingleBean;
import com.topad.bean.MyInfoBean;
import com.topad.bean.MyNeedBean;
import com.topad.bean.MyNeedListBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.DialogManager;
import com.topad.util.LogUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.CircleImageView;
import com.topad.view.customviews.MyGridView;
import com.topad.view.customviews.TitleView;
import com.topad.view.customviews.mylist.MyListView;

import org.android.agoo.ut.UT;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * ${todo}<我的需求详情页>
 *
 * @author lht
 * @data: on 15/10/28 16:32
 */
public class MyNeedDetailsActivity extends BaseActivity implements View.OnClickListener ,AliPayInterface {
    private static final String LTAG = MyNeedDetailsActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** 名称 **/
    private TextView mName;
    /** 价钱 **/
    private TextView mMoney;
    /** 是否托管 **/
    private TextView mTVState;
    /** 内容 **/
    private TextView mContent;
    /** 地址 **/
    private TextView mAddress;
    /** 类型 **/
    private TextView mTVType;
    /** 时间 **/
    private TextView mTVTime;

    /** 项目已完成，进行中布局 **/
    private LinearLayout mLYProductFinish;
    /** 公司头像 **/
    private CircleImageView mTVGSIcon;
    /** 公司名 **/
    private TextView mTVGSName;
    /** 项目状态 **/
    private TextView mTVProgectState;
    /** 时间 **/
    private TextView mTVProgectTime;
    /** 项目完成 **/
    private Button mFinish;
    /** 完成文案提示 **/
    private TextView mTVPrompt;

//    /** 已、未托管布局 **/
//    private LinearLayout mLYTrust;
    /** 项目款托管 **/
    private Button mProjectTrust;
    /** 未完成文案提示 **/
    private TextView mTVPrompt2;
    /** 取消项目 **/
    private Button mProjectCancel;
    /** 列表 **/
    private ListView mListview;


    /** 状态 0 - 未开始 1－项目进行中，2-项目完成  */
    private String state;
    /** 需求id **/
    private String needId;
    /** **/
    private GrabSingleBean grabSingleBean;
    /** 数据源 **/
    private ArrayList<MyNeedBean> bankList = new ArrayList<MyNeedBean>();

    /** 获取个人信息 **/
    private static final int GET_USERINFE = 0;

    /** 获取个人信息 **/
    private Handler updateHandler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                // 获取个人信息
                case GET_USERINFE:
                    Bundle b = msg.getData();
                    String userid = b.getString("userid");
                    getInfoData(userid);
                    break;
            }
        };
    };

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_need_details;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);

        mName = (TextView) findViewById(R.id.tv_name);
        mMoney = (TextView) findViewById(R.id.tv_price);
        mTVState = (TextView) findViewById(R.id.tv_state);
        mContent = (TextView) findViewById(R.id.tv_content);
        mAddress = (TextView) findViewById(R.id.tv_address);
        mTVType = (TextView) findViewById(R.id.tv_type);
        mTVTime = (TextView) findViewById(R.id.tv_time);

        mLYProductFinish = (LinearLayout) findViewById(R.id.ly_product_finish);
        mTVGSIcon = (CircleImageView) findViewById(R.id.iv_gs_icon);
        mTVGSName = (TextView) findViewById(R.id.tv_gs_name);
        mTVProgectState = (TextView) findViewById(R.id.tv_progect_state);
        mTVProgectTime = (TextView) findViewById(R.id.tv_progect_time);
        mFinish = (Button) findViewById(R.id.btn_finish);
        mTVPrompt = (TextView) findViewById(R.id.tv_prompt);


//        mLYTrust = (LinearLayout) findViewById(R.id.ly_trust);
        mProjectTrust = (Button) findViewById(R.id.btn_project_trust);
        mTVPrompt2 = (TextView) findViewById(R.id.tv_prompt2);
        mProjectCancel = (Button) findViewById(R.id.btn_project_cancel);
        mListview = (ListView) findViewById(R.id.listview);

        mFinish.setOnClickListener(this);
        mProjectTrust.setOnClickListener(this);
        mProjectCancel.setOnClickListener(this);
    }

    @Override
    public void initData() {
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            grabSingleBean = (GrabSingleBean) intent.getSerializableExtra("data_details");
            state = intent.getStringExtra("state");
            needId = intent.getStringExtra("needId");
        }

        showView();
    }

    public void showView() {
        // 设置title
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
        // 名字
        if (!Utils.isEmpty(grabSingleBean.getTitle())) {
            mTitleView.setTitle(grabSingleBean.getTitle());
        }
        // 名字
        if (!Utils.isEmpty(grabSingleBean.getCompanyname())) {
            mName.setText(grabSingleBean.getCompanyname());
        }

        // 价格
        if (!Utils.isEmpty(grabSingleBean.getBudget())) {
            SpannableStringBuilder ssb = new SpannableStringBuilder("￥" + grabSingleBean.getBudget());
            mMoney.setText(ssb.toString());
        }

        // 介绍
        if (!Utils.isEmpty(grabSingleBean.getDetail())) {
            mContent.setText(grabSingleBean.getDetail());
        }

        // 地址
        if (!Utils.isEmpty(grabSingleBean.getAddress())) {
            mAddress.setText(grabSingleBean.getAddress());
        }

        // 0 - 未开始 1－项目进行中，2-项目完成
        if ("0".equals(state)) {//  未开始
            mLYProductFinish.setVisibility(View.GONE);
            mFinish.setVisibility(View.GONE);
            mTVPrompt.setVisibility(View.GONE);
//            mLYTrust.setVisibility(View.VISIBLE);
            mTVPrompt2.setVisibility(View.VISIBLE);
            mProjectCancel.setVisibility(View.VISIBLE);
            mListview.setVisibility(View.VISIBLE);

            // 托管
            if (!Utils.isEmpty(grabSingleBean.getIspay())){
                if("0".equals(grabSingleBean.getIspay())){
                    mProjectTrust.setVisibility(View.VISIBLE);
                    mTVPrompt2.setVisibility(View.VISIBLE);
                    mTVState.setVisibility(View.GONE);
                }else{
                    mProjectTrust.setVisibility(View.GONE);
                    mTVPrompt2.setVisibility(View.GONE);
                    mTVState.setVisibility(View.VISIBLE);
                }
            }

            getData();
        }else if ("1".equals(state)) {// 项目进行中
            mLYProductFinish.setVisibility(View.VISIBLE);
            mFinish.setVisibility(View.VISIBLE);
            mTVPrompt.setVisibility(View.VISIBLE);
//            mLYTrust.setVisibility(View.GONE);
            mTVPrompt2.setVisibility(View.GONE);
            mProjectCancel.setVisibility(View.GONE);
            mListview.setVisibility(View.GONE);

            // 托管
            if (!Utils.isEmpty(grabSingleBean.getIspay())){
                if("0".equals(grabSingleBean.getIspay())){
                    mProjectTrust.setVisibility(View.VISIBLE);
                    mTVPrompt2.setVisibility(View.VISIBLE);
                    mTVState.setVisibility(View.GONE);
                }else{
                    mProjectTrust.setVisibility(View.GONE);
                    mTVPrompt2.setVisibility(View.GONE);
                    mTVState.setVisibility(View.VISIBLE);
                }
            }

            // 类别
            if (!Utils.isEmpty(grabSingleBean.getType1())
                    && !Utils.isEmpty(grabSingleBean.getType2())) {
                SpannableStringBuilder ssb = new SpannableStringBuilder(grabSingleBean.getType1() + "-" + grabSingleBean.getType2());
                mTVTime.setText(ssb.toString());
            }
            // 时间
            if (!Utils.isEmpty(grabSingleBean.getEnddate())) {
                String[] sourceStrArray = grabSingleBean.getEnddate().split(" ");
                mTVType.setText(sourceStrArray[0]);
                mTVType.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.pic_time), null, null, null);
            }
            getInfoData(grabSingleBean.getUserid2());
        } else if ("2".equals(state)) {// 项目完成
            mLYProductFinish.setVisibility(View.VISIBLE);
            mFinish.setVisibility(View.GONE);
            mTVPrompt.setVisibility(View.GONE);
//            mLYTrust.setVisibility(View.GONE);
            mProjectCancel.setVisibility(View.GONE);
            mListview.setVisibility(View.GONE);
            mProjectTrust.setVisibility(View.GONE);
            mTVPrompt2.setVisibility(View.GONE);
            // 托管
            if (!Utils.isEmpty(grabSingleBean.getIspay())){
                if("0".equals(grabSingleBean.getIspay())){
                    mTVState.setVisibility(View.GONE);
                }else{
                    mTVState.setVisibility(View.VISIBLE);
                }
            }

            // 类别
            if (!Utils.isEmpty(grabSingleBean.getType1())
                    && !Utils.isEmpty(grabSingleBean.getType2())) {
                SpannableStringBuilder ssb = new SpannableStringBuilder("类型：" + grabSingleBean.getType1() + "-" + grabSingleBean.getType2());
                mTVTime.setText(ssb.toString());
            }
            // 时间
            if (!Utils.isEmpty(grabSingleBean.getEnddate())) {
                String[] sourceStrArray = grabSingleBean.getEnddate().split(" ");
                mTVType.setText(sourceStrArray[0]);
                mTVType.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.pic_time), null, null, null);
            }
            getInfoData(grabSingleBean.getUserid2());
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 项目完成
            case R.id.btn_finish:
                DialogManager.showDialog(MyNeedDetailsActivity.this, null, "点击确定，将完成项目。托管的项目款将直接打入接单方钱包。",
                        "确定", "", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 拼接url
                                StringBuffer sb = new StringBuffer();
                                sb.append(Constants.getCurrUrl()).append(Constants.URL_NEED_ENDPRJ).append("?");
                                String url = sb.toString();
                                RequestParams rp = new RequestParams();
                                rp.add("needid", needId);
                                rp.add("userid", grabSingleBean.getUserid());
                                rp.add("token", TopADApplication.getSelf().getToken());
                                postWithLoading(url, rp, false, new HttpCallback() {
                                    @Override
                                    public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                                        mFinish.setVisibility(View.GONE);
                                        mProjectTrust.setVisibility(View.GONE);
                                        mTVPrompt2.setVisibility(View.GONE);
                                        mTVPrompt.setVisibility(View.GONE);
                                        mTVProgectState.setText("项目已完成");
                                    }

                                    @Override
                                    public void onFailure(BaseBean base) {
                                        int status = base.getStatus();// 状态码
                                        String msg = base.getMsg();// 错误信息
//                        ToastUtil.show(mContext, "status = " + status + "\n"
//                                + "msg = " + msg);
                                    }
                                }, BaseBean.class);
                            }
                        }, null ,true, null);


                break;

            // 项目款托管
            case R.id.btn_project_trust:
                AliPayUtil aliPayUtil=new AliPayUtil(MyNeedDetailsActivity.this);
                aliPayUtil.aliPay(MyNeedDetailsActivity.this,"项目款托管",TopADApplication.getSelf().getUserId()+"|3|"+grabSingleBean.getId(),grabSingleBean.getBudget());
                break;

            // 项目取消
            case R.id.btn_project_cancel:
                // 拼接url
                StringBuffer sbcancel = new StringBuffer();
                sbcancel.append(Constants.getCurrUrl()).append(Constants.URL_NEED_DELPROJECT).append("?");
                String urlcancel = sbcancel.toString();
                RequestParams rpcancel = new RequestParams();
                rpcancel.add("needid", needId);
                rpcancel.add("userid", grabSingleBean.getUserid());
                rpcancel.add("token", TopADApplication.getSelf().getToken());
                postWithLoading(urlcancel, rpcancel, false, new HttpCallback() {
                    @Override
                    public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                        finish();
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

            default:
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
            // 成功后
            mProjectTrust.setVisibility(View.GONE);
            mTVPrompt2.setVisibility(View.GONE);
            mTVState.setVisibility(View.VISIBLE);
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

    private class ListAdapter extends BaseAdapter {
        private LayoutInflater mInflater;

        public ListAdapter() {
            mInflater = LayoutInflater.from(mContext);
        }

        @Override
        public int getCount() {
            // TODO Auto-generated method stub
            return bankList.size();
        }

        @Override
        public Object getItem(int arg0) {
            // TODO Auto-generated method stub
            return bankList.get(arg0);
        }

        @Override
        public long getItemId(int arg0) {
            // TODO Auto-generated method stub
            return arg0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate((R.layout.activity_need_details_item), null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.tv_need_item_gs_icon);
                holder.name = (TextView) convertView.findViewById(R.id.tv_need_item_gs_name);
                holder.state = (TextView) convertView.findViewById(R.id.tv_need_item_state);
                holder.time = (TextView) convertView.findViewById(R.id.tv_need_item_time);
                holder.agree = (Button) convertView.findViewById(R.id.btn_agree);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            if (!Utils.isEmpty(bankList.get(position).getImghead())) {
                String picUrl = Constants.getCurrUrl() + Constants.CASE_IMAGE_URL_HEADER + bankList.get(position).getImghead();
                ImageLoader.getInstance().displayImage(picUrl, holder.icon, TopADApplication.getSelf().getImageLoaderOption(),
                        new ImageLoadingListener() {
                            @Override
                            public void onLoadingStarted(String s, View view) {

                            }

                            @Override
                            public void onLoadingFailed(String s, View view, FailReason failReason) {

                            }

                            @Override
                            public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                            }

                            @Override
                            public void onLoadingCancelled(String s, View view) {

                            }
                        });
            }
            holder.icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MyNeedDetailsActivity.this, DetailsActivity.class);
                    intent.putExtra("data_details", bankList.get(position));
                    startActivity(intent);
                }
            });

            // 公司名
            holder.name.setText(bankList.get(position).getCompanyname());

            // 时间
            Date date = null;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            try {
                date = sdf.parse(bankList.get(position).getAdddate());
            } catch (ParseException e) {
                e.printStackTrace();
            }
//            holder.time.setText(Utils.getTimeFormatText(date));
            holder.time.setVisibility(View.INVISIBLE);
            // 同意
            holder.agree.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 拼接url
                    StringBuffer sb = new StringBuffer();
                    sb.append(Constants.getCurrUrl()).append(Constants.URL_NEED_START).append("?");
                    String url = sb.toString();
                    RequestParams rp = new RequestParams();
                    rp.add("needid", needId);
                    rp.add("userid1", grabSingleBean.getUserid());
                    rp.add("userid2", bankList.get(position).getUserid());
                    rp.add("token", TopADApplication.getSelf().getToken());
                    postWithLoading(url, rp, false, new HttpCallback() {
                        @Override
                        public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                            mLYProductFinish.setVisibility(View.VISIBLE);
                            mFinish.setVisibility(View.VISIBLE);
                            mTVPrompt.setVisibility(View.VISIBLE);
//                            mLYTrust.setVisibility(View.GONE);

                            mProjectTrust.setVisibility(View.GONE);
                            mTVPrompt2.setVisibility(View.GONE);
                            mProjectCancel.setVisibility(View.GONE);
                            mListview.setVisibility(View.GONE);
                            mTVState.setVisibility(View.VISIBLE);
                            state="1";
                            // 类别
                            if (!Utils.isEmpty(grabSingleBean.getType1())
                                    && !Utils.isEmpty(grabSingleBean.getType2())) {
                                SpannableStringBuilder ssb = new SpannableStringBuilder(grabSingleBean.getType1() + "-" + grabSingleBean.getType2());
                                mTVTime.setText(ssb.toString());
                                mTVTime.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                            }
                            // 时间
                            if (!Utils.isEmpty(grabSingleBean.getEnddate())) {
                                String[] sourceStrArray = grabSingleBean.getEnddate().split(" ");
                                mTVType.setText(sourceStrArray[0]);
                                mTVType.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.pic_time), null, null, null);
                            }

                            Message msg = new Message();
                            msg.what = GET_USERINFE;
                            Bundle b = new Bundle();
                            b.putString("userid", bankList.get(position).getUserid());
                            msg.setData(b);
                            updateHandler.sendMessage(msg);
                        }

                        @Override
                        public void onFailure(BaseBean base) {
                            int status = base.getStatus();// 状态码
                            String msg = base.getMsg();// 错误信息
//                            ToastUtil.show(mContext, "status = " + status + "\n"
//                                    + "msg = " + msg);
                        }
                    }, BaseBean.class);
                }
            });
            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView name;
            TextView state;
            TextView time;
            Button agree;
        }
    }

    /**
     * 获取抢单列表数据
     */
    public void getData() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_GET_REQUEST_LIST).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("needid", needId);
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                MyNeedListBean bean = (MyNeedListBean) t;
                if (bean != null && bean.data.size() != 0) {
                    for (int i = 0; i < bean.data.size(); i++) {
                        bankList.add(bean.data.get(i));
                    }
                    mListview.setAdapter(new ListAdapter());
                }

                String str = bean.data.size() + "人抢单";
                SpannableStringBuilder spanStrContent = new SpannableStringBuilder(str);
                ForegroundColorSpan span_1 = new ForegroundColorSpan(Color.argb(255, 255, 0, 0));
                spanStrContent.setSpan(span_1, 0, 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
                mTVType.setText(spanStrContent.toString());

                // 时间
                if (!Utils.isEmpty(grabSingleBean.getEnddate())) {
                    String[] sourceStrArray = grabSingleBean.getEnddate().split(" ");
                    mTVTime.setText(sourceStrArray[0]);
                    mTVTime.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(R.drawable.pic_time), null, null, null);
                }

            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, "status = " + status + "\n"
                        + "msg = " + msg);
            }
        }, MyNeedListBean.class);
    }


    /**
     * 获取个人数据
     */
    public void getInfoData(String userid) {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.GETINFO).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", userid);
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                final MyInfoBean base = (MyInfoBean) t;
                if (base != null) {

                    // 公司头像
                    if (!Utils.isEmpty(base.getData().getImghead())) {
                        String picUrl = Constants.getCurrUrl() + Constants.CASE_IMAGE_URL_HEADER + base.getData().getImghead();
                        ImageLoader.getInstance().displayImage(picUrl, mTVGSIcon, TopADApplication.getSelf().getImageLoaderOption(),
                                new ImageLoadingListener() {
                                    @Override
                                    public void onLoadingStarted(String s, View view) {

                                    }

                                    @Override
                                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                                    }

                                    @Override
                                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                                    }

                                    @Override
                                    public void onLoadingCancelled(String s, View view) {

                                    }
                                });
                    }


                    // 公司名
                    if(!Utils.isEmpty(base.getData().getCompanyname())){
                        mTVGSName.setText(base.getData().getCompanyname());
                    }

                    // 0 - 未开始 1－项目进行中，2-项目完成
                    if ("2".equals(state)) {
                        mTVProgectState.setText("项目已完成");
                    }else if("1".equals(state)){
                        mTVProgectState.setText("项目进行中...");
                    }

                    // 时间
                    if(!Utils.isEmpty(base.getData().getAdddate())){
                        String[] sourceStrArray = base.getData().getAdddate().split(" ");
                        mTVProgectTime.setText(sourceStrArray[0]);
                    }

                    mTVGSIcon.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            MyNeedBean myNeedBean = new MyNeedBean();
                            myNeedBean.setAdddate(base.getData().getAdddate());
                            myNeedBean.setAddress(base.getData().getAddress());
                            myNeedBean.setBirthday(base.getData().getBirthday());
                            myNeedBean.setCompanyname(base.getData().getCompanyname());
                            myNeedBean.setId(base.getData().getId());
                            myNeedBean.setImgcard1(base.getData().getImgcard1());
                            myNeedBean.setImgcard2(base.getData().getImgcard2());
                            myNeedBean.setImgdiploma(base.getData().getImgdiploma());
                            myNeedBean.setImghead(base.getData().getImghead());
                            myNeedBean.setImglicense(base.getData().getImglicense());
                            myNeedBean.setImgnamecard(base.getData().getImgnamecard());
                            myNeedBean.setImghead(base.getData().getImghead());
                            myNeedBean.setIntro(base.getData().getIntro());
                            myNeedBean.setJob1(base.getData().getJob1());
                            myNeedBean.setJob2(base.getData().getJob2());
                            myNeedBean.setNeedid(needId);
                            myNeedBean.setNickname(base.getData().getNickname());
                            myNeedBean.setSex(base.getData().getSex());
                            myNeedBean.setUserid(base.getData().getUserid());

                            Intent intent = new Intent(MyNeedDetailsActivity.this, DetailsActivity.class);
                            intent.putExtra("data_details", myNeedBean);
                            startActivity(intent);
                        }
                    });
                }
            }

            @Override
            public void onFailure(BaseBean base) {
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, msg);
            }
        }, MyInfoBean.class);
    }

}
