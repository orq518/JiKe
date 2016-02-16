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
import android.widget.TextView;
import android.widget.Toast;

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
 * ${todo}<我的需求－侧栏入口>
 *
 * @author lht
 * @data: on 15/12/7 14:41
 */
public class MyNeedsActivity extends BaseActivity implements View.OnClickListener{
    private static final String LTAG = MyNeedsActivity.class.getSimpleName();
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
        mTitleView.setTitle("我的需求");
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
                Intent intent = new Intent(mContext, MyNeedDetailsActivity.class);
//                intent.putExtra("state", "0");
                intent.putExtra("state", bankList.get(position-1).getStatus());
                intent.putExtra("needId", bankList.get(position-1).getId());
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
                convertView = mInflater.inflate((R.layout.fargment_my_need_item), null);
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
            if("0".equals(bankList.get(position).getIspay())){
                holder.state.setVisibility(View.GONE);
            }else{
                holder.state.setVisibility(View.VISIBLE);
            }

            holder.content.setText(bankList.get(position).getDetail());
            String[] sourceStrArray = bankList.get(position).getAdddate().split(" ");
            holder.time.setText(sourceStrArray[0]);

            if(!Utils.isEmpty(bankList.get(position).getAdddate())){
                // 时间
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try {
                    date = sdf.parse(bankList.get(position).getAdddate());
                    holder.countdown.setText(Utils.getTimeFormatText(date));
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
        rp.add("isselfpost", "1"); // 是否是自己发布的
        rp.add("isqd", "0"); // 我要抢单该值为1
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