package com.topad.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.AddProductBean;
import com.topad.bean.BaseBean;
import com.topad.bean.ReleaseMediaBean;
import com.topad.bean.SearchListBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.RecordMediaPlayer;
import com.topad.util.RecordTools;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;
import com.topad.view.interfaces.IRecordFinish;

/**
 * ${todo}<媒体发布>
 *     category＝ 电视－1、广播－2、报纸－3、户外－4、杂志－5、网络－6
 * @author lht
 * @data: on 15/10/30 16:09
 */
public class MediaReleaseActivity extends BaseActivity implements OnClickListener, IRecordFinish {
    private static final String LTAG = MediaReleaseActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** 选择媒体类别 **/
    private RelativeLayout mLaySelectMedia;
    /** 地址 **/
    private RelativeLayout mLayAddressMedia;
    /** 地址 **/
    private RelativeLayout mLayProveMedia;
    /** 语音 **/
    private LinearLayout mLayVoice;
    /** 键盘 **/
    private LinearLayout mLayKeyboard;
    /** 语音播放 **/
    private LinearLayout mVoiceLayout;
    /** 媒体名 **/
    private EditText mETMediaName;
    /** 栏目 **/
    private EditText mETColumn;
    /** 详情 **/
    private EditText mETDetails;
    /** 添加 **/
    private ImageView mETAdd;
    /** 语音 **/
    private ImageView mIVVoice;
    /** 键盘 **/
    private ImageView mIVKeyboard;
    /** 提交 **/
    private Button mBTSubmit;
    /** 提交并继续 **/
    private Button mBTAdd;
    /** 录音 **/
    private Button mRecord;
    /** 媒体 **/
    private TextView mMedia;
    /** 地址 **/
    private TextView mAddress;

    /** 类别 **/
    private String category;
    /** 媒体名称 **/
    private String mediaName;
    /** 栏目名称 **/
    private String subName;
    /** lat **/
    private double lat;
    /** lon **/
    private double lon;
    /** 类别 **/
    private String[] categoryArray ={"电视","广播","报纸","户外","杂志","网络"};
    /** 媒体代理证明上传文件 **/
    private String mediacert;
    public static int SELECT_MEDIA = 1;
    public static int SELECT_ADDRESS= 2;
    public static int SELECT_UP_PIC= 3;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_media_release;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            category = intent.getStringExtra("category");
        }

        // 顶部标题布局
        mTitleView = (TitleView) findViewById(R.id.title);
        mLaySelectMedia = (RelativeLayout) findViewById(R.id.lay_select_media);
        mLayAddressMedia = (RelativeLayout) findViewById(R.id.lay_address_media);
        mLayProveMedia = (RelativeLayout) findViewById(R.id.lay_prove_media);
        mLayVoice = (LinearLayout) findViewById(R.id.layout_voice);
        mLayKeyboard = (LinearLayout) findViewById(R.id.layout_keyboard);
        mVoiceLayout = (LinearLayout) findViewById(R.id.voice_layout);

        mETMediaName = (EditText) findViewById(R.id.et_media_name);
        mETColumn = (EditText) findViewById(R.id.et_column);
        mETDetails = (EditText) findViewById(R.id.et_details);
        mETAdd = (ImageView) findViewById(R.id.iv_add);
        mIVVoice = (ImageView) findViewById(R.id.ic_voice);
        mIVKeyboard = (ImageView) findViewById(R.id.ic_keyboard);
        mBTSubmit = (Button) findViewById(R.id.bt_submit_release);
        mBTAdd = (Button) findViewById(R.id.bt_add);
        mRecord = (Button) findViewById(R.id.record_bt);
        mMedia = (TextView) findViewById(R.id.tv_select_media_newspaper);
        mAddress = (TextView) findViewById(R.id.tv_select_media_address);

        if(!Utils.isEmpty(category)){
            if(category.equals("1")){
                mTitleView.setTitle("电视媒体发布");
                mETMediaName.setHint("电视台名称：例如 CCTV1");
                mETColumn.setVisibility(View.VISIBLE);
                mETColumn.setHint("具体栏目：中国新闻");
            }else if(category.equals("2")){
                mTitleView.setTitle("广播媒体发布");
                mETMediaName.setHint("广播电台名称：例如 中央人民广播电台");
                mETColumn.setVisibility(View.VISIBLE);
                mETColumn.setHint("具体栏目：中国之声");
            }else if(category.equals("3")){
                mTitleView.setTitle("报纸媒体发布");
                mETMediaName.setHint("报纸名称：例如 人民日报");
                mETColumn.setVisibility(View.GONE);
            }else if(category.equals("4")){
                mTitleView.setTitle("户外媒体发布");
                mETMediaName.setHint("媒体名称：例如 首都机场 T3 航站楼");
                mETColumn.setVisibility(View.GONE);
            }else if(category.equals("5")){
                mTitleView.setTitle("杂志媒体发布");
                mETMediaName.setHint("杂志名称：例如 时尚");
                mETColumn.setVisibility(View.GONE);
            }else if(category.equals("6")){
                mTitleView.setTitle("网络媒体发布");
                mETMediaName.setHint("网媒名称：例如 爱奇艺");
                mETColumn.setVisibility(View.GONE);
            }
        }
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());


        mLayVoice.setOnClickListener(this);
        mLayKeyboard.setOnClickListener(this);
        mLaySelectMedia.setOnClickListener(this);
        mLayAddressMedia.setOnClickListener(this);
        mLayProveMedia.setOnClickListener(this);
        mETAdd.setOnClickListener(this);
        mBTSubmit.setOnClickListener(this);
        mBTSubmit.setOnClickListener(this);
        mIVVoice.setOnClickListener(this);
        mIVKeyboard.setOnClickListener(this);
        mRecord.setOnClickListener(this);

        // 媒体名称
        mETMediaName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mETMediaName);
                if (!Utils.isEmpty(data)) {
                    mediaName = data;
                    String subName = getData(mETMediaName);
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
                String name = getData(mETMediaName);

                if(category.equals("1") || category.equals("2")){
                    String subName = getData(mETColumn);
                    if (!Utils.isEmpty(name)
                            && !Utils.isEmpty(subName)
                            && !Utils.isEmpty(mMedia.getText().toString())
                            && !Utils.isEmpty(mAddress.getText().toString())) {
                        setNextBtnState(true);
                    } else {
                        setNextBtnState(false);
                    }
                }else{
                    if (!Utils.isEmpty(name)
                            && !Utils.isEmpty(mMedia.getText().toString())
                            && !Utils.isEmpty(mAddress.getText().toString())) {
                        setNextBtnState(true);
                    } else {
                        setNextBtnState(false);
                    }
                }
            }
        });

        // lanmu名称
        mETColumn.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mETColumn);
                if (!Utils.isEmpty(data)) {
                    subName = data;
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
                String name = getData(mETMediaName);
                String subName = getData(mETColumn);
                if(category.equals("1") || category.equals("2")){
                    if (!Utils.isEmpty(name)
                            && !Utils.isEmpty(subName)
                            && !Utils.isEmpty(mMedia.getText().toString())
                            && !Utils.isEmpty(mAddress.getText().toString())) {
                        setNextBtnState(true);
                    } else {
                        setNextBtnState(false);
                    }
                }else{
                    if (!Utils.isEmpty(name)
                            && !Utils.isEmpty(mMedia.getText().toString())
                            && !Utils.isEmpty(mAddress.getText().toString())) {
                        setNextBtnState(true);
                    } else {
                        setNextBtnState(false);
                    }
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    /**
     * 录音成功
     * @param voicePath
     */
    @Override
    public void RecondSuccess(String voicePath) {
        final RelativeLayout voiceLayout = (RelativeLayout) getLayoutInflater().inflate(R.layout.media_layout, null);
        voiceLayout.setTag(voicePath);
        ImageView play= (ImageView) voiceLayout.findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RecordMediaPlayer player = RecordMediaPlayer.getInstance();
                player.play((String) voiceLayout.getTag());
            }
        });
        ImageView delete= (ImageView) voiceLayout.findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        mVoiceLayout.addView(voiceLayout);
        mLayKeyboard.setVisibility(View.GONE);
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            // 选择媒体类别
            case R.id.lay_select_media:
                Intent intents = new Intent(MediaReleaseActivity.this, SelectMediaListActivity.class);
                intents.putExtra("category", category);
                startActivityForResult(intents, SELECT_MEDIA);

                IntentFilter filter = new IntentFilter();
                filter.addAction(Constants.BROADCAST_ACTION_MEDIA_CLASS);
                registerReceiver(broadcastReceiver, filter);

                break;

            // 地址-- 定位进入地图
            case R.id.lay_address_media:
                Intent intent = new Intent(MediaReleaseActivity.this, LocationMapActivity.class);
                startActivityForResult(intent, SELECT_ADDRESS);
                break;

            // 键盘
            case R.id.ic_keyboard:
                mLayKeyboard.setVisibility(View.GONE);
                mLayVoice.setVisibility(View.VISIBLE);
                mRecord.setText("按住说话");
                break;

            // 证明
            case R.id.lay_prove_media:
                intent = new Intent(mContext, MediaReoeaseUploadPicActivity.class);
                startActivityForResult(intent, SELECT_UP_PIC);
                break;

            // 语音
            case R.id.ic_voice:
                mLayVoice.setVisibility(View.GONE);
                mLayKeyboard.setVisibility(View.VISIBLE);
                break;

            // 录音
            case R.id.record_bt:
                RecordTools recordTools = new RecordTools(mContext, MediaReleaseActivity.this);
                recordTools.showVoiceDialog();
                break;

            // 添加
            case R.id.iv_add:

                break;

            // 提交
            case R.id.bt_submit_release:
                submit("0");
                break;

            // 提交并继续
            case R.id.bt_add:
                submit("1");
                break;

            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_MEDIA && resultCode == RESULT_OK && data != null) {
            // 媒体类型
            Bundle MarsBuddle = data.getExtras();
            String MarsMessage = MarsBuddle.getString( "mediaName");
            mMedia.setVisibility(View.VISIBLE);
            mMedia.setText(MarsMessage);
        }else if(requestCode == SELECT_ADDRESS && resultCode == RESULT_OK && data != null){
            // 地址
            Bundle MarsBuddle = data.getExtras();
            String MarsMessage = MarsBuddle.getString("location");
            lon = MarsBuddle.getDouble("lon");
            lat = MarsBuddle.getDouble("lat");
            mAddress.setVisibility(View.VISIBLE);
            mAddress.setText(MarsMessage);
        }else if(requestCode == SELECT_UP_PIC && resultCode == RESULT_OK && data != null){
            // 地址
            Bundle MarsBuddle = data.getExtras();
            mediacert = MarsBuddle.getString("mediacert");
        }

        super.onActivityResult(requestCode, resultCode, data);
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
        if (mBTAdd == null)
            return;
        mBTAdd.setEnabled(flag);
        mBTAdd.setClickable(flag);
        mBTSubmit.setEnabled(flag);
        mBTSubmit.setClickable(flag);
    }

    /**
     * 提交
     *
     * @return
     */
    public void submit(final String type) {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_MEDIA_ADD).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("type1", categoryArray[Integer.parseInt(category) - 1]); // 1-6
        rp.add("type2", mMedia.getText().toString()); // 传中文名
        rp.add("type3", "");
        rp.add("medianame", mediaName);
        if(!Utils.isEmpty(category)){
            if(category.equals("1") || category.equals("2")){
                rp.add("subname", subName);
            }else{
                rp.add("subname", "");
            }
        }

        rp.add("location", mAddress.getText().toString());
        rp.add("longitude", lat + "");
        rp.add("latitude", lon + "");
        rp.add("mediacert", mediacert);
        rp.add("token", TopADApplication.getSelf().getToken());

        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                ReleaseMediaBean bean = (ReleaseMediaBean) t;
                if (bean != null) {
                    if("0".equals(type)){
                        finish();
                    }else{
                        mMedia.setText("");
                        mAddress.setText("");
                        mediaName = "";
                        subName = "";
                        lat = 0.0;
                        lon = 0.0;
                        mMedia.setText("");
                    }
                }
            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息

                LogUtil.d(LTAG, "status = " + status + "\n" + "msg = " + msg);
                ToastUtil.show(mContext, msg);
            }
        }, ReleaseMediaBean.class, true);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        }

    }

    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (Constants.BROADCAST_ACTION_MEDIA_CLASS.equals(action)) { //我的类别
                String str = intent.getStringExtra("media_class");
                if (!Utils.isEmpty(str) ) {
                    // 媒体类型
                    mMedia.setVisibility(View.VISIBLE);
                    mMedia.setText(str);
                }
            }
        }
    };
}
