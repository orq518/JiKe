package com.topad.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.BaseBean;
import com.topad.bean.CaseBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.ImageManager;
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
 * ${todo}<添加案例页>
 *
 * @author lht
 * @data: on 15/11/2 18:06
 */
public class AddCaseActivity extends BaseActivity implements View.OnClickListener{
    private static final String LTAG = AddCaseActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** 产品详情 **/
    private EditText mETDetails;
    /** 添加案例 **/
    private ImageView mIVAdd;
    /** 提交 **/
    private Button mBTAdd;

    /** 案例简介 **/
    private String intro;
    /** 案例实体 **/
    private CaseBean caseBean;
    final int PICKPHOTO = 1;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_add_case;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        mETDetails = (EditText) findViewById(R.id.et_case_details);
        mIVAdd = (ImageView) findViewById(R.id.iv_add_case);
        mBTAdd = (Button) findViewById(R.id.btn_add_case);

        mIVAdd.setOnClickListener(this);
        mBTAdd.setOnClickListener(this);
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
        mTitleView.setTitle("添加案例");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        setNextBtnState(false);

        // 产品详情
        mETDetails.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mETDetails);
                if (!Utils.isEmpty(data)) {
                    intro = data;
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
                String details = getData(mETDetails);

                if (!Utils.isEmpty(details) && details.length() > 0 ) {
                    setNextBtnState(true);
                } else {
                    setNextBtnState(false);
                }
            }
        });
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
        Intent intent;
        switch (v.getId()) {
            // 添加案例
            case R.id.iv_add_case:
                intent = new Intent(mContext,  SelectPicPopupWindow.class);
                startActivityForResult(intent, PICKPHOTO);
                break;

            // 提交
            case R.id.btn_add_case:
                if(!Utils.isEmpty(intro)){
                    caseBean.setIntro(intro);
                }
                intent = new Intent(AddCaseActivity.this, AddProductActivity.class );
                intent.putExtra("data", caseBean);
                setResult(RESULT_OK, intent);
                finish();
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
        if (mBTAdd == null)
            return;
        mBTAdd.setEnabled(flag);
        mBTAdd.setClickable(flag);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case PICKPHOTO:
                if (data != null) {
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

    /**
     * 上传案例图片
     * @param picPath
     */
    public void upLoadHeadPhoto(final String picPath) {
        File file = new File(picPath);
        if (file != null) {
            // 拼接url
            StringBuffer sb = new StringBuffer();
            sb.append(Constants.getCurrUrl()).append(Constants.URL_ADD_PHOTO).append("?");
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
                                caseBean = new CaseBean();
                                caseBean.setPicPath(img);

                                Bitmap image = PictureUtil.getSmallBitmap(picPath);
                                if(image != null){
                                    mIVAdd.setImageBitmap(image);
                                }
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
}
