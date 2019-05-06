package com.eddex.jackle.jumpcutter.internet;

import android.net.Uri;

import okhttp3.HttpUrl;
import okhttp3.MultipartBody;
import okhttp3.Request;
import okhttp3.RequestBody;

public class ServerWrapper {

    public Boolean ping() {

        Request request = new Request.Builder()
                .url("0.0.0.0:80")
                .get()
                .build();

        // TODO: check if connection to server works.

        return true;
    }

    public String uploadVideo(Uri videoPath) {

        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM) // TODO: check if this type is correct
                //.addPart() // TODO: add video to POST request
                .build();

        Request request = new Request.Builder()
                .url("0.0.0.0:80/upload")
                .post(requestBody)
                .build();

        return null; // TODO: return video id returned by the request
    }

    public String processVideo(String videoId) {

        HttpUrl processUrl = new ProcessUrlBuilder()
                .withHost("0.0.0.0:80")
                .withSoundedSpeed("1.2")
                .withSilentSpeed("99")
                .Build();

        Request request = new Request.Builder()
                .url(processUrl)
                .get()
                .build();

        return null; // TODO: return download id returned by the request
    }

    public String downloadVideo(String downloadId) {

        Request request = new Request.Builder()
                .url("0.0.0.0:80/download")
                .get()
                .build();

        return null; // TODO: return video location on phone
    }
}
