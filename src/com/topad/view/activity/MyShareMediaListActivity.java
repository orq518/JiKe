package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
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

import com.topad.R;
import com.topad.amap.ToastUtil;
import com.topad.bean.AdProductBean;
import com.topad.bean.AdServiceBean;
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
 * ${todo}<我发布的媒体>
 *     category＝ 广告创意－1、营销策略－2、影视广告－3、动漫创作－4
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
    /** 类别 **/
    private String category;

    private final int MSG_REFRESH = 1000;
    private final int MSG_LOADMORE = 2000;
    protected android.os.Handler mHandler = new android.os.Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_REFRESH:

                    break;

                case MSG_LOADMORE:

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
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            category = intent.getStringExtra("category");
        }

        // 顶部标题布局
        mTitleView = (TitleView) view.findViewById(R.id.title);
        if(!Utils.isEmpty(category)){
            if(category.equals("1")){
                mTitleView.setTitle(getString(R.string.ads_advertising_creative_title));
            }else if(category.equals("2")){
                mTitleView.setTitle(getString(R.string.ads_marketing_strategy_title));
            }else if(category.equals("3")){
                mTitleView.setTitle(getString(R.string.ads_tvc_title));
            }else if(category.equals("4")){
                mTitleView.setTitle(getString(R.string.ads_anime_create_title));
            }
        }
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

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
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        // 设置适配器
        adapter = new ListAdapter(mContext);
        mListView.setAdapter(adapter);

        // listview单击
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(MyShareMediaListActivity.this, ADSDetailsActivity.class);
                intent.putExtra("title",bankList.get(position).getServicename());
                intent.putExtra("data",bankList.get(position));
                startActivity(intent);
            }
        });

        // 设置回调函数
        mListView.setMyListViewListener(new MyListView.IMyListViewListener() {

            @Override
            public void onRefresh() {
                // 模拟刷新数据，1s之后停止刷新
                mHandler.postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        mListView.stopRefresh();
                        Toast.makeText(MyShareMediaListActivity.this, "refresh",
                                Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(MSG_REFRESH);
                    }
                }, 1000);
            }

            @Override
            public void onLoadMore() {
                mHandler.postDelayed(new Runnable() {
                    // 模拟加载数据，1s之后停止加载
                    @Override
                    public void run() {
                        mListView.stopLoadMore();
                        Toast.makeText(MyShareMediaListActivity.this, "loadMore",
                                Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(MSG_LOADMORE);
                    }
                }, 1000);
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
                    Toast.makeText(mContext, "delete", Toast.LENGTH_SHORT).show();
                    // 点击完成之后，关闭删除menu
                    swipeLayout.close();
                    bankList.remove(position);
                    notifyDataSetChanged();
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
            count.setText(bankList.get(position).getSalecount());
            companyName.setText(bankList.get(position).getCompanyname());

            if(!Utils.isEmpty(bankList.get(position).getImglicense())){
                authIcon.setImageDrawable(getResources().getDrawable(R.drawable.ads_icon_rz_ok));
            }else{
                authIcon.setImageDrawable(getResources().getDrawable(R.drawable.ads_icon_rz_ing));
            }

            switch (position){
                case 0:
                    icon.setImageResource(R.drawable.product0);
                    break;
                case 1:
                    icon.setImageResource(R.drawable.product1);
                    break;
                case 2:
                    icon.setImageResource(R.drawable.product2);
                    break;
                case 3:
                    icon.setImageResource(R.drawable.product3);
                    break;
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
     * 设置数据--测试
     */
    private void setData() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_AD_SERVICE_GETLIST).append("?");
        String url = sb.toString();
        RequestParams rp=new RequestParams();
//        rp.add("type2", category);
////        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("userid", "0");

        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                AdServiceBean serviceBean = (AdServiceBean) t;
                if (serviceBean != null && serviceBean.data.size()!= 0) {
                    for(int i = 0; i < serviceBean.data.size(); i++){
                        bankList.add(serviceBean.data.get(i));
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
        }, AdServiceBean.class);

    }
}

