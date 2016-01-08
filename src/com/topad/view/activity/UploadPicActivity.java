package com.topad.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.topad.R;
import com.topad.util.LogUtil;
import com.topad.util.PictureUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.TitleView;

/**
 * ${todo}<上传照片>
 *
 * @author ouruiqiang
 * @data: on 15/11/2 16:35
 */
public class UploadPicActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = UploadPicActivity.class.getSimpleName();
    /**
     * 上下文
     **/
    private Activity mContext;
    /**
     * 顶部布局
     **/
    private TitleView mTitleView;
    ImageView pic_1,pic_2;
    Button btn_save;
    String title;
    /**
     * 0：上传身份证  1：上传毕业证  2：上传名片  3，上传公司认证
     */
    int type;
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
        Intent intent=getIntent();
        title=intent.getStringExtra("title");
        type=intent.getIntExtra("type",0);
        mTitleView = (TitleView) findViewById(R.id.title);
        pic_1=(ImageView) findViewById(R.id.pic_1);
        pic_2=(ImageView) findViewById(R.id.pic_2);
        if(type!=0){
            pic_2.setVisibility(View.GONE);
        }
        btn_save=(Button) findViewById(R.id.btn_save);
        pic_1.setOnClickListener(this);
        pic_2.setOnClickListener(this);
        btn_save.setOnClickListener(this);
        switch(type){
            case 0:
                break;
            case 1:
                pic_1.setImageResource(R.drawable.upload_biyezheng);
                break;
            case 2:
                pic_1.setImageResource(R.drawable.upload_mingpian);
                break;
            case 3:
                pic_1.setImageResource(R.drawable.uploadback);
                break;
        }
        int width=Utils.getScreenWidth(this);
        LinearLayout.LayoutParams params= (LinearLayout.LayoutParams) pic_1.getLayoutParams();
        params.width=width*2/3;
        params.height=params.width*2/3;
        if(type==0) {
            LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) pic_2.getLayoutParams();
            params2.width = width * 2 / 3;
            params2.height = params2.width * 2 / 3;
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
        mTitleView.setTitle(title);
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

                Intent intent1 = new Intent(mContext,
                        SelectPicPopupWindow.class);
                startActivityForResult(intent1, PICKPHOTO);
                break;
            case R.id.pic_2://

                Intent intent2 = new Intent(mContext,
                        SelectPicPopupWindow.class);
                startActivityForResult(intent2, PICKPHOTO_2);
                break;
            case R.id.btn_save://确认保存


                break;

            default:
                break;
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
                            pic_1.setImageBitmap(image);
                        }
                    }
                }
                break;
            case PICKPHOTO_2:
                if (data != null) {
                    LogUtil.d("ouou", "#####path:" + data.getStringExtra("path"));
                    String picPath = data.getStringExtra("path");
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
