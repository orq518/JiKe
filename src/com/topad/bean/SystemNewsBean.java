package com.topad.bean;

import java.util.List;

/**
 * ${todo}<系统消息实体>
 *
 * @author lht
 * @data: on 15/10/26 11:06
 */
public class SystemNewsBean extends  BaseBean {
    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;

    /**
     * userid : 12
     * id : 1
     * isdelete : 0
     * audiofile : audiofile.mp3
     * body : 测试消息，语音是假的，不用播放
     * addtime : 2016-01-30 10:46:37
     * title : 测试消息，语音是假的
     */

    private List<DataEntity> data;

    public void setData(List<DataEntity> data) {
        this.data = data;
    }

    public List<DataEntity> getData() {
        return data;
    }

    public static class DataEntity {
        private String userid;
        private String id;
        private String isdelete;
        private String audiofile;
        private String body;
        private String addtime;
        private String title;

        public void setUserid(String userid) {
            this.userid = userid;
        }

        public void setId(String id) {
            this.id = id;
        }

        public void setIsdelete(String isdelete) {
            this.isdelete = isdelete;
        }

        public void setAudiofile(String audiofile) {
            this.audiofile = audiofile;
        }

        public void setBody(String body) {
            this.body = body;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUserid() {
            return userid;
        }

        public String getId() {
            return id;
        }

        public String getIsdelete() {
            return isdelete;
        }

        public String getAudiofile() {
            return audiofile;
        }

        public String getBody() {
            return body;
        }

        public String getAddtime() {
            return addtime;
        }

        public String getTitle() {
            return title;
        }
    }
}
