package com.example.thaib.testtab.album;

import android.app.Activity;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.thaib.testtab.R;
import com.example.thaib.testtab.Utils.FileUtils;

import java.io.File;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;

/**
 * Created by thaib on 17/04/2018.
 */

public class AlbumAdapter extends ArrayAdapter {

    private Context context;
    private LinkedList<Album> albums;
    public static final File albumsLocation = new File(Environment.getExternalStorageDirectory(), "Albums06/");

    public AlbumAdapter(Context context, LinkedList<Album> albums) {
        super(context, R.layout.album_list_item, albums);
        this.context = context;
        this.albums = albums;
//        for (Album album:albums) {
//            Glide.with(context).load(album.getLastestImage()).preload();
//        }
    }

    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        AlbumViewHolder myViewHolder;
        View row = convertView;
        if (row == null) {
            LayoutInflater layout = ((Activity) context).getLayoutInflater();
            row = layout.inflate(R.layout.album_list_item, null);
            myViewHolder = new AlbumViewHolder();
            myViewHolder.imgAlbum = (ImageView) row.findViewById(R.id.ImgAlbum);
            myViewHolder.txtNameAlbum = (TextView) row.findViewById(R.id.txtNameAlbum);
            myViewHolder.txtItem = (TextView) row.findViewById(R.id.txtItem);
            row.setTag(myViewHolder);
        } else {
            myViewHolder = (AlbumViewHolder) row.getTag();
        }

        Album album = albums.get(position);
        myViewHolder.txtNameAlbum.setText(album.getName());
        myViewHolder.txtItem.setText(String.valueOf(album.getSize()));

        File lastedImage = album.getLastestImage();
        if (lastedImage == null) {
            Glide.with(this.context).load(R.drawable.empty_album)
                    .fitCenter().into(myViewHolder.imgAlbum);
        } else {
            Glide.with(this.context).load(lastedImage)
                    .placeholder(R.drawable.empty_album)
                    .fitCenter().priority(Priority.IMMEDIATE)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(myViewHolder.imgAlbum);
        }

        return (row);
    }

    public boolean containsAlbum(String name) {
        for (Album album : albums) {
            if (album.getName().equals(name))
                return true;
        }
        return false;
    }

    public boolean addAlbum(Album album) {
        File newAlbumFile = album.getFile();
        boolean result = newAlbumFile.mkdirs();
        if (result)
            albums.add(album);
        this.notifyDataSetChanged();
        return result;
    }

    public boolean deleteAlbum(Album album) {
        boolean res = false;
        if (album != null) {
            File file = album.getFile();
            FileUtils.deleteDirectory(file);
            res = albums.remove(album);
            this.notifyDataSetChanged();
        }
        return res;
    }

    public boolean renameAlbum(Album album, String newName) {
        File file = album.getFile();
        File newFile = new File(albumsLocation, newName);
        boolean res = file.renameTo(newFile);
        if (res) {
            album.setFile(newFile);
            notifyDataSetChanged();
        }
        return res;
    }

    public boolean hideAlbum(Album album) {
        File file = album.getFile();
        File newFile = new File(albumsLocation, "." + album.getName());
        if (!newFile.exists()) {
            boolean res = file.renameTo(newFile);
            if (res) {
                albums.remove(album);
                notifyDataSetChanged();
            }
            return res;
        }
        return false;
    }

    public boolean moveAlbum(Album oAlbum, Album nAlbum) {
        if (oAlbum.equals(nAlbum)) {
            return true;
        }
        LinkedList<File> oImgs = oAlbum.getImages();
        File newFolder = nAlbum.getFile();

        for (File img : oImgs) {
            File to = new File(newFolder, img.getName());
            boolean res = img.renameTo(to);
            if (!res) {
                return res;
            }
            nAlbum.addImage(to);
        }
        oAlbum.clearAlbum();
        this.notifyDataSetChanged();
        return true;
    }

    public void reloadList() {
        this.albums = albums;
        this.notifyDataSetChanged();
    }

    public LinkedList<Album> getAlbums() {
        return albums;
    }

    public void sortAlbum(final SortType type) {
        Collections.sort(albums, new Comparator<Album>() {
            @Override
            public int compare(Album o1, Album o2) {
                switch (type) {
                    case NAME_A_Z:
                        return o1.getName().compareTo(o2.getName());
                    case NAME_Z_A:
                        return o2.getName().compareTo(o1.getName());
                    case ITEMS_INC:
                        return o1.getSize() - o2.getSize();
                    case ITEMS_DESC:
                        return o2.getSize() - o1.getSize();
                }
                return o1.getName().compareTo(o2.getName());
            }
        });
        this.notifyDataSetChanged();
    }
}
