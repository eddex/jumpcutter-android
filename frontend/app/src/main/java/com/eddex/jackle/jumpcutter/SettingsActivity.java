package com.eddex.jackle.jumpcutter;

import android.content.ClipData;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.eddex.jackle.jumpcutter.injection.DaggerServerComponent;
import com.eddex.jackle.jumpcutter.injection.ServerComponent;
import com.eddex.jackle.jumpcutter.internet.ServerWrapper;

public class SettingsActivity extends AppCompatActivity {

    ServerWrapper server;
    String processId;
    String downloadId;

    public SettingsActivity() {

        ServerComponent component = DaggerServerComponent.create();
        this.server = component.provideServerWrapper();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getFragmentManager()
                .beginTransaction()
                .replace(android.R.id.content, new SettingsActivityInitializer())
                .commit();
        handleActivityCall();
    }

    /**
     * Handles the different ways to share videos or video paths to this app
     */
    private void handleActivityCall() {
        Intent intent = getIntent();
        String type = intent.getType();

        //sent from main activity
        if (type.startsWith("video/from_main_activity")) {
            this.uploadSelectedLocalVideo(intent);
        }
        // video from filesystem sent per Share Button
        else if (type.startsWith("video/")) {
            this.uploadSharedLocalVideo(intent);
        }
        // youtube link share per share button (in youtube app)
        else if (type.startsWith("text/")) {
            this.uploadSharedYoutubeVideo(intent);
        }
    }

    /**
     * Uploads local video path to the server
     * Video was selected from the jumpcutter main activity
     * @param intent
     */
    private void uploadSelectedLocalVideo(Intent intent) {
        Bundle extras = intent.getExtras();
        String path = extras.getString("videoUri");
        if (path == null) {
            throw new NullPointerException("videoUri or path was null???");
        }
        Uri localUri = Uri.parse(path);
        AsyncTask.execute(() -> {
            String filePath = null;
            Log.d("","URI = "+ localUri);
            if (localUri != null && "content".equals(localUri.getScheme())) {
                Cursor cursor = this.getContentResolver().query(
                    localUri,
                    new String[] { android.provider.MediaStore.Video.VideoColumns.DATA },
                    null,
                    null,
                    null);
                cursor.moveToFirst();
                filePath = cursor.getString(0);
                cursor.close();
            } else {
                filePath = localUri.getPath();
            }
            Log.d("","Chosen path = "+ filePath);
            String id = server.uploadVideo(filePath);
            runOnUiThread(() -> processId = id);
        });
    }

    /**
     * Uploads local video path to the server
     * Video was shared from local filesystem, per share button
     * @param intent
     */
    private void uploadSharedLocalVideo(Intent intent) {
        ClipData.Item item = intent.getClipData().getItemAt(0);
        Uri localPath = item.getUri();
        AsyncTask.execute(() -> server.uploadVideo(localPath.toString()));
    }

    /**
     * Uploads a youtube video to the server
     * Youtube link shared per youtube app
     * @param intent
     */
    private void uploadSharedYoutubeVideo(Intent intent) {
        Bundle extras = intent.getExtras();
        String youtubeUrl = extras.getString(Intent.EXTRA_TEXT);

        if (youtubeUrl.contains("https://youtu.be/")) {
            AsyncTask.execute(() -> this.processId = server.downloadYouTubeVideo(youtubeUrl));
        } else {
            throw new IllegalArgumentException("not a youtube link");
        }
    }
}
