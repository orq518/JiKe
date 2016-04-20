package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.IsCompanyBean;
import com.topad.bean.ReleaseMediaBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.MyGridView;
import com.topad.view.customviews.TitleView;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * ${todo}<我有资源>
 *
 * @author lht
 * @data: on 15/10/30 14:10
 */
public class MyMediaActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = MyMediaActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** 电视 **/
    private LinearLayout mLayTV;
    /** 广播 **/
    private LinearLayout mLayRadio;
    /** 报纸 **/
    private LinearLayout mLayOutdoor;
    /** 户外 **/
    private LinearLayout mLayNewspaper;
    /** 杂志 **/
    private LinearLayout mLayMgz;
    /** 网络 **/
    private LinearLayout mLayWeb;
    /** 其他资源 **/
    private RelativeLayout mLayOther;

    /** 广告创意 **/
    private LinearLayout mLayGgcy;
    /** 平面设计 **/
    private LinearLayout mLayPmsj;
    /** 媒体营销 **/
    private LinearLayout mLayMtyx;
    /** 营销推广 **/
    private LinearLayout mLayYxtg;
    /** 影视动漫 **/
    private LinearLayout mLayYsdm;
    /** 文案策划 **/
    private LinearLayout mLayWach;

    /** 广告检测 **/
    private LinearLayout mLayGgjc;
    /** 专业培训 **/
    private LinearLayout mLayZypx;
    /** 管理咨询 **/
    private LinearLayout mLayGlzx;
    /** 网站软件 **/
    private LinearLayout mLayWzrj;
    /** 公关服务 **/
    private LinearLayout mLayGgfw;
    /** 品牌／公司起名 **/
    private LinearLayout mLayName;

    /** 名片设计 **/
    private LinearLayout mLayMpsj;
    /** 图文输出 **/
    private LinearLayout mLayTwsc;
    /** 出版印刷 **/
    private LinearLayout mLayCbys;
    /** 展览服务 **/
    private LinearLayout mLayZlfw;
    /** 法律咨询 **/
    private LinearLayout mLayFlzx;
    /** 其他服务 **/
    private LinearLayout mLayQtfw;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_my_media;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        mLayTV = (LinearLayout) findViewById(R.id.ih_tv_layout);
        mLayRadio = (LinearLayout) findViewById(R.id.ih_broadcast_layout);
        mLayOutdoor = (LinearLayout) findViewById(R.id.ih_outdoor_layout);
        mLayNewspaper = (LinearLayout) findViewById(R.id.ih_newspaper_layout);
        mLayMgz = (LinearLayout) findViewById(R.id.ih_magazine_layout);
        mLayWeb = (LinearLayout) findViewById(R.id.ih_net_layout);
        mLayOther = (RelativeLayout) findViewById(R.id.rl_other);

        mLayGgcy = (LinearLayout) findViewById(R.id.ih_ggcy_layout);
        mLayPmsj = (LinearLayout) findViewById(R.id.ih_pmsj_layout);
        mLayMtyx = (LinearLayout) findViewById(R.id.ih_mtyx_layout);
        mLayYxtg = (LinearLayout) findViewById(R.id.ih_yxtg_layout);
        mLayYsdm = (LinearLayout) findViewById(R.id.ih_ysdm_layout);
        mLayWach = (LinearLayout) findViewById(R.id.ih_wach_layout);

        mLayGgjc = (LinearLayout) findViewById(R.id.ih_ggjc_layout);
        mLayZypx = (LinearLayout) findViewById(R.id.ih_zypx_layout);
        mLayGlzx = (LinearLayout) findViewById(R.id.ih_glzx_layout);
        mLayWzrj = (LinearLayout) findViewById(R.id.ih_wzrj_layout);
        mLayGgfw = (LinearLayout) findViewById(R.id.ih_ggfw_layout);
        mLayName = (LinearLayout) findViewById(R.id.ih_name_layout);

        mLayMpsj = (LinearLayout) findViewById(R.id.ih_mpsj_layout);
        mLayTwsc = (LinearLayout) findViewById(R.id.ih_twsc_layout);
        mLayCbys = (LinearLayout) findViewById(R.id.ih_cbys_layout);
        mLayZlfw = (LinearLayout) findViewById(R.id.ih_zlfw_layout);
        mLayFlzx = (LinearLayout) findViewById(R.id.ih_flzx_layout);
        mLayQtfw = (LinearLayout) findViewById(R.id.ih_qtfw_layout);

        mLayTV.setOnClickListener(this);
        mLayRadio.setOnClickListener(this);
        mLayOutdoor.setOnClickListener(this);
        mLayNewspaper.setOnClickListener(this);
        mLayMgz.setOnClickListener(this);
        mLayWeb.setOnClickListener(this);
        mLayOther.setOnClickListener(this);

        mLayGgcy.setOnClickListener(this);
        mLayPmsj.setOnClickListener(this);
        mLayMtyx.setOnClickListener(this);
        mLayYxtg.setOnClickListener(this);
        mLayYsdm.setOnClickListener(this);
        mLayWach.setOnClickListener(this);

        mLayGgjc.setOnClickListener(this);
        mLayZypx.setOnClickListener(this);
        mLayGlzx.setOnClickListener(this);
        mLayWzrj.setOnClickListener(this);
        mLayGgfw.setOnClickListener(this);
        mLayName.setOnClickListener(this);

        mLayMpsj.setOnClickListener(this);
        mLayTwsc.setOnClickListener(this);
        mLayCbys.setOnClickListener(this);
        mLayZlfw.setOnClickListener(this);
        mLayFlzx.setOnClickListener(this);
        mLayQtfw.setOnClickListener(this);

    }

    @Override
    public void initData() {
        mTitleView.setTitle("我有媒体");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
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
            // 电视
            case R.id.ih_tv_layout:
                intent = new Intent(MyMediaActivity.this, SelectMediaListActivity.class);
                intent.putExtra("category", "1");
                intent.putExtra("from", "0");
                startActivity(intent);

                break;

            // 广播
            case R.id.ih_broadcast_layout:
                intent = new Intent(MyMediaActivity.this, SelectMediaListActivity.class);
                intent.putExtra("category", "2");
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            // 报纸
            case R.id.ih_newspaper_layout:
                intent = new Intent(MyMediaActivity.this, SelectMediaListActivity.class);
                intent.putExtra("category", "3");
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            // 户外
            case R.id.ih_outdoor_layout:
                intent = new Intent(MyMediaActivity.this, SelectMediaListActivity.class);
                intent.putExtra("category", "4");
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            // 杂志
            case R.id.ih_magazine_layout:
                intent = new Intent(MyMediaActivity.this, SelectMediaListActivity.class);
                intent.putExtra("category", "5");
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            // 网络
            case R.id.ih_net_layout:
                intent = new Intent(MyMediaActivity.this, SelectMediaListActivity.class);
                intent.putExtra("category", "6");
                intent.putExtra("from", "0");
                startActivity(intent);
                break;

            // 其他资源
            case R.id.rl_other:
                intent = new Intent(MyMediaActivity.this, MediaReleaseActivity.class);
                intent.putExtra("category", "7");
                startActivity(intent);
                break;

            // 我的服务产品－广告创意
            case R.id.ih_ggcy_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 0);
                startActivity(intent);
                break;

            // 我的服务产品－平面设计
            case R.id.ih_pmsj_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－媒体营销
            case R.id.ih_mtyx_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－营销推广
            case R.id.ih_yxtg_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－影视动漫
            case R.id.ih_ysdm_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－文案策划
            case R.id.ih_wach_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－广告检测
            case R.id.ih_ggjc_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－专业培训
            case R.id.ih_zypx_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－管理咨询
            case R.id.ih_glzx_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－网站软件
            case R.id.ih_wzrj_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－公关服务
            case R.id.ih_ggfw_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－品牌／公司起名
            case R.id.ih_name_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－名片设计
            case R.id.ih_mpsj_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－图文输出
            case R.id.ih_twsc_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－出版印刷
            case R.id.ih_cbys_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－展览服务
            case R.id.ih_zlfw_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－法律咨询
            case R.id.ih_flzx_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            // 我的服务产品－其他服务
            case R.id.ih_qtfw_layout:
                intent = new Intent(MyMediaActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "4");
                intent.putExtra("type", 1);
                startActivity(intent);
                break;

            default:
                break;
        }
    }
}
