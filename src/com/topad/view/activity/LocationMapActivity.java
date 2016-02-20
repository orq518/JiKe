package com.topad.view.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.LatLngBounds;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.bean.LocationBean;
import com.topad.util.LogUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<定位页面>
 *
 * @author lht
 * @data: on 15/11/4 15:02
 */
public class LocationMapActivity extends Activity implements AMapLocationListener,
        AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener{
    private static final String LTAG = LocationMapActivity.class.getSimpleName();
    // 上下文
    private Context mContext;
    // 顶部布局
    private TitleView mTitleView;

    private AMap aMap;
    private MapView mapView;
    // 定位地址
    private String location;
    // lat
    private double lat;
    // lon
    private double lon;
    private GeocodeSearch geocoderSearch;
    private LatLonPoint latLonPoint;
    private LocationManagerProxy mLocationManagerProxy;
    private LocationSource.OnLocationChangedListener mListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);

        mContext = this;
        mTitleView = (TitleView) findViewById(R.id.title);
        mTitleView.setTitle("定位");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        mapView = (MapView) findViewById(R.id.map);
        aMap = mapView.getMap();
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        init();

        aMap.setOnCameraChangeListener(this);
    }

    /**
     * 初始化AMap对象
     */
    private void init() {
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);

        if(TopADApplication.getSelf().getLocation()!= null){
            lat = TopADApplication.getSelf().getLocation().latitude;
            lon = TopADApplication.getSelf().getLocation().longitude;
            location = TopADApplication.getSelf().getLocation().location;

            LatLngBounds.Builder builder = new LatLngBounds.Builder();
            LatLng latlng = new LatLng(lat, lon);
            builder.include(latlng);

            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngBounds(builder.build(), 10);
            aMap.moveCamera(cameraUpdate);
        }

        initLocation();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    /**
     * 方法必须重写
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        LatLng target = cameraPosition.target;
        lat = target.latitude;
        lon = target.longitude;
        latLonPoint = new LatLonPoint(target.latitude, target.longitude);

        //latLonPoint参数表示一个Latlng，第二参数表示范围多少米，GeocodeSearch.AMAP表示是国测局坐标系还是GPS原生坐标系
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200, GeocodeSearch.AMAP);
        geocoderSearch.getFromLocationAsyn(query);

    }

    @Override
    public void onCameraChangeFinish(CameraPosition cameraPosition) {

    }

    @Override
    public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
        if (i == 0) {
            if (regeocodeResult != null && regeocodeResult.getRegeocodeAddress() != null && regeocodeResult.getRegeocodeAddress().getFormatAddress() != null) {
                location = regeocodeResult.getRegeocodeAddress().getFormatAddress() + "附近";
//                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(AMapUtil.convertToLatLng(latLonPoint), 11));
            }
        }
    }

    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }

    public void initLocation() {
        mLocationManagerProxy = LocationManagerProxy.getInstance(this);
        //此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
        //注意设置合适的定位时间的间隔，并且在合适时间调用removeUpdates()方法来取消定位请求
        //在定位结束后，在合适的生命周期调用destroy()方法
        //其中如果间隔时间为-1，则定位只定一次
        mLocationManagerProxy.requestLocationData(LocationProviderProxy.AMapNetwork, 15 * 60 * 1000, 15, this);
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

            mListener.onLocationChanged(aMapLocation);// 显示系统小蓝点
            lat = geoLat;
            lon = geoLng;
            location = curAddress;

            aMap.animateCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lon)),4000,null);
            LocationBean locationBean = new LocationBean();
            locationBean.location = curAddress;
            locationBean.longitude = geoLng;
            locationBean.latitude = geoLat;
            TopADApplication.getSelf().setLocation(locationBean);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

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
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBack() {
        stopLocation();
        if (!Utils.isEmpty(location)) {
            Intent intent = new Intent(LocationMapActivity.this, MediaReleaseActivity.class);
            intent.putExtra("location", location);
            intent.putExtra("lat", lat);
            intent.putExtra("lon", lon);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
