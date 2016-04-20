package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.AdDetailsBean;
import com.topad.bean.AdServiceCaseListBean;
import com.topad.bean.BaseBean;
import com.topad.bean.LoginBean;
import com.topad.bean.SearchItemBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.Md5;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

import java.util.ArrayList;

/**
 * ${todo}<首页其他搜索页面>
 *
 * @author lht
 * @data: on 16/04/12 16:35
 */
public class OtherSearchActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = OtherSearchActivity.class.getSimpleName();
    // 上下文
    private Context mContext;
    // 顶部布局
    private TitleView mTitleView;
    // 搜索内容
    private EditText mETContent;
    // 提交
    private Button mBTLogin;
    // 标题
    private String title;
    // 内容
    private String content;
    // 来源 0-其他1，媒体搜索 1-其他2，服务搜素
    private String from;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_other_search;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        mETContent = (EditText) findViewById(R.id.et_content);
        mBTLogin = (Button) findViewById(R.id.btn_login);

        mBTLogin.setOnClickListener(this);

        // 设置登录按钮
        setNextBtnState(false);
    }

    @Override
    public void initData() {
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            title = getIntent().getStringExtra("title");
            from = getIntent().getStringExtra("from");
        }
        showView();
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        mTitleView.setTitle(title);
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        // 设置搜索内容
        mETContent.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mETContent);
                if (!Utils.isEmpty(data)) {
                    content = data;
                }

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onTextChanged(CharSequence arg0, int arg1, int arg2,
                                      int arg3) {
                String content = getData(mETContent);

                if (!Utils.isEmpty(content)) {
                    setNextBtnState(true);
                } else {
                    setNextBtnState(false);
                }

            }
        });

    }

    /**
     * 顶部布局
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
        Intent mIntent;
        switch (v.getId()) {
            // 提交
            case R.id.btn_login:
                // 媒体搜索
                if("0".equals(from)){
                    // 选择好的条件
                    ArrayList<SearchItemBean> itemBeans = new ArrayList<SearchItemBean>();
                    SearchItemBean bean = new SearchItemBean();
                    bean.locaion = " ";
                    bean.name = content;
                    bean.type = " ";
                    bean.voice = " ";

                    itemBeans.add(bean);

                    mIntent = new Intent(mContext, SearchResultListActivity.class);
                    mIntent.putExtra("searchtype", "其他");
                    mIntent.putParcelableArrayListExtra("searchKeys", itemBeans);
                    startActivity(mIntent);
                    finish();
                }
                // 服务搜素
                else if("1".equals(from)){
                    mIntent = new Intent(mContext, ADSListActivity.class);
                    mIntent.putExtra("title", "搜索服务");
                    mIntent.putExtra("type1", "其他");
                    mIntent.putExtra("type2", "");
                    mIntent.putExtra("sname", content);
                    startActivity(mIntent);
                }


                break;

            default:
                break;
        }
    }

    /**
     * 去除EditText的空格
     *
     * @param et
     * @return
     */
    public String getData(EditText et) {
        String s = et.getText().toString();
        return s.replaceAll(" ", "");
    }

    /**
     * 设置下一步按钮
     *
     * @param flag
     */
    private void setNextBtnState(boolean flag) {
        if (mBTLogin == null)
            return;
        mBTLogin.setEnabled(flag);
        mBTLogin.setClickable(flag);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            System.exit(0);
        }
        return super.onKeyDown(keyCode, event);
    }
}
