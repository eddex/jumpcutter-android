package com.eddex.jackle.jumpcutter;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class SettingsActivity extends AppCompatActivity {

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

        // TODO: upload video
    }

    private void UploadYoutubeVideo(Intent intent)
    {
        Bundle extras = intent.getExtras();
        String youtubeUrl = extras.getString(Intent.EXTRA_TEXT);

        // TODO: check if actually a youtube link
        // TODO: download youtube video
        // TODO: upload youtube video to service
    }
}
