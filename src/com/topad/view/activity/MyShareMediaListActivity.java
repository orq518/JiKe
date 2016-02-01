package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.AdDetailsBean;
import com.topad.bean.AdProductBean;
import com.topad.bean.AdServiceBean;
import com.topad.bean.AdServiceCaseListBean;
import com.topad.bean.AdServiceDetailsBean;
import com.topad.bean.BaseBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;
import com.topad.view.customviews.mylist.BaseSwipeAdapter;
import com.topad.view.customviews.mylist.MyListView;
import com.topad.view.customviews.mylist.SimpleSwipeListener;
import com.topad.view.customviews.mylist.SwipeLayout;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * ${todo}<我的服务产品>
 *
 * @author lht
 * @data: on 15/10/26 11:06
 */
public class MyShareMediaListActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = MyShareMediaListActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** listView **/
    private MyListView mListView;
    /** 只是用来模拟异步获取数据 **/
    private Handler handler;
    /** 适配器 **/
    private ListAdapter adapter;
    /** 数据源 **/
    private ArrayList<AdProductBean> bankList = new ArrayList<AdProductBean>();

    /** view **/
    private LinearLayout view;
    /** 请求页数 **/
    private int page = 1;

    private final int MSG_CASE_LIST = 1000;
    protected android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CASE_LIST:
                    Bundle b = msg.getData();
                    final AdDetailsBean detailsBean = (AdDetailsBean) b.getSerializable("details_bean");
                    String serviceId = b.getString("service_id");

                    // 获取产品案例列表信息
                    StringBuffer sb = new StringBuffer();
                    sb.append(Constants.getCurrUrl()).append(Constants.URL_CASE_LIST).append("?");
                    String url = sb.toString();
                    RequestParams rp=new RequestParams();
                    rp.add("serviceid", serviceId);
//                    rp.add("serviceid", "6");

                    postWithLoading(url, rp, false, new HttpCallback() {
                        @Override
                        public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                            AdServiceCaseListBean bean = (AdServiceCaseListBean) t;
                            if(bean != null){
                                Intent intent = new Intent(MyShareMediaListActivity.this, AddProductActivity.class);
                                intent.putExtra("data_details", detailsBean);
                                intent.putExtra("data_case", bean);
                                intent.putExtra("from", "1");
                                startActivity(intent);
                            }
                        }

                        @Override
                        public void onFailure(BaseBean base) {
                            int status = base.getStatus();// 状态码
                            String msg = base.getMsg();// 错误信息
                            ToastUtil.show(mContext, "status = " + status + "\n"
                                    + "msg = " + msg);
                        }
                    }, AdServiceCaseListBean.class);
                    break;
            }
        }
    };

    @Override
    public int setLayoutById() {
        mContext = this;
        return 0;
    }

    @Override
    public View setLayoutByView() {
        view = (LinearLayout)View.inflate(this, R.layout.activity_ads_list, null);
        return view;
    }

    @Override
    public void initViews() {
        // 顶部标题布局
        mTitleView = (TitleView) view.findViewById(R.id.title);
        mTitleView.setTitle("我的服务产品设计");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
        mTitleView.setRightVisiable(true);
        mTitleView.setRightClickListener(new TitleRightOnClickListener(), "+" , 30);

        // listview
        mListView = (MyListView) findViewById(R.id.listview);

    }

    /**
     * 请求数据
     */
    @Override
    public void initData() {
        setData();

        // 设置listview可以加载、刷新
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        // 设置适配器
        adapter = new ListAdapter(mContext);
        mListView.setAdapter(adapter);

        // listview单击
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    final int position, long id) {
                final String serviceId = bankList.get(position - 1).getServiceid();

                // 获取产品详情信息
                StringBuffer sb = new StringBuffer();
                sb.append(Constants.getCurrUrl()).append(Constants.URL_GET_INFO).append("?");
                String url = sb.toString();
                RequestParams rp=new RequestParams();
                rp.add("serviceid", serviceId);
//                rp.add("serviceid", "6");
                postWithLoading(url, rp, false, new HttpCallback() {
                    @Override
                    public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {

                        final AdServiceDetailsBean detailsBean = (AdServiceDetailsBean) t;
                        if(detailsBean != null && detailsBean.data.size()>0){

                            mHandler.postDelayed(new Runnable() {

                                @Override
                                public void run() {

                                    Message msg = new Message();
                                    Bundle b = new Bundle();// 存放数据
                                    b.putSerializable("details_bean", detailsBean.data.get(0));
                                    b.putString("service_id", serviceId);
                                    msg.setData(b);
                                    msg.what = MSG_CASE_LIST;
                                    mHandler.sendMessage(msg);
                                }
                            }, 500);
                        }

                    }

                    @Override
                    public void onFailure(BaseBean base) {
                        int status = base.getStatus();// 状态码
                        String msg = base.getMsg();// 错误信息
                        ToastUtil.show(mContext, "status = " + status + "\n"
                                + "msg = " + msg);
                    }
                }, AdServiceDetailsBean.class);
            }
        });

        // 设置回调函数
        mListView.setMyListViewListener(new MyListView.IMyListViewListener() {

            @Override
            public void onRefresh() {
                bankList.clear();
                page = 1;
                setData();
            }

            @Override
            public void onLoadMore() {
                page ++;
                setData();
            }
        });

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.tv_layout:

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

    /**
     * 顶部布局--右按钮事件监听
     */
    public class TitleRightOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MyShareMediaListActivity.this, AddProductActivity.class);
            intent.putExtra("from", "2");
            startActivity(intent);

        }

    }

    /**
     * 自定义适配器
     */
    public class ListAdapter extends BaseSwipeAdapter {
        // 上下文对象
        private Context mContext;
        private ImageView authIcon;
        private ImageView icon;
        private TextView name;
        private TextView money;
        private TextView count;
        private TextView praise;
        private TextView companyName;

        // 构造函数
        public ListAdapter(Context mContext) {
            this.mContext = mContext;
        }

        // SwipeLayout的布局id
        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }

        @Override
        public View generateView(final int position, ViewGroup parent) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.activity_ad_service_item, parent, false);
            final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));

            // 当隐藏的删除menu被打开的时候的回调函数
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
                    Toast.makeText(mContext, "Open", Toast.LENGTH_SHORT).show();
                }
            });

            // 双击的回调函数
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout,
                                          boolean surface) {
                    Toast.makeText(mContext, "DoubleClick",
                            Toast.LENGTH_SHORT).show();
                }
            });

            // 添加删除布局的点击事件
            v.findViewById(R.id.ll_menu).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View arg0) {
//                    Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
                    // 点击完成之后，关闭删除menu
                    // 拼接url
                    StringBuffer sb = new StringBuffer();
                    sb.append(Constants.getCurrUrl()).append(Constants.URL_DEL_SERVICE).append("?");
                    String url = sb.toString();
                    RequestParams rp=new RequestParams();
                    rp.add("userid", TopADApplication.getSelf().getUserId());
                    rp.add("serviceid", bankList.get(position).getServiceid());
                    rp.add("token", TopADApplication.getSelf().getToken());

                    postWithLoading(url, rp, false, new HttpCallback() {
                        @Override
                        public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                            if(bankList.size() > 0){
                                swipeLayout.close();
                                bankList.remove(position);
                                notifyDataSetChanged();
                            }
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

            return v;
        }

        // 对控件的填值操作独立出来了，我们可以在这个方法里面进行item的数据赋值
        @Override
        public void fillValues(int position, View convertView) {
            authIcon = (ImageView) convertView.findViewById(R.id.ads_auth_icon);
            icon = (ImageView) convertView.findViewById(R.id.ads_icon);
            name = (TextView) convertView.findViewById(R.id.tv_name);
            money = (TextView) convertView.findViewById(R.id.tv_money);
            count = (TextView) convertView.findViewById(R.id.tv_count);
            companyName = (TextView) convertView.findViewById(R.id.tv_companyName);

            name.setText(bankList.get(position).getServicename());
            SpannableStringBuilder ssb = new SpannableStringBuilder("￥" +  bankList.get(position).getPrice() + "/单品");
            money.setText(ssb.toString());
            SpannableStringBuilder ssb2 = new SpannableStringBuilder("已出售：" +  bankList.get(position).getSalecount() + "笔");
            count.setText(ssb2.toString());
            companyName.setText(bankList.get(position).getCompanyname());

            if(!Utils.isEmpty(bankList.get(position).getImglicense())){
                authIcon.setImageDrawable(getResources().getDrawable(R.drawable.ads_icon_rz_ok));
            }else{
                authIcon.setImageDrawable(getResources().getDrawable(R.drawable.ads_icon_rz_ing));
            }


            if(!Utils.isEmpty(bankList.get(position).getImghead())){
                String headerpicUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + TopADApplication.getSelf().getMyInfo().getImghead();
                if(!Utils.isEmpty(headerpicUrl)){
                    ImageLoader.getInstance().displayImage(headerpicUrl, icon,
                            TopADApplication.getSelf().getImageLoaderOption());
                }
            }

        }

        @Override
        public int getCount() {
            return bankList.size();
        }

        @Override
        public Object getItem(int position) {
            return bankList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
    }

    /**
     * 设置数据
     */
    private void setData() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_AD_SERVICE_GETLIST).append("?");
        String url = sb.toString();
        RequestParams rp=new RequestParams();
        rp.add("type1", "");
        rp.add("type2", "");
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("page", page + "");
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                AdServiceBean serviceBean = (AdServiceBean) t;
                if (serviceBean != null && serviceBean.data.size()!= 0) {
                    for(int i = 0; i < serviceBean.data.size(); i++){
                        bankList.add(serviceBean.data.get(i));
                        adapter.notifyDataSetChanged();
                    }
                }

                mListView.stopRefresh();

                if(bankList == null || bankList.size() == 0){
                    mListView.setPullLoadEnable(false);
                }else{
                    mListView.setPullLoadEnable(true);
                }
        }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, "status = " + status + "\n"
                        + "msg = " + msg);
            }
        }, AdServiceBean.class);

    }
}

