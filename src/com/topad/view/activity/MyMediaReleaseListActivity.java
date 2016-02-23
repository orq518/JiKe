package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.GrabSingleBean;
import com.topad.bean.GrabSingleListBean;
import com.topad.bean.MediaReleaseBean;
import com.topad.bean.MediaReleaseListBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;
import com.topad.view.customviews.mylist.BaseSwipeAdapter;
import com.topad.view.customviews.mylist.MyListView;
import com.topad.view.customviews.mylist.SimpleSwipeListener;
import com.topad.view.customviews.mylist.SwipeLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Handler;

/**
 * ${todo}<我的媒体发布列表－侧栏入口>
 *
 * @author lht
 * @data: on 15/12/7 14:41
 */
public class MyMediaReleaseListActivity extends BaseActivity implements View.OnClickListener{
    private static final String LTAG = MyMediaReleaseListActivity.class.getSimpleName();
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
    private ArrayList<MediaReleaseBean> bankList = new ArrayList<MediaReleaseBean>();
    // 请求页数
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
        mTitleView.setTitle("我发布的媒体");
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

    private class ListAdapter extends BaseSwipeAdapter {
        private LayoutInflater mInflater;
        private ImageView icon;
        private TextView name;
        private TextView type;
        private TextView time;
        private TextView address;
        public ListAdapter() {
            mInflater = LayoutInflater.from(mContext);
        }


        // SwipeLayout的布局id
        @Override
        public int getSwipeLayoutResourceId(int position) {
            return R.id.swipe;
        }

        @Override
        public View generateView(final int position, ViewGroup parent) {
            View v = LayoutInflater.from(mContext).inflate(R.layout.activity_media_release_details, parent, false);
            final SwipeLayout swipeLayout = (SwipeLayout) v.findViewById(getSwipeLayoutResourceId(position));

            // 当隐藏的删除menu被打开的时候的回调函数
            swipeLayout.addSwipeListener(new SimpleSwipeListener() {
                @Override
                public void onOpen(SwipeLayout layout) {
//                    Toast.makeText(mContext, "Open", Toast.LENGTH_SHORT).show();
                }
            });

            // 双击的回调函数
            swipeLayout.setOnDoubleClickListener(new SwipeLayout.DoubleClickListener() {
                @Override
                public void onDoubleClick(SwipeLayout layout,
                                          boolean surface) {
//                            Toast.makeText(mContext, "DoubleClick",
//                                    Toast.LENGTH_SHORT).show();
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
                    sb.append(Constants.getCurrUrl()).append(Constants.URL_MEDIA_DELMEDIA).append("?");
                    String url = sb.toString();
                    RequestParams rp=new RequestParams();
                    rp.add("userid", TopADApplication.getSelf().getUserId());
                    rp.add("mediaid", bankList.get(position).getId());
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
//                            ToastUtil.show(mContext, "status = " + status + "\n"
//                                    + "msg = " + msg);
                        }
                    }, BaseBean.class);
                }
            });

            return v;
        }

        // 对控件的填值操作独立出来了，我们可以在这个方法里面进行item的数据赋值
        @Override
        public void fillValues(int position, View convertView) {
            icon = (ImageView) convertView.findViewById(R.id.im_icon);
            name = (TextView) convertView .findViewById(R.id.tv_name);
            type = (TextView) convertView .findViewById(R.id.tv_type);
            time = (TextView) convertView .findViewById(R.id.tv_time);
            address = (TextView) convertView .findViewById(R.id.tv_address);

            if(!Utils.isEmpty(bankList.get(position).getMedianame())){
                name.setText(bankList.get(position).getMedianame());
            }

            if(!Utils.isEmpty(bankList.get(position).getType1())){
                type.setText(bankList.get(position).getType1());
            }

            if(!Utils.isEmpty(bankList.get(position).getAddtime())){
                // 时间
                Date date = null;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                try {
                    date = sdf.parse(bankList.get(position).getAddtime());
                    time.setText(Utils.getTimeFormatText(date));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            if(!Utils.isEmpty(bankList.get(position).getLocation())){
                address.setText(bankList.get(position).getLocation());
            }

            String headerpicUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + TopADApplication.getSelf().getMyInfo().getImghead();
            if(!Utils.isEmpty(headerpicUrl)){
                ImageLoader.getInstance().displayImage(headerpicUrl, icon, TopADApplication.getSelf().getImageLoaderOption(),
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

            ImageLoader.getInstance().displayImage(headerpicUrl, icon, TopADApplication.getSelf().getImageLoaderOption());

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
        sb.append(Constants.getCurrUrl()).append(Constants.URL_MEDIA_GETLIST).append("?");
        String url = sb.toString();
        RequestParams rp=new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("page", page + "");
        rp.add("token",TopADApplication.getSelf().getToken());

        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                MediaReleaseListBean bean = (MediaReleaseListBean) t;
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
        }, MediaReleaseListBean.class);
    }
}