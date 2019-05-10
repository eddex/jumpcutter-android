package com.eddex.jackle.jumpcutter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.eddex.jackle.jumpcutter.injection.DaggerServerComponent;
import com.eddex.jackle.jumpcutter.injection.ServerComponent;
import com.eddex.jackle.jumpcutter.internet.ServerWrapper;
import com.eddex.jackle.jumpcutter.internet.SettingsProvider;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

public class ProcessingActivity extends AppCompatActivity {

    ServerWrapper server;

    public ProcessingActivity() {

        // create ServerWrapper using dependency injection
        ServerComponent component = DaggerServerComponent.create();
        this.server = component.provideServerWrapper();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.processing);

        Intent intent = getIntent();
        String type = intent.getType();

        if (type != null && type.equals("video/local")) {

            this.processLocalVideo(intent);
        }
        else if (type != null && type.equals("video/youtube")) {

            this.processYouTubeVideo(intent);
        }
    }

    private void processLocalVideo(Intent intent) {

        Bundle extras = intent.getExtras();
        String path = extras.getString("videoUri");
        if (path == null) {
            throw new NullPointerException("videoUri or path was null???");
        }
        Uri localUri = Uri.parse(path);
        File videoCopy = getCopyFileFromUri(localUri);

        ProgressBar uploadProgressBar = this.findViewById(R.id.progressBar_upload);
        ProgressBar processingProgressBar = this.findViewById(R.id.progressBar_processing);
        ProgressBar downloadProgressBar = this.findViewById(R.id.progressBar_download);
        Button showMyVideosButton = this.findViewById(R.id.buttonShowMyVideos);
        Context context = getApplicationContext();

        AsyncTask.execute(() -> {
            uploadProgressBar.setProgress(33);
            String processId = this.server.uploadVideo(videoCopy);
            uploadProgressBar.setProgress(66);
            runOnUiThread(() -> Toast.makeText(context, "video uploaded", Toast.LENGTH_SHORT).show());
            uploadProgressBar.setProgress(100);

            if (this.server.HasError) {
                Log.e("ProcessingActivity", "Server error during upload.");
                return;
            }

            processingProgressBar.setProgress(33);
            String downloadId = this.server.processVideo(processId, new SettingsProvider(context));
            processingProgressBar.setProgress(66);
            runOnUiThread(() -> Toast.makeText(context, "video processed", Toast.LENGTH_SHORT).show());
            processingProgressBar.setProgress(100);

            if (this.server.HasError) {
                Log.e("ProcessingActivity", "Server error during processing.");
                return;
            }

            downloadProgressBar.setProgress(33);
            this.server.downloadVideo(downloadId);
            downloadProgressBar.setProgress(66);
            runOnUiThread(() -> Toast.makeText(context, "video downloaded", Toast.LENGTH_SHORT).show());
            downloadProgressBar.setProgress(100);

            runOnUiThread(() -> showMyVideosButton.setEnabled(true));
        });
    }

    private void processYouTubeVideo(Intent intent) {

        Bundle extras = intent.getExtras();
        String youTubeLink = extras.getString("youTubeLink");

        if (youTubeLink.contains("https://youtu.be/")) {
            AsyncTask.execute(() -> this.server.downloadYouTubeVideo(youTubeLink));
        } else {
            throw new IllegalArgumentException("not a youtube link");
        }
    }

    private File getCopyFileFromUri(Uri localUri)
    {
        File copy = new File( getFilesDir(),"copy.mp4");

        //Copy URI contents into temporary file.
        try {
            copy.delete();
            copy.createNewFile();
            InputStream in = getContentResolver().openInputStream(localUri);

            // copy data from uri source to temp file
            OutputStream out = new FileOutputStream(copy);
            byte[] buf = new byte[1024];
            int len;
            while ((len = in.read(buf)) > 0) {
                out.write(buf, 0, len);
            }
            out.close();
            in.close();

            return copy;
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void showProcessedVideos(View view) {
        Intent myVideosIntent = new Intent(this, MyVideosActivity.class);
        this.startActivity(myVideosIntent);
    }
}
