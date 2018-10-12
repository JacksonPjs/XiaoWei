package com.xiaowei.net;

import com.google.gson.Gson;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * retrofit工具类
 */
public abstract class RetrofitUtils {



    private static Retrofit mRetrofit;

    /**
     * 获取Retrofit对象
     *
     * @return
     */
    protected static Retrofit getRetrofit() {

        if (null == mRetrofit) {


            //Retrofit2后使用build设计模式
            mRetrofit = new Retrofit.Builder()
                    //设置服务器路径
                    .baseUrl(NetService.API_SERVER)
                    //增加返回值为String的支持
//                    .addConverterFactory(ScalarsConverterFactory.create())
                    //添加转化库，默认是Gson
                    .addConverterFactory(GsonConverterFactory.create(new Gson()))
                    //添加自定义解析
//                    .addConverterFactory(CustomConverterFactory.create())
                    //添加回调库，采用RxJava
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(OkHttpUtils.getOkHttpClient())
                    .build();
        }

        return mRetrofit;
    }

}
