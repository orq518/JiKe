package com.topad.util;


public class Constants {
    // 内部版本号
    public static final int INTERNAL_VERSION = 1;
    public static final String T_CHANNEL = "HD_COMMON";

    public static String softVersionName;
    /********** 接口 *********/
    // 登录
    public static final String URL_LOGIN = "/user/login";
    // 注册
    public static final String URL_REGISTER = "/user/register";
    // 获取验证码
    public static final String URL_GETCODE = "/user/getcode";
    // 重置密码
    public static final String URL_RESETPWD = "/user/resetpwd";
    // 个人资料-上传图片（头像、身份证、毕业证、名片、公司认证营业执照、媒体代理证明）
    public static final String UPLOAD_PHOTO = "/user/uploadphoto";
    // 个人资料-上传头像
    public static final String UPLOAD_HEAD = "/user/updatehead";
    // 获取个人资料
    public static final String GETINFO = "/user/getinfo";
    // 提交个人资料
    public static final String SAVEINFO = "/user/savebasicinfo";
    // 更新个人简介
    public static final String UPDATE_INTRO = "/user/updateintro";
    // 获取广告服务产品列表（查询或我的产品列表）
    public static final String URL_AD_SERVICE_GETLIST = "/adservice/getlist";
    // 更新职业
    public static final String URL_UPDATEJOB = "/user/updatejob";
    // 发布需求
    public static final String URL_NEED_ADD = "/need/add";
    // 更新其它图片数据
    public static final String URL_UPDATE_IMG = "/user/updateimg";
    // 媒体搜索
    public static final String URL_MEDIA_SEARCH = "/media/search";
    // 下载图片拼接
    public static final String IMAGE_URL_HEADER = "/serviceimg/";
    // 下载案例图片拼接
    public static final String CASE_IMAGE_URL_HEADER = "/serviceimg/";
    // 添加产品
    public static final String URL_ADD_PRODUCT= "/adservice/add";
    // 添加案例图片
    public static final String URL_ADD_PHOTO= "/adservice/addphoto";
    // 添加案例
    public static final String URL_ADD_CASE= "/adservice/addcase";
    // 删除产品
    public static final String URL_DEL_SERVICE= "/adservice/delservice";
    // 修改产品
    public static final String URL_EDIT_SERVICE= "/adservice/saveedit";
    // 产品详细信息
    public static final String URL_GET_INFO = "/adservice/getinfo";
    // 产品案例列表
    public static final String URL_CASE_LIST = "/adservice/caselist";
    // 删除案例
    public static final String URL_DEL_CASE = "/adservice/delcase";
    // 发布媒体
    public static final String URL_MEDIA_ADD = "/media/add";
    // 请联系我
    public static final String URL_CONTACTME = "/media/contactme";
    // 获取需求列表（抢单列表，我的需求，我的抢单）
    public static final String URL_NEED_GETLIST = "/need/getlist";
    // 获取我发布媒体列表
    public static final String URL_MEDIA_GETLIST = "/media/getlist";
    // 获取钱包金额
    public static final String URL_WALLET_GETMONEY = "/wallet/getmoney";
    // 购买产品
    public static final String URL_BUY_IT = "/adservice/buyit";
    // 甄选项目
    public static final String URL_NEDD_SEARCH = "/need/search";
    // 上传我的位置
    public static final String URL_UPDATE_LOCATION = "/user/updatepos";
    // 获取附近符合要求的人
    public static final String URL_GET_USERS_NEARBY = "/need/getusers";
    // 查看需求详情
    public static final String URL_NEED_GET_DETAIL = "/need/getdetail";
    // 返回抢单列表
    public static final String URL_GET_REQUEST_LIST = "/need/getrequestlist";
    // 开始项目
    public static final String URL_NEED_START = "/need/start";
    // 项目取消
    public static final String URL_NEED_DELPROJECT = "/need/delproject";
    // 项目完成
    public static final String URL_NEED_ENDPRJ = "/need/endprj";
    // 充值
    public static final String URL_ALI_RETUREN = "/pay/alireturen";
    // 提现
    public static final String URL_GET_MONEY = "/pay/getmoney";
    // 系统消息
    public static final String URL_USER_GETMSG = "/user/getmsg";
    // 申诉
    public static final String URL_SHEN_SU = "http://www.uput.cn/case/topad/shensu.html";

    public static String ALIAS_TYPE="topad";
    /**
     * 测试环境
     */
    public static final String URL_TEST = "http://topad.uput.cn";
    /**
     * 生产环境
     */
    public static final String URL_PUBLISH = "http://topad.uput.cn";


    // 当前环境，默认测试环境
    public static String CURR_URL = URL_TEST;

    // 钱包的环境  true 表示线上环境  false 测试环境
    public static boolean WALLET_ONLINE = false;
    // 支付的环境  true 表示线上环境  false 测试环境
    public static boolean PAY_ONLINE = false;

    /**
     * 获取当前环境
     *
     * @return 当前环境URL
     */
    public static String getCurrUrl() {
        // debug状态为false时，返回生产环境
        return (CURR_URL);
    }

    /**
     * 切换正式还是测试环境
     */
    public static void setUrlOnline(boolean isOnline) {
        if (isOnline) {
            CURR_URL = URL_PUBLISH;
            WALLET_ONLINE = true;
            PAY_ONLINE = true;
        } else {
            CURR_URL = URL_TEST;
            WALLET_ONLINE = false;
            PAY_ONLINE = false;
        }
    }


    /**
     * 广播Action
     **/
    public static final String BROADCAST_ACTION_GETZHIYE = "broadcast_action_login";   //获得职业
    public static final String BROADCAST_ACTION_PRODUCT_CLASS = "broadcast_action_product_class";   //我的产品类别
    public static final String BROADCAST_ACTION_MEDIA_CLASS = "broadcast_action_media_class";   //我的媒体类别
    public static final String BROADCAST_ACTION_LOGOUT = "broadcast_action_logout"; //登出
    public static final String BROADCAST_ACTION_CLEAR_TOKEN = "broadcast_action_clear_token";   //清除token
    public static final String BROADCAST_ACTION_SUCCEED_LOGIN_INDEX = "broadcast_action_succeed_login_index";   //登录成功后跳转的tabindex


}