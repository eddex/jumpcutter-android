package com.eddex.jackle.jumpcutter.helpers;

import android.os.Environment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.inject.Inject;

/**
 * Wrapper class not testable file system calls etc.
 */
public class FileSystemWrapper {

    /**
     * Only used by Dagger framework for dependency injection.
     */
    @Inject
    public FileSystemWrapper() { }

    /**
     * Save a video to the jumpcutter directory.
     * @param video: The video to save to the filesystem.
     * @throws IOException: Error when writing to filesystem goes wrong.
     */
    public void saveDownloadedVideo(byte[] video) throws IOException {
        // create file
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "jumpcutter");

        String timestamp = new SimpleDateFormat("dd-MM-yyyy_HH-mm-ss", Locale.US).format(new Date());
        File videoFile = new File(dir, String.format("jumpcutter_video_%s", timestamp));

        // write content to file
        FileOutputStream fileStream = new FileOutputStream(videoFile);
        fileStream.write(video);
        fileStream.flush();
        fileStream.close();
    }
}
