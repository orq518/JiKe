package com.topad.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.topad.R;
import com.topad.UploadInterface;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.LoginBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.Md5;
import com.topad.util.PictureUtil;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.UploadUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.CircleImageView;
import com.topad.view.customviews.PickDatePopwindow;
import com.topad.view.customviews.TitleView;
import com.topad.view.interfaces.IDatePick;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * ${todo}<完善资料>
 *
 * @author ouruiqiang
 * @data: on 15/11/2 16:35
 */
public class CompleteInforActivity extends BaseActivity implements View.OnClickListener, IDatePick,UploadUtil.OnUploadProcessListener {
    private static final String LTAG = CompleteInforActivity.class.getSimpleName();
    /**
     * 上下文
     **/
    private Activity mContext;
    /**
     * 顶部布局
     **/
    private TitleView mTitleView;
    /**
     * 登录
     **/
    private Button mBTLogin;

    LinearLayout mainlayout;
    TextView tv_bithday;
    TextView gerenjianjie, xuanzezhiye, shenfenyanzheng;
    CircleImageView add_head_pic;
    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_complete_infor;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        add_head_pic= (CircleImageView) findViewById(R.id.add_head_pic);
        add_head_pic.setOnClickListener(this);
        mainlayout = (LinearLayout) findViewById(R.id.scroll_layout);
        mTitleView = (TitleView) findViewById(R.id.title);
        mBTLogin = (Button) findViewById(R.id.btn_login);
        tv_bithday = (TextView) findViewById(R.id.tv_bithday);
        tv_bithday.setOnClickListener(this);
        mBTLogin.setOnClickListener(this);
        gerenjianjie = (TextView) findViewById(R.id.gerenjianjie);
        xuanzezhiye = (TextView) findViewById(R.id.xuanzezhiye);
        shenfenyanzheng = (TextView) findViewById(R.id.shenfenyanzheng);
        gerenjianjie.setOnClickListener(this);
        xuanzezhiye.setOnClickListener(this);
        shenfenyanzheng.setOnClickListener(this);
        // 设置登录按钮
        setNextBtnState(false);
    }

    @Override
    public void initData() {
        showView();
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 设置顶部标题布局
        mTitleView.setTitle("完善资料");
        mTitleView.setLeftVisiable(true);
        mTitleView.setRightVisiable(false);
        mTitleView.setLeftClickListener(new TitleRightOnClickListener());
    }

    @Override
    public void setDate(String dateString) {//选择日期
        tv_bithday.setText(dateString);
    }



    /**
     * 顶部布局--注册按钮事件监听
     */
    public class TitleRightOnClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            finish();
        }

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case  R.id.add_head_pic://上传头像
                Intent intent1 = new Intent(mContext,
                        SelectPicPopupWindow.class);
                startActivityForResult(intent1, PICKPHOTO);
                break;
            case R.id.gerenjianjie://个人简介
                intent = new Intent(mContext, PersonalProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.shenfenyanzheng://身份验证
                intent = new Intent(mContext, IdentityAutherActivity.class);
                startActivity(intent);

                break;
            case R.id.xuanzezhiye://选择职业
                intent = new Intent(mContext, ShareNeedsActivity.class);
                intent.putExtra("from", "1");
                startActivity(intent);

                IntentFilter filter = new IntentFilter();
                filter.addAction(Constants.BroadCast_Action_GETZHIYE);
                registerReceiver(broadcastReceiver, filter);
                break;
            case R.id.btn_login://确认保存


                break;
            case R.id.tv_bithday://出生日期
                PickDatePopwindow datePick = new PickDatePopwindow(mContext);
                datePick.registeDatePick(this);
                datePick.showAtLocation(mainlayout,
                        Gravity.BOTTOM, 0, 0);

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
    protected void onDestroy() {
        super.onDestroy();
        try {
            unregisterReceiver(broadcastReceiver);
        } catch (Exception e) {
        }

    }
    final int PICKPHOTO = 1;
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
                            upLoadHeadPhoto(picPath);
                        }
                    }
                }
                break;
            default:
                break;

        }
    }

    public void upLoadHeadPhoto( String picPath){

        File file = new File(picPath);
        if(file!=null)
        {
            // 拼接url
            StringBuffer sb = new StringBuffer();
            sb.append(Constants.getCurrUrl()).append(Constants.UPLOAD_PHOTO).append("?");
            String url = sb.toString();
            String fileKey = "pic";
            UploadUtil uploadUtil = UploadUtil.getInstance();;
            uploadUtil.setOnUploadProcessListener(this); //设置监听器监听上传状态

            Map<String, String> params = new HashMap<String, String>();
            params.put("orderId", "11111");
            uploadUtil.uploadFile( picPath,fileKey, url,params);
//            UploadUtil.uploadFile(file, url, new UploadInterface() {
//                @Override
//                public void onSucceed(BaseBean bean) {
//
//                }
//
//                @Override
//                public void onFailed(BaseBean bean) {
//
//                }
//            });
        }


    }
    private final BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if (Constants.BroadCast_Action_GETZHIYE.equals(action)) {//取得职业
                String zhiyeString = intent.getStringExtra("zhiye");
                LogUtil.d("zhiyeString:"+zhiyeString);
                if(!Utils.isEmpty(zhiyeString)) {
                    xuanzezhiye.setText(zhiyeString);
                }
            }
        }
    };


    @Override
    public void onUploadDone(int responseCode, String message) {

    }

    @Override
    public void onUploadProcess(int uploadSize) {

    }

    @Override
    public void initUpload(int fileSize) {

    }
}
