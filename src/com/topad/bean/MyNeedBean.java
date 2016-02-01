package com.topad.bean;

import java.io.Serializable;

/**
 * ${todo}<我的需求>
 *
 * @author lht
 * @data: on 15/10/28 16:12
 */
public class MyNeedBean implements Serializable {
    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    public String needid;
    public String id;
    public String userid;
    public String nickname;
    public String companyname;
    public String sex;
    public String birthday;
    public String address;
    public String job1;
    public String job2;
    public String intro;
    public String imghead;
    public String imgcard1;
    public String imgcard2;
    public String imgdiploma;
    public String imgnamecard;
    public String imglicense;
    public String adddate;

    public String getNeedid() {
        return needid;
    }

    public void setNeedid(String needid) {
        this.needid = needid;
    }

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

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
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

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    @Override
    public String toString() {
        return "MyNeedBean{" +
                "needid='" + needid + '\'' +
                ", id='" + id + '\'' +
                ", userid='" + userid + '\'' +
                ", nickname='" + nickname + '\'' +
                ", companyname='" + companyname + '\'' +
                ", sex='" + sex + '\'' +
                ", birthday='" + birthday + '\'' +
                ", address='" + address + '\'' +
                ", job1='" + job1 + '\'' +
                ", job2='" + job2 + '\'' +
                ", intro='" + intro + '\'' +
                ", imghead='" + imghead + '\'' +
                ", imgcard1='" + imgcard1 + '\'' +
                ", imgcard2='" + imgcard2 + '\'' +
                ", imgdiploma='" + imgdiploma + '\'' +
                ", imgnamecard='" + imgnamecard + '\'' +
                ", imglicense='" + imglicense + '\'' +
                ", adddate='" + adddate + '\'' +
                '}';
    }
}