package com.example.toeicapplication.di;

import com.example.toeicapplication.network.service.ResultService;
import com.example.toeicapplication.network.service.UserService;
import com.example.toeicapplication.utilities.AppConstants;
import com.google.gson.Gson;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.components.SingletonComponent;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module
@InstallIn(SingletonComponent.class)
public class NetworkModule {
    @Singleton
    @Provides
    public OkHttpClient provideOkHttpClient(){
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .connectTimeout(AppConstants.NETWORK_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(AppConstants.NETWORK_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(AppConstants.NETWORK_TIME_OUT, TimeUnit.SECONDS);

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        httpClient.addInterceptor(interceptor);

        httpClient.addInterceptor(chain -> {
            Request original = chain.request();
            Request.Builder requestBuilder = original.newBuilder()
                    .addHeader("Accept", AppConstants.CONTENT_TYPE)
                    .addHeader("Content-Type", AppConstants.CONTENT_TYPE);

            return chain.proceed(requestBuilder.build());
        });

        return httpClient.build();
    }

    @Singleton
    @Provides
    public Retrofit provideRetrofit(Gson gson, OkHttpClient httpClient){
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .baseUrl(AppConstants.API_ENDPOINT)
                .client(httpClient)
                .build();
    }

    @Singleton
    @Provides
    public UserService provideUserService(Retrofit retrofit){
        return retrofit.create(UserService.class);
    }

    @Singleton
    @Provides
    public ResultService provideResultService(Retrofit retrofit){
        return retrofit.create(ResultService.class);
    }
}
