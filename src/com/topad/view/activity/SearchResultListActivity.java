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
     * 选择好的搜索条件
     */
    ArrayList<SearchItemBean> itemBeans = new ArrayList<SearchItemBean>();

    /**
     * view
     **/
    private LinearLayout view;
int curPage=1;
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
    int searchType;
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
        searchType = getIntent().getIntExtra("searchtype", 0);
        itemBeans=getIntent().getParcelableArrayListExtra("searchKeys");
        // 顶部标题布局
        mTitleView = (TitleView) view.findViewById(R.id.title);
        mTitleView.setTitle("搜索");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
        mTitleView.setRightVisiable(true);
        mListView = (MyListView) findViewById(R.id.listview);
        submitSearch(curPage,0);
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
                curPage=1;
                submitSearch(curPage,0);
            }

            @Override
            public void onLoadMore() {
                submitSearch(curPage,1);
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

    /**
     * 提交搜索
     * refreshType 0:下拉刷新   1：上拉加载更多
     */
    public void submitSearch(int page,final int refreshType) {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_MEDIA_SEARCH).append("?");
        String url = sb.toString();


        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("page", ""+page);
        rp.add("type1", (searchType+1)+"");

        for (int i = 0; i < itemBeans.size(); i++) {
            SearchItemBean itembean = itemBeans.get(i);

            String parameName1=null;
            String parameName2=null;
            String parameName3=null;
            String parameName4=null;
            switch (i) {
                case 0:
                    parameName1="type21";
                    parameName2="type31";
                    parameName3="str11";
                    parameName4="str21";
                    break;
                case 1:
                    parameName1="type22";
                    parameName2="type32";
                    parameName3="str12";
                    parameName4="str22";
                    break;
                case 2:
                    parameName1="type23";
                    parameName2="type33";
                    parameName3="str13";
                    parameName4="str23";
                    break;
            }
            switch (searchType) {
                case 0://电视
                case 1://广播
                    //第一个条件
                    rp.add(parameName1, itembean.type);//第二个分类条件
                    rp.add(parameName2, "");//户外搜索有值  第三个分类条件
                    rp.add(parameName3, itembean.name);//文本框1
                    rp.add(parameName4, itembean.lanmu_name);
                    break;
                case 2://报纸
                case 4://杂志
                case 5://网络
                    //第一个条件
                    rp.add(parameName1, itembean.type);//第二个分类条件
                    rp.add(parameName2, "");//户外搜索有值  第三个分类条件
                    rp.add(parameName3, itembean.name);//文本框1
                    rp.add(parameName4, "");
                    break;
                case 3://户外
                    //第一个条件
                    rp.add(parameName1, itembean.type);//第二个分类条件
                    rp.add(parameName2, itembean.name);//户外搜索有值  第三个分类条件
                    rp.add(parameName3, itembean.locaion);//文本框1
                    rp.add(parameName4, "");
                    break;

            }
        }
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                BaseBean base = (BaseBean) t;
                if (base != null) {
                    ToastUtil.show(mContext, base.getMsg());
                    if(refreshType==0){
                        curPage=1;
                        mListView.stopRefresh();
                    }else{
                        mListView.stopLoadMore();
                        curPage++;
                    }
                }
            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, msg);
            }
        }, BaseBean.class);

    }
}

