package com.topad.view.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.topad.R;
import com.topad.bean.BaseBean;
import com.topad.net.HttpCallback;
import com.topad.net.HttpUtil;
import com.topad.net.http.LoadingDialogCalback;
import com.topad.net.http.RequestParams;
import com.topad.util.LogUtil;
import com.topad.util.Utils;
import com.topad.view.customviews.CircleProgressDialog;

/**
 * Fragment基类
 *
 * @author dewyze
 */
public abstract class BaseFragment extends Fragment {
    public boolean isNeedRefresh;
    public BaseBean base;
    public CircleProgressDialog mReadingProgress;
    /** 上下文 **/
    private Context mContext;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mContext = activity;
        LogUtil.d(getFragmentName() + ": onAttach()");
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d(getFragmentName() + ": onCreate()");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        LogUtil.d(getFragmentName() + ": onCreateView()");
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LogUtil.d(getFragmentName() + ":  onViewCreated()");
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        LogUtil.d(getFragmentName() + ": onActivityCreated()");
    }

    @Override
    public void onStart() {
        super.onStart();
        LogUtil.d(getFragmentName() + ": onStart()");
    }

    @Override
    public void onResume() {
        super.onResume();
        LogUtil.d(getFragmentName() + ":  onResume()");

    }

    @Override
    public void onPause() {
        super.onPause();
        LogUtil.d(getFragmentName() + ":  onPause()");

    }

    @Override
    public void onStop() {
        super.onStop();
        LogUtil.d(getFragmentName() + ": onStop()");
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        LogUtil.d(getFragmentName() + ":  onDestroyView()");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d(getFragmentName() + ":  onDestroy()");
    }

    @Override
    public void onDetach() {
        super.onDetach();
        LogUtil.d(getFragmentName() + ": onDetach()");
    }
    public void setVisible(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        LogUtil.d(this+"  是否显示：" + isVisibleToUser + "    isNeedRefresh:" + isNeedRefresh);
    }
    /**
     * fragment name
     */
    public abstract String getFragmentName();

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
    }
    /**
     * 联网，显示联网对话框
     *
     * @param url         地址
     * @param needEncript 是否加密
     * @param callback    回调对象
     * @param clazz       model类，json字符串映射到model
     * @param <T>
     */
    public <T> void postWithLoading(String url, RequestParams rp, boolean needEncript,
                                    HttpCallback callback, Class<T> clazz) {

        if (!Utils.isNetworkConnected(mContext)){
            if(base == null){
                base = new BaseBean();
            }
            base.setStatus(-1);
            callback.onFailure(base);

            Utils.showToast(mContext, getString(R.string.no_connection));
            return ;
        }
        showProgressDialog(true);
        HttpUtil.getInstance().post(mContext, url, rp, needEncript, callback, clazz, new LoadingDialogCalback() {
            @Override
            public void closeDialog() {
                closeProgressDialog();
            }
        }, false);
    }

    /**
     * 联网，显示联网对话框
     *
     * @param url               地址
     * @param needEncript       是否加密
     * @param callback          回调对象
     * @param clazz             model类，json字符串映射到model
     * @param <T>
     * @param needResultCode    访问失败时，是否返回返回码
     */
    public <T> void postWithLoading( String url, RequestParams rp, boolean needEncript,
                                     HttpCallback callback, Class<T> clazz, boolean needResultCode) {
        if (!Utils.isNetworkConnected(mContext)){
            if(base == null){
                base = new BaseBean();
            }
            Utils.showToast(mContext, this.getString(R.string.no_connection));
            base.setStatus(-1);
            callback.onFailure(base);
            return ;
        }
        showProgressDialog(true);
        HttpUtil.getInstance().post(mContext, url, rp, needEncript, callback, clazz, new LoadingDialogCalback() {
            @Override
            public void closeDialog() {
                closeProgressDialog();
            }
        }, true);
    }

    /**
     * 不弹出联网对话框
     *
     * @param @param url            请求Url,参数拼接在url后面
     * @param @param needEncript    是否需要加密，true：需要；false:不需要
     * @param @param callback       回调接口
     * @param @param clazz          model类，json字符串映射到model
     * @return void
     */
    public <T> void postWithoutLoading( String url, RequestParams rp, boolean needEncript, HttpCallback callback, Class<T> clazz) {
        if (!Utils.isNetworkConnected(mContext)){
            if(base == null){
                base = new BaseBean();
            }
            closeProgressDialog();
            Utils.showToast(mContext, getString(R.string.no_connection));
            base.setStatus(-1);
            base.setMsg(getString(R.string.no_connection));
            callback.onFailure(base);
            return ;
        }
        HttpUtil.getInstance().post(mContext, url, rp, needEncript, callback, clazz, new LoadingDialogCalback() {
            @Override
            public void closeDialog() {

            }
        }, true);
    }

    /**
     * @param @param msg
     * @param @param cancelAble
     *               true:could cancel,false:can't cancel
     * @return void
     * @Description: 可以取消的loading对话框
     */
    public void showProgressDialog( String msg, boolean cancelAble) {
        if (mReadingProgress == null || !mReadingProgress.isShowing()) {
            if (mReadingProgress == null) {
                mReadingProgress = new CircleProgressDialog(mContext, R.style.loading_dialog);
            }
            mReadingProgress.setCancelable(cancelAble);
            mReadingProgress.setMessage(msg);
            mReadingProgress.show();
        }
    }

    /**
     * 可取消的进度条
     *
     * @param allowCancel true:could cancel,false:can't cancel
     */
    public void showProgressDialog(boolean allowCancel) {
        if (mReadingProgress == null || !mReadingProgress.isShowing()) {
            if (mReadingProgress == null) {
                mReadingProgress = new CircleProgressDialog(mContext, R.style.loading_dialog);
            }
            mReadingProgress.setCancelable(allowCancel);
            mReadingProgress.show();
            if (allowCancel) {
                mReadingProgress.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        cancel();
                    }
                });
            }
        }
    }

    /**
     * @param @param msg    loading文案
     * @return void
     * @Description: 显示loading对话框
     */
    public void showProgressDialog( String msg) {
        if (mReadingProgress == null || !mReadingProgress.isShowing()) {
            if (mReadingProgress == null) {
                mReadingProgress = new CircleProgressDialog(mContext, R.style.loading_dialog);
            }
            mReadingProgress.setMessage(msg);
            mReadingProgress.setCancelable(false);
            mReadingProgress.show();
        }
    }

    /**
     * @param
     * @return void
     * @Description: 显示loading对话框，使用默认文案
     */
    public void showProgressDialog() {
        if (mReadingProgress == null || !mReadingProgress.isShowing()) {
            if (mReadingProgress == null) {
                mReadingProgress = new CircleProgressDialog(mContext, R.style.loading_dialog);
            }
            mReadingProgress.setCancelable(false);
            mReadingProgress.show();
        }
    }

    /**
     * 关闭loading对话框
     *
     * @param
     * @return void
     * @Description:
     */
    public void closeProgressDialog() {
        if (mReadingProgress != null) {
            mReadingProgress.dismiss();
            mReadingProgress = null;
        }
    }

    /**
     * @param
     * @return void
     * @Description: 发送取消http请求
     */
    protected void cancel() {
        HttpUtil.getInstance().cancel();
    }

}
