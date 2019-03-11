package com.lgy.netlibrary.retrifit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * @Author Li Gui Yun
 * @date 2019年03月11日11:44
 * @email guiyun.li@aihuishou.com
 **/
public class MyRetrofit {

    private static MyRetrofit getInstance() {
        return MyRetrofitViewHolder.MYRETROFIT;
    }

    final static class MyRetrofitViewHolder {
        final static MyRetrofit MYRETROFIT = new MyRetrofit();
    }

    public Retrofit createRetrofit() {
        // 设置超时
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(60, TimeUnit.SECONDS);
        builder.readTimeout(60, TimeUnit.SECONDS);
        builder.writeTimeout(60, TimeUnit.SECONDS);
        builder.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request request = chain.request();
                Request.Builder requestBuilder = request.newBuilder()
                        .addHeader("X-Requested-With", "XMLHttpRequest")
                        .addHeader("Accept", "application/json")
                        .addHeader("Version", "${BuildConfig.VERSION_CODE}")
                        .addHeader("Platform", "Android")
                        .method(request.method(), request.body());
                return chain.proceed(requestBuilder.build());
            }
        });
        return new Retrofit.Builder()
                // 设置请求的域名
                .baseUrl("")
                // 设置解析转换工厂，用自己定义的
                .addConverterFactory(ResponseConvert.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(builder.build())
                .build();

    }
}
