package com.topad.view.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.PoiKeywordSearchActivity;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.MyInfoBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.PictureUtil;
import com.topad.util.RecordMediaPlayer;
import com.topad.util.RecordTools;
import com.topad.util.SystemBarTintManager;
import com.topad.util.Utils;
import com.topad.view.customviews.PickDatePopwindow;
import com.topad.view.customviews.TitleView;
import com.topad.view.interfaces.IDatePick;
import com.topad.view.interfaces.IRecordFinish;

import java.util.ArrayList;
import java.util.List;

/**
 * 发布需求编辑界面
 */
public class ShareNeedsEditActivity_Peixun extends BaseActivity implements IRecordFinish, View.OnClickListener, IDatePick {

    /**
     * title布局
     **/
    private TitleView mTitle;
    MediaAdapter adapter;
    TextView data_pic;
    Context mContext;
    EditText et_title, et_detail, et_money,et_address;
    RelativeLayout mainlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public int setLayoutById() {
        mContext=this;
        return R.layout.activity_needs_edit_peixun;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    private void applySelectedColor() {
        int color = Color.argb(0, Color.red(0), Color.green(0), Color.blue(0));
        mTintManager.setTintColor(color);
    }

    /**
     * 沉浸式状态栏
     **/
    private SystemBarTintManager mTintManager;
    String type1, type2;
    @Override
    public void initViews() {
        mainlayout= (RelativeLayout) findViewById(R.id.mainlayout);
        et_title = (EditText) findViewById(R.id.et_title);
        et_detail = (EditText) findViewById(R.id.et_detail);
        et_money = (EditText) findViewById(R.id.et_money);
        et_address= (EditText) findViewById(R.id.et_address);
        type1=getIntent().getStringExtra("type1");
        type2=getIntent().getStringExtra("type2");
        mTintManager = new SystemBarTintManager(this);
        mTintManager.setStatusBarTintEnabled(true);
        mTintManager.setNavigationBarTintEnabled(true);
        applySelectedColor();
        // 顶部布局
        mTitle = (TitleView) findViewById(R.id.title);
        // 设置顶部布局
        mTitle.setTitle("发布需求");
        mTitle.setLeftClickListener(new TitleLeftOnClickListener());
        mTitle.setRightImageClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ShareNeedsEditActivity_Peixun.this, PoiKeywordSearchActivity.class);
                startActivity(intent);
            }
        }, R.drawable.bt_search);
        adapter = new MediaAdapter(this);
        data_pic = (TextView) findViewById(R.id.data_pic);
    }

    @Override
    public void initData() {

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            onBack();
        }
        return super.onKeyDown(keyCode, event);
    }
    /**
     * 提交需求
     */
    public void sumitNeeds() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_NEED_ADD).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("needtype", "2");//1：普通需求   2：培训需求   3：招聘需求
        rp.add("type1", type1);//
        rp.add("type2", type2);//
        rp.add("type3", "");//
        rp.add("title", et_title.getText().toString());//标题
        rp.add("detail", et_detail.getText().toString());//详情
        rp.add("recordfilename", "");// recordfilename 录音文件名
        rp.add("photolist", "");//photolist      图片文件名
        rp.add("budget", et_money.getText().toString());//budget 预算金额
        rp.add("ispay", "");//ispay  是否托管  0未托管，1已托管
        rp.add("enddate", data_pic.getText().toString());//enddate 项目结束时间 默认为7天后
        rp.add("needTName", "");//needTName 需要实名认证 1或0
        rp.add("needSafemoney", "");//needSafemoney 需要保证经
        rp.add("needFinis", "");//needFinis    保证完成
        rp.add("needSelf", "");//needSelf 保证原创
        rp.add("needRepair", "");//needRepair  保证维护
        rp.add("address", et_address.getText().toString());//培训地点
        rp.add("discuss", "");//薪金面议  0或者1

        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                ToastUtil.show(mContext, ((BaseBean) t).getMsg());
                finish();
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
    protected void onResume() {
        super.onResume();
    }

    final int PICKPHOTO = 1;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.carame:
                //使用startActivityForResult启动SelectPicPopupWindow当返回到此Activity的时候就会调用onActivityResult函数
                Intent intent1 = new Intent(ShareNeedsEditActivity_Peixun.this,
                        SelectPicPopupWindow.class);
                startActivityForResult(intent1, PICKPHOTO);
                break;
            case R.id.voice:
                RecordTools recordTools = new RecordTools(ShareNeedsEditActivity_Peixun.this, ShareNeedsEditActivity_Peixun.this);
                recordTools.showVoiceDialog();
                break;
            case R.id.data_pic:
                PickDatePopwindow datePick = new PickDatePopwindow(ShareNeedsEditActivity_Peixun.this);
                datePick.registeDatePick(this);
                datePick.showAtLocation(mainlayout,
                        Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btn_submit:
                sumitNeeds();
                break;

            default:
                break;
        }
    }

    @Override
    public void onBack() {
        finish();
    }

    /**
     * 录音完成
     *
     * @param voicePath
     */
    @Override
    public void RecondSuccess(String voicePath) {
        MeidaType meidaType = new MeidaType();
        meidaType.type = "2";
        meidaType.pathString = voicePath;
        meidaTypeList.add(meidaType);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void setDate(String dateString) {
        if (!Utils.isEmpty(dateString)) {
            data_pic.setText(dateString);
        }
    }

    /**
     * 设置底部布局
     */

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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PICKPHOTO:
                if (data != null) {
                    LogUtil.d("ouou", "#####path:" + data.getStringExtra("path"));
                    String picPath = data.getStringExtra("path");
                    if (!Utils.isEmpty(picPath)) {
                        Bitmap image = PictureUtil
                                .getSmallBitmap(picPath);
                        if (image != null) {
                            MeidaType meidaType = new MeidaType();
                            meidaType.type = "1";
                            meidaType.bitmap = image;
                            meidaType.pathString = picPath;
                            meidaTypeList.add(meidaType);
                            adapter.notifyDataSetChanged();
                        }
                    }
                }
                break;
            default:
                break;

        }
    }

//public void addpic(Bitmap bitmap){

    //
//
//}
    class MeidaType {
        /**
         * 1：图片  2：语音
         */
        String type;
        Bitmap bitmap;
        String pathString;
    }

    private List<MeidaType> meidaTypeList = new ArrayList<MeidaType>();

    //自定义适配器
    class MediaAdapter extends BaseAdapter {
        private LayoutInflater inflater;


        public MediaAdapter(Context context) {
            super();

            inflater = LayoutInflater.from(context);
        }

        @Override
        public int getCount() {
            if (null != meidaTypeList) {
                return meidaTypeList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return meidaTypeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.media_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.play = (ImageView) convertView.findViewById(R.id.play);
                viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            MeidaType meidaType = meidaTypeList.get(position);
            if (meidaType.type.equals("1")) {//图片
                viewHolder.play.setImageBitmap(meidaType.bitmap);
            } else {
                viewHolder.play.setImageResource(R.drawable.voice_play);
            }
            viewHolder.delete.setTag(meidaType.pathString);
            viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String tag = (String) v.getTag();
                    int index = -1;
                    MeidaType curType = null;
                    for (int i = 0; i < meidaTypeList.size(); i++) {
                        if (tag.equals(meidaTypeList.get(i).pathString)) {
                            curType = meidaTypeList.get(i);
                            index = i;

                            break;
                        }
                    }
                    if (curType != null && index >= 0 && curType.type.equals("2")) {
                        RecordMediaPlayer player = RecordMediaPlayer.getInstance();
                        player.deleteFile(curType.pathString);
                    }
                    meidaTypeList.remove(index);
                    adapter.notifyDataSetChanged();


                }
            });

            return convertView;
        }

    }

    class ViewHolder {
        public ImageView play;
        public ImageView delete;
    }
}


