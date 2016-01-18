package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Message;
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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.SearchItemBean;
import com.topad.bean.SearchResultBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.UploadUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;
import com.topad.view.customviews.mylist.MyListView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
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
     * 搜索结果
     */
    ArrayList<SearchResultBean.DataEntity> searchResultList = new ArrayList<>();
    String voicePath;
    /**
     * view
     **/
    private LinearLayout view;
    int curPage = 1;
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

    String type1;

    @Override
    public void initViews() {
        searchType = getIntent().getIntExtra("searchtype", 0);
        switch (searchType) {
            case 0://电视
                type1 = "电视";
                break;
            case 1://广播
                type1 = "广播";
                break;
            case 2://报纸
                type1 = "报纸";
                break;
            case 3://户外
                type1 = "户外";
                break;
            case 4://杂志
                type1 = "杂志";
                break;
            case 5://网络
                type1 = "网络";
                break;
        }
        voicePath = getIntent().getStringExtra("voicePath");
        itemBeans = getIntent().getParcelableArrayListExtra("searchKeys");
        // 顶部标题布局
        mTitleView = (TitleView) view.findViewById(R.id.title);
        mTitleView.setTitle("搜索");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());
        mTitleView.setRightVisiable(true);
        mListView = (MyListView) findViewById(R.id.listview);
        submitSearch(curPage, 0);
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
            }
        });

        // 设置回调函数
        mListView.setMyListViewListener(new MyListView.IMyListViewListener() {

            @Override
            public void onRefresh() {
                curPage = 1;
                submitSearch(curPage, 0);
            }

            @Override
            public void onLoadMore() {
                submitSearch(curPage, 1);
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
            return searchResultList.size();
        }

        @Override
        public Object getItem(int position) {
            return searchResultList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
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

            /**
             * id : 10
             * addtime : 2016-01-15 11:13:14
             * medianame : cctv1
             * location : 嘉和丽园公寓(东门)
             * subname : qqqq
             * type1 : 电视
             * type3 :
             * userid : 8
             * type2 : 中央电视台
             * longitude : 39.958031
             * latitude : 116.465878
             * mediacert :
             */
             SearchResultBean.DataEntity bean = searchResultList.get(position);
            final String mediaid = bean.getId();
            String addtime = bean.getAddtime();
            String medianame = bean.getMedianame();
            String location = bean.getLocation();
            String type1 = bean.getType1();
            String type2 = bean.getType2();
            String type3 = bean.getType3();
            String longitude = bean.getLongitude();
            final String latitude = bean.getLatitude();
            final String userid = bean.getUserid();
            String mediacert = bean.getMediacert();
            //左侧头像
            String picUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + mediacert;
            if(!Utils.isEmpty(picUrl)){
                ImageLoader.getInstance().displayImage(picUrl, viewHolder.left_ic, TopADApplication.getSelf().getImageLoaderOption());

            }
             viewHolder.name.setText(medianame);
            viewHolder.lanmu.setText(type2);
            viewHolder.type.setText(type1);
            viewHolder.time.setText(addtime);
            viewHolder.location.setText(location);
            viewHolder.contactme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(Utils.isEmpty(voicePath)){
                        contactme(userid,mediaid,"");
                    }else{
                        uploadMedia(userid,mediaid,voicePath);
                    }

                }
            });

            return convertView;
        }


        class ViewHolder {
            ImageView left_ic;
            TextView name, lanmu, type, time, location;
            Button contactme;
        }

    }

    public void uploadMedia(final String userid, final String mediaid, final String pathString) {

        File file = new File(pathString);
        if (file != null) {
            // 拼接url
            StringBuffer sb = new StringBuffer();
            sb.append(Constants.getCurrUrl()).append(Constants.UPLOAD_PHOTO).append("?");
            String url = sb.toString();
            String fileKey = "userfile";
            UploadUtil uploadUtil = UploadUtil.getInstance(mContext);
            uploadUtil.setOnUploadProcessListener(new UploadUtil.OnUploadProcessListener() {
                @Override
                public void onUploadDone(int responseCode, String message) {
                    LogUtil.d("#responseCode:" + responseCode);
                    LogUtil.d("#message:" + message);
                    if (responseCode == 1 && !Utils.isEmpty(message)) {//上传图片之后服务器返回数据  有可能失败
                        try {
                            JSONObject respObj = new JSONObject(message);
                            String status = respObj.getString("status");// 状态码
                            String msg = respObj.getString("msg");// 错误信息
                            String img = respObj.getString("img");// 图片名
                            contactme(userid,mediaid,img);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//
                    } else {//上传失败  服务器报错
                        Toast.makeText(mContext, "上传失败", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onUploadProcess(int uploadSize) {
                }

                @Override
                public void initUpload(int fileSize) {

                }
            }); //设置监听器监听上传状态

            Map<String, String> params = new HashMap<String, String>();
            uploadUtil.uploadFile(pathString, fileKey, url, params);
        }


    }

    /**
     * @param userid  媒体发布用户ID
     * @param mediaid 媒体id
     * @param recfile 查询时的录音文件
     */
    public void contactme(String userid, String mediaid, String recfile) {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_CONTACTME).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("userid2", userid);
        rp.add("mediaid", mediaid);
        rp.add("recfile", recfile);
        String phoneNumber = (String) SharedPreferencesUtils.get(mContext, SharedPreferencesUtils.USER_PHONR, "");
        rp.add("mobile", phoneNumber);
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                BaseBean base = (BaseBean) t;
                if (base != null) {
                    Toast.makeText(mContext, base.getMsg(), Toast.LENGTH_SHORT).show();
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

    /**
     * 提交搜索
     * refreshType 0:下拉刷新   1：上拉加载更多
     */
    public void submitSearch(int page, final int refreshType) {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_MEDIA_SEARCH).append("?");
        String url = sb.toString();


        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("page", "" + page);
        rp.add("type1", type1);

        for (int i = 0; i < itemBeans.size(); i++) {
            SearchItemBean itembean = itemBeans.get(i);

            String parameName1 = null;
            String parameName2 = null;
            String parameName3 = null;
            String parameName4 = null;
            switch (i) {
                case 0:
                    parameName1 = "type21";
                    parameName2 = "type31";
                    parameName3 = "str11";
                    parameName4 = "str21";
                    break;
                case 1:
                    parameName1 = "type22";
                    parameName2 = "type32";
                    parameName3 = "str12";
                    parameName4 = "str22";
                    break;
                case 2:
                    parameName1 = "type23";
                    parameName2 = "type33";
                    parameName3 = "str13";
                    parameName4 = "str23";
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
                SearchResultBean base = (SearchResultBean) t;
                if (base != null) {
                    ToastUtil.show(mContext, base.getMsg());
                    if (refreshType == 0) {
                        curPage = 1;
                        mListView.stopRefresh();
                        searchResultList.clear();
                        searchResultList.addAll(base.getData());
                    } else {
                        searchResultList.addAll(base.getData());
                        mListView.stopLoadMore();
                        curPage++;
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, msg);
            }
        }, SearchResultBean.class);

    }
}

