package com.topad.view.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.topad.R;
import com.topad.TopADApplication;
import com.topad.amap.ToastUtil;
import com.topad.bean.AdDetailsBean;
import com.topad.bean.AdProductBean;
import com.topad.bean.AdServiceCaseListBean;
import com.topad.bean.AdServiceDetailsBean;
import com.topad.bean.AddCaseBean;
import com.topad.bean.AddProductBean;
import com.topad.bean.BaseBean;
import com.topad.bean.CaseBean;
import com.topad.net.HttpCallback;
import com.topad.net.http.RequestParams;
import com.topad.util.Constants;
import com.topad.util.ImageManager;
import com.topad.util.LogUtil;
import com.topad.util.PictureUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.MyGridviewCase;
import com.topad.view.customviews.TitleView;

import java.util.ArrayList;
import java.util.List;

/**
 * ${todo}<添加/编辑产品页>
 *
 * @author lht
 * @data: on 15/11/2 18:06
 */
public class AddProductActivity extends BaseActivity implements View.OnClickListener {
    private static final String LTAG = AddProductActivity.class.getSimpleName();
    /** 上下文 **/
    private Context mContext;
    /** 顶部布局 **/
    private TitleView mTitleView;
    /** 产品类别 **/
    private LinearLayout mLYClass;
    /** 产品类别 **/
    private TextView mTVClass;
    /** 产品名称 **/
    private EditText mETName;
    /** 产品价格 **/
    private EditText mETOffer;
    /** 产品详情 **/
    private EditText mETDetails;
    /** 提交 **/
    private Button mBTAdd;
    /** 职业分类1 **/
    private String type1;
    /** 职业分类2 **/
    private String type2;
    /** 产品名称 **/
    private String servicename;
    /** 出售价格 **/
    private String price;
    /** 产品简介 **/
    private String intro;
    /** 数据源 **/
    private ArrayList<CaseBean> caseList = new ArrayList<CaseBean>();

    final int CASE = 1;
    /** 添加案例 **/
    private MyGridviewCase mAddDetailGridview;
    /** Adapter **/
    private MediaAdapter adapter;
    /** 案例图片数据元 **/
    private List<CaseType> caseTypeList = new ArrayList<CaseType>();
    /** 编辑-产品详情数据元 **/
    private AdDetailsBean mAdDetailsBean;
    /** 编辑-产品案例数据元 **/
    private AdServiceCaseListBean mAdCaseListBean;

    /** 来源 1-编辑，2-添加 **/
    private String from;

    @Override
    public int setLayoutById() {
        mContext = this;
        return R.layout.activity_add_product;
    }

    @Override
    public View setLayoutByView() {
        return null;
    }

    @Override
    public void initViews() {
        mTitleView = (TitleView) findViewById(R.id.title);
        mLYClass = (LinearLayout) findViewById(R.id.ly_product_class);
        mTVClass = (TextView) findViewById(R.id.tv_product_class);
        mETName = (EditText) findViewById(R.id.et_product_name);
        mETOffer = (EditText) findViewById(R.id.et_product_offer);
        mETDetails = (EditText) findViewById(R.id.et_product_details);
        mBTAdd = (Button) findViewById(R.id.btn_add);
        mAddDetailGridview = (MyGridviewCase) findViewById(R.id.add_case_gridview);

        mLYClass.setOnClickListener(this);
        mBTAdd.setOnClickListener(this);
    }

    @Override
    public void initData() {
        // 接收数据
        Intent intent = getIntent();
        if (intent != null) {
            mAdDetailsBean = (AdDetailsBean) intent.getSerializableExtra("data_details");
            mAdCaseListBean = (AdServiceCaseListBean) intent.getSerializableExtra("data_case");
            from = intent.getStringExtra("from");

        } else {
            LogUtil.d(LTAG + "--" + "intent is null!");
        }

        showView();
    }

    /**
     * 显示数据
     */
    private void showView() {
        // 编辑
        if ("1".equals(from)) {
            if (!Utils.isEmpty(mAdDetailsBean.getServicename())) {
                mETName.setText(mAdDetailsBean.getServicename());
            }

            if (!Utils.isEmpty(mAdDetailsBean.getPrice())) {
                mETOffer.setText(mAdDetailsBean.getPrice());
            }

            if (!Utils.isEmpty(mAdDetailsBean.getIntro())) {
                mETDetails.setText(mAdDetailsBean.getIntro());
            }

            if (!Utils.isEmpty(mAdDetailsBean.getType1()) &&
                    !Utils.isEmpty(mAdDetailsBean.getType2())) {
                mTVClass.setText(mAdDetailsBean.getType1() + "-" + mAdDetailsBean.getType2());
                mTVClass.setVisibility(View.VISIBLE);
            }

            for(int i=0; i<mAdCaseListBean.data.size(); i++){
                CaseType meidaType = new CaseType();
                meidaType.type = "1";
                meidaType.id = mAdCaseListBean.data.get(i).getId();
                if(!Utils.isEmpty(mAdCaseListBean.data.get(i).getImgs())){
                    String[] aa = mAdCaseListBean.data.get(i).getImgs().split("\\|");
                    if(aa.length > 0){
                        meidaType.picPath = aa[0];
                    }
                }
                caseTypeList.add(0, meidaType);
            }
        } else {

        }

        // 设置顶部标题布局
        mTitleView.setTitle("我的服务产品设计方案");
        mTitleView.setLeftClickListener(new TitleLeftOnClickListener());

        if (!Utils.isEmpty(mETName.getText().toString())
                && !Utils.isEmpty(mETOffer.getText().toString())
                && !Utils.isEmpty(mETDetails.getText().toString())
                && !Utils.isEmpty(mTVClass.getText().toString())) {
            setNextBtnState(true);
        } else {
            setNextBtnState(false);
        }

        // 产品名称
        mETName.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mETName);
                if (!Utils.isEmpty(data)) {
                    servicename = data;
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
                String name = getData(mETName);
                String offer = getData(mETOffer);
                String details = getData(mETDetails);

                if (!Utils.isEmpty(name) && name.length() > 0
                        && !Utils.isEmpty(offer) && offer.length() > 0
                        && !Utils.isEmpty(details) && details.length() > 0
                        && !Utils.isEmpty(mTVClass.getText().toString())) {
                    setNextBtnState(true);
                } else {
                    setNextBtnState(false);
                }
            }
        });

        // 产品价格
        mETOffer.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String data = getData(mETOffer);
                if (!Utils.isEmpty(data)) {
                    price = data;
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
                String name = getData(mETName);
                String offer = getData(mETOffer);
                String details = getData(mETDetails);

                if (!Utils.isEmpty(name) && name.length() > 0
                        && !Utils.isEmpty(offer) && offer.length() > 0
                        && !Utils.isEmpty(details) && details.length() > 0
                        && !Utils.isEmpty(mTVClass.getText().toString())) {
                    setNextBtnState(true);
                } else {
                    setNextBtnState(false);
                }
            }
        });

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
                String name = getData(mETName);
                String offer = getData(mETOffer);
                String details = getData(mETDetails);

                if (!Utils.isEmpty(name) && name.length() > 0
                        && !Utils.isEmpty(offer) && offer.length() > 0
                        && !Utils.isEmpty(details) && details.length() > 0
                        && !Utils.isEmpty(mTVClass.getText().toString())) {
                    setNextBtnState(true);
                } else {
                    setNextBtnState(false);
                }
            }
        });

        adapter = new MediaAdapter(this);
        initPicData();
        mAddDetailGridview.setAdapter(adapter);
        mAddDetailGridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                CaseType meidaType = (CaseType) adapter.getItem(position);
                if (meidaType.type.equals("1")) {//图片

                } else if (meidaType.type.equals("2")) {//添加案例
                    Intent intent = new Intent(mContext, AddCaseActivity.class);
                    startActivityForResult(intent, CASE);
                }
            }
        });

        mAddDetailGridview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                CaseType meidaType = (CaseType) adapter.getItem(position);
                meidaType.isShowDeleteed = true;
                adapter.notifyDataSetChanged();
                return true;
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
            // 产品类别
            case R.id.ly_product_class:
                intent = new Intent(mContext, ProductClassListActivity.class);
                startActivity(intent);

                IntentFilter filter = new IntentFilter();
                filter.addAction(Constants.BROADCAST_ACTION_PRODUCT_CLASS);
                registerReceiver(broadcastReceiver, filter);
                break;

            // 提交
            case R.id.btn_add:
                submit();
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
            if (Constants.BROADCAST_ACTION_PRODUCT_CLASS.equals(action)) { //我的产品类别
                String str = intent.getStringExtra("media_class");
                String typea = intent.getStringExtra("type1");
                String typeb = intent.getStringExtra("type2");
                if (!Utils.isEmpty(str) && !Utils.isEmpty(typea) && !Utils.isEmpty(typeb)) {
                    // 媒体类型
                    mTVClass.setVisibility(View.VISIBLE);
                    mTVClass.setText(str);

                    type1 = typea;
                    type2 = typeb;
                }
            }
        }
    };

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        switch (requestCode) {
            case CASE:
                if (data != null) {
                    Bundle buddle = data.getExtras();
                    CaseBean caseBean = (CaseBean) buddle.getSerializable("data");
                    caseList.add(caseBean);

                    CaseType meidaType = new CaseType();
                    meidaType.type = "1";
                    Bitmap image = PictureUtil.getSmallBitmap(caseBean.getPicPaths().get(0));
                    meidaType.image = image;
                    meidaType.picPath = caseBean.getImgs().get(0);
                    caseTypeList.add(0, meidaType);
                    adapter.notifyDataSetChanged();
                }
                break;
            default:
                break;

        }
    }

    /**
     * 提交
     *
     * @return
     */
    public void submit() {
        // 编辑
        if ("1".equals(from)) {
            // 拼接url
            StringBuffer sb = new StringBuffer();
            sb.append(Constants.getCurrUrl()).append(Constants.URL_EDIT_SERVICE).append("?");
            String url = sb.toString();
            RequestParams rp = new RequestParams();
            rp.add("userid", TopADApplication.getSelf().getUserId());
            rp.add("type1", type1);
            rp.add("type2", type2);
            rp.add("servicename", servicename);
            rp.add("price", price);
            rp.add("intro", intro);
            rp.add("token", TopADApplication.getSelf().getToken());

            postWithLoading(url, rp, false, new HttpCallback() {
                @Override
                public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                    AddProductBean bean = (AddProductBean) t;
                    if (bean != null && !Utils.isEmpty(bean.getServiceid())
                            && caseList != null && caseList.size() > 1) {
                        addCase(bean.getServiceid());
                    }
                }

                @Override
                public void onFailure(BaseBean base) {
                    int status = base.getStatus();// 状态码
                    String msg = base.getMsg();// 错误信息

                    LogUtil.d(LTAG, "status = " + status + "\n" + "msg = " + msg);
                    ToastUtil.show(mContext, msg);
                }
            }, AddProductBean.class, true);

        }
        // 添加
        else {
            // 拼接url
            StringBuffer sb = new StringBuffer();
            sb.append(Constants.getCurrUrl()).append(Constants.URL_ADD_PRODUCT).append("?");
            String url = sb.toString();
            RequestParams rp = new RequestParams();
            rp.add("userid", TopADApplication.getSelf().getUserId());
            rp.add("type1", type1);
            rp.add("type2", type2);
            rp.add("servicename", servicename);
            rp.add("price", price);
            rp.add("intro", intro);
            rp.add("token", TopADApplication.getSelf().getToken());

            postWithLoading(url, rp, false, new HttpCallback() {
                @Override
                public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                    AddProductBean bean = (AddProductBean) t;
                    if (bean != null && !Utils.isEmpty(bean.getServiceid())
                            && caseList != null && caseList.size() > 1) {
                        addCase(bean.getServiceid());
                    }
                }

                @Override
                public void onFailure(BaseBean base) {
                    int status = base.getStatus();// 状态码
                    String msg = base.getMsg();// 错误信息

                    LogUtil.d(LTAG, "status = " + status + "\n" + "msg = " + msg);
                    ToastUtil.show(mContext, msg);
                }
            }, AddProductBean.class, true);

        }

    }

    /**
     * 提交案例
     *
     * @return
     */
    public void addCase(String serviceid) {
        for (int i = 0; i < caseList.size(); i++) {
            // 拼接url
            StringBuffer sb = new StringBuffer();
            sb.append(Constants.getCurrUrl()).append(Constants.URL_ADD_CASE).append("?");
            String url = sb.toString();
            RequestParams rp = new RequestParams();
            rp.add("userid", TopADApplication.getSelf().getUserId());
            rp.add("serviceid", serviceid);

            StringBuffer img = new StringBuffer();
            for (int j = 0; j < caseList.get(i).getImgs().size(); j++) {
                if (j >= 1 && j < caseList.get(i).getImgs().size() - 1) {
                    img.append("|" + caseList.get(i).getImgs().get(i));
                } else {
                    img.append(caseList.get(i).getImgs().get(i));
                }

            }
            rp.add("imgs", img.toString());
            rp.add("intro", caseList.get(i).getIntro());
            rp.add("token", TopADApplication.getSelf().getToken());

            postWithLoading(url, rp, false, new HttpCallback() {
                @Override
                public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                    AddCaseBean bean = (AddCaseBean) t;
                    if (bean != null && !Utils.isEmpty(bean.getCaseid())) {
                        finish();
                    }
                }

                @Override
                public void onFailure(BaseBean base) {
                    int status = base.getStatus();// 状态码
                    String msg = base.getMsg();// 错误信息

                    LogUtil.d(LTAG, "status = " + status + "\n" + "msg = " + msg);
                    ToastUtil.show(mContext, msg);
                }
            }, AddCaseBean.class, true);
        }
    }


    /**
     * 自定义适配器
     */
    class MediaAdapter extends BaseAdapter {
        private LayoutInflater inflater;
        int width;


        public MediaAdapter(Context context) {
            super();
            inflater = LayoutInflater.from(context);
            int[] screenSize = Utils.getScreenDispaly(mContext);
            width = screenSize[0];
        }

        public void setData(List<CaseType> meidaTypeList) {
            meidaTypeList.clear();
            meidaTypeList.addAll(meidaTypeList);
            notifyDataSetChanged();
        }

        public List<CaseType> getData() {
            return caseTypeList;
        }

        @Override
        public int getCount() {
            if (null != caseTypeList) {
                return caseTypeList.size();
            } else {
                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return caseTypeList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.case_layout, null);
                viewHolder = new ViewHolder();
                viewHolder.play = (ImageView) convertView.findViewById(R.id.play);
                viewHolder.delete = (ImageView) convertView.findViewById(R.id.delete);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            CaseType caseType = caseTypeList.get(position);
            if (caseType.type.equals("1")) { // 图片
                if(caseType.image == null){
                    String picUrl = Constants.getCurrUrl() + Constants.CASE_IMAGE_URL_HEADER + caseType.picPath;

                    ImageManager.getInstance(mContext).getBitmap(picUrl,
                            new ImageManager.ImageCallBack() {
                                @Override
                                public void loadImage(ImageView imageView, Bitmap bitmap) {
                                    if (bitmap != null && imageView != null) {
                                        imageView.setImageBitmap(bitmap);
                                        imageView
                                                .setScaleType(ImageView.ScaleType.FIT_XY);
                                    }
                                }
                            }, viewHolder.play);
                }else{
                    viewHolder.play.setImageBitmap(caseType.image);
                }

                viewHolder.play.setScaleType(ImageView.ScaleType.FIT_XY);
            } else if (caseType.type.equals("2")) { //添加图片
                viewHolder.play.setImageResource(R.drawable.pic_add_item);
                viewHolder.play.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            }

            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) viewHolder.play.getLayoutParams();
            LogUtil.d("##params.width:" + params.width);
            params.width = (width - 100) / 4;
            params.height = (width - 100) / 4;
            if (caseType.isShowDeleteed) {
                viewHolder.delete.setVisibility(View.VISIBLE);
                viewHolder.delete.setTag(caseType.picPath);

                viewHolder.delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        // 删除案例
                        if(!Utils.isEmpty(caseTypeList.get(position).id)){
                            StringBuffer sb = new StringBuffer();
                            sb.append(Constants.getCurrUrl()).append(Constants.URL_DEL_CASE).append("?");
                            String url = sb.toString();
                            RequestParams rp=new RequestParams();
                            rp.add("userid", TopADApplication.getSelf().getUserId());
                            rp.add("caseid", caseTypeList.get(position).id);
                            rp.add("token", TopADApplication.getSelf().getToken());

                            postWithLoading(url, rp, false, new HttpCallback() {
                                @Override
                                public <T> void onModel(int respStatusCode, String respErrorMsg, T t) {
                                    String tag = (String) v.getTag();
                                    int index = -1;
                                    CaseType curType = null;
                                    for (int i = 0; i < caseTypeList.size(); i++) {
                                        if (tag.equals(caseTypeList.get(i).picPath)) {
                                            curType = caseTypeList.get(i);
                                            index = i;

                                            break;
                                        }
                                    }

                                    caseTypeList.remove(index);
                                    adapter.notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(BaseBean base) {
                                    int status = base.getStatus();// 状态码
                                    String msg = base.getMsg();// 错误信息
                                    ToastUtil.show(mContext, "status = " + status + "\n"
                                            + "msg = " + msg);
                                }
                            }, BaseBean.class);
                        }else{
                            String tag = (String) v.getTag();
                            int index = -1;
                            CaseType curType = null;
                            for (int i = 0; i < caseTypeList.size(); i++) {
                                if (tag.equals(caseTypeList.get(i).picPath)) {
                                    curType = caseTypeList.get(i);
                                    index = i;

                                    break;
                                }
                            }

                            caseTypeList.remove(index);
                            adapter.notifyDataSetChanged();
                        }
                    }
                });
            } else {
                viewHolder.delete.setVisibility(View.GONE);
            }
            return convertView;
        }

    }

    class ViewHolder {
        public ImageView play;
        public ImageView delete;
    }

    class CaseType {
        String id;
        String type;  // 1：图片  2：添加图片
        String picPath;
        Bitmap image;
        boolean isShowDeleteed;
    }

    /**
     * 初始化添加案例
     */
    public void initPicData() {
        CaseType meidaType_pic = new CaseType();
        meidaType_pic.type = "2";
        caseTypeList.add(meidaType_pic);
        adapter.notifyDataSetChanged();
    }
}