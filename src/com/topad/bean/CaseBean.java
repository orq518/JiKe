package com.topad.bean;

import java.io.Serializable;

/**
 * ${todo}<案例>
 *
 * @author lht
 * @data: on 15/10/29 15:18
 */
public class CaseBean  implements Serializable {
    /**product
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122567982665L;
    private String intro;
    private String picPath;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getPicPath() {
        return picPath;
    }

    public void setPicPath(String picPath) {
        this.picPath = picPath;
    }
}
