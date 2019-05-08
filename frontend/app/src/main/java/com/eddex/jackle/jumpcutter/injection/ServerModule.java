package com.eddex.jackle.jumpcutter.injection;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;

@Module
class ServerModule {
    
    @Provides
    OkHttpClient provideOkHttpClient() {
        return new OkHttpClient.Builder().build();
    }
}
