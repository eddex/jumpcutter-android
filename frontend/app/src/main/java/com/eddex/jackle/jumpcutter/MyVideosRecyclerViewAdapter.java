package com.eddex.jackle.jumpcutter;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.provider.MediaStore;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class MyVideosRecyclerViewAdapter extends RecyclerView.Adapter<MyVideosRecyclerViewAdapter.ViewHolder> {
    private ArrayList<File> data;
    private LayoutInflater inflater;
    private ItemClickListener clickListener;

    // data is passed into the constructor
    public MyVideosRecyclerViewAdapter(Context context, ArrayList<File> data) {
        this.inflater = LayoutInflater.from(context);
        Collections.sort(data, new FileComparator());
        this.data = data;
    }

    private class FileComparator implements Comparator<File> {
        @Override
        public int compare(File s, File t) {
            return s.getName().compareTo(t.getName());
        }
    }

    /**
     * Inflates the row layout.
     */
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }


    /**
     * Binds the data to each row.
     */
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        File video = this.data.get(position);
        holder.myTextView.setText(video.getName());

        Bitmap bmThumbnail = ThumbnailUtils.createVideoThumbnail(video.getPath(), MediaStore.Video.Thumbnails.MICRO_KIND);
        holder.thumbnail.setImageBitmap(bmThumbnail);
    }

    /**
     * Item count used by another method.
     */
    @Override
    public int getItemCount() {
        return (this.data == null) ?  0 : this.data.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView myTextView;
        ImageView thumbnail;
        ImageButton deleteButton;

        ViewHolder(View itemView) {
            super(itemView);


            myTextView = itemView.findViewById(R.id.videoFilename);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            itemView.setOnClickListener(this);
            deleteButton = itemView.findViewById(R.id.delete_local_video_button);

            deleteButton.setOnClickListener(v -> {
                int index = getAdapterPosition();
                File toDelete = data.get(index);
                showDeleteDialog(toDelete, index);
            });
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    private void showDeleteDialog(File video, int index) {
        AlertDialog.Builder builder = new AlertDialog.Builder(inflater.getContext());

        builder.setTitle("Confirm")
                .setMessage("Are you sure?")
                .setPositiveButton("YES", (dialog, which) -> {
                    deleteVideo(video, index);
                    dialog.dismiss();
                })
                .setNegativeButton("NO", (dialog, which) -> dialog.dismiss());

        AlertDialog alert = builder.create();
        alert.show();
    }

    private void deleteVideo(File toDelete, int index) {
        if (toDelete.delete()) {
            this.data.remove(toDelete);
            notifyItemRangeChanged(index, this.data.size()-index+1);
        }
    }

    // convenience method for getting data at click position
    File getItem(int id) {
        return this.data.get(id);
    }

    /**
     * Allows clicks events to be caught.
     */
    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    /**
     * Parent activity will implement this method to respond to click events.
     */
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
