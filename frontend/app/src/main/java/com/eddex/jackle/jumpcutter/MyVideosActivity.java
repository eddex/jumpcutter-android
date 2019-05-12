package com.eddex.jackle.jumpcutter;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class MyVideosActivity extends AppCompatActivity implements MyVideosRecyclerViewAdapter.ItemClickListener{

    private int REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION = 122;
    MyVideosRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myvideos);

        getExternalStorageWritePermission();
        FillViewWithVideos();
    }

    @Override
    public void onItemClick(View view, int position) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        // gets the file and parses teh path to an uri
        Uri intentUri = Uri.parse(adapter.getItem(position).getPath());
        intent.setDataAndType(intentUri, "video/*");
        startActivity(intent);
    }

    private void FillViewWithVideos() {
        RecyclerView recyclerView = findViewById(R.id.rvAnimals);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        File dir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MOVIES), "jumpcutter");
        dir.mkdir();
        adapter = new MyVideosRecyclerViewAdapter(this, new ArrayList<File>(Arrays.asList(dir.listFiles())));
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);
    }

    /**
     * Inflates (creates) menu
     * @param menu activity menu which will be inflated
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    /**
     * Occurs when menu item is clicked
     * @param item clicked menu item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.about:
                this.startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getExternalStorageWritePermission() {
        // Check whether this app has write external storage permission or not.
        int writeExternalStoragePermission = ContextCompat.checkSelfPermission(MyVideosActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (writeExternalStoragePermission!= PackageManager.PERMISSION_GRANTED) {
            // Request user to grant write external storage permission.
            ActivityCompat.requestPermissions(MyVideosActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_WRITE_EXTERNAL_STORAGE_PERMISSION) {
            int grantResultsLength = grantResults.length;
            if (grantResultsLength > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getApplicationContext(), "You grant write external storage permission.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "You denied write external storage permission.", Toast.LENGTH_LONG).show();
            }
        }
    }
}
