package com.topad.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.MyInfoBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.PictureUtil;
import com.topad.util.UploadUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * ${todo}<上传照片>
 *
 * @author lht
 * @data: on 15/10/30 16:09
 */
public class MediaReoeaseUploadPicActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = MediaReoeaseUploadPicActivity.class.getSimpleName();
    // 上下文
    private Activity mContext;
    // 顶部布局
    private TitleView mTitleView;
    private ImageView pic_1, pic_2;
    private Button btn_save;
    private String pathString1, pathString2;
    private String img_name1, img_name2;
    private MyInfoBean.DataEntity myInfoBean;
    String picurl;
    // 判断是否公司认证
    String isCompany;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_upload_shenfenzheng;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        Intent intent = getIntent();
        picurl = intent.getStringExtra("picurl");
        isCompany = intent.getStringExtra("is_company");
        mTitleView = (TitleView) findViewById(R.id.title);
        pic_1 = (ImageView) findViewById(R.id.pic_1);
        pic_2 = (ImageView) findViewById(R.id.pic_2);

        pic_2.setVisibility(View.GONE);

        btn_save = (Button) findViewById(R.id.btn_save);
        pic_1.setOnClickListener(this);
        pic_2.setOnClickListener(this);
        btn_save.setOnClickListener(this);

//        myInfoBean = TopADApplication.getSelf().getMyInfo();
//        if (myInfoBean != null && !Utils.isEmpty(myInfoBean.getImglicense())) {
//            String headerpicUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + myInfoBean.getImglicense();
//            getPic(headerpicUrl, pic_1);
//        } else {
//            pic_1.setImageResource(R.drawable.uploadback);
//        }
        if (!Utils.isEmpty(picurl)) {
            String headerpicUrl = Constants.getCurrUrl() + Constants.IMAGE_URL_HEADER + picurl;
            getPic(headerpicUrl, pic_1);
            btn_save.setClickable(false);
            btn_save.setEnabled(false);
            pic_1.setClickable(false);
        } else {
            pic_1.setImageResource(R.drawable.uploadback);
        }


        int width = Utils.getScreenWidth(this);
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) pic_1.getLayoutParams();
        params.width = width * 2 / 3;
        params.height = params.width * 2 / 3;
    }


    public void getPic(String imageURL, ImageView imageView) {
        ImageLoader.getInstance().displayImage(imageURL, imageView, TopADApplication.getSelf().getImageLoaderOption(),
                new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
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
        mTitleView.setTitle("公司认证");
        mTitleView.setLeftVisiable(true);
        mTitleView.setRightVisiable(false);
        mTitleView.setLeftClickListener(new TitleRightOnClickListener());
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

    final int PICKPHOTO = 1;
    final int PICKPHOTO_2 = 2;

    @Override
    public void onClick(View v) {
        super.onClick(v);
        switch (v.getId()) {
            case R.id.pic_1://
                Intent intent1 = new Intent(mContext, SelectPicPopupWindow.class);
                startActivityForResult(intent1, PICKPHOTO);
                break;
            case R.id.pic_2://
                Intent intent2 = new Intent(mContext, SelectPicPopupWindow.class);
                startActivityForResult(intent2, PICKPHOTO_2);
                break;
            case R.id.btn_save://确认保存
                if (!Utils.isEmpty(pathString1)) {
                    uploadPic(pathString1);
                }
                break;

            default:
                break;
        }
    }

    public void uploadPic(final String picPath) {

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
                            if (Utils.isEmpty(img_name1)) {
                                img_name1 = img;
                            } else {
                                img_name2 = img;
                            }

                            if (!Utils.isEmpty(img_name1)) {
                                submit();
                            } else {//上传失败
                                if (!Utils.isEmpty(msg)) {
                                    Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(mContext, "图片上传失败", Toast.LENGTH_SHORT).show();
                                }

                            }
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
        }


    }

    public void submit() {
        // 拼接url
        StringBuffer sb = new StringBuffer();
        sb.append(Constants.getCurrUrl()).append(Constants.URL_UPDATE_IMG).append("?");
        String url = sb.toString();
        RequestParams rp = new RequestParams();
        rp.add("userid", TopADApplication.getSelf().getUserId());
        rp.add("token", TopADApplication.getSelf().getToken());
        rp.add("img1", img_name1);
        rp.add("img2", img_name2);
        rp.add("imgtype", "4");

        postWithLoading(url, rp, false, new HttpCallback() {
            @Override
            public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                BaseBean base = (BaseBean) t;
                if (base != null) {
                    ToastUtil.show(mContext, base.getMsg());
                    Intent intent;
                    if("1".equals(isCompany)){
                        intent = new Intent(MediaReoeaseUploadPicActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }else{
                        intent = new Intent(MediaReoeaseUploadPicActivity.this, MediaReleaseActivity.class);
                        intent.putExtra("mediacert", img_name1);
                        setResult(RESULT_OK, intent);
                        finish();
                    }

                }
            }

            @Override
            public void onFailure(BaseBean base) {
                int status = base.getStatus();// 状态码
                String msg = base.getMsg();// 错误信息
//                ToastUtil.show(mContext, msg);
            }
        }, BaseBean.class);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PICKPHOTO:
                if (data != null) {
                    LogUtil.d("ouou", "#####path:" + data.getStringExtra("path"));
                    String picPath = data.getStringExtra("path");
                    pathString1 = picPath;
                    if (!Utils.isEmpty(picPath)) {
                        Bitmap image = PictureUtil
                                .getSmallBitmap(picPath);
                        if (image != null) {
                            pic_1.setImageBitmap(image);
                        }
                    }
                }
                break;
            case PICKPHOTO_2:
                if (data != null) {
                    LogUtil.d("ouou", "#####path:" + data.getStringExtra("path"));
                    String picPath = data.getStringExtra("path");
                    pathString2 = picPath;
                    if (!Utils.isEmpty(picPath)) {
                        Bitmap image = PictureUtil
                                .getSmallBitmap(picPath);
                        if (image != null) {
                            pic_2.setImageBitmap(image);
                        }
                    }
                }
                break;
            default:
                break;

        }
    }


}
