package com.twiceyuan.intercom.model.remote;

/**
 * Created by twiceYuan on 8/30/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 * <p>
 * 一个上传完成的图片对象, 由 sm.ms 返回
 */
public class UploadedImage {
    public String filename; // shadowsocks.jpg	上传文件时所用的文件名
    public String storename; // 561cc4e3631b1.png	上传后的文件名
    public String hash; // nLbCw63NheaiJp1	随机字符串，用于删除文件
    public String delete; // https://sm.ms/api/delete/nLbCw63NheaiJp1	删除上传的图片文件专有链接
    public String url; // https://ooo.0o0.ooo/2015/10/13/561cfc3282a13.png	图片服务器地址
    public String path; // /2015/10/13/561cfc3282a13.png	图片的相对地址
    public String msg; // No files were uploaded.	上传图片出错时将会出现

    public int size; // 187851	文件大小
    public int width; // 1157	图片的宽度
    public int height; // 680	图片的高度
}
