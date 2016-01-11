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
 * ${todo}<产品类别列表页>
 *
 * @author lht
 * @data: on 15/11/2 18:06
 */
public class ProductClassListActivity extends BaseActivity implements View.OnClickListener {
    /** title布局 **/
    private TitleView mTitle;
    /** 上下文 **/
    private Context mContext;
    private ListView listview;
    private ListViewAdapter adapter;
    private String[] arrayTV = new String[]{"广告创意", "平面设计", "营销推广",
            "影视动漫", "文案策划", "广告监测",
            "专家培训", "管理咨询", "网站建设",
            "公关服务", "企业招聘", "其他服务"};
    private int[] arrayIM = new int[]{R.drawable.icon_idea, R.drawable.icon_vi, R.drawable.icon_yx,
            R.drawable.icon_video, R.drawable.icon_write, R.drawable.icon_monitor,
            R.drawable.icon_teach, R.drawable.icon_advisory, R.drawable.icon_web,
            R.drawable.icon_gg, R.drawable.icon_offer, R.drawable.icon_others};

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

    @Override
    public void initViews() {
        // 顶部布局
        mTitle = (TitleView) findViewById(R.id.title);
        mTitle.setTitle("产品类别");
        mTitle.setLeftClickListener(new TitleLeftOnClickListener());

        listview = (ListView) findViewById(R.id.listview);
        adapter = new ListViewAdapter(this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ProductClassListActivity.this, NeedsListActivity.class);
                intent.putExtra("from", "2");
                intent.putExtra("type", position);
                startActivity(intent);
                finish();
            }
        });
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
            return arrayTV.length;
        }

        @Override
        public Object getItem(int position) {
            return arrayTV[position];
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
            viewHolder.name.setText(arrayTV[position]);
            viewHolder.left_ic.setImageResource(arrayIM[position]);
            viewHolder.right_ic.setVisibility(View.INVISIBLE);

            return convertView;
        }


        class ViewHolder {
            ImageView left_ic, right_ic;
            TextView name;
        }

    }
}

