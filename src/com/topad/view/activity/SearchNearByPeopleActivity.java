package com.topad.view.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.AMap.InfoWindowAdapter;
import com.amap.api.maps2d.AMap.OnMarkerClickListener;
import com.amap.api.maps2d.AMapUtils;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.NaviPara;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.LocationBean;
import com.topad.bean.NearByBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.view.customviews.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * AMapV1地图中简单介绍poisearch搜索
 */
public class SearchNearByPeopleActivity extends BaseActivity implements
        OnMarkerClickListener, InfoWindowAdapter, OnClickListener {
    private AMap aMap;
    Context mContext;
    LocationBean locationBean = TopADApplication.getSelf().getLocation();
    /**
     * title布局
     **/
    private TitleView mTitle;
    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_nearby;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        // 顶部布局
        mTitle = (TitleView) findViewById(R.id.title);
        // 设置顶部布局
        mTitle.setTitle("附近的人");
        mTitle.setLeftClickListener(new TitleLeftOnClickListener());
        type1 = getIntent().getStringExtra("type1");
        type2 = getIntent().getStringExtra("type2");
        type3 = getIntent().getStringExtra("type3");
        init();
        getNearByPeople();
    }
    /**
     * 顶部布局--左按钮事件监听
     */
    public class TitleLeftOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            onBack();
        }

    }

    @Override
    public void initData() {

    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        if (aMap == null) {
            aMap = ((SupportMapFragment) getSupportFragmentManager()
                    .findFragmentById(R.id.map)).getMap();
            setUpMap();
        }
    }

    /**
     * 设置页面监听
     */
    private void setUpMap() {
        aMap.setOnMarkerClickListener(this);// 添加点击marker监听事件
        aMap.setInfoWindowAdapter(this);// 添加显示infowindow监听事件
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        marker.showInfoWindow();
        return false;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @Override
    public View getInfoWindow(final Marker marker) {

        LogUtil.d("marker.getTitle():" + marker.getTitle());
        LogUtil.d("marker.getSnippet():" + marker.getSnippet());
        View view = getLayoutInflater().inflate(R.layout.poikeywordsearch_uri,
                null);

        TextView title = (TextView) view.findViewById(R.id.title);
        title.setText(marker.getTitle());

        TextView snippet = (TextView) view.findViewById(R.id.snippet);
        snippet.setText(marker.getSnippet());
        ImageButton button = (ImageButton) view
                .findViewById(R.id.start_amap_app);
        // 调起高德地图app
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startAMapNavi(marker);
            }
        });
        return view;
    }

    /**
     * 调起高德地图导航功能，如果没安装高德地图，会进入异常，可以在异常中处理，调起高德地图app的下载页面
     */
    public void startAMapNavi(Marker marker) {
        // 构造导航参数
        NaviPara naviPara = new NaviPara();
        // 设置终点位置
        naviPara.setTargetPoint(marker.getPosition());
        // 设置导航策略，这里是避免拥堵
        naviPara.setNaviStyle(NaviPara.DRIVING_AVOID_CONGESTION);

        // 调起高德地图导航
        try {
            AMapUtils.openAMapNavi(naviPara, getApplicationContext());
        } catch (com.amap.api.maps2d.AMapException e) {

            // 如果没安装会进入异常，调起下载页面
            AMapUtils.getLatestAMapApp(getApplicationContext());

        }

    }

    /**
     * Button点击事件回调方法
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            /**
             * 点击搜索按钮
             */
            case R.id.searchButton:
//			searchButton();
                break;
            /**
             * 点击下一页按钮
             */
            case R.id.nextButton:
                break;
            default:
                break;
        }
    }

    List<NearByBean.DataEntity> nearbyList = new ArrayList<NearByBean.DataEntity>();
    String type1, type2, type3;

    /**
     * 获取附近符合要求的人
     */
    public void getNearByPeople() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_GET_USERS_NEARBY).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("needtype", "1");//1：普通需求   2：培训需求   3：招聘需求
        rp.add("type1", type1);//
        rp.add("type2", type2);//
        rp.add("type3", type3);//

        rp.add("location", locationBean.location);//位置名
        rp.add("longitude", "" + locationBean.longitude);//经度
        rp.add("latitude", "" + locationBean.latitude);//纬度

        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                if (t != null) {
                    ToastUtil.show(mContext, ((BaseBean) t).getMsg());
                    NearByBean nearByBean = (NearByBean) t;
                    if (nearByBean.getData() != null) {
                        nearbyList.clear();
                        nearbyList.addAll(nearByBean.getData());
                        for (int i = 0; i < nearbyList.size(); i++) {
                            NearByBean.DataEntity entity = nearbyList.get(i);
                            drawMarkers(entity);
                        }
                    }
                }


            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, msg);
            }
        }, NearByBean.class);
    }

    public void drawMarkers(NearByBean.DataEntity entity) {
        com.amap.api.maps2d.model.MarkerOptions markerOption = new com.amap.api.maps2d.model.MarkerOptions();
        markerOption.position(new com.amap.api.maps2d.model.LatLng(Double.parseDouble(entity.getLatitude()),
                Double.parseDouble(entity.getLongitude())));
        markerOption.title(entity.getLocation()).snippet(entity.getMobile());
        markerOption.icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        markerOption.draggable(false);
        Marker marker = aMap.addMarker(markerOption);
//        marker.setObject("11");//这里可以存储用户数据
//		Marker marker = aMap.addMarker(new MarkerOptions()
//				.position(locationBean.latitude,locationBean.longitude)
//				.title("好好学习")
//				.icon(BitmapDescriptorFactory
//						.defaultMarker(BitmapDescriptorFactory.HUE_AZURE))
//				.draggable(true));
		marker.showInfoWindow();// 设置默认显示一个infowinfow
    }
}
