package com.twiceyuan.intercom.service;

import com.twiceyuan.intercom.model.remote.Result;
import com.twiceyuan.intercom.model.remote.UploadedImage;

import okhttp3.MultipartBody;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import rx.Observable;

/**
 * Created by twiceYuan on 8/30/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * sm.ms 图床服务
 */
public interface ImageHostService {

    @POST("api/upload")
    @Multipart
    Observable<Result<UploadedImage>> uploadImage(@Part MultipartBody.Part imageFile);
}
