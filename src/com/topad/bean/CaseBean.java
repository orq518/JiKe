package com.topad.bean;

import android.graphics.Bitmap;

import java.io.Serializable;
import java.util.ArrayList;

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
    private static final long serialVersionUID = 4768457122567982665L;
    private String intro;
    private ArrayList<String> imgs;
    private ArrayList<String> picPaths;

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public ArrayList<String> getImgs() {
        return imgs;
    }

    public void setImgs(ArrayList<String> imgs) {
        this.imgs = imgs;
    }

    public ArrayList<String> getPicPaths() {
        return picPaths;
    }

    public void setPicPaths(ArrayList<String> picPaths) {
        this.picPaths = picPaths;
    }
}
