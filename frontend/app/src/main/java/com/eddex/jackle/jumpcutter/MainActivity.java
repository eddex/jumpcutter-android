package com.eddex.jackle.jumpcutter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.eddex.jackle.jumpcutter.injection.DaggerServerComponent;
import com.eddex.jackle.jumpcutter.injection.ServerComponent;
import com.eddex.jackle.jumpcutter.internet.ServerWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ServerComponent component = DaggerServerComponent.create();
        ServerWrapper server = component.provideServerWrapper();
        Context context = getApplicationContext();

        AsyncTask.execute(() -> {
            Boolean online = server.ping();
                runOnUiThread(() -> {
                    String message = online ? "server online: ready to convert." : "server offline: try again later.";
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                });
        });
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
            case R.id.myvideos:
                this.startActivity(new Intent(this, MyVideosActivity.class));
                return true;
            case R.id.about:
                this.startActivity(new Intent(this, AboutActivity.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Select a video from your local filesystem
     * The selected
     * @param view
     */
    public void selectLocalVideo(View view) {
        Intent intent = new Intent();
        intent.setType("video/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, "Select Video"), 0);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        else {
            Intent intent = new Intent(this, SettingsActivity.class);
            intent.setType("video/from_main_activity");
            intent.putExtra("videoUri", data.getDataString());
            this.startActivity(intent);
        }
    }
}
