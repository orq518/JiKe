package com.topad;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.topad.bean.MyInfoBean;
import com.topad.util.LogUtil;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.Utils;
import com.topad.view.activity.MainActivity;
import com.topad.view.fragment.BaseFragment;

import java.util.List;

/**
 * The author ou on 2015/7/15.
 */
public class TopADApplication extends Application {
    private static Context context;
    static int[] screenDispaly;
    private static PackageInfo mPackageInfo;

    private String token;// 标识是否登录状态
    private String userId;// UserId

    private Handler handler = new Handler();
    FragmentManager mFragmentManager;
    MyInfoBean.DataEntity myInfoBean;
    DisplayImageOptions options;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        //设置ImageLoader参数
        options = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(false)
                .showImageForEmptyUri(R.drawable.stay_tuned_icon)
                .showImageOnFail(R.drawable.stay_tuned_icon)
                .showImageOnLoading(R.drawable.stay_tuned_icon)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();
        initImageLoader(this);
    }
    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        LogUtil.d("#####退出");
    }

    public static Context getContext() {
        return context;
    }


    public PackageInfo getPackageInfo() {
        if (mPackageInfo == null) {
            mPackageInfo = Utils.getPackageInfo(this);
        }
        return mPackageInfo;
    }

    public static TopADApplication getSelf() {
        return (TopADApplication) context;
    }


    /**
     * 获取屏幕分辨率
     *
     * @return
     */
    public static int[] getScreenDispaly() {
        if (screenDispaly == null) {
            return screenDispaly = Utils.getScreenDispaly(context);
        } else {
            return screenDispaly;
        }
    }

    /**
     * 判断是否登录状态
     */
    public boolean isLogin() {

        return !TextUtils.isEmpty(getToken());
    }

    /**
     * 获取全局账户信息
     *
     * @return
     */
    public String getToken() {
        if (TextUtils.isEmpty(token)) {
            token = (String) SharedPreferencesUtils.get(this, SharedPreferencesUtils.KEY_TOKEN, "");
        }
        LogUtil.d("getToken()--->token:" + token);
        return token;
    }

    public DisplayImageOptions getImageLoaderOption() {
        return options;
    }

    /**
     * 获取UserId
     *
     * @return
     */
    public String getUserId() {
        if (TextUtils.isEmpty(userId)) {
            userId = (String) SharedPreferencesUtils.get(this, SharedPreferencesUtils.USER_ID, "");
        }
        LogUtil.d("getUserId()--->userId:" + userId);
        return userId;
    }

    /**
     * 退出登录
     */
    public void logout() {
        token = null;
        SharedPreferencesUtils.put(this, SharedPreferencesUtils.KEY_TOKEN, "");
        SharedPreferencesUtils.put(this, SharedPreferencesUtils.USER_ID, "");
    }

    /**
     * 保存我的个人信息
     *
     * @return
     */
    public void setMyInfo(MyInfoBean.DataEntity myInfo) {
        myInfoBean = myInfo;
    }

    /**
     * 获取UserId
     *
     * @return
     */
    public MyInfoBean.DataEntity getMyInfo() {
        return myInfoBean;
    }
}
