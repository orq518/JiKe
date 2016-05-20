package com.topad.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.CheckMSGBean;
import com.topad.bean.IsCompanyBean;
import com.topad.bean.LocationBean;
import com.topad.bean.LoginBean;
import com.topad.bean.MyInfoBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.ActivityCollector;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.Md5;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.Utils;
import com.topad.view.customviews.CircleImageView;
import com.topad.view.customviews.TitleView;
import com.umeng.message.PushAgent;

/**
 * 主界面
 */
public class MainActivity extends BaseActivity implements View.OnClickListener, View.OnTouchListener, AMapLocationListener {
    private static final String LTAG = MainActivity.class.getSimpleName();
    // 上下文
    private Context mContext;
    // title布局
    private TitleView mTitle;
    // 我有媒体
    private ImageView mMyMedia;
    // 发布需求
    private ImageView mReleaseDemand;
    // 我要抢单
    private ImageView mGrabSingle;
    // 电视
    private LinearLayout mLYTv;
    // 广播
    private LinearLayout mLYBc;
    // 报纸
    private LinearLayout mLYNp;
    // 户外
    private LinearLayout mLYOd;
    // 杂志
    private LinearLayout mLYMz;
    // 网络
    private LinearLayout mLYNet;
    // 其他1
    private RelativeLayout mRLQiTa1;
    // 其他2
    private RelativeLayout mRLQiTa2;
    // 广告创意
    private LinearLayout mAdvertisingCreativEe;
    // 营销策略
    private LinearLayout mMarketingStrategy;
    // 影视广告
    private LinearLayout mTVC;
    // 动漫创作
    private LinearLayout mAnimeCreate;
    // vi设计
    private LinearLayout mViDesign;
    // LOGO设计
    private LinearLayout mLogoDesign;
    // APP/UI设计
    private LinearLayout mAppUiDesign;
    // 海报设计
    private LinearLayout mProductDesign;
    // 包装设计
    private LinearLayout mDackingDesign;
    // 装修设计
    private LinearLayout mRenovationDesign;
    // 公关服务
    private LinearLayout mPublicRelationsService;
    // 管理咨询
    private LinearLayout mManagementConsultation;
    // 网络营销
    private LinearLayout mNetworkMarketing;
    // 专业培训
    private LinearLayout mProfessionalTraining;
    // 广告检测
    private LinearLayout mCommercialDetection;
    // 商务报告
    private LinearLayout mBusinessReport;
    // 图文输出
    private LinearLayout mEquipmentLease;
    // 展览服务
    private LinearLayout mConference;
    // 法律服务
    private LinearLayout mLegalServices;
    // 起名服务
    private LinearLayout mIntellectualProperty;
    // 网站建议
    private LinearLayout mWebsiteSuggestion;

    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    LinearLayout left_drawer;
    CircleImageView imageView_header;//头像
    TextView tv_name;//名字
    MyInfoBean.DataEntity myInfoBean;
    TextView left_tv_red;

    int OUTDOORLIST = 1;
    int BAOZHILIST = 2;
    final int getprovice = 12313;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext=this;
        PushAgent.getInstance(this).onAppStart();
    }

    @Override
    public int setLayoutById() {
        return R.layout.activity_main_drawer;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        // 顶部布局
        mTitle = (TitleView) findViewById(R.id.title);
        mMyMedia = (ImageView) findViewById(R.id.my_media);
        mReleaseDemand = (ImageView) findViewById(R.id.release_demand);
        mGrabSingle = (ImageView) findViewById(R.id.grab_single);

        mLYTv = (LinearLayout) findViewById(R.id.tv_layout);
        mLYBc = (LinearLayout) findViewById(R.id.broadcast_layout);
        mLYNp = (LinearLayout) findViewById(R.id.newspaper_layout);
        mLYOd = (LinearLayout) findViewById(R.id.outdoor_layout);
        mLYMz = (LinearLayout) findViewById(R.id.magazine_layout);
        mLYNet = (LinearLayout) findViewById(R.id.net_layout);
        mRLQiTa1 = (RelativeLayout) findViewById(R.id.rl_other1);
        mRLQiTa2 = (RelativeLayout) findViewById(R.id.rl_other2);
        mAdvertisingCreativEe = (LinearLayout) findViewById(R.id.advertising_creative_layout);
        mMarketingStrategy = (LinearLayout) findViewById(R.id.marketing_strategy_layout);
        mTVC = (LinearLayout) findViewById(R.id.tvc_layout);
        mAnimeCreate = (LinearLayout) findViewById(R.id.anime_create_layout);
        mViDesign = (LinearLayout) findViewById(R.id.vi_design_layout);
        mLogoDesign = (LinearLayout) findViewById(R.id.logo_design_layout);
        mAppUiDesign = (LinearLayout) findViewById(R.id.app_ui_design_layout);
        mProductDesign = (LinearLayout) findViewById(R.id.product_design_layout);
        mDackingDesign = (LinearLayout) findViewById(R.id.packing_design_layout);
        mRenovationDesign = (LinearLayout) findViewById(R.id.renovation_design_layout);
        mPublicRelationsService = (LinearLayout) findViewById(R.id.public_relations_service_layout);
        mManagementConsultation = (LinearLayout) findViewById(R.id.management_consultation_layout);
        mNetworkMarketing = (LinearLayout) findViewById(R.id.network_marketing_layout);
        mProfessionalTraining = (LinearLayout) findViewById(R.id.professional_training_layout);
        mCommercialDetection = (LinearLayout) findViewById(R.id.commercial_detection_layout);
        mBusinessReport = (LinearLayout) findViewById(R.id.business_report_layout);
        mEquipmentLease = (LinearLayout) findViewById(R.id.equipment_lease_layout);
        mConference = (LinearLayout) findViewById(R.id.conference_layout);
        mLegalServices = (LinearLayout) findViewById(R.id.legal_services_layout);
        mIntellectualProperty = (LinearLayout) findViewById(R.id.intellectual_property_layout);
        mWebsiteSuggestion = (LinearLayout) findViewById(R.id.website_suggestion_layout);

        String proviceString= TopADApplication.getSelf().getProvice();
        // 设置顶部布局
        mTitle.setTitle(getString(R.string.app_name));
        mTitle.setLeftVisiable(true);
        mTitle.setLeftIcon(R.drawable.leftmenu);
        mTitle.setLeftClickListener(new TitleLeftOnClickListener());
        mTitle.setRightClickListener1(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ProvinceActivity.class);
                startActivityForResult(intent,getprovice);
            }
        },proviceString,R.drawable.outdoor_location);
        mMyMedia.setOnClickListener(this);
        mReleaseDemand.setOnClickListener(this);
        mGrabSingle.setOnClickListener(this);
        mRLQiTa1.setOnClickListener(this);
        mRLQiTa2.setOnClickListener(this);
        mAdvertisingCreativEe.setOnClickListener(this);
        mMarketingStrategy.setOnClickListener(this);
        mTVC.setOnClickListener(this);
        mAnimeCreate.setOnClickListener(this);
        mViDesign.setOnClickListener(this);
        mLogoDesign.setOnClickListener(this);
        mAppUiDesign.setOnClickListener(this);
        mProductDesign.setOnClickListener(this);
        mDackingDesign.setOnClickListener(this);
        mRenovationDesign.setOnClickListener(this);
        mPublicRelationsService.setOnClickListener(this);
        mManagementConsultation.setOnClickListener(this);
        mNetworkMarketing.setOnClickListener(this);
        mProfessionalTraining.setOnClickListener(this);
        mCommercialDetection.setOnClickListener(this);
        mBusinessReport.setOnClickListener(this);
        mEquipmentLease.setOnClickListener(this);
        mConference.setOnClickListener(this);
        mLegalServices.setOnClickListener(this);
        mIntellectualProperty.setOnClickListener(this);
        mWebsiteSuggestion.setOnClickListener(this);

        LinearLayout.LayoutParams lp1 = (LinearLayout.LayoutParams) mLYTv.getLayoutParams();
        lp1.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 6;

        LinearLayout.LayoutParams lp2 = (LinearLayout.LayoutParams) mLYBc.getLayoutParams();
        lp2.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 6;

        LinearLayout.LayoutParams lp3 = (LinearLayout.LayoutParams) mLYNp.getLayoutParams();
        lp3.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 6;

        LinearLayout.LayoutParams lp4 = (LinearLayout.LayoutParams) mLYOd.getLayoutParams();
        lp4.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 6;

        LinearLayout.LayoutParams lp5 = (LinearLayout.LayoutParams) mLYMz.getLayoutParams();
        lp5.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 6;

        LinearLayout.LayoutParams lp6 = (LinearLayout.LayoutParams) mLYNet.getLayoutParams();
        lp6.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 6;

        LinearLayout.LayoutParams lp7 = (LinearLayout.LayoutParams) mAdvertisingCreativEe.getLayoutParams();
        lp7.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 5 - 20;

        LinearLayout.LayoutParams lp8 = (LinearLayout.LayoutParams) mMarketingStrategy.getLayoutParams();
        lp8.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 5 - 20;

        LinearLayout.LayoutParams lp9 = (LinearLayout.LayoutParams) mTVC.getLayoutParams();
        lp9.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 5 - 20;

        LinearLayout.LayoutParams lp10 = (LinearLayout.LayoutParams) mAnimeCreate.getLayoutParams();
        lp10.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 5 - 20;

        LinearLayout.LayoutParams lp11 = (LinearLayout.LayoutParams) mViDesign.getLayoutParams();
        lp11.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 4;

        LinearLayout.LayoutParams lp12 = (LinearLayout.LayoutParams) mLogoDesign.getLayoutParams();
        lp12.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 4;

        LinearLayout.LayoutParams lp13 = (LinearLayout.LayoutParams) mAppUiDesign.getLayoutParams();
        lp13.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 4;

        LinearLayout.LayoutParams lp14 = (LinearLayout.LayoutParams) mProductDesign.getLayoutParams();
        lp14.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 4;

        LinearLayout.LayoutParams lp15 = (LinearLayout.LayoutParams) mDackingDesign.getLayoutParams();
        lp15.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 4;

        LinearLayout.LayoutParams lp16 = (LinearLayout.LayoutParams) mRenovationDesign.getLayoutParams();
        lp16.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 4;

        LinearLayout.LayoutParams lp17 = (LinearLayout.LayoutParams) mLegalServices.getLayoutParams();
        lp17.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 5 + 20;

        LinearLayout.LayoutParams lp18 = (LinearLayout.LayoutParams) mIntellectualProperty.getLayoutParams();
        lp18.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 5 + 20;

        LinearLayout.LayoutParams lp19 = (LinearLayout.LayoutParams) mWebsiteSuggestion.getLayoutParams();
        lp19.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 5 + 20;

        LinearLayout.LayoutParams lp20 = (LinearLayout.LayoutParams) mMyMedia.getLayoutParams();
        lp20.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 7 + 10;

        LinearLayout.LayoutParams lp21 = (LinearLayout.LayoutParams) mReleaseDemand.getLayoutParams();
        lp21.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 7 + 10;

        LinearLayout.LayoutParams lp22 = (LinearLayout.LayoutParams) mGrabSingle.getLayoutParams();
        lp22.height = (Utils.getScreenWidth(MainActivity.this) * 1) / 7 + 10;

        left_drawer = (LinearLayout) findViewById(R.id.left_drawer);
        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
                R.drawable.leftmenu, R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
                myInfoBean = TopADApplication.getSelf().getMyInfo();
                if (myInfoBean != null) {
                    String headerpicUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + myInfoBean.getImghead();
                    String nameString = myInfoBean.getNickname();
                    if (!Utils.isEmpty(nameString) && !Utils.isEmpty(headerpicUrl)) {
                        tv_name.setText(nameString);
                        ImageLoader.getInstance().displayImage(headerpicUrl, imageView_header, TopADApplication.getSelf().getImageLoaderOption());
                    } else {
                        getMyInfo();
                    }
                } else {
                    getMyInfo();
                }


            }
        };
        mDrawerToggle.syncState();
        initLeftMenu();
        getMyInfo();//获取我的个人信息
        initLocation();
        TopADApplication.getSelf().bindUmeng();
        checkNewMessage();


        IntentFilter filter = new IntentFilter();
        filter.addAction(Constants.BROADCAST_ACTION_UPDATA_MYINFO);
        registerReceiver(broadcastReceiver, filter);
    }

    @Override
    public void initData() {
        boolean ishaveToken=getIntent().getBooleanExtra("ishaveToken",false);
        // 本地存储mobienumber
        String mUserName= (String) SharedPreferencesUtils.get(mContext, SharedPreferencesUtils.USER_PHONR,"");
        // 本地存储mobienumber
        String mPassword=(String)SharedPreferencesUtils.get(mContext, SharedPreferencesUtils.USER_PSW,"");

        if(!ishaveToken&&!Utils.isEmpty(mUserName)&&!Utils.isEmpty(mPassword)){
            login(mUserName,mPassword);
        }

    }
    public  void login(final String mUserName, String mPassword){
//        // 拼接url
//        StringBuffer sb = new StringBuffer();
//        sb.append(Constants.getCurrUrl()).append(Constants.URL_LOGIN).append("?");
//        String url = sb.toString();
//        RequestParams rp = new RequestParams();
//        TopADApplication.getSelf().getUserId();
//        rp.add("mobile", mUserName);
//        rp.add("pwd", Md5.md5s(mPassword));
//
//        postWithLoading(url, rp, false, new HttpCallback() {
//            @Override
//            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
//                LoginBean login = (LoginBean) t;
//                if (login != null) {
//                    // 本地缓存token
//                    if (!Utils.isEmpty(login.getToken())) {
//                        SharedPreferencesUtils.put(mContext, SharedPreferencesUtils.KEY_TOKEN, login.getToken());
//                    }
//                }
//
//            }
//
//            @Override
//            public void onFailure(BaseBean base) {
//                int status = base.getStatus();// 状态码
//                String msg = base.getMsg();// 错误信息
//            }
//        }, LoginBean.class);
    }
    public void initLeftMenu() {
        left_tv_red = (TextView) findViewById(R.id.left_tv_red);
        findViewById(R.id.csmm).setOnTouchListener(this);
        findViewById(R.id.wszl).setOnTouchListener(this);
        findViewById(R.id.gsrz).setOnTouchListener(this);
        findViewById(R.id.cpsj).setOnTouchListener(this);
        findViewById(R.id.wdqd).setOnTouchListener(this);
        findViewById(R.id.wdxq).setOnTouchListener(this);
        findViewById(R.id.wddd).setOnTouchListener(this);
        findViewById(R.id.fbmt).setOnTouchListener(this);
        findViewById(R.id.wdqb).setOnTouchListener(this);
        findViewById(R.id.xtxx).setOnTouchListener(this);
        findViewById(R.id.yjfk).setOnTouchListener(this);
        findViewById(R.id.quit).setOnTouchListener(this);
        imageView_header = (CircleImageView) findViewById(R.id.header_im);
        tv_name = (TextView) findViewById(R.id.tv_name);
    }

    private LocationManagerProxy mLocationManagerProxy;

    public void initLocation() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(
                LocationProviderProxy.AMapNetwork, 15 * 60 * 1000, 15, this);
        mLocationManagerProxy.setGpsEnable(false);
    }

    private void stopLocation() {
        if (mLocationManagerProxy != null) {
            mLocationManagerProxy.removeUpdates(this);
            mLocationManagerProxy.destory();
        }
        mLocationManagerProxy = null;
    }

    @Override
    public void onLocationChanged(AMapLocation aMapLocation) {

        if (aMapLocation != null && aMapLocation.getAMapException().getErrorCode() == 0) {
            //获取位置信息
            Double geoLat = aMapLocation.getLatitude();
            Double geoLng = aMapLocation.getLongitude();
            String curAddress = aMapLocation.getAddress();
            LogUtil.d("定位成功：" + curAddress);
            getUploadMyLocation(curAddress, "" + geoLng, "" + geoLat);
            LocationBean locationBean = new LocationBean();
            locationBean.location = curAddress;
            locationBean.longitude = geoLng;
            locationBean.latitude = geoLat;
            locationBean.province = aMapLocation.getProvince();
            TopADApplication.getSelf().setLocation(locationBean);
        }

    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_layout://电视
                intent = new Intent(MainActivity.this, OtherSearchListActivity.class);
                intent.putExtra("searchType", 0);
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            case R.id.broadcast_layout://广播
                intent = new Intent(MainActivity.this, OtherSearchListActivity.class);
                intent.putExtra("searchType", 1);
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            case R.id.newspaper_layout://报纸
                intent = new Intent(MainActivity.this, OtherSearchListActivity.class);
                intent.putExtra("searchType", 2);
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            case R.id.outdoor_layout://户外
                intent = new Intent(MainActivity.this, OutDoorSearchListActivity.class);
                intent.putExtra("searchType", 3);
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            case R.id.magazine_layout://杂志
                intent = new Intent(MainActivity.this, OtherSearchListActivity.class);
                intent.putExtra("searchType", 4);
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            case R.id.net_layout://网络
                intent = new Intent(MainActivity.this, OtherSearchListActivity.class);
                intent.putExtra("searchType", 5);
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            case R.id.rl_other1://其他1，媒体搜索
                intent = new Intent(MainActivity.this, OtherSearchActivity.class);
                intent.putExtra("title", "搜索资源");
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            case R.id.rl_other2://其他2，服务搜素
                intent = new Intent(MainActivity.this, OtherSearchActivity.class);
                intent.putExtra("title", "搜索服务");
                intent.putExtra("from", "1");
                startActivity(intent);
                break;

            case R.id.advertising_creative_layout://广告创意
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "广告创意");
                intent.putExtra("type1", "广告创意");
                intent.putExtra("type2", "");
                startActivity(intent);
                break;

            case R.id.marketing_strategy_layout://营销推广
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "营销推广");
                intent.putExtra("type1", "营销推广");
                intent.putExtra("type2", "");
                startActivity(intent);
                break;

            case R.id.tvc_layout://影视动漫
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "影视动漫");
                intent.putExtra("type1", "影视动漫");
                intent.putExtra("type2", "");
                startActivity(intent);
                break;

            case R.id.anime_create_layout://文案策划
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "文案策划");
                intent.putExtra("type1", "文案策划");
                intent.putExtra("type2", "");
                startActivity(intent);
                break;

            case R.id.vi_design_layout://VI设计
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "VI设计");
                intent.putExtra("type1", "平面设计");
                intent.putExtra("type2", "VI设计");
                startActivity(intent);
                break;

            case R.id.logo_design_layout://LOGO设计
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "LOGO设计");
                intent.putExtra("type1", "平面设计");
                intent.putExtra("type2", "LOGO设计");
                startActivity(intent);
                break;

            case R.id.app_ui_design_layout://APP/UI设计
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "APP/UI设计");
                intent.putExtra("type1", "平面设计");
                intent.putExtra("type2", "APP/UI设计");
                startActivity(intent);
                break;

            case R.id.product_design_layout://海报设计
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "海报设计");
                intent.putExtra("type1", "平面设计");
                intent.putExtra("type2", "海报设计");
                startActivity(intent);
                break;

            case R.id.packing_design_layout://包装设计
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "包装设计");
                intent.putExtra("type1", "平面设计");
                intent.putExtra("type2", "包装设计");
                startActivity(intent);
                break;

            case R.id.renovation_design_layout://装修设计
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "装修设计");
                intent.putExtra("type1", "平面设计");
                intent.putExtra("type2", "装修设计");
                startActivity(intent);
                break;

            case R.id.public_relations_service_layout://公关服务
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "公关服务");
                intent.putExtra("type1", "公关服务");
                intent.putExtra("type2", "");
                startActivity(intent);
                break;

            case R.id.management_consultation_layout://管理咨询
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "管理咨询");
                intent.putExtra("type1", "管理咨询");
                intent.putExtra("type2", "");
                startActivity(intent);
                break;

            case R.id.network_marketing_layout://网络营销
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "网络营销");
                intent.putExtra("type1", "营销推广");
                intent.putExtra("type2", "网络营销");
                startActivity(intent);
                break;

            case R.id.professional_training_layout://专业培训
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "专业培训");
                intent.putExtra("type1", "专业培训");
                intent.putExtra("type2", "");
                startActivity(intent);
                break;

            case R.id.commercial_detection_layout://广告监测
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "广告监测");
                intent.putExtra("type1", "广告监测");
                intent.putExtra("type2", "");
                startActivity(intent);
                break;

            case R.id.business_report_layout://商业报告
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "商业报告");
                intent.putExtra("type1", "管理咨询");
                intent.putExtra("type2", "各类行业研究报告");
                startActivity(intent);
                break;

            case R.id.equipment_lease_layout://图文输出
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "图文输出");
                intent.putExtra("type1", "其他服务");
                intent.putExtra("type2", "图文输出");
                startActivity(intent);
                break;

            case R.id.conference_layout://展览服务
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "展览服务");
                intent.putExtra("type1", "其他服务");
                intent.putExtra("type2", "展览服务");
                startActivity(intent);
                break;

            case R.id.legal_services_layout://法律服务
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "法律服务");
                intent.putExtra("type1", "其他服务");
                intent.putExtra("type2", "法律咨询服务");
                startActivity(intent);
                break;

            case R.id.intellectual_property_layout://起名服务
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "起名服务");
                intent.putExtra("type1", "其他服务");
                intent.putExtra("type2", "品牌起名/公司起名");
                startActivity(intent);
                break;

            case R.id.website_suggestion_layout://网站建设
                intent = new Intent(MainActivity.this, ADSListActivity.class);
                intent.putExtra("title", "网站建设");
                intent.putExtra("type1", "网站建设");
                intent.putExtra("type2", "");
                startActivity(intent);
                break;

            case R.id.my_media://我有资源
                intent = new Intent(MainActivity.this, MyMediaActivity.class);
                startActivity(intent);
                break;

            case R.id.release_demand://发布需求
                intent = new Intent(MainActivity.this, ShareNeedsActivity.class);
                startActivity(intent);
                break;

            case R.id.grab_single://我要抢单
                intent = new Intent(MainActivity.this, MyWantGrabsingleActivity.class);
                startActivity(intent);
                break;

            default:
                break;
        }
    }

    @Override
    public void onBack() {
        stopLocation();
        finish();
    }

    float moveXY;
    float lastX = 0.0f;
    float lastY = 0.0f;
    boolean isNeedUp;

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isNeedUp = true;
                lastX = event.getX();
                lastY = event.getY();
                leftMenuTouch(v, true, false);
                break;
            case MotionEvent.ACTION_MOVE:
                float cx = event.getX();
                float cy = event.getY();
                moveXY = Math.abs(cx - lastX) + Math.abs(cy - lastY);
                LogUtil.d("moveXY:" + moveXY);
                if (moveXY > 20) {
                    leftMenuTouch(v, false, false);
                    isNeedUp = false;
                }

                break;
            case MotionEvent.ACTION_UP:
                if (isNeedUp) {
                    leftMenuTouch(v, false, true);
                }
                break;
        }
        return false;
    }

    public void leftMenuTouch(View v, boolean isPressed, boolean isGogoNew) {
        if (isPressed) {
            switch (v.getId()) {
                case R.id.csmm:
                    ((ImageView) (v.findViewById(R.id.im_csmm))).setImageResource(R.drawable.left_rewrite_blue);
                    ((TextView) (v.findViewById(R.id.tv_csmm))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.wszl:
                    ((ImageView) (v.findViewById(R.id.im_wszl))).setImageResource(R.drawable.left_wanshan_blue);
                    ((TextView) (v.findViewById(R.id.tv_wszl))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.gsrz:
                    ((ImageView) (v.findViewById(R.id.im_gsrz))).setImageResource(R.drawable.left_renzheng_blue);
                    ((TextView) (v.findViewById(R.id.tv_gsrz))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.cpsj:
                    ((ImageView) (v.findViewById(R.id.im_cpsj))).setImageResource(R.drawable.left_products_blue);
                    ((TextView) (v.findViewById(R.id.tv_cpsj))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.wdqd:
                    ((ImageView) (v.findViewById(R.id.im_wdqd))).setImageResource(R.drawable.left_qiangdan_blue);
                    ((TextView) (v.findViewById(R.id.tv_wdqd))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.wdxq:
                    ((ImageView) (v.findViewById(R.id.im_wdxq))).setImageResource(R.drawable.left_demands_blue);
                    ((TextView) (v.findViewById(R.id.tv_wdxq))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.wddd:
                    ((ImageView) (v.findViewById(R.id.im_wddd))).setImageResource(R.drawable.left_dingdan_blue);
                    ((TextView) (v.findViewById(R.id.tv_wddd))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.fbmt:
                    ((ImageView) (v.findViewById(R.id.im_fbmt))).setImageResource(R.drawable.left_media_blue);
                    ((TextView) (v.findViewById(R.id.tv_fbmt))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.wdqb:
                    ((ImageView) (v.findViewById(R.id.im_wdqb))).setImageResource(R.drawable.left_wallet_blue);
                    ((TextView) (v.findViewById(R.id.tv_wdqb))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.xtxx:
                    ((ImageView) (v.findViewById(R.id.im_xtxx))).setImageResource(R.drawable.left_message_blue);
                    ((TextView) (v.findViewById(R.id.tv_xtxx))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.yjfk:
                    ((ImageView) (v.findViewById(R.id.im_yjfk))).setImageResource(R.drawable.left_message_blue);
                    ((TextView) (v.findViewById(R.id.tv_yjfk))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;
                case R.id.quit:
                    ((ImageView) (v.findViewById(R.id.im_quit))).setImageResource(R.drawable.left_zhux_blue);
                    ((TextView) (v.findViewById(R.id.tv_quit))).setTextColor(getResources().getColor(R.color.act_home_tab_blue_normal));
                    break;

            }
        } else {
            switch (v.getId()) {
                case R.id.csmm:
                    ((ImageView) (v.findViewById(R.id.im_csmm))).setImageResource(R.drawable.left_rewrite);
                    ((TextView) (v.findViewById(R.id.tv_csmm))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.wszl:
                    ((ImageView) (v.findViewById(R.id.im_wszl))).setImageResource(R.drawable.left_wanshan);
                    ((TextView) (v.findViewById(R.id.tv_wszl))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.gsrz:
                    ((ImageView) (v.findViewById(R.id.im_gsrz))).setImageResource(R.drawable.left_renzheng);
                    ((TextView) (v.findViewById(R.id.tv_gsrz))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.cpsj:
                    ((ImageView) (v.findViewById(R.id.im_cpsj))).setImageResource(R.drawable.left_products);
                    ((TextView) (v.findViewById(R.id.tv_cpsj))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.wdqd:
                    ((ImageView) (v.findViewById(R.id.im_wdqd))).setImageResource(R.drawable.left_qiangdan);
                    ((TextView) (v.findViewById(R.id.tv_wdqd))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.wdxq:
                    ((ImageView) (v.findViewById(R.id.im_wdxq))).setImageResource(R.drawable.left_demands);
                    ((TextView) (v.findViewById(R.id.tv_wdxq))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.wddd:
                    ((ImageView) (v.findViewById(R.id.im_wddd))).setImageResource(R.drawable.left_dingdan);
                    ((TextView) (v.findViewById(R.id.tv_wddd))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.fbmt:
                    ((ImageView) (v.findViewById(R.id.im_fbmt))).setImageResource(R.drawable.left_media);
                    ((TextView) (v.findViewById(R.id.tv_fbmt))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.wdqb:
                    ((ImageView) (v.findViewById(R.id.im_wdqb))).setImageResource(R.drawable.left_wallet);
                    ((TextView) (v.findViewById(R.id.tv_wdqb))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.xtxx:
                    ((ImageView) (v.findViewById(R.id.im_xtxx))).setImageResource(R.drawable.left_message);
                    ((TextView) (v.findViewById(R.id.tv_xtxx))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.yjfk:
                    ((ImageView) (v.findViewById(R.id.im_yjfk))).setImageResource(R.drawable.left_message);
                    ((TextView) (v.findViewById(R.id.tv_yjfk))).setTextColor(getResources().getColor(R.color.white));
                    break;
                case R.id.quit:
                    ((ImageView) (v.findViewById(R.id.im_quit))).setImageResource(R.drawable.left_zhux);
                    ((TextView) (v.findViewById(R.id.tv_quit))).setTextColor(getResources().getColor(R.color.white));
                    break;
            }
            if (isGogoNew) {
                leftMenuClick(v);
            }
        }

    }

    public void leftMenuClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.csmm://重设密码
                intent = new Intent(MainActivity.this, ResetPasswordActivity.class);
                startActivity(intent);
                break;

            case R.id.wszl://完善资料，个人资料
                intent = new Intent(MainActivity.this, CompleteInforActivity.class);
                startActivity(intent);
                break;

            case R.id.gsrz://公司认证
                intent = new Intent(MainActivity.this, UploadPicActivity.class);
                intent.putExtra("title", "公司认证");
                intent.putExtra("type", 3);
                startActivity(intent);
                break;

            case R.id.cpsj://我的服务产品
                intent = new Intent(MainActivity.this, MyShareMediaListActivity.class);
                startActivity(intent);
                break;

            case R.id.wdqd://我的抢单
                intent = new Intent(MainActivity.this, MyGrabSingleListActivity.class);
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            case R.id.wdxq://我的需求
                intent = new Intent(MainActivity.this, MyNeedsActivity.class);
                startActivity(intent);
                break;

            case R.id.wddd://我的订单
                intent = new Intent(MainActivity.this, MyGrabSingleListActivity.class);
                intent.putExtra("from", "1");
                startActivity(intent);
                break;

            case R.id.fbmt://我发布的媒体
                intent = new Intent(MainActivity.this, MyMediaReleaseListActivity.class);
                startActivity(intent);
                break;

            case R.id.wdqb://我的钱包
                intent = new Intent(MainActivity.this, MyWalletActivity.class);
                startActivity(intent);
                break;

            case R.id.xtxx://系统消息
                intent = new Intent(MainActivity.this, SystemNewsActivity.class);
                startActivity(intent);
                left_tv_red.setVisibility(View.GONE);
                break;

            case R.id.yjfk://意见反馈
                WebViewActivity.LaunchSelf(mContext, Constants.URL_SUGGEST, "意见反馈");
                break;

            case R.id.quit://退出登录
                ((TopADApplication) TopADApplication.getContext()).logout();

                intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
                break;

        }
        mDrawerLayout.closeDrawer(left_drawer);
    }

    /**
     * 顶部布局--左按钮事件监听
     */
    public class TitleLeftOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mDrawerLayout.openDrawer(left_drawer);
        }

    }

    long mBackTime;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            if (System.currentTimeMillis() - mBackTime < 1500) {
                ActivityCollector.finishAll();
                mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        onBack();
                    }
                }, 1000);
            } else {
                mBackTime = System.currentTimeMillis();
                Utils.showToast(this, "再按一次离开" + getResources().getString(R.string.app_name));
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }
    Handler mHandler = new Handler() {
    };

    /**
     * 获取我的个人信息
     */
    public void getMyInfo() {

        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.GETINFO).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        postWithoutLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                MyInfoBean base = (MyInfoBean) t;
                if (base != null) {
                    myInfoBean = base.getData();
                    TopADApplication.getSelf().setMyInfo(base.getData());
                    if (!Utils.isEmpty(myInfoBean.getMobile())) {
                        SharedPreferencesUtils.put(mContext, SharedPreferencesUtils.USER_PHONR, myInfoBean.getMobile());
                    }
                    String headerpicUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + myInfoBean.getImghead();
                    String nameString = myInfoBean.getNickname();
                    if (!Utils.isEmpty(nameString) && !Utils.isEmpty(headerpicUrl)) {
                        tv_name.setText(nameString);
                        ImageLoader.getInstance().displayImage(headerpicUrl, imageView_header, TopADApplication.getSelf().getImageLoaderOption());
                    }
                }
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
     * 上传个人地址
     */
    public void getUploadMyLocation(String location, String longitude, String latitude) {

        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_UPDATE_LOCATION).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("location", location);
        rp.add("longitude", longitude);
        rp.add("latitude", latitude);

        rp.add("devicetoken", TopADApplication.getSelf().getDeviceToken());
        rp.add("clienttype", "0");

        postWithoutLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                BaseBean base = (BaseBean) t;
                if (base != null) {
                    LogUtil.d("成功" + base.getMsg());
                }
            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, msg);
            }
        }, BaseBean.class);

    }

    /**
     * 检查是否有新消息
     */
    public void checkNewMessage() {

        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_CHECK_MSG).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        postWithoutLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                CheckMSGBean base = (CheckMSGBean) t;
                if (base != null) {

                    String newmsg = base.newmsg;
                    if (!Utils.isEmpty(newmsg)) {
                        int num = Integer.parseInt(newmsg);

                        if (num > 0) {
                            mTitle.setLeftRedVisiable(true);
                            left_tv_red.setVisibility(View.VISIBLE);
                        } else {
                            mTitle.setLeftRedVisiable(false);
                            left_tv_red.setVisibility(View.GONE);
                        }

                    }
                    LogUtil.d("成功" + base.getMsg());
//                    {"status":10000,"msg":"ok","newmsg":0}
                }
            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
            }
        }, CheckMSGBean.class);

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
            if (Constants.BROADCAST_ACTION_UPDATA_MYINFO.equals(action)) {
                getMyInfo();
//                myInfoBean = TopADApplication.getSelf().getMyInfo();
//                String headerpicUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + myInfoBean.getImghead();
//                String nameString = myInfoBean.getNickname();
//                if (!Utils.isEmpty(nameString) && !Utils.isEmpty(headerpicUrl)) {
//                    tv_name.setText(nameString);
//                    ImageLoader.getInstance().displayImage(headerpicUrl, imageView_header, TopADApplication.getSelf().getImageLoaderOption());
//                }
            }
        }
    };

    /**
     * 判断是否为公司,已启用20160419
     *
     * @return
     */
    public void isCompany() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_USER_IS_COMPANY).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                IsCompanyBean bean = (IsCompanyBean) t;
                if (bean != null) {
                    Intent intent;
                    if ("1".equals(bean.getIscompany())) {
                        intent = new Intent(MainActivity.this, MyMediaActivity.class);
                        startActivity(intent);
                    }else{
                        intent = new Intent(MainActivity.this, IsCompanyActivity.class);
                        startActivity(intent);
                    }
                }

            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息

                LogUtil.d(LTAG, "status = " + status + "\n" + "msg = " + msg);
//                ToastUtil.show(mContext, msg);
            }
        }, IsCompanyBean.class, true);
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case getprovice:
                if (data != null) {
                    String provice = data.getStringExtra("provice");
                    if (!Utils.isEmpty(provice)) {
                        mTitle.setRightText(provice);
                        SharedPreferencesUtils.put(this, SharedPreferencesUtils.KEY_PROVICE, provice);
                    }
                }
                break;
            default:
                break;
        }
    }
}
