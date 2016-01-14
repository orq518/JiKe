package com.topad.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ${todo}<广告服务详情实体>
 *
 * @author lht
 * @data: on 15/10/26 11:06
 */
public class AdDetailsBean implements Serializable {
    /**product
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    /**  **/
    protected String id;
    /**  **/
    protected String userid;
    /**  **/
    protected String type1;
    /**  **/
    protected String type2;
    /**  **/
    protected String servicename;
    /**  **/
    protected String price;
    /**  **/
    protected String salecount;
    /**  **/
    protected String intro;
    /**  **/
    protected String adddate;
    /**  **/
    protected String isdelete;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getType1() {
        return type1;
    }

    public void setType1(String type1) {
        this.type1 = type1;
    }

    public String getType2() {
        return type2;
    }

    public void setType2(String type2) {
        this.type2 = type2;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSalecount() {
        return salecount;
    }

    public void setSalecount(String salecount) {
        this.salecount = salecount;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getIsdelete() {
        return isdelete;
    }

    public void setIsdelete(String isdelete) {
        this.isdelete = isdelete;
    }
}
