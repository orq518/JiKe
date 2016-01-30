package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.GrabSingleBean;
import com.topad.bean.MyNeedBean;
import com.topad.bean.MyNeedListBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Utils;
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
public class MyNeedDetailsActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = MyNeedDetailsActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** 案例 **/
    private MyGridView mGridView;
    /** 名称 **/
    private TextView mName;
    /** 价钱 **/
    private TextView mMoney;
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
    /** 公司名 **/
    private TextView mTVGSName;
    /** 项目状态 **/
    private TextView mTVProgectState;
    /** 时间 **/
    private TextView mTVProgectTime;
    /** 项目完成 **/
    private Button mFinish;

    /** 已、未托管布局 **/
    private LinearLayout mLYTrust;
    /** 项目款托管 **/
    private Button mProjectTrust;
    /** 取消项目 **/
    private Button mProjectCancel;
    /** 列表 **/
    private ListView mListview;


    /** 状态 1－项目完成，2-项目进行中 ，3-已托管 ，4－未托管 */
    private String state;
    /** 需求id **/
    private String needId;
    /** **/
    private GrabSingleBean grabSingleBean;
    /** 数据源 **/
    private ArrayList<MyNeedBean> bankList = new ArrayList<MyNeedBean>();

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
        mContent = (TextView) findViewById(R.id.tv_content);
        mAddress = (TextView) findViewById(R.id.tv_address);
        mTVType = (TextView) findViewById(R.id.tv_type);
        mTVTime = (TextView) findViewById(R.id.tv_time);

        mLYProductFinish = (LinearLayout) findViewById(R.id.ly_product_finish);
        mTVGSName = (TextView) findViewById(R.id.tv_gs_name);
        mTVProgectState = (TextView) findViewById(R.id.tv_progect_state);
        mTVProgectTime = (TextView) findViewById(R.id.tv_progect_time);
        mFinish = (Button) findViewById(R.id.btn_finish);

        mLYTrust = (LinearLayout) findViewById(R.id.ly_trust);
        mProjectTrust = (Button) findViewById(R.id.btn_project_trust);
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
        mTitleView.setTitle("项目详情");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        // 名字
        if (!Utils.isEmpty(grabSingleBean.getTitle())) {
            mName.setText(grabSingleBean.getTitle());
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

        // 类别
        if (!Utils.isEmpty(grabSingleBean.getType1())
                && !Utils.isEmpty(grabSingleBean.getType2())) {
            SpannableStringBuilder ssb = new SpannableStringBuilder("类型：" + grabSingleBean.getType1() + "-" + grabSingleBean.getType2());
            mTVType.setText(ssb.toString());
        }

        // 时间
        if (!Utils.isEmpty(grabSingleBean.getAdddate())) {
            String[] sourceStrArray = grabSingleBean.getAdddate().split(" ");
            mTVTime.setText(sourceStrArray[0]);
        }

        if ("1".equals(state)) {// 项目完成
            mLYProductFinish.setVisibility(View.VISIBLE);
            mFinish.setVisibility(View.GONE);
            mLYTrust.setVisibility(View.GONE);
            mProjectTrust.setVisibility(View.GONE);
            mProjectCancel.setVisibility(View.GONE);
            mListview.setVisibility(View.GONE);

        } else if ("2".equals(state)) {// 项目进行中
            mLYProductFinish.setVisibility(View.VISIBLE);
            mFinish.setVisibility(View.VISIBLE);
            mLYTrust.setVisibility(View.GONE);
            mProjectTrust.setVisibility(View.GONE);
            mProjectCancel.setVisibility(View.GONE);
            mListview.setVisibility(View.GONE);
        } else if ("3".equals(state)) {// 已托管
            mLYProductFinish.setVisibility(View.GONE);
            mFinish.setVisibility(View.GONE);
            mLYTrust.setVisibility(View.VISIBLE);
            mProjectTrust.setVisibility(View.GONE);
            mProjectCancel.setVisibility(View.VISIBLE);
            mListview.setVisibility(View.VISIBLE);
        } else if ("4".equals(state)) {// 未托管
            mLYProductFinish.setVisibility(View.GONE);
            mFinish.setVisibility(View.GONE);
            mLYTrust.setVisibility(View.VISIBLE);
            mProjectTrust.setVisibility(View.VISIBLE);
            mProjectCancel.setVisibility(View.VISIBLE);
            mListview.setVisibility(View.VISIBLE);
        }

        getData();
        mListview.setAdapter(new ListAdapter());
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 项目完成
            case R.id.btn_finish:
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

            // 项目款托管
            case R.id.btn_project_trust:
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
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                convertView = mInflater.inflate((R.layout.activity_need_details_item), null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.tv_need_item_gs_icon);
                holder.name = (TextView) convertView.findViewById(R.id.tv_need_item_gs_name);
                holder.state = (TextView) convertView.findViewById(R.id.tv_need_item_state);
                holder.time = (TextView) convertView.findViewById(R.id.tv_need_item_state);
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
            holder.time.setText(Utils.getTimeFormatText(date));

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
                    rp.add("userid2", grabSingleBean.getUserid2());
                    rp.add("token", TopADApplication.getSelf().getToken());
                    postWithLoading(url, rp, false, new HttpCallback() {
                        @Override
                        public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                            mProjectTrust.setVisibility(View.GONE);
                        }

                        @Override
                        public void onFailure(BaseBean base) {
                            int status = base.getStatus();// 状态码
                            String msg = base.getMsg();// 错误信息
                            ToastUtil.show(mContext, "status = " + status + "\n"
                                    + "msg = " + msg);
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
     * 获取数据
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
}
