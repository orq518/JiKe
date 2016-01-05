package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.topad.R;
import com.topad.bean.GrabSingleBean;
import com.topad.bean.SystemNewsBean;
import com.topad.view.customviews.TitleView;
import com.topad.view.customviews.mylist.MyListView;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * ${todo}<我的抢单－侧栏入口>
 *
 * @author lht
 * @data: on 15/12/7 14:41
 */
public class GrabSingleActivity extends BaseActivity implements View.OnClickListener{
    private static final String LTAG = GrabSingleActivity.class.getSimpleName();
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
    private ArrayList<GrabSingleBean> bankList = new ArrayList<GrabSingleBean>();

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
        return R.layout.activity_grab_single;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);;
        mListView = (MyListView) findViewById(R.id.listview);
    }

    @Override
    public void initData() {
        setData();

        showView();
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        mTitleView.setTitle("我的抢单");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        // 设置listview可以加载、刷新
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        // 设置适配器
        adapter = new ListAdapter();
        mListView.setAdapter(adapter);

        // listview单击
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent intent = new Intent(mContext, GrabSingleDetailsActivity.class);
                intent.putExtra("state", "2");
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
                        Toast.makeText(GrabSingleActivity.this, "refresh",
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
                        Toast.makeText(GrabSingleActivity.this, "loadMore",
                                Toast.LENGTH_SHORT).show();
                        mHandler.sendEmptyMessage(MSG_LOADMORE);
                    }
                }, 1000);
            }
        });
    }

    /**
     * 顶部布局--按钮事件监听
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
//            //
//            case R.id.btn_cash:
//
//                break;
            default:
                break;
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
                convertView = mInflater.inflate((R.layout.fargment_grab_single_item), null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.im_icon);
                holder.name = (TextView) convertView .findViewById(R.id.tv_name);
                holder.price = (TextView) convertView .findViewById(R.id.tv_price);
                holder.state = (TextView) convertView .findViewById(R.id.tv_state);
                holder.content = (TextView) convertView .findViewById(R.id.tv_content);
                holder.time = (Button) convertView .findViewById(R.id.btn_time);
                holder.countdown = (Button) convertView .findViewById(R.id.btn_countdown);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(bankList.get(position).name);
            holder.price.setText(bankList.get(position).price);
            holder.state.setText(bankList.get(position).state);
            holder.content.setText(bankList.get(position).content);
            holder.time.setText(bankList.get(position).time);
            holder.countdown.setText(bankList.get(position).countdown);
            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView name;
            TextView state;
            TextView price;
            TextView content;
            TextView time;
            TextView countdown;
        }
    }

    /**
     * 设置数据--测试
     */
    private void setData() {
        GrabSingleBean bModel0 = new GrabSingleBean();
        bModel0.name = "北京市聚宝网深圳分公司";
        bModel0.price = "￥120000";
        bModel0.state = "已托管";
        bModel0.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
        bModel0.time = "2015-10-22";
        bModel0.countdown = "还有3天到期";
        bankList.add(bModel0);

        GrabSingleBean bModel1 = new GrabSingleBean();
        bModel1.name = "北京市聚宝网深圳分公司";
        bModel1.price = "￥120000";
        bModel1.state = "已托管";
        bModel1.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
        bModel1.time = "2015-10-22";
        bModel1.countdown = "还有3天到期";
        bankList.add(bModel1);

        GrabSingleBean bModel2 = new GrabSingleBean();
        bModel2.name = "北京市聚宝网深圳分公司";
        bModel2.price = "￥120000";
        bModel2.state = "已托管";
        bModel2.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
        bModel2.time = "2015-10-22";
        bModel2.countdown = "还有3天到期";
        bankList.add(bModel2);

        GrabSingleBean bModel3 = new GrabSingleBean();
        bModel3.name = "北京市聚宝网深圳分公司";
        bModel3.price = "￥120000";
        bModel3.state = "已托管";
        bModel3.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
        bModel3.time = "2015-10-22";
        bModel3.countdown = "还有3天到期";
        bankList.add(bModel3);

        GrabSingleBean bModel4 = new GrabSingleBean();
        bModel4.name = "北京市聚宝网深圳分公司";
        bModel4.price = "￥120000";
        bModel4.state = "已托管";
        bModel4.content = "高价发标编辑捕鱼游戏一套完整成熟的概率数值控制代码～";
        bModel4.time = "2015-10-22";
        bModel4.countdown = "还有3天到期";
        bankList.add(bModel4);
    }
}