package com.twiceyuan.intercom.usecase;

import android.content.Context;
import android.net.Uri;

import com.twiceyuan.intercom.App;
import com.twiceyuan.intercom.common.RequestUtil;
import com.twiceyuan.intercom.model.remote.Result;
import com.twiceyuan.intercom.model.remote.UploadedImage;
import com.twiceyuan.intercom.service.ImageHostService;
import com.twiceyuan.log.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Observable;

/**
 * Created by twiceYuan on 9/3/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * 图片上传功能
 */
public class ImageHost {

    private final ImageHostService mService;
    private final Context mContext;

    @Inject @Singleton
    public ImageHost(App app, ImageHostService service) {
        mContext = app;
        mService = service;
    }

    public Observable<UploadedImage> uploadImage(Uri image) {
        if (image == null) {
            return Observable.error(new IllegalStateException("找不到该图片"));
        }

        InputStream inputStream = null;
        try {
            inputStream = mContext.getContentResolver().openInputStream(image);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (inputStream == null) {
            return Observable.error(new RuntimeException("图片读取失败, 上传失败"));
        }

        File tempFile = new File(mContext.getExternalCacheDir(), String.valueOf(inputStream.hashCode() + ".png"));
        FileOutputStream stream = null;

        try {
            stream = new FileOutputStream(tempFile);
            byte[] buffer = new byte[1024]; // Adjust if you want
            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                stream.write(buffer, 0, bytesRead);
            }
            stream.close();
            inputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        if (stream == null) {
            return Observable.error(new RuntimeException("上传失败"));
        }

        Observable<Result<UploadedImage>> uploadImage = mService.uploadImage(RequestUtil.INSTANCE.filePart("smfile", tempFile));
        uploadImage.doOnUnsubscribe(() -> {
            if (tempFile.exists()) {
                L.i("delete cache image: %s", tempFile.delete() ? "success" : "failed");
            }
        });

        return uploadImage.flatMap(result -> {
            if (result.code.equals("success")) {
                return Observable.just(result.data);
            } else {
                return Observable.error(new IllegalStateException("上传失败: " + result.msg));
            }
        });
    }
}
