package com.eddex.jackle.jumpcutter.injection;

import java.util.concurrent.TimeUnit;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
class ServerModule {
    
    @Provides
    OkHttpClient provideOkHttpClient() {

        // set all timeouts to 5min since the processing of the video can take a long time.
        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(5, TimeUnit.MINUTES)
                .connectTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .build();
        return client;
    }
}
