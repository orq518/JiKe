package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.MyInfoBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.SystemBarTintManager;
import com.topad.view.customviews.TitleView;

/**
 * 主界面
 */
public class QiyeZhaopin3Activity extends BaseActivity implements View.OnClickListener {


    String[] tempArray;
    /**
     * title布局
     **/
    private TitleView mTitle;
    ListView listview;
    ListViewAdapter adapter;
    int type;
    Context mContext;
    String type1, type2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_search_list;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    String titleString = null;
    /**
     * from: null或者0 来自发布需求   1：来自职业选择
     */
    String from;
    /**
     * 沉浸式状态栏
     **/
    private SystemBarTintManager mTintManager;

    private void applySelectedColor() {
        int color = Color.argb(0, Color.red(0), Color.green(0), Color.blue(0));
        mTintManager.setTintColor(color);
    }

    @Override
    public void initViews() {
        type1=getIntent().getStringExtra("type1");
        type2=getIntent().getStringExtra("type2");
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
        applySelectedColor();
        Intent intent = getIntent();
        titleString = intent.getStringExtra("title");
        type = intent.getIntExtra("type", 0);
        switch (type) {
            case 0:
                tempArray = this.getResources().getStringArray(R.array.guanggaogongsi);
                break;
            case 1:
                tempArray = this.getResources().getStringArray(R.array.guanggaozhu);
                break;
            case 2:
                tempArray = this.getResources().getStringArray(R.array.dianshimeiti);
                break;
            case 3:
                tempArray = this.getResources().getStringArray(R.array.guangbomeiti);
                break;
            case 4:
                tempArray = this.getResources().getStringArray(R.array.baozhizazhi);
                break;
            case 5:
                tempArray = this.getResources().getStringArray(R.array.huwaimeiti);
                break;
            case 6:
                tempArray = this.getResources().getStringArray(R.array.hulianwangmeiti);
                break;
            case 7:
                tempArray = this.getResources().getStringArray(R.array.yingxiaocehua);
                break;
            case 8:
                tempArray = this.getResources().getStringArray(R.array.jishurencai);
                break;
            case 9:
                tempArray = this.getResources().getStringArray(R.array.daxueshengshixi);
                break;
        }

        // 顶部布局
        mTitle = (TitleView) findViewById(R.id.title);
        // 设置顶部布局
        mTitle.setTitle(titleString);
        mTitle.setLeftClickListener(new TitleLeftOnClickListener());
        listview = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter(this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(QiyeZhaopin3Activity.this, ShareNeedsEditActivity_Zhaopin.class);
                intent.putExtra("type1",type1);
                intent.putExtra("type2",type2);
                intent.putExtra("type3",tempArray[position]);
                startActivity(intent);
            }
        });
    }

    /**
     * 更新职业
     */
    public void getMyInfo(String job1, String job2) {

        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_UPDATEJOB).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("job1", job1);
        rp.add("job2", job2);
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                MyInfoBean base = (MyInfoBean) t;
                if (base != null) {
                    ToastUtil.show(mContext, base.getMsg());
                    finish();
                }
            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
                ToastUtil.show(mContext, msg);
            }
        }, MyInfoBean.class);

    }

    @Override
    public void initData() {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
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
            onBack();
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBack();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBack() {
        finish();
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
            return tempArray.length;
        }

        @Override
        public Object getItem(int position) {
            return tempArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.needs_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.left_ic = (ImageView) convertView.findViewById(R.id.left_ic);
                viewHolder.right_ic = (ImageView) convertView.findViewById(R.id.right_ic);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(tempArray[position]);
            viewHolder.left_ic.setVisibility(View.GONE);
            if ("1".equals(from)) {
                viewHolder.right_ic.setVisibility(View.INVISIBLE);
            }
            return convertView;
        }


        class ViewHolder {
            ImageView left_ic, right_ic;
            TextView name;
        }

    }
}

