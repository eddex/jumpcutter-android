package com.eddex.jackle.jumpcutter.internet;

import com.eddex.jackle.jumpcutter.helpers.FileSystemWrapper;
import com.eddex.jackle.jumpcutter.helpers.SettingsProvider;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import okhttp3.mock.MockInterceptor;

import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ServerWrapperTest {

    @Test
    public void ping_ServerOnline_ReturnsTrue() {

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .get()
                .url("https://jumpcutter.letum.ch/")
                .respond("ok");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        ServerWrapper server = new ServerWrapper(okHttpClient, null);
        Boolean response = server.ping();

        Assert.assertEquals(true, response);
    }

    @Test
    public void ping_ResourceNotFound_ReturnsFalse() {

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .get()
                .url("https://jumpcutter.letum.ch/")
                .answer(request -> new Response.Builder().code(404));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        ServerWrapper server = new ServerWrapper(okHttpClient, null);
        Boolean response = server.ping();

        Assert.assertEquals(false, response);
    }

    @Test(expected = NullPointerException.class)
    public void uploadVideo_FileIsNull_ThrowsNullPointerException() {

        ServerWrapper server = new ServerWrapper(null, null);

        server.downloadVideo(null);
    }

    @Test
    public void uploadVideo_VideoUploaded_ReturnsVideoId() {

        final String ExpectedVideoId = "video_id_1";

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .post()
                .url("https://jumpcutter.letum.ch/upload")
                .respond(ExpectedVideoId);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        File videoFile = new File("./src/test/java/com/eddex/jackle/jumpcutter/internet/test.mp4");
        ServerWrapper server = new ServerWrapper(okHttpClient, null);
        String videoId = server.uploadVideo(videoFile);

        Assert.assertEquals(ExpectedVideoId, videoId);
    }

    @Test
    public void uploadVideo_VideoUploadFailed_ReturnsNull() {

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .post()
                .url("https://jumpcutter.letum.ch/upload")
                .answer(request -> new Response.Builder().code(404));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        File videoFile = new File("./src/test/java/com/eddex/jackle/jumpcutter/internet/test.mp4");
        ServerWrapper server = new ServerWrapper(okHttpClient, null);
        String videoId = server.uploadVideo(videoFile);

        Assert.assertEquals(null, videoId);
    }

    @Test(expected = NullPointerException.class)
    public void processVideo_NoSettingsProvider_ThrowsNullPointerException() {

        ServerWrapper server = new ServerWrapper(null, null);

        server.processVideo(null, null);
    }

    @Test
    public void processVideo_NoAdvancedOptions_ServerCallAsExpectedAndDownloadIdReturned() {

        String ExpectedDownloadId = "download_id";

        String SoundedSpeed = "1";
        String SilentSpeed = "2";
        String SilenceThreshold = "3";
        String FrameMargin = "4";

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .get()
                .url(new ProcessUrlBuilder()
                        .withHost("jumpcutter.letum.ch")
                        .withSoundedSpeed(SoundedSpeed)
                        .withSilentSpeed(SilentSpeed)
                        .withSilentThreshold(SilenceThreshold)
                        .withFrameMargin(FrameMargin)
                        .build()
                        .toString())
                .respond(ExpectedDownloadId);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        SettingsProvider settingsProviderMock = Mockito.mock(SettingsProvider.class);
        when(settingsProviderMock.getSoundSpeed())
                .thenReturn(SoundedSpeed);
        when(settingsProviderMock.getSilenceSpeed())
                .thenReturn(SilentSpeed);
        when(settingsProviderMock.getSilenceThreshold())
                .thenReturn(SilenceThreshold);
        when(settingsProviderMock.getFrameMargin())
                .thenReturn(FrameMargin);
        when(settingsProviderMock.getAdvancedOptionsSwitchEnabled())
                .thenReturn(false);

        ServerWrapper server = new ServerWrapper(okHttpClient, null);
        String downloadId = server.processVideo(null, settingsProviderMock);

        Assert.assertEquals(ExpectedDownloadId, downloadId);
    }

    @Test
    public void processVideo_WithAdvancedOptions_ServerCallAsExpectedAndDownloadIdReturned() {

        String ExpectedDownloadId = "download_id";

        String SoundedSpeed = "1";
        String SilentSpeed = "2";
        String SilenceThreshold = "3";
        String FrameMargin = "4";
        String FrameRate = "5";
        String FrameQuality = "6";
        String SampleRate = "7";

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .get()
                .url(new ProcessUrlBuilder()
                        .withHost("jumpcutter.letum.ch")
                        .withSoundedSpeed(SoundedSpeed)
                        .withSilentSpeed(SilentSpeed)
                        .withSilentThreshold(SilenceThreshold)
                        .withFrameMargin(FrameMargin)
                        .withFrameRate(FrameRate)
                        .withFrameQuality(FrameQuality)
                        .withSampleRate(SampleRate)
                        .build()
                        .toString())
                .respond(ExpectedDownloadId);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        SettingsProvider settingsProviderMock = Mockito.mock(SettingsProvider.class);
        when(settingsProviderMock.getSoundSpeed())
                .thenReturn(SoundedSpeed);
        when(settingsProviderMock.getSilenceSpeed())
                .thenReturn(SilentSpeed);
        when(settingsProviderMock.getSilenceThreshold())
                .thenReturn(SilenceThreshold);
        when(settingsProviderMock.getFrameMargin())
                .thenReturn(FrameMargin);
        when(settingsProviderMock.getFrameRate())
                .thenReturn(FrameRate);
        when(settingsProviderMock.getFrameQuality())
                .thenReturn(FrameQuality);
        when(settingsProviderMock.getSampleRate())
                .thenReturn(SampleRate);
        when(settingsProviderMock.getAdvancedOptionsSwitchEnabled())
                .thenReturn(true);

        ServerWrapper server = new ServerWrapper(okHttpClient, null);
        String downloadId = server.processVideo(null, settingsProviderMock);

        Assert.assertEquals(ExpectedDownloadId, downloadId);
    }

    @Test
    public void processVideo_ProcessingFailed_ReturnsNull() {

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .get()
                .urlStarts("https://jumpcutter.letum.ch/process")
                .answer(request -> new Response.Builder().code(500));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        ServerWrapper server = new ServerWrapper(okHttpClient, null);
        SettingsProvider settingsProviderMock = Mockito.mock(SettingsProvider.class);
        String response = server.processVideo(null, settingsProviderMock);

        Assert.assertEquals(null, response);
    }

    @Test
    public void downloadVideo_DownloadFailed_ReturnsFalse() {

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .get()
                .urlStarts("https://jumpcutter.letum.ch/download?download_id")
                .answer(request -> new Response.Builder().code(500));
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        ServerWrapper server = new ServerWrapper(okHttpClient, null);
        Boolean response = server.downloadVideo(null);

        Assert.assertEquals(false, response);
    }

    @Test
    public void downloadVideo_DownloadSuccessful_ReturnsTrue() {

        final String DownloadId = "download_id";

        MockInterceptor okHttpMockInterceptor = new MockInterceptor();
        okHttpMockInterceptor.addRule()
                .get()
                .urlStarts("https://jumpcutter.letum.ch/download?download_id=" + DownloadId)
                .respond("I am video");
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .addInterceptor(okHttpMockInterceptor)
                .build();

        FileSystemWrapper fileSystemWrapperMock = Mockito.mock(FileSystemWrapper.class);

        ServerWrapper server = new ServerWrapper(okHttpClient, fileSystemWrapperMock);
        Boolean response = server.downloadVideo(DownloadId);

        Assert.assertEquals(true, response);
    }
}
