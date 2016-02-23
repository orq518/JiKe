package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.AdDetailsBean;
import com.topad.bean.AdServiceCaseListBean;
import com.topad.bean.BaseBean;
import com.topad.bean.BuyItBean;
import com.topad.bean.MyWalletBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.MyGridView;
import com.topad.view.customviews.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * ${todo}<服务详情>
 *     category＝ 广告创意－1、营销策略－2、影视广告－3、动漫创作－4
 * @author lht
 * @data: on 15/10/26 18:32
 */
public class ADSDetailsActivity extends BaseActivity implements OnClickListener {
    private static final String LTAG = ADSDetailsActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** ScrollView **/
    private ScrollView mScrollView;
    /** 图片 **/
    private ImageView mAdsIcon;
    /** 名称 **/
    private TextView mName;
    /** 价钱 **/
    private TextView mMoney;
    /** 笔数 **/
    private TextView mCount;
    /** 好评 **/
    private TextView mPraise;
    /** 成交额 **/
    private TextView mBusiness;
    /** 认证 **/
    private ImageView mAuthIcon;
    /** 地址 **/
    private TextView mAddress;
    /** 内容 **/
    private TextView mContent;
    /** 案例 **/
    private MyGridView mGridView;
    /** 收藏 **/
    private Button mCollect;
    /** 联系服务商 **/
    private Button mCall;
    /** 购买此产品 **/
    private Button mBuy;

    /** 编辑-产品详情数据元 **/
    private AdDetailsBean mAdDetailsBean;
    /** 编辑-产品案例数据元 **/
    private AdServiceCaseListBean mAdCaseListBean;
    /** 认证状态 **/
    private String mImgLicense;
    /** 地址 **/
    private String address;
    /** 图片 **/
    private String img;
    /** 电话 **/
    private String mobile;
    /** serviceid **/
    private String serviceid;

    /** 案例图片数据元 **/
    private List<String> imgs = new ArrayList<String>();

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_ads_details;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        mScrollView = (ScrollView) findViewById(R.id.scroll_view);
        mAdsIcon = (ImageView) findViewById(R.id.ads_icon);
        mName = (TextView) findViewById(R.id.tv_name);
        mMoney = (TextView) findViewById(R.id.tv_money);
        mCount = (TextView) findViewById(R.id.tv_count);
        mPraise = (TextView) findViewById(R.id.tv_praise);
        mBusiness = (TextView) findViewById(R.id.tv_business);
        mPraise = (TextView) findViewById(R.id.tv_praise);
        mAuthIcon = (ImageView) findViewById(R.id.ads_auth_icon);

        mAddress = (TextView) findViewById(R.id.tv_address);
        mContent = (TextView) findViewById(R.id.tv_introduce_content);
        mGridView = (MyGridView) findViewById(R.id.gv_case);
        mCollect = (Button) findViewById(R.id.btn_collect);
        mCall = (Button) findViewById(R.id.btn_call);
        mBuy = (Button) findViewById(R.id.btn_buy);

        mCollect.setOnClickListener(this);
        mCall.setOnClickListener(this);
        mBuy.setOnClickListener(this);
    }

    @Override
    public void initData() {
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            mAdDetailsBean = (AdDetailsBean) intent.getSerializableExtra("data_details");
            mAdCaseListBean = (AdServiceCaseListBean) intent.getSerializableExtra("data_case");
            mImgLicense = intent.getStringExtra("data_img_license");
            address = intent.getStringExtra("data_address");
            img  = intent.getStringExtra("data_img");
            mobile  = intent.getStringExtra("data_mobile");
            serviceid  = intent.getStringExtra("data_serviceid");

        }
        // 图片
        if(!Utils.isEmpty(img)){
            String picUrl = Constants.getCurrUrl() + Constants.CASE_IMAGE_URL_HEADER + img;
            LogUtil.d("tao", "11111111"+picUrl);
            ImageLoader.getInstance().displayImage(picUrl, mAdsIcon, TopADApplication.getSelf().getImageLoaderOption(),
                    new ImageLoadingListener(){
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


        // 名称
        if(!Utils.isEmpty(mAdDetailsBean.getServicename())){
            mName.setText(mAdDetailsBean.getServicename());
        }

        // 价钱
        if(!Utils.isEmpty(mAdDetailsBean.getPrice())){
            SpannableStringBuilder ssb = new SpannableStringBuilder("￥" +  mAdDetailsBean.getPrice() + "/单品");
            mMoney.setText(ssb.toString());
        }
        // 笔数
        if(!Utils.isEmpty(mAdDetailsBean.getSalecount())){
            SpannableStringBuilder ssb2 = new SpannableStringBuilder("已出售：" +  mAdDetailsBean.getSalecount() + "笔");
            mCount.setText(ssb2.toString());
        }
        // 认证
        if(!Utils.isEmpty(mImgLicense)){
            mAuthIcon.setImageDrawable(getResources().getDrawable(R.drawable.ads_icon_rz_ok));
        }else{
            mAuthIcon.setImageDrawable(getResources().getDrawable(R.drawable.ads_icon_rz_ing));
        }
        // 内容
        if(!Utils.isEmpty(mAdDetailsBean.getIntro())){
            mContent.setText(mAdDetailsBean.getIntro());
        }
        // 地址
        if(!Utils.isEmpty(address)){
            mAddress.setText(address);
        }else{
            mAddress.setText("");
        }
        // 显示数据
        showView();
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        mTitleView.setTitle("产品详情");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        mScrollView.scrollTo(0, 0);

        //为GridView设置适配器
        mGridView.setAdapter(new MyAdapter(this));
        //注册监听事件
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                Intent intent = new Intent(ADSDetailsActivity.this, ADSCaseActivity.class);
                intent.putExtra("data_case", mAdCaseListBean);
                startActivity(intent);
            }
        });

        setData();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 收藏
            case R.id.btn_collect:

                break;

            // 联系服务商
            case R.id.btn_call:
                //用intent启动拨打电话
                if(!Utils.isEmpty(mobile)){
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + mobile));
                    startActivity(intent);
                }
                break;

            // 购买此产品
            case R.id.btn_buy:
                if(!Utils.isEmpty(serviceid)){
                    // 拼接url
                    StringBuffer sb = new StringBuffer();
                    sb.append(Constants.getCurrUrl()).append(Constants.URL_BUY_IT).append("?");
                    String url = sb.toString();
                    RequestParams rp = new RequestParams();
                    rp.add("userid", TopADApplication.getSelf().getUserId());
                    rp.add("serviceid", serviceid);
                    rp.add("userid2", mAdDetailsBean.getUserid());
                    rp.add("type1", mAdDetailsBean.getType1());
                    rp.add("type2", mAdDetailsBean.getType2());
                    rp.add("title", mAdDetailsBean.getServicename());
                    rp.add("detail", mAdDetailsBean.getIntro());
                    rp.add("budget", mAdDetailsBean.getPrice());
                    rp.add("token", TopADApplication.getSelf().getToken());

                    postWithLoading(url, rp, false, new HttpCallback() {
                        @Override
                        public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                            BuyItBean bean = (BuyItBean) t;
                            if (bean != null && !Utils.isEmpty(bean.getNeedid())) {
                                Intent intent = new Intent(ADSDetailsActivity.this, MyNeedsActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }

                        @Override
                        public void onFailure(BaseBean base) {
                            int status = base.getStatus();// 状态码
                            String msg = base.getMsg();// 错误信息
                            ToastUtil.show(mContext, "status = " + status + "\n"
                                    + "msg = " + msg);
                        }
                    }, BuyItBean.class);
                }
                break;

            default:
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

    //自定义适配器
    class MyAdapter extends BaseAdapter {
        //上下文对象
        private Context context;

        MyAdapter(Context context) {
            this.context = context;
        }

        public int getCount() {
            return imgs.size();
        }

        public Object getItem(int item) {
            return item;
        }

        public long getItemId(int id) {
            return id;
        }

        //创建View方法
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(context);
                imageView.setLayoutParams(
                        new GridView.LayoutParams(
                                (int) (getResources().getDisplayMetrics().density*115),
                                (int) (getResources().getDisplayMetrics().density*115)));//设置ImageView对象布局
                imageView.setAdjustViewBounds(false);//设置边界对齐
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);//设置刻度的类型
                imageView.setPadding(8, 8, 8, 8);//设置间距
            } else {
                imageView = (ImageView) convertView;
            }

            //为ImageView设置图片资源
            String picUrl = Constants.getCurrUrl() + Constants.CASE_IMAGE_URL_HEADER + imgs.get(position);
            ImageLoader.getInstance().displayImage(picUrl, imageView, TopADApplication.getSelf().getImageLoaderOption());
            return imageView;
        }
    }

    /**
     * 设置案例数据
     */
    private void setData() {
        if(mAdCaseListBean != null && mAdCaseListBean.data.size()>0){
            if(!Utils.isEmpty(mAdCaseListBean.data.get(0).getImgs())){
                String[] aa = mAdCaseListBean.data.get(0).getImgs().split("\\|");
                for(int i = 0; i < aa.length; i++){
                    imgs.add(0, aa[i]);
                }
            }
        }
    }
}

