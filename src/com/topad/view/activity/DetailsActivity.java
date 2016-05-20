package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.GrabSingleBean;
import com.topad.bean.LoginBean;
import com.topad.bean.MyNeedBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Md5;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.Utils;
import com.topad.view.customviews.CircleImageView;
import com.topad.view.customviews.MyGridView;
import com.topad.view.customviews.TitleView;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * ${todo}<查看详情页面>
 *
 * @author lht
 * @data: on 15/11/2 16:35
 */
public class DetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = DetailsActivity.class.getSimpleName();
    // 上下文
    private Context mContext;
    // 顶部布局
    private TitleView mTitleView;
    // 头像
    private CircleImageView mIVIcon;
    // vip
    private TextView mTVVip;
    // 名字
    private TextView mTVName;
    // 生日
    private TextView mTVBirthday;
    // 地址
    private TextView mTVAddress;
    // 手机
    private TextView mTVPhone;
    // 职业
    private TextView mTVOccupation;
    // 个人简介
    private TextView mTVPersonal;
    // ta的服务
    private LinearLayout mLyTadefuwu;
    // 案例
    private MyGridView mGridView;
    // 数据源
    private ArrayList<HashMap<String,String>> list;
    // 数据源
    private MyNeedBean myNeedBean;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_details;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        initData();

        mTitleView = (TitleView) findViewById(R.id.title);
        mIVIcon = (CircleImageView) findViewById(R.id.im_details_icon);
        mTVVip = (TextView) findViewById(R.id.tv_vip);
        mTVName = (TextView) findViewById(R.id.tv_details_name);
        mTVBirthday = (TextView) findViewById(R.id.tv_birthday);
        mTVAddress = (TextView) findViewById(R.id.tv_address);
        mTVPhone = (TextView) findViewById(R.id.tv_phone);
        mLyTadefuwu = (LinearLayout) findViewById(R.id.ly_tadefuwu);
        mTVOccupation = (TextView) findViewById(R.id.tv_occupation);
        mTVPersonal = (TextView) findViewById(R.id.tv_personal);
        mGridView = (MyGridView) findViewById(R.id.gv_in);

        mLyTadefuwu.setOnClickListener(this);

        //为GridView设置适配器
        mGridView.setAdapter(new MyAdapter(this, setData()));

        showView();
    }

    public void initData() {
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            myNeedBean = (MyNeedBean) intent.getSerializableExtra("data_details");
        }
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        mTitleView.setTitle("查看资料");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        if(myNeedBean != null){
            if(!Utils.isEmpty(myNeedBean.getImghead())){
                String picUrl = Constants.getCurrUrl() + Constants.CASE_IMAGE_URL_HEADER + myNeedBean.getImghead();
                ImageLoader.getInstance().displayImage(picUrl, mIVIcon, TopADApplication.getSelf().getImageLoaderOption(),
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

            if(!Utils.isEmpty(myNeedBean.getNickname())){
                mTVName.setText(myNeedBean.getNickname() + "        ");
                if(!Utils.isEmpty(myNeedBean.getSex())){
                    if("1".equals(myNeedBean.getSex())){
                        mTVName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_man) , null);
                    }else{
                        mTVName.setCompoundDrawablesWithIntrinsicBounds(null, null, getResources().getDrawable(R.drawable.icon_female) , null);
                    }
                }
            }

            if(!Utils.isEmpty(myNeedBean.getBirthday())){
                mTVBirthday.setText(myNeedBean.getBirthday());
            }

            if(!Utils.isEmpty(myNeedBean.getAddress())){
                mTVAddress.setText(myNeedBean.getAddress());
            }

            if(!Utils.isEmpty(TopADApplication.getSelf().getMyInfo().getMobile())){
                mTVPhone.setText(TopADApplication.getSelf().getMyInfo().getMobile());
            }

            if(!Utils.isEmpty(myNeedBean.getJob1()) && !Utils.isEmpty(myNeedBean.getJob2())){
                mTVOccupation.setText(myNeedBean.getJob1() + "-" + myNeedBean.getJob2());
            }

            if(!Utils.isEmpty(myNeedBean.getIntro())){
                mTVPersonal.setText(myNeedBean.getIntro());
            }
        }

        if(!Utils.isEmpty(TopADApplication.getSelf().getMyInfo().getIscompany())){
            if("1".equals(TopADApplication.getSelf().getMyInfo().getIscompany())){
                mTVVip.setVisibility(View.VISIBLE);
            }else{
                mTVVip.setVisibility(View.GONE);
            }
        }
    }

    /**
     * 顶部布局
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
            // ta的服务
            case R.id.ly_tadefuwu:
                Intent intent = new Intent(DetailsActivity.this, TaDeFuWuListActivity.class);
                intent.putExtra("from", "0");
                intent.putExtra("userid", myNeedBean.getUserid());
                intent.putExtra("imghead", myNeedBean.getImghead());
                startActivity(intent);
                break;
            default:
                break;
        }
    }

    //自定义适配器
    class MyAdapter extends BaseAdapter {
        //上下文对象
        private Context context;
        ArrayList<HashMap<String,String>> list = new ArrayList<HashMap<String,String>>();

        MyAdapter(Context context, ArrayList<HashMap<String,String>> list) {
            this.context = context;
            this.list = list;
        }

        public int getCount() {
            return list.size();
        }

        public Object getItem(int item) {
            return item;
        }

        public long getItemId(int id) {
            return id;
        }

        //创建View方法
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null){
                //根据布局文件获取View返回值
                convertView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.activity_grab_single_details_item_layout, null);
            }
            ImageView imageview = (ImageView) convertView.findViewById(R.id.icon);
            TextView name = (TextView) convertView.findViewById(R.id.tv_name);

            imageview.setImageResource(Integer.parseInt(list.get(position).get("icon")));
            name.setText( list.get(position).get("name"));
            return convertView;
        }
    }

    /**
     * 设置数据
     */
    private ArrayList<HashMap<String,String>> setData() {
        list = new ArrayList<HashMap<String,String>>();

        // 实名认证
        HashMap<String, String> map =  new HashMap<String,String>();
        if(!Utils.isEmpty(myNeedBean.getImgcard1()) || !Utils.isEmpty(myNeedBean.getImgcard2())){
            map.put("icon", String.valueOf(R.drawable.shiming_normal));
        }else{
            map.put("icon", String.valueOf(R.drawable.shiming));
        }
        map.put("name", "实名认证");
        list.add(map);

        // 职业证书
        HashMap<String, String> map1 =  new HashMap<String,String>();
        if(!Utils.isEmpty(myNeedBean.getImgdiploma())){
            map1.put("icon", String.valueOf(R.drawable.icon_job_y));
        }else{
            map1.put("icon", String.valueOf(R.drawable.icon_job_n));
        }
        map1.put("name", "职业证书");
        list.add(map1);

        // 名片
        HashMap<String, String> map2 =  new HashMap<String,String>();
        if(!Utils.isEmpty(myNeedBean.getImgnamecard())){
            map2.put("icon", String.valueOf(R.drawable.icon_card_y));
        }else{
            map2.put("icon", String.valueOf(R.drawable.icon_card_n));
        }
        map2.put("name", "名片");
        list.add(map2);

        // 客户保障
        HashMap<String, String> map3 =  new HashMap<String,String>();
        if(!Utils.isEmpty(myNeedBean.getImglicense())){
            map3.put("icon", String.valueOf(R.drawable.icon_job_y));
        }else{
            map3.put("icon", String.valueOf(R.drawable.icon_job_n));
        }
        map3.put("name", "客户保障");
        list.add(map3);

        return list;
    }
}
