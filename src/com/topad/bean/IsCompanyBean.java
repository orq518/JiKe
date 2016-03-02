package com.topad.bean;

import java.io.Serializable;

/**
 * ${todo}<是否是公司实体>
 *
 * @author lht
 * @data: on 16/03/02 20:06
 */
public class IsCompanyBean implements Serializable {
    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    /** 状态码 **/
    protected int status;
    /** error信息 **/
    protected String msg;
    /** 0则不是公司 **/
    public String iscompany ;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getIscompany() {
        return iscompany;
    }

    public void setIscompany(String iscompany) {
        this.iscompany = iscompany;
    }
}
