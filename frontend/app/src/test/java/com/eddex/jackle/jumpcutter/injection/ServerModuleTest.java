package com.eddex.jackle.jumpcutter.injection;

import org.junit.Assert;
import org.junit.Test;

import okhttp3.OkHttpClient;

public class ServerModuleTest {

    @Test
    public void provideOkHttpClient_ReturnOkHttpClient() {

        ServerModule module = new ServerModule();

        OkHttpClient client = module.provideOkHttpClient();

        Assert.assertNotNull(client);
    }
}
