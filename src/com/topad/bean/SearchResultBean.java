package com.topad.bean;

import java.util.List;

/**
 * The author 欧瑞强 on 2016/1/15.
 * todo
 */
public class SearchResultBean extends BaseBean{


    private List<DataEntity> data;


    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        /**
         * id : 10
         * addtime : 2016-01-15 11:13:14
         * medianame : cctv1
         * location : 嘉和丽园公寓(东门)
         * subname : qqqq
         * type1 : 电视
         * type3 :
         * userid : 8
         * type2 : 中央电视台
         * longitude : 39.958031
         * latitude : 116.465878
         * mediacert :
         */

        private String id;
        private String addtime;
        private String medianame;
        private String location;
        private String subname;
        private String type1;
        private String type3;
        private String userid;
        private String type2;
        private String longitude;
        private String latitude;
        private String mediacert;
        private String imghead;

        public void setImghead(String imghead) {
            this.imghead = imghead;
        }

        public String getImghead() {
            return imghead;
        }
        public void setId(String id) {
            this.id = id;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setMedianame(String medianame) {
            this.medianame = medianame;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public void setSubname(String subname) {
            this.subname = subname;
        }

        public void setType1(String type1) {
            this.type1 = type1;
        }

        public void setType3(String type3) {
            this.type3 = type3;
        }

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public void setType2(String type2) {
            this.type2 = type2;
        }

        public void setLongitude(String longitude) {
            this.longitude = longitude;
        }

        public void setLatitude(String latitude) {
            this.latitude = latitude;
        }

        public void setMediacert(String mediacert) {
            this.mediacert = mediacert;
        }

        public String getId() {
            return id;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getMedianame() {
            return medianame;
        }

        public String getLocation() {
            return location;
        }

        public String getSubname() {
            return subname;
        }

        public String getType1() {
            return type1;
        }

        public String getType3() {
            return type3;
        }

        public String getUserid() {
            return userid;
        }

        public String getType2() {
            return type2;
        }

        public String getLongitude() {
            return longitude;
        }

        public String getLatitude() {
            return latitude;
        }

        public String getMediacert() {
            return mediacert;
        }
    }
}
