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
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.AdServiceBean;
import com.topad.bean.BaseBean;
import com.topad.bean.LoginBean;
import com.topad.bean.SystemNewsBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Md5;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.Utils;
import com.topad.view.customviews.PTRListView;
import com.topad.view.customviews.PullToRefreshView;
import com.topad.view.customviews.TitleView;
import com.topad.view.customviews.mylist.MyListView;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * ${todo}<系统消息>
 *
 * @author lht
 * @data: on 15/12/7 14:41
 */
public class SystemNewsActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = SystemNewsActivity.class.getSimpleName();
    // 上下文
    private Context mContext;
    // 顶部布局
    private TitleView mTitleView;
    // listView
    private MyListView mListView;
    // 只是用来模拟异步获取数据
    private Handler handler;
    // 适配器
    private ListAdapter adapter;
    // 数据源
    private ArrayList<SystemNewsBean.DataEntity> bankList = new ArrayList<SystemNewsBean.DataEntity>();

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
        return R.layout.activity_system_news;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        ;
        mListView = (MyListView) findViewById(R.id.listview);
    }

    @Override
    public void initData() {
        setData();

        showView();

        getMessage(0);
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        mTitleView.setTitle("我的消息");
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
//                Intent intent = new Intent(SystemNewsActivity.this, ADSDetailsActivity.class);
//                intent.putExtra("title",bankList.get(position).name);
//                startActivity(intent);
            }
        });

        // 设置回调函数
        mListView.setMyListViewListener(new MyListView.IMyListViewListener() {

            @Override
            public void onRefresh() {
                getMessage(0);
            }

            @Override
            public void onLoadMore() {
                getMessage(1);
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
                convertView = mInflater.inflate((R.layout.activity_system_news_item_layout), null);
                holder = new ViewHolder();
                holder.icon = (ImageView) convertView.findViewById(R.id.im_icon);
                holder.content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tv_title = (TextView) convertView.findViewById(R.id.tv_title);
                holder.time = (TextView) convertView.findViewById(R.id.tv_time);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            SystemNewsBean.DataEntity entity=bankList.get(position);
            holder.tv_title.setText(entity.getTitle());
            holder.content.setText(entity.getBody());
            holder.time.setText(entity.getAddtime());
            return convertView;
        }

        class ViewHolder {
            ImageView icon;
            TextView tv_title;
            TextView content;
            TextView time;
        }
    }

    /**
     * 设置数据--测试
     */
    private void setData() {

    }

    public void getMessage(final int type) {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_USER_GETMSG).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        if (type == 0) {
            rp.add("lastmsgid", "0");//最后一个消息的ID
        }else{
            if(bankList!=null&&bankList.size()>0){
                rp.add("lastmsgid", bankList.get(bankList.size()-1).getId());//最后一个消息的ID
            }

        }



        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                if(type==0) {
                    mListView.stopRefresh();
                }else{
                    mListView.stopLoadMore();
                }

                SystemNewsBean base = (SystemNewsBean) t;
                if (base != null) {
                    if(type==0) {
                        bankList.clear();
                    }
                    bankList.addAll(base.getData());
                    adapter.notifyDataSetChanged();

                }
            }

            @Override
            public void onFailure(BaseBean base) {
                if(type==0) {
                    mListView.stopRefresh();
                }else{
                    mListView.stopLoadMore();
                }
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, msg);
            }
        }, SystemNewsBean.class);


    }
}