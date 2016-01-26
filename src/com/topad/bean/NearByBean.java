package com.topad.bean;

import java.util.List;

/**
 * The author 欧瑞强 on 2016/1/21.
 * todo
 */
public class NearByBean extends  BaseBean{
    /**
     * id : 8
     * pwd : 96E79218965EB72C92A549DD5A330112
     * logintime : 2016-01-21 04:25:22
     * location : 北京市朝阳区霄云路靠近嘉和丽园公寓(东门)
     * token : F02C3ECD-4CD3-E400-084D-A0835FB19A99
     * regdate : 2015-12-31 13:58:10
     * longitude : 116.46609800
     * latitude : 39.95792900
     * logincount : 88
     * mobile : 18600565154
     */

    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String id;
        private String pwd;
        private String logintime;
        private String location;
        private String token;
        private String regdate;
        private String longitude;
        private String latitude;
        private String logincount;
        private String mobile;

        public void setId(String id) {
            this.id = id;
        }

        public void setPwd(String pwd) {
            this.pwd = pwd;
        }

        public void setLogintime(String logintime) {
            this.logintime = logintime;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public void setRegdate(String regdate) {
            this.regdate = regdate;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setLogincount(String logincount) {
            this.logincount = logincount;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getId() {
            return id;
        }

        public String getPwd() {
            return pwd;
        }

        public String getLogintime() {
            return logintime;
        }

        public String getLocation() {
            return location;
        }

        public String getToken() {
            return token;
        }

        public String getRegdate() {
            return regdate;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getLogincount() {
            return logincount;
        }

        public String getMobile() {
            return mobile;
        }
    }
}
