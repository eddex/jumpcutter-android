package com.eddex.jackle.jumpcutter.internet;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.mock.MockInterceptor;

@RunWith(MockitoJUnitRunner.class)
public class ServerWrapperTest {

    @Test
    public void ping_ServerOnline_ReturnsTrue() {

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .get()
                .respond("ok");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        ServerWrapper server = new ServerWrapper(okHttpClient);
        Boolean response = server.ping();

        Assert.assertEquals(true, response);
    }

    @Test
    public void ping_ResourceNotFound_ReturnsFalse() {

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .get()
                .answer(request -> new Response.Builder().code(404));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        ServerWrapper server = new ServerWrapper(okHttpClient);
        Boolean response = server.ping();

        Assert.assertEquals(false, response);
    }
}
