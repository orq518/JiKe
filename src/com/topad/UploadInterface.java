package com.topad;

import com.topad.bean.BaseBean;

/**
 * The author 欧瑞强 on 2016/1/8.
 * todo
 */
public interface UploadInterface {

    public void onSucceed(BaseBean bean);

    public void onFailed(BaseBean bean);
}
