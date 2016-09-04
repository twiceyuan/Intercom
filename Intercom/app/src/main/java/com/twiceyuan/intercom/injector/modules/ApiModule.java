package com.twiceyuan.intercom.injector.modules;

import com.twiceyuan.intercom.service.ImageHostService;
import com.twiceyuan.log.L;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by twiceYuan on 8/30/16.
 * Email: i@twiceyuan.com
 * Site: http://twiceyuan.com
 *
 * Retrofit REST API 对象模块
 */
@Module
public class ApiModule {

    private Retrofit.Builder mRetrofit;
    private L.Logger mLogger;

    public ApiModule() {
        mLogger = L.createLogger().setTag(this);
        mRetrofit = new Retrofit.Builder()
                .client(buildClient())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create());
    }

    OkHttpClient buildClient() {

        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // set time out interval
        builder.readTimeout(10, TimeUnit.MINUTES);
        builder.connectTimeout(10, TimeUnit.MINUTES);
        builder.writeTimeout(10, TimeUnit.MINUTES);

        // print Log
        builder.interceptors().add(buildLoggerInterceptor());
        return builder.build();
    }

    HttpLoggingInterceptor buildLoggerInterceptor() {
        return new HttpLoggingInterceptor(message -> {
            if (message.startsWith("{")) {
                mLogger.json(message);
            } else {
                mLogger.i(message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY);
    }

    /**
     * 啊哈哈哈哈哈图床 API
     *
     * @return 获得图床 API
     */
    @Provides @Singleton
    public ImageHostService provideImageHost() {
        return mRetrofit.baseUrl("https://sm.ms").build().create(ImageHostService.class);
    }
}
