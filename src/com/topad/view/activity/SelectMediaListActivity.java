package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
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
import com.topad.bean.SearchListBean;
import com.topad.util.SystemBarTintManager;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

import java.util.ArrayList;

/**
 * ${todo}<选择媒体列表>
 *
 * @author lht
 * @data: on 15/10/27 14:40
 */
public class SelectMediaListActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = SelectMediaListActivity.class.getSimpleName();
    /** itle布局 **/
    private TitleView mTitle;
    private ListView listview;
    private ListViewAdapter adapter;
    private String[] mediaString;
    /** 类别 **/
    private String category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayoutById() {
        return R.layout.activity_search_list;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        Intent intent = getIntent();
        if (intent != null) {
            category = intent.getStringExtra("category");
        }
        if("1".equals(category)){
            mediaString =  getResources().getStringArray(R.array.dianshi);
        }else if("2".equals(category)){
            mediaString =  getResources().getStringArray(R.array.guangbo);
        }else if("3".equals(category)){
            mediaString =  getResources().getStringArray(R.array.baozhi);
        }else if("4".equals(category)){
            mediaString =  getResources().getStringArray(R.array.huwai);
        }else if("5".equals(category)){
            mediaString =  getResources().getStringArray(R.array.zazhi);
        }else if("6".equals(category)){
            mediaString =  getResources().getStringArray(R.array.wangluo);
        }

        // 顶部布局
        mTitle = (TitleView) findViewById(R.id.title);
        // 设置顶部布局
        mTitle.setTitle("选择媒体类别");
        mTitle.setLeftClickListener(new TitleLeftOnClickListener());

        listview = (ListView) findViewById(R.id.listview);



        adapter = new ListViewAdapter(this);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(SelectMediaListActivity.this, MediaReleaseActivity.class );
                intent.putExtra( "mediaName", mediaString[position]);
                setResult(RESULT_OK, intent);
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
            return mediaString.length;
        }

        @Override
        public Object getItem(int position) {
            return mediaString[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = mInflater.inflate(R.layout.search_list_item, null);
                viewHolder = new ViewHolder();
                viewHolder.left_ic = (ImageView) convertView.findViewById(R.id.left_ic);
                viewHolder.name = (TextView) convertView.findViewById(R.id.name);
                viewHolder.type = (TextView) convertView.findViewById(R.id.type);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.name.setText(mediaString[position]);
            viewHolder.left_ic.setVisibility(View.INVISIBLE);
            viewHolder.type.setVisibility(View.INVISIBLE);

            return convertView;
        }


        class ViewHolder {
            ImageView left_ic;
            TextView name, type;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBack();
        }
        return super.onKeyDown(keyCode, event);
    }
}

