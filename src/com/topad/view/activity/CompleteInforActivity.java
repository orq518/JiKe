package com.topad.view.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.LoginBean;
import com.topad.bean.MyInfoBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.ImageManager;
import com.topad.util.JSONUtils;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ${todo}<完善资料>
 *
 * @author ouruiqiang
 * @data: on 15/11/2 16:35
 */
public class CompleteInforActivity extends BaseActivity implements View.OnClickListener, IDatePick {
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
    EditText et_realname, et_address;
    RadioButton sex1, sex2;
    MyInfoBean.DataEntity myInfoBean;

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
        sex1 = (RadioButton) findViewById(R.id.sex1);
        sex2 = (RadioButton) findViewById(R.id.sex2);
        et_realname = (EditText) findViewById(R.id.et_realname);
        et_address = (EditText) findViewById(R.id.et_address);
        add_head_pic = (CircleImageView) findViewById(R.id.add_head_pic);
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
        myInfoBean = TopADApplication.getSelf().getMyInfo();
        if (myInfoBean != null) {
            mHandler.sendEmptyMessageDelayed(0, 500);
        } else {
            getMyInfo();
        }

    }

    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            initViewData();
        }
    };

    public void initViewData() {
        try {

            String name = myInfoBean.getNickname();
            String sex = myInfoBean.getSex();
            String address = myInfoBean.getAddress();
            String birthday = myInfoBean.getBirthday();

            if (!Utils.isEmpty(name)) {
                et_realname.setText(name);
            }
            if (("1").equals(sex)) {
                sex1.setChecked(true);
            } else {
                sex2.setChecked(true);
            }
            if (!Utils.isEmpty(address)) {
                et_address.setText(address);
            }
            if (!Utils.isEmpty(birthday)) {
                tv_bithday.setText(birthday);
            }
            if (!Utils.isEmpty(myInfoBean.getJob1()) && !Utils.isEmpty(myInfoBean.getJob2())) {
                xuanzezhiye.setText(myInfoBean.getJob1() + "-" + myInfoBean.getJob2());
            }
            if (!Utils.isEmpty(myInfoBean.getImghead())) {
                String headerpicUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + myInfoBean.getImghead();
                getHeaderPic(headerpicUrl);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
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

    /**
     * 获取个人资料
     */
    public void getMyInfo() {

        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.GETINFO).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                MyInfoBean base = (MyInfoBean) t;
                if (base != null) {
                    myInfoBean = base.getData();

                    initViewData();
//                    String status = respObj.getString("status");// 状态码
//                    String msg = respObj.getString("msg");// 错误信息
//                    String img = respObj.getString("img");// 图片名


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

    public void getHeaderPic(String imageURL) {
        ImageManager.getInstance(this).getBitmap(imageURL,
                new ImageManager.ImageCallBack() {
                    @Override
                    public void loadImage(ImageView imageView, Bitmap bitmap) {
                        if (bitmap != null && imageView != null) {
                            imageView.setImageBitmap(bitmap);
                            imageView
                                    .setScaleType(ImageView.ScaleType.FIT_XY);
                        }
                    }
                }, add_head_pic);
    }

    /**
     * 提交个人资料
     */
    public void uploadMyInfo() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.SAVEINFO).append("?");
        String url = sb.toString();

        String username = et_realname.getText().toString();
        String birthday = tv_bithday.getText().toString();
        String address = et_address.getText().toString();
        String sex = sex1.isChecked() ? "1" : "0";

        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("username", username);
        rp.add("birthday", birthday);
        rp.add("address", address);
        rp.add("sex", sex);
        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                BaseBean base = (BaseBean) t;
                if (base != null) {
                    ToastUtil.show(mContext, base.getMsg());
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

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Intent intent;
        switch (v.getId()) {
            case R.id.add_head_pic://上传头像
                Intent intent1 = new Intent(mContext,
                        SelectPicPopupWindow.class);
                startActivityForResult(intent1, PICKPHOTO);
                break;
            case R.id.gerenjianjie://个人简介
                intent = new Intent(mContext, PersonalProfileActivity.class);
                intent.putExtra("myintro", myInfoBean.getIntro());
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
                filter.addAction(Constants.BROADCAST_ACTION_GETZHIYE);
                registerReceiver(broadcastReceiver, filter);
                break;
            case R.id.btn_login://确认保存
                uploadMyInfo();

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
                        upLoadHeadPhoto(picPath);
                    }
                }
                break;
            default:
                break;

        }
    }

    public void upLoadHeadPhoto(final String picPath) {

        File file = new File(picPath);
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
                            if (!Utils.isEmpty(img)) {
                                // 拼接url
                                StringBuffer sb = new StringBuffer();
                                sb.append(Constants.getCurrUrl()).append(Constants.UPLOAD_HEAD).append("?");
                                String url = sb.toString();
                                RequestParams rp = new RequestParams();
                                rp.add("userid", TopADApplication.getSelf().getUserId());
                                rp.add("token", TopADApplication.getSelf().getToken());
                                rp.add("imghead", img);
                                postWithLoading(url, rp, false, new HttpCallback() {
                                    @Override
                                    public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                                        BaseBean base = (BaseBean) t;
                                        if (base != null) {
                                            Bitmap image = PictureUtil
                                                    .getSmallBitmap(picPath);
                                            if (image != null) {
                                                add_head_pic.setImageBitmap(image);
                                            }
                                            ToastUtil.show(mContext, base.getMsg());
                                        }
                                    }

                                    @Override
                                    public void onFailure(BaseBean base) {
                                        int status = base.getStatus();// 状态码
                                        String msg = base.getMsg();// 错误信息
                                        ToastUtil.show(mContext, msg);
                                    }
                                }, BaseBean.class);


                            } else {//上传失败
                                if (!Utils.isEmpty(msg)) {
                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "图片上传失败", Toast.LENGTH_SHORT).show();
                                }

                            }
//                            {"status":10000,"msg":"ok","img":"cf42431b2a053ab0692d00ac9b50c690.jpg"}
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

//
                    } else {//上传失败  服务器报错
                        Toast.makeText(mContext, "图片上传失败", Toast.LENGTH_SHORT).show();
                    }
                }

                int totalSize;

                @Override
                public void onUploadProcess(int uploadSize) {
//                    LogUtil.d("#uploadSize:" + uploadSize);
//                    if(totalSize!=0){
//                        double percent=uploadSize*100/totalSize;
//                        LogUtil.d("#percent:" + percent);
//                    }
                }

                @Override
                public void initUpload(int fileSize) {
//                    totalSize=fileSize;
//                    LogUtil.d("#fileSize:" + fileSize);
                }
            }); //设置监听器监听上传状态

            Map<String, String> params = new HashMap<String, String>();
            uploadUtil.uploadFile(picPath, fileKey, url, params);
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
            if (Constants.BROADCAST_ACTION_GETZHIYE.equals(action)) {//取得职业
                String zhiyeString = intent.getStringExtra("zhiye");
                LogUtil.d("zhiyeString:" + zhiyeString);
                if (!Utils.isEmpty(zhiyeString)) {
                    xuanzezhiye.setText(zhiyeString);
                }
            }
        }
    };


}
