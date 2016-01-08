package com.topad.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ${todo}<广告产品实体>
 *
 * @author lht
 * @data: on 15/10/26 11:06
 */
public class AdProductBean implements Serializable {
    /**product
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    /**  **/
    public String serviceid;
    /** 产品服务id **/
    public String id;
    /**  **/
    public String userid;
    /**  **/
    public String type1;
    /**  **/
    public String type2;
    /** 产品名字 **/
    public String servicename;
    /** 售价，显示为（¥price/单品） **/
    public String price;
    /** 销售笔数 **/
    public String salecount;
    /**  **/
    public String intro;
    /**  **/
    public String adddate;
    /**  **/
    public String isdelete;
    /**  **/
    public String nickname;
    /**  **/
    public String companyname;
    /**  **/
    public String sex;
    /**  **/
    public String job1;
    /**  **/
    public String job2;
    /**  **/
    public String imghead;
    /**  **/
    public String imgcard1;
    /**  **/
    public String imgcard2;
    /**  **/
    public String imgdiploma;
    /**  **/
    public String imgnamecard;
    /** 认证，有值为认证过 **/
    public String imglicense;


    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getCompanyname() {
        return companyname;
    }

    public void setCompanyname(String companyname) {
        this.companyname = companyname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getJob1() {
        return job1;
    }

    public void setJob1(String job1) {
        this.job1 = job1;
    }

    public String getJob2() {
        return job2;
    }

    public void setJob2(String job2) {
        this.job2 = job2;
    }

    public String getImghead() {
        return imghead;
    }

    public void setImghead(String imghead) {
        this.imghead = imghead;
    }

    public String getImgcard1() {
        return imgcard1;
    }

    public void setImgcard1(String imgcard1) {
        this.imgcard1 = imgcard1;
    }

    public String getImgcard2() {
        return imgcard2;
    }

    public void setImgcard2(String imgcard2) {
        this.imgcard2 = imgcard2;
    }

    public String getImgdiploma() {
        return imgdiploma;
    }

    public void setImgdiploma(String imgdiploma) {
        this.imgdiploma = imgdiploma;
    }

    public String getImgnamecard() {
        return imgnamecard;
    }

    public void setImgnamecard(String imgnamecard) {
        this.imgnamecard = imgnamecard;
    }

    public String getImglicense() {
        return imglicense;
    }

    public void setImglicense(String imglicense) {
        this.imglicense = imglicense;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
}
