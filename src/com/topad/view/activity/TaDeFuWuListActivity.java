package com.topad.view.activity;

import android.content.Context;
import android.os.Message;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.AdProductBean;
import com.topad.bean.AdServiceBean;
import com.topad.bean.BaseBean;
import com.topad.bean.SystemNewsBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;
import com.topad.view.customviews.mylist.MyListView;

import java.util.ArrayList;
import java.util.logging.Handler;

/**
 * ${todo}<ta的服务列表>
 *
 * @author lht
 * @data: on 15/12/7 14:41
 */
public class TaDeFuWuListActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = TaDeFuWuListActivity.class.getSimpleName();
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
    private ArrayList<AdProductBean> bankList = new ArrayList<AdProductBean>();
    /** userid **/
    private String userid;
    /** imghead **/
    private String imghead;
    /** 请求页数 **/
    private int page = 1;

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
        mListView = (MyListView) findViewById(R.id.listview);
    }

    @Override
    public void initData() {
        setData();

        getMessage();

        showView();

    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        mTitleView.setTitle("ta的服务产品设计");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        // 设置listview可以加载、刷新
        mListView.setPullLoadEnable(true);
        mListView.setPullRefreshEnable(true);
        // 设置适配器
        adapter = new ListAdapter();
        mListView.setAdapter(adapter);

        // 设置回调函数
        mListView.setMyListViewListener(new MyListView.IMyListViewListener() {

            @Override
            public void onRefresh() {
                bankList.clear();
                page = 1;
                getMessage();
            }

            @Override
            public void onLoadMore() {
                page ++;
                getMessage();
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
                convertView = mInflater.inflate((R.layout.activity_ad_service_item), null);
                holder = new ViewHolder();
                holder.authIcon = (ImageView) convertView.findViewById(R.id.ads_auth_icon);
                holder.icon = (ImageView) convertView.findViewById(R.id.ads_icon);
                holder.name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.money = (TextView) convertView.findViewById(R.id.tv_money);
                holder.count = (TextView) convertView.findViewById(R.id.tv_count);
                holder.companyName = (TextView) convertView.findViewById(R.id.tv_companyName);
                holder.type = (TextView) convertView.findViewById(R.id.tv_class);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.type.setText(bankList.get(position).getType1() + "-" + bankList.get(position).getType2());
            holder.name.setText(bankList.get(position).getServicename());
            SpannableStringBuilder ssb = new SpannableStringBuilder("￥" + bankList.get(position).getPrice() + "/单品");
            holder.money.setText(ssb.toString());
            SpannableStringBuilder ssb2 = new SpannableStringBuilder("已出售：" + bankList.get(position).getSalecount() + "笔");
            holder.count.setText(ssb2.toString());
            holder.companyName.setText(bankList.get(position).getCompanyname());

            if (!Utils.isEmpty(bankList.get(position).getImglicense())) {
                holder.authIcon.setImageDrawable(getResources().getDrawable(R.drawable.ads_icon_rz_ok));
            } else {
                holder.authIcon.setImageDrawable(getResources().getDrawable(R.drawable.ads_icon_rz_ing));
            }


            if (!Utils.isEmpty(bankList.get(position).getImghead())) {
                String headerpicUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + imghead;
                if (!Utils.isEmpty(headerpicUrl)) {
                    ImageLoader.getInstance().displayImage(headerpicUrl, holder.icon,
                            TopADApplication.getSelf().getImageLoaderOption());
                }
            }

            return convertView;
        }

        class ViewHolder {
            private ImageView authIcon;
            private ImageView icon;
            private TextView name;
            private TextView money;
            private TextView count;
            private TextView praise;
            private TextView companyName;
            private TextView type;
        }


    }
    /**
     * 设置数据--测试
     */
    private void setData() {
        imghead = getIntent().getStringExtra("imghead");
        userid = getIntent().getStringExtra("userid");
    }

    public void getMessage() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_AD_SERVICE_GETLIST).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("type1", "");
        rp.add("type2", "");
        rp.add("province", TopADApplication.getSelf().getProvice());
        rp.add("userid", userid);
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
//                ToastUtil.show(mContext, "status = " + status + "\n"
//                        + "msg = " + msg);
            }
        }, AdServiceBean.class);


    }
}