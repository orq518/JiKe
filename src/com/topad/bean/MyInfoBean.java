package com.topad.bean;

import java.util.List;

/**
 * The author 欧瑞强 on 2016/1/8.
 * todo
 */
public class MyInfoBean extends BaseBean{


    /**
     * data : [{"birthday":"2016-1-8","sex":"0","job1":"","job2":"","nickname":"星期一","adddate":"2016-01-08 14:01:09","userid":"8","companyname":"","imglicense":"","imghead":"b22e8db6d70f283d6d0632bc8d05cfc8.jpg","intro":"","id":"3","imgnamecard":"","imgdiploma":"","address":"兔兔","imgcard1":"","imgcard2":""}]
     * msg : ok
     * status : 10000
     */

    /**
     * birthday : 2016-1-8
     * sex : 0
     * job1 :
     * job2 :
     * nickname : 星期一
     * adddate : 2016-01-08 14:01:09
     * userid : 8
     * companyname :
     * imglicense :
     * imghead : b22e8db6d70f283d6d0632bc8d05cfc8.jpg
     * intro :
     * id : 3
     * imgnamecard :
     * imgdiploma :
     * address : 兔兔
     * imgcard1 :
     * imgcard2 :
     */

    private DataEntity data;


    public void setData(DataEntity data) {
        this.data = data;
    }


    public DataEntity getData() {
        return data;
    }

    public static class DataEntity {
        private String birthday;
        private String sex;
        private String job1;
        private String job2;
        private String nickname;
        private String adddate;
        private String userid;
        private String companyname;
        private String imglicense;
        private String imghead;
        private String intro;//个人简介
        private String id;
        private String imgnamecard;
        private String imgdiploma;
        private String address;
        private String imgcard1;
        private String imgcard2;

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public void setJob1(String job1) {
            this.job1 = job1;
        }

        public void setJob2(String job2) {
            this.job2 = job2;
        }

        public void setNickname(String nickname) {
            this.nickname = nickname;
        }

        public void setAdddate(String adddate) {
            this.adddate = adddate;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public void setCompanyname(String companyname) {
            this.companyname = companyname;
        }

        public void setImglicense(String imglicense) {
            this.imglicense = imglicense;
        }

        public void setImghead(String imghead) {
            this.imghead = imghead;
        }

        public void setIntro(String intro) {
            this.intro = intro;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setImgnamecard(String imgnamecard) {
            this.imgnamecard = imgnamecard;
        }

        public void setImgdiploma(String imgdiploma) {
            this.imgdiploma = imgdiploma;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public void setImgcard1(String imgcard1) {
            this.imgcard1 = imgcard1;
        }

        public void setImgcard2(String imgcard2) {
            this.imgcard2 = imgcard2;
        }

        public String getBirthday() {
            return birthday;
        }

        public String getSex() {
            return sex;
        }

        public String getJob1() {
            return job1;
        }

        public String getJob2() {
            return job2;
        }

        public String getNickname() {
            return nickname;
        }

        public String getAdddate() {
            return adddate;
        }

        public String getUserid() {
            return userid;
        }

        public String getCompanyname() {
            return companyname;
        }

        public String getImglicense() {
            return imglicense;
        }

        public String getImghead() {
            return imghead;
        }

        public String getIntro() {
            return intro;
        }

        public String getId() {
            return id;
        }

        public String getImgnamecard() {
            return imgnamecard;
        }

        public String getImgdiploma() {
            return imgdiploma;
        }

        public String getAddress() {
            return address;
        }

        public String getImgcard1() {
            return imgcard1;
        }

        public String getImgcard2() {
            return imgcard2;
        }
    }
}
