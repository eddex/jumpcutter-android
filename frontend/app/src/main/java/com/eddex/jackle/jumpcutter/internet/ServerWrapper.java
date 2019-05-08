package com.eddex.jackle.jumpcutter.internet;

import android.net.Uri;

import java.io.IOException;

import javax.inject.Inject;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ServerWrapper {

    private final OkHttpClient okHttpClient;
    private final String Scheme = "https";
    private final String Host = "jumpcutter.letum.ch";

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
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        try {
            Response response = okHttpClient.newCall(request).execute();
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
     * @param videoPath: The path to the video file on the file system.
     * @return The video id. This id can be used for the processVideo() method.
     */
    public String uploadVideo(Uri videoPath) {

        HttpUrl url = new HttpUrl.Builder()
                .scheme(this.Scheme)
                .host(this.Host)
                .addPathSegment("upload")
                .build();

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) // TODO: check if this type is correct
                //.addPart() // TODO: add video to POST request
                .build();

        Request request = new Request.Builder()
                .url(url)
                .post(requestBody)
                .build();

        return null; // TODO: return video id returned by the request
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
                .addPathSegment("youtube")
                .addQueryParameter("url", youtubeUrl)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        // TODO: send request asynchronously.

        return null;
    }

    /**
     * Start processing the video on the server. Processing of a video can take a long time.
     * @param videoId: The id of the video to process.
     *               (This id is returned by the uploadVideo() or the downloadYouTubeVideo() method)
     * @return The download id of the processed video. Not equal to the video id!
     */
    public String processVideo(String videoId) {

        HttpUrl url = new ProcessUrlBuilder()
                .withHost(this.Host)
                .withSoundedSpeed("1.2")
                .withSilentSpeed("99")
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return null; // TODO: return download id returned by the request
    }

    /**
     * Download a processed video.
     * @param downloadId: The download id returned by the processVideo() method.
     * @return The local path to the downloaded video.
     */
    public String downloadVideo(String downloadId) {

        HttpUrl url = new HttpUrl.Builder()
                .scheme(this.Scheme)
                .host(this.Host)
                .addPathSegment("download")
                .addQueryParameter("download_id", downloadId)
                .build();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .build();

        return null; // TODO: return video location on phone
    }
}
