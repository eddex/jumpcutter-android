package com.eddex.jackle.jumpcutter.internet;

import org.junit.Assert;
import org.junit.Test;

import okhttp3.HttpUrl;

public class ProcessUrlBuilderTest {

    @Test
    public void build_WithHost_DefaultSchemeIsHttpsAndProcessPathSegmentAdded() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .build();

        Assert.assertEquals("https://test.tt/process", url.toString());
    }

    @Test
    public void build_WithSchemeHttp_SchemeIsHttp() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withScheme("http")
                .build();

        Assert.assertEquals("http://test.tt/process", url.toString());
    }

    @Test (expected = IllegalArgumentException.class)
    public void build_WithInvalidScheme_ThrowsIllegalArgumentException() {

        new ProcessUrlBuilder()
            .withHost("test.tt")
            .withScheme("asd")
            .build();
    }

    @Test
    public void build_WithPort_PortAdded() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withPort(8080)
                .build();

        Assert.assertEquals("https://test.tt:8080/process", url.toString());
    }

    @Test
    public void build_WithPort80_DefaultPortIgnored() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withPort(80)
                .build();

        Assert.assertEquals("https://test.tt/process", url.toString());
    }

    @Test
    public void build_NoPort_DefaultPortUsed() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .build();

        Assert.assertEquals("https://test.tt/process", url.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void build_InvalidPort_ThrowsIllegalArgumentException() {

        new ProcessUrlBuilder()
                .withHost("test.tt")
                .withPort(0)
                .build();
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

    @Test
    public void build_WithAllSettings_UrlCorrectlyConstructed() {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost("test.tt")
                .withPort(8080)
                .withVideoId("1")
                .withSilentThreshold("2")
                .withSoundedSpeed("3")
                .withSilentSpeed("4")
                .withFrameMargin("5")
                .withSampleRate("6")
                .withFrameRate("7")
                .withFrameQuality("8")
                .build();

        Assert.assertEquals(
            "https://test.tt:8080/process?video_id=1&silent_threshold=2&sounded_speed=3&silent_speed=4&frame_margin=5&sample_rate=6&frame_rate=7&frame_quality=8",
            url.toString());
    }
}
