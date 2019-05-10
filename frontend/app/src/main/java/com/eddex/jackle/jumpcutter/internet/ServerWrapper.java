package com.eddex.jackle.jumpcutter.internet;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerWrapper {

    private final OkHttpClient okHttpClient;
    private final String Scheme = "http";
    private final String Host = "jumpcutter.letum.ch"; // on emulator localhost = 10.0.2.2
    private final int Port = 80;

    @Inject
    public ServerWrapper(OkHttpClient okHttpClient) {
        this.okHttpClient = okHttpClient;
    }

    /**
     * Get a message from the server.
     * @return A Boolean indicating whether the server is online or not.
     */
    public Boolean ping() {

        HttpUrl url = new HttpUrl.Builder()
                .scheme(this.Scheme)
                .host(this.Host)
                //.port(this.Port)
                .build();

        System.out.println("ServerWrapper: ping() " + url);

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = this.okHttpClient.newCall(request).execute();
            System.out.print(response.body());
            return response.isSuccessful();
        }
        catch (IOException e) {
            return false;
        }
        catch (IllegalStateException e) {
            return false;
        }
    }

    /**
     * Upload a video from the local file system to the server.
     * @param video: The video file on the local file system.
     * @return The video id. This id can be used for the processVideo() method.
     */
    public String uploadVideo(File video) {

        HttpUrl url = new HttpUrl.Builder()
                .scheme(this.Scheme)
                .host(this.Host)
                //.port(this.Port)
                .addPathSegment("upload")
                .build();

        Log.d("ServerWrapper", "uploadVideo(), file exists: " + video.exists());
        Log.d("ServerWrapper", "File: " + video);

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart(
                        "file",
                        video.getName(),
                        RequestBody.create(MediaType.parse("video/"), video))
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return this.getStringResponseFromServer(request);
    }

    /**
     * Send a YouTube url to the server. The server then downloads and prepares the video for processing.
     * @param youtubeUrl: An url to a YouTube video.
     * @return The video id. This id can be used for the processVideo() method.
     */
    public String downloadYouTubeVideo(String youtubeUrl) {

        HttpUrl url = new HttpUrl.Builder()
                .scheme(this.Scheme)
                .host(this.Host)
                //.port(this.Port)
                .addPathSegment("youtube")
                .addQueryParameter("url", youtubeUrl)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return getStringResponseFromServer(request);
    }

    /**
     * Start processing the video on the server. Processing of a video can take a long time.
     * @param videoId: The id of the video to process.
     *               (This id is returned by the uploadVideo() or the downloadYouTubeVideo() method)
     * @return The download id of the processed video. Not equal to the video id!
     */
    public String processVideo(String videoId, SettingsProvider settingsProvider) {

        ProcessUrlBuilder urlBuilder = new ProcessUrlBuilder()
                .withHost(this.Host)
                //.withPort(this.Port)
                .withVideoId(videoId)
                .withSoundedSpeed(settingsProvider.getSoundSpeed())
                .withSilentSpeed(settingsProvider.getSilenceSpeed())
                .withSilentThreshold(settingsProvider.getSilenceThreshold())
                .withFrameMargin(settingsProvider.getFrameMargin());

        if (settingsProvider.getAdvancedOptionsSwitchEnabled()) {
            urlBuilder
                .withSampleRate(settingsProvider.getSampleRate())
                .withFrameRate(settingsProvider.getFrameRate())
                .withFrameQuality(settingsProvider.getFrameQuality());
        }

        HttpUrl url = urlBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return this.getStringResponseFromServer(request);
    }

    /**
     * Download a processed video.
     * @param downloadId: The download id returned by the processVideo() method.
     */
    public void downloadVideo(String downloadId) {

        HttpUrl url = new HttpUrl.Builder()
                .scheme(this.Scheme)
                .host(this.Host)
                //.port(this.Port)
                .addPathSegment("download")
                .addQueryParameter("download_id", downloadId)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        Response response = null;

        try {
            // get video
            response = this.okHttpClient.newCall(request).execute();
            byte[] video = response.body().bytes();

            // create file
            File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "jumpcutter");
            File videoFile = new File(dir, String.format("jumpcutter_video_{0}", new Timestamp(new Date().getTime()).toString()));

            // write content to file
            FileOutputStream fileStream = new FileOutputStream(videoFile);
            fileStream.write(video);
            fileStream.flush();
            fileStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


        // TODO: save video to directory used by MyVideosActivity
    }

    private String getStringResponseFromServer(Request request) {
        try {
            Response response = this.okHttpClient.newCall(request).execute();
            String responseValue = response.body().string();
            Log.d("ServerWrapper", responseValue);
            return responseValue;
        }
        catch (IOException e) {
            return null;
        }
        catch (IllegalStateException e) {
            return null;
        }
    }
}
