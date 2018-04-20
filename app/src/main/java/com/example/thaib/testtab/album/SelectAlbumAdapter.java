package com.example.thaib.testtab.album;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.thaib.testtab.R;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;

public abstract class SelectAlbumAdapter extends ArrayAdapter {
    private Context context;
    private LinkedList<Album> albums;
    public static final File albumsLocation = new File(Environment.getExternalStorageDirectory(), "Albums06/");

    public SelectAlbumAdapter(Context context, LinkedList<Album> albums) {
        super(context,R.layout.album_list_item,albums);
        this.context=context;
        this.albums=albums;
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        AlbumViewHolder myViewHolder;
        View row =convertView;
        if(row == null) {
            LayoutInflater layout = ((Activity) context).getLayoutInflater();
            row = layout.inflate(R.layout.select_album_item, null);
            myViewHolder=new AlbumViewHolder();
            myViewHolder.imgAlbum = (ImageView) row.findViewById(R.id.ImgAlbum);
            myViewHolder.txtNameAlbum = (TextView) row.findViewById(R.id.txtNameAlbum);
            myViewHolder.txtItem = (TextView) row.findViewById(R.id.txtItem);
            row.setTag(myViewHolder);
        }else {
            myViewHolder=(AlbumViewHolder)row.getTag();
        }

        final Album album=albums.get(position);
        myViewHolder.txtNameAlbum.setText(album.getName());
        myViewHolder.txtItem.setText( String.valueOf(album.getSize()));

        final View finalRow = row;
        row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onRowClick(album);
            }
        });
        return (row);
    }

    abstract void onRowClick(Album clickedAlbum);
}
