package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.GrabSingleBean;
import com.topad.bean.GrabSingleListBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;
import com.topad.view.customviews.mylist.MyListView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Handler;

/**
 * ${todo}<我的抢单列表－侧栏入口>
 *
 * @author lht
 * @data: on 15/12/7 14:41
 */
public class MyGrabSingleListActivity extends BaseActivity implements View.OnClickListener{
    private static final String LTAG = MyGrabSingleListActivity.class.getSimpleName();
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
    /** 请求页数 **/
    private int page = 1;
    /** 来源0-我的抢单，1-我的订单 **/
    private String from;

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
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            from = intent.getStringExtra("from");
        }
        setData();

        showView();
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        if("0".equals(from)){
            mTitleView.setTitle("我的抢单");
        }else{
            mTitleView.setTitle("我的订单");
        }

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
                String state = "0";
                if("1".equals(bankList.get(position-1).getStatus())){
                    state = "2";// 2未选择抢单人
                }else if("3".equals(bankList.get(position-1).getStatus())){
                    state = "3";// 3项目已取消
                }else if("1".equals(bankList.get(position-1).getStatus()) && TopADApplication.getSelf().getUserId().equals(bankList.get(position-1).getUserid2())){
                    state = "1";// 1抢单成功
                }else if("1".equals(bankList.get(position-1).getStatus()) && !TopADApplication.getSelf().getUserId().equals(bankList.get(position-1).getUserid2())){
                    state = "4";// 4已选择其他
                }

                intent.putExtra("state", state);
                intent.putExtra("data_details", bankList.get(position-1));
                startActivity(intent);
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
                holder.time = (TextView) convertView .findViewById(R.id.tv_time);
                holder.countdown = (TextView) convertView .findViewById(R.id.tv_countdown);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.name.setText(bankList.get(position).getTitle());
            SpannableStringBuilder ssb = new SpannableStringBuilder("￥" + bankList.get(position).getBudget());
            holder.price.setText(ssb.toString());
            holder.state.setText(bankList.get(position).getStatus());
            holder.content.setText(bankList.get(position).getDetail());
            String[] sourceStrArray = bankList.get(position).getAdddate().split(" ");
            holder.time.setText(sourceStrArray[0]);

            if (!Utils.isEmpty(bankList.get(position).getEnddate())) {
                // 时间
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try {
                    date = sdf.parse(bankList.get(position).getEnddate());
                    long diff = new Date().getTime() - date.getTime();
                    long day = 60 * 60 * 24 * 1000;// 1天
                    long r = 0;
                    if (diff > day) {
                        r = (diff / day);
                        holder.countdown.setText("还有" + r + "天有效期");
                    }else{
                        holder.countdown.setText("项目已过期");
                    }

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

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
     * 设置数据
     */
    private void setData() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_NEED_GETLIST).append("?");
        String url = sb.toString();
        RequestParams rp=new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("type1", "0"); // 当是我的数据默认为0
        rp.add("type2", "0");// 当是我的数据默认为0
        rp.add("isselfpost", "0"); // 是否是自己发布的
        if("0".equals(from)){
            rp.add("isqd", "0"); // 我的抢单该值为0，我要抢单该值为1，我的订单该值为2
        }else{
            rp.add("isqd", "2");
        }
        rp.add("province",TopADApplication.getSelf().getProvice());
        rp.add("page", page + "");
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                GrabSingleListBean bean = (GrabSingleListBean) t;
                if (bean != null && bean.data.size()!= 0) {
                    for(int i = 0; i < bean.data.size(); i++){
                        bankList.add(bean.data.get(i));
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
        }, GrabSingleListBean.class);
    }
}