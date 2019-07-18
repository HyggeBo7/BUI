package com.dan.library.entity;

import com.dan.library.util.JsonUtil;

import java.io.Serializable;

/**
 * @fileName: AjaxResult.java
 * @author: Bo
 * @createDate: 2018/9/12 15:58
 * @description: 结果返回
 */
public class AjaxResult implements Serializable {

    private static final long serialVersionUID = 4068125799671082560L;

    public static final Integer SUCCESS = 1;
    public static final Integer PARAM_ERROR = -1;
    public static final Integer SERVER_ERROR = -500;
    private Integer code;
    private String msg;
    private Object data;

    public AjaxResult() {
        this.code = SUCCESS;
    }

    public AjaxResult(Object data) {
        this.code = SUCCESS;
        if (null == data) {
            data = "";
        }

        this.data = data;
    }

    public AjaxResult(Integer code) {
        this.code = code;
    }

    public AjaxResult(Integer code, String msg) {
        this.code = code;
        if (null == msg) {
            msg = "";
        }

        this.msg = msg;
    }

    public AjaxResult(Integer code, String msg, Object data) {
        if (null == data) {
            data = "";
        }

        if (null == msg) {
            msg = "";
        }

        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    @Override
    public String toString() {
        return JsonUtil.toJson(this);
    }

    public String dataToJson() {
        return this.data == null ? "" : JsonUtil.toJson(this.data);
    }

    public Integer getCode() {
        return this.code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
