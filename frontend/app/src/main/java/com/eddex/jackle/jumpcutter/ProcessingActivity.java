package com.eddex.jackle.jumpcutter;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.eddex.jackle.jumpcutter.injection.DaggerServerComponent;
import com.eddex.jackle.jumpcutter.injection.ServerComponent;
import com.eddex.jackle.jumpcutter.internet.ServerWrapper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class ProcessingActivity extends AppCompatActivity {

    ServerWrapper server;
    String processId;
    String downloadId;

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
        AsyncTask.execute(() -> this.processId = this.server.uploadVideo(videoCopy));
    }

    private void processYouTubeVideo(Intent intent) {

        Bundle extras = intent.getExtras();
        String youTubeLink = extras.getString("youTubeLink");

        if (youTubeLink.contains("https://youtu.be/")) {
            AsyncTask.execute(() -> this.processId = this.server.downloadYouTubeVideo(youTubeLink));
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
}
