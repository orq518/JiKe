package com.topad;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.graphics.Bitmap;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NotificationCompat;
import android.text.TextUtils;
import android.widget.RemoteViews;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.topad.bean.LocationBean;
import com.topad.bean.MyInfoBean;
import com.topad.util.Constants;
import com.topad.util.LogUtil;
import com.topad.util.SharedPreferencesUtils;
import com.topad.util.Utils;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.UmengRegistrar;
import com.umeng.message.entity.UMessage;

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
    LocationBean locationBean;
    private PushAgent mPushAgent;

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
//                .showImageOnLoading(R.drawable.stay_tuned_icon)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .displayer(new SimpleBitmapDisplayer()).build();
        initImageLoader(this);
        initUmeng();
    }

    public void initUmeng() {

        MobclickAgent.setDebugMode(true);
        // SDK在统计Fragment时，需要关闭Activity自带的页面统计，
        // 然后在每个页面中重新集成页面统计的代码(包括调用了 onResume 和 onPause 的Activity)。
        MobclickAgent.openActivityDurationTrack(false);


        mPushAgent = PushAgent.getInstance(this);
        mPushAgent.setDebugMode(true);
        mPushAgent.enable();
        String device_token = UmengRegistrar.getRegistrationId(context);
        LogUtil.d("##device_token:" + device_token);
        UmengMessageHandler messageHandler = new UmengMessageHandler() {
            /**
             * 参考集成文档的1.6.3
             * http://dev.umeng.com/push/android/integration#1_6_3
             * */
            @Override
            public void dealWithCustomMessage(final Context context, final UMessage msg) {
                new Handler().post(new Runnable() {

                    @Override
                    public void run() {
                        // TODO Auto-generated method stub
                        // 对自定义消息的处理方式，点击或者忽略
                        boolean isClickOrDismissed = true;
                        if (isClickOrDismissed) {
                            //自定义消息的点击统计
                            UTrack.getInstance(getApplicationContext()).trackMsgClick(msg);
                        } else {
                            //自定义消息的忽略统计
                            UTrack.getInstance(getApplicationContext()).trackMsgDismissed(msg);
                        }
                        Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
                    }
                });
            }

            /**
             * 参考集成文档的1.6.4
             * http://dev.umeng.com/push/android/integration#1_6_4
             * */
            @Override
            public Notification getNotification(Context context,
                                                UMessage msg) {
                switch (msg.builder_id) {
                    case 1:
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        RemoteViews myNotificationView = new RemoteViews(context.getPackageName(), R.layout.notification_view);
                        myNotificationView.setTextViewText(R.id.notification_title, msg.title);
                        myNotificationView.setTextViewText(R.id.notification_text, msg.text);
                        myNotificationView.setImageViewBitmap(R.id.notification_large_icon, getLargeIcon(context, msg));
                        myNotificationView.setImageViewResource(R.id.notification_small_icon, getSmallIconId(context, msg));
                        builder.setContent(myNotificationView);
                        builder.setContentTitle(msg.title)
                                .setContentText(msg.text)
                                .setTicker(msg.ticker)
                                .setAutoCancel(true);
                        Notification mNotification = builder.build();
                        //由于Android v4包的bug，在2.3及以下系统，Builder创建出来的Notification，并没有设置RemoteView，故需要添加此代码
                        mNotification.contentView = myNotificationView;
                        return mNotification;
                    default:
                        //默认为0，若填写的builder_id并不存在，也使用默认。
                        return super.getNotification(context, msg);
                }
            }
        };
        mPushAgent.setMessageHandler(messageHandler);

        /**
         * 该Handler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * 参考集成文档的1.6.2
         * http://dev.umeng.com/push/android/integration#1_6_2
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Toast.makeText(context, msg.custom, Toast.LENGTH_LONG).show();
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

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

    String device_token;

    public String getDeviceToken() {
        if(Utils.isEmpty(device_token)){
            device_token = UmengRegistrar.getRegistrationId(context);
        }
        return device_token;
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

    public void bindUmeng() {
        if (TextUtils.isEmpty(userId)) {
            userId = (String) SharedPreferencesUtils.get(this, SharedPreferencesUtils.USER_ID, "");
        }
        //绑定友盟账号
        try {
            if (!mPushAgent.isRegistered()) {
                LogUtil.d("未注册，不能添加友盟ALIAS");
                return;
            }
            new AddAliasTask(userId, Constants.ALIAS_TYPE).execute();
        } catch (Exception e) {

        }
    }

    /**
     * 退出登录
     */
    public void logout() {
        token = null;
        userId = null;
        myInfoBean = null;

        String mUserId = (String) SharedPreferencesUtils.get(this, SharedPreferencesUtils.USER_ID, "");
        if (!Utils.isEmpty(mUserId)) {
            //绑定友盟账号
            try {
                new RemoveAliasTask(mUserId, Constants.ALIAS_TYPE).execute();
            } catch (Exception e) {

            }
        }
        SharedPreferencesUtils.put(this, SharedPreferencesUtils.KEY_TOKEN, "");
        SharedPreferencesUtils.put(this, SharedPreferencesUtils.USER_ID, "");
        SharedPreferencesUtils.put(this, SharedPreferencesUtils.USER_PHONR, "");

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


    /**
     * 保存我的位置信息
     *
     * @return
     */
    public void setLocation(LocationBean location) {
        locationBean = location;
    }

    /**
     * 获取位置
     *
     * @return
     */
    public LocationBean getLocation() {
        return locationBean;
    }

    class AddAliasTask extends AsyncTask<Void, Void, Boolean> {

        String alias;
        String aliasType;

        public AddAliasTask(String aliasString, String aliasTypeString) {
            // TODO Auto-generated constructor stub
            this.alias = aliasString;
            this.aliasType = aliasTypeString;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                if (!Utils.isEmpty(alias)) {
                    mPushAgent.removeAlias(alias, aliasType);
                }

                return mPushAgent.addAlias(alias, aliasType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (Boolean.TRUE.equals(result))
                LogUtil.d("alias was set successfully.");
        }

    }

    class RemoveAliasTask extends AsyncTask<Void, Void, Boolean> {

        String alias;
        String aliasType;

        public RemoveAliasTask(String aliasString, String aliasTypeString) {
            // TODO Auto-generated constructor stub
            this.alias = aliasString;
            this.aliasType = aliasTypeString;
        }

        protected Boolean doInBackground(Void... params) {
            try {
                return mPushAgent.removeAlias(alias, aliasType);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return false;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            if (Boolean.TRUE.equals(result))
                LogUtil.d("alias was set successfully.");
        }

    }
}
