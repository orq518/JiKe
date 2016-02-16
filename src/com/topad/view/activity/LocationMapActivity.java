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
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.LocationSource;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.topad.R;
import com.topad.amap.AMapUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<定位页面>
 *
 * @author lht
 * @data: on 15/11/4 15:02
 */
public class LocationMapActivity extends Activity implements AMap.OnCameraChangeListener, GeocodeSearch.OnGeocodeSearchListener {
    private static final String LTAG = LocationMapActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;

    private AMap aMap;
    private MapView mapView;
    /** 定位地址 **/
    private String location;
    /** lat **/
    private double lat;
    /** lon **/
    private double lon;
    private GeocodeSearch geocoderSearch;
    private LatLonPoint latLonPoint;
    private Marker regeoMarker;

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
        if (aMap == null) {
        }
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
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
        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 200,GeocodeSearch.AMAP);
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
        if(!Utils.isEmpty(location)){
            Intent intent = new Intent(LocationMapActivity.this, MediaReleaseActivity.class );
            intent.putExtra( "location", location);
            intent.putExtra( "lat", lat);
            intent.putExtra( "lon", lon);
            setResult(RESULT_OK, intent);
        }
        finish();
    }
}
