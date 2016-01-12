package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.AdProductBean;
import com.topad.bean.AdServiceBean;
import com.topad.bean.BaseBean;
import com.topad.bean.SearchItemBean;
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
 * 搜索结果
 * * @author ou
 *
 * @data: on 15/10/26 11:06
 */
public class SearchResultListActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = SearchResultListActivity.class.getSimpleName();
    /**
     * 上下文
     **/
    private Context mContext;
    /**
     * 顶部布局
     **/
    private TitleView mTitleView;
    /**
     * listView
     **/
    private MyListView mListView;
    /**
     * 只是用来模拟异步获取数据
     **/
    private Handler handler;
    /**
     * 适配器
     **/
    private ListViewAdapter adapter;
    /**
     * 选择好的条件
     */
    ArrayList<SearchItemBean> itemBeans = new ArrayList<SearchItemBean>();

    /**
     * view
     **/
    private LinearLayout view;

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
        view = (LinearLayout) View.inflate(this, R.layout.activity_ads_list, null);
        return view;
    }

    @Override
    public void initViews() {
        // 顶部标题布局
        mTitleView = (TitleView) view.findViewById(R.id.title);
        mTitleView.setTitle("搜索");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
        mTitleView.setRightVisiable(true);
        mListView = (MyListView) findViewById(R.id.listview);

    }

    /**
     * 请求数据
     */

    @Override
    public void initData() {

        // 设置listview可以加载、刷新
        mListView.setPullLoadEnable(false);
        mListView.setPullRefreshEnable(true);
        // 设置适配器
        adapter = new ListViewAdapter(mContext);
        mListView.setAdapter(adapter);

        // listview单击
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
//                Intent intent = new Intent(SearchResultListActivity.this, ADSDetailsActivity.class);
//                intent.putExtra("title",bankList.get(position).getServicename());
//                intent.putExtra("data",bankList.get(position));
//                startActivity(intent);
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
                        Toast.makeText(SearchResultListActivity.this, "refresh",
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
                        Toast.makeText(SearchResultListActivity.this, "loadMore",
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

    /**
     * 顶部布局--左按钮事件监听
     */
    public class TitleRightOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SearchResultListActivity.this, AddProductActivity.class);
            startActivity(intent);

        }

    }

    public class ListViewAdapter extends BaseAdapter {

        private Context mCtx;
        private LayoutInflater mInflater;
        private ViewHolder mHolder;

        public ListViewAdapter(Context mCtx) {
            this.mCtx = mCtx;
            mInflater = LayoutInflater.from(mCtx);
        }


        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public Object getItem(int position) {
            return itemBeans.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.search_result_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.left_ic = (ImageView) convertView.findViewById(R.id.left_ic);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.lanmu = (TextView) convertView.findViewById(R.id.lanmu);
                viewHolder.type = (TextView) convertView.findViewById(R.id.type);
                viewHolder.time = (TextView) convertView.findViewById(R.id.time);
                viewHolder.location = (TextView) convertView.findViewById(R.id.location);
                viewHolder.contactme = (Button) convertView.findViewById(R.id.contactme);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            return convertView;
        }


        class ViewHolder {
            ImageView left_ic;
            TextView name, lanmu, type, time, location;
            Button contactme;
        }

    }
}

