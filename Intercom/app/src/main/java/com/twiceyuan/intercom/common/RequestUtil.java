package com.twiceyuan.intercom.common;

import java.io.File;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;


/**
 * Created by twiceYuan on 9/3/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 请求相关工具类
 */
public class RequestUtil {

    /**
     * MultiPart 上传文件时构造一个文件的 Part
     * @param partName 该 part 的 key 值
     * @param file 文件
     * @return 该文件的 Part
     */
    public static MultipartBody.Part filePart(String partName, File file) {
        RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
        return  MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }
}
