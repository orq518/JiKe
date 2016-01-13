package com.topad.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ${todo}<广告服务案例实体>
 *
 * @author lht
 * @data: on 15/10/26 11:06
 */
public class AdServiceCaseBean implements Serializable {
    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    /**  **/
    protected String id;
    /**  **/
    protected String userid;
    /**  **/
    protected String serviceid;
    /**  **/
    protected String imgs;
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

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getImgs() {
        return imgs;
    }

    public void setImgs(String imgs) {
        this.imgs = imgs;
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
