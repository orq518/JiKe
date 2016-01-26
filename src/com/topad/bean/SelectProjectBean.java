package com.topad.bean;

import java.io.Serializable;

/**
 * ${todo}<甄选项入参实体>
 *
 * @author lht
 * @data: on 15/10/26 11:06
 */
public class SelectProjectBean implements Serializable {
    /**
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    /**  **/
    protected String ispay;
    /**  **/
    protected String paytype;
    /**  **/
    protected String type1;
    /**  **/
    protected String type2;
    /**  **/
    protected String page;

    public String getIspay() {
        return ispay;
    }

    public void setIspay(String ispay) {
        this.ispay = ispay;
    }

    public String getPaytype() {
        return paytype;
    }

    public void setPaytype(String paytype) {
        this.paytype = paytype;
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

    public String getPage() {
        return page;
    }

    public void setPage(String page) {
        this.page = page;
    }
}
