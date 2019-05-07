package com.eddex.jackle.jumpcutter;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eddex.jackle.jumpcutter.internet.ServerWrapper;

public class SettingsActivity extends AppCompatActivity {
    ServerWrapper server = new ServerWrapper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsActivityInitializer())
                .commit();
        HandleActivityCall();
    }

    public void HandleActivityCall()
    {
        Intent intent = getIntent();
        String type = intent.getType();
        if (type.contains("video/")) // sent a video
        {
            this.UploadLocalVideo(intent);
        }
        else if (type.contains("text/")) // expecting youtube url
        {
            this.UploadYoutubeVideo(intent);
        }
    }

    private void UploadLocalVideo(Intent intent)
    {
        ClipData.Item item = intent.getClipData().getItemAt(0);
        Uri localPath = item.getUri();
        server.uploadVideo(localPath);
    }

    private void UploadYoutubeVideo(Intent intent)
    {
        Bundle extras = intent.getExtras();
        String youtubeUrl = extras.getString(Intent.EXTRA_TEXT);
        if (youtubeUrl.contains("https://youtu.be/")) // shared from youtube app
        {
            server.downloadYouTubeVideo(youtubeUrl);
        }
    }
}
