package com.eddex.jackle.jumpcutter.internet;

import org.junit.Assert;
import org.junit.Test;

import okhttp3.HttpUrl;

public class ProcessUrlBuilderTest {

    @Test
    public void build_WithHost_SchemeIsHttpsAndProcessPathSegmentAdded() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .build();

        Assert.assertEquals("https://test.tt/process", url.toString());
    }

    @Test
    public void build_WithVideoId_AddedAsQueryParameter() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withVideoId("123")
                .build();

        Assert.assertEquals("https://test.tt/process?video_id=123", url.toString());
    }

    @Test
    public void build_WithSilentThreshold_AddedAsQueryParameter() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withSilentThreshold("17")
                .build();

        Assert.assertEquals("https://test.tt/process?silent_threshold=17", url.toString());
    }

    @Test
    public void build_WithSoundedSpeed_AddedAsQueryParameter() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withSoundedSpeed("99")
                .build();

        Assert.assertEquals("https://test.tt/process?sounded_speed=99", url.toString());
    }

    @Test
    public void build_WithSilentSpeed_AddedAsQueryParameter() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withSilentSpeed("1")
                .build();

        Assert.assertEquals("https://test.tt/process?silent_speed=1", url.toString());
    }

    @Test
    public void build_WithFrameMargin_AddedAsQueryParameter() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withFrameMargin("3")
                .build();

        Assert.assertEquals("https://test.tt/process?frame_margin=3", url.toString());
    }

    @Test
    public void build_WithSampleRate_AddedAsQueryParameter() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withSampleRate("5")
                .build();

        Assert.assertEquals("https://test.tt/process?sample_rate=5", url.toString());
    }

    @Test
    public void build_WithFrameRate_AddedAsQueryParameter() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withFrameRate("30")
                .build();

        Assert.assertEquals("https://test.tt/process?frame_rate=30", url.toString());
    }

    @Test
    public void build_WithFrameQuality_AddedAsQueryParameter() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withFrameQuality("100")
                .build();

        Assert.assertEquals("https://test.tt/process?frame_quality=100", url.toString());
    }
}
