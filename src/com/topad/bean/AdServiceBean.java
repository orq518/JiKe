package com.topad.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * ${todo}<广告服务实体>
 *
 * @author lht
 * @data: on 15/10/26 11:06
 */
public class AdServiceBean implements Serializable {
    /**product
     * serialVersionUID:TODO（用一句话描述这个变量表示什么）
     */
    private static final long serialVersionUID = 4768927122317982665L;
    /** 状态码 **/
    protected int status;
    /** error信息 **/
    protected String msg;
    /** data **/
    public ArrayList<AdProductBean> data = new ArrayList<AdProductBean>();

}
