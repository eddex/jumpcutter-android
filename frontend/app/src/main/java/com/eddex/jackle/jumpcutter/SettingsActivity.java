package com.eddex.jackle.jumpcutter;

import android.content.ClipData;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settingsview);
        // replaces the FrameLayout from SettingsView with the actual preferences(fragment)
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.preferences_content, new SettingsFragment())
                .commit();
    }

    /**
     * Handles the different ways to share videos or YouTube links with this app.
     */
    public void startButtonClicked(View view) {
        Intent sourceIntent = getIntent();
        String type = sourceIntent.getType();

        Intent processIntent = new Intent(this, ProcessingActivity.class);

        if (type.startsWith("video/from_main_activity")) {
            handleFilePickerLocalVideo(sourceIntent, processIntent);
        }
        else if (type.startsWith("video/")) {
            handleSharedLocalVideo(sourceIntent, processIntent);
        }
        else if (type.startsWith("text/")) {
            handleSharedYouTubeLink(sourceIntent, processIntent);
        }
    }

    /**
     * Handles the case where a local file has been chosen with the file picker.
     * @param sourceIntent: The received intent.
     * @param processIntent: The intent to open the processing activity.
     */
    private void handleFilePickerLocalVideo(Intent sourceIntent, Intent processIntent) {

        Bundle extras = sourceIntent.getExtras();
        String path = extras.getString("videoUri");
        if (path == null) {
            throw new NullPointerException("videoUri or path was null???");
        }
        Uri localUri = Uri.parse(path);
        this.processLocalVideo(localUri, processIntent);
    }

    /**
     * Handles the case where a local file has been shared with this app.
     * @param sourceIntent: The received intent.
     * @param processIntent: The intent to open the processing activity.
     */
    private void handleSharedLocalVideo(Intent sourceIntent, Intent processIntent) {
        ClipData.Item item = sourceIntent.getClipData().getItemAt(0);
        Uri localUri = item.getUri();
        this.processLocalVideo(localUri, processIntent);
    }

    /**
     * Handles the case where a YouTube video has been shared with this app.
     * @param sourceIntent: The received intent.
     * @param processIntent: The intent to open the processing activity.
     */
    private void handleSharedYouTubeLink(Intent sourceIntent, Intent processIntent) {
        Bundle extras = sourceIntent.getExtras();
        String youTubeLink = extras.getString(Intent.EXTRA_TEXT);
        this.processYouTubeVideo(youTubeLink, processIntent);
    }

    /**
     * Open ProcessingActivity and start processing a video from the local file system.
     * @param localUri: The path to where the file can be found (as content:// URI)
     * @param processIntent: The intent to open the processing activity.
     */
    private void processLocalVideo(Uri localUri, Intent processIntent) {

        processIntent.setType("video/local");
        processIntent.putExtra("videoUri", localUri);
        this.startActivity(processIntent);
    }

    /**
     * Open ProcessingActivity and start processing a YouTube video.
     * @param processIntent: The intent to open the processing activity.
     */
    private void processYouTubeVideo(String youTubeLink, Intent processIntent) {

        processIntent.setType("video/youtube");
        processIntent.putExtra("youTubeLink", youTubeLink);
        this.startActivity(processIntent);
    }
}
