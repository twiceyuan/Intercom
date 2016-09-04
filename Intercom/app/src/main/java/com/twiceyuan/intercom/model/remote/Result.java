package com.twiceyuan.intercom.model.remote;

/**
 * Created by twiceYuan on 9/2/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 一般请求结果
 */
public class Result<Data> {

    public String code;
    public String msg;
    public Data data;

    public Data getData() {
        return data;
    }
}
