package com.topad;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;

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

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();

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
    }
}
