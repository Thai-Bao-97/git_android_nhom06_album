package com.example.thaib.testtab.album;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.thaib.testtab.R;

import java.util.LinkedList;

public class AlbumFragment extends Fragment {

    private Context context;
    private ListView listAlbum;
    private AlbumController albumController;


    public AlbumFragment() {


    }

    public static AlbumFragment newInstance(Context context) {
        AlbumFragment fragment = new AlbumFragment();
        fragment.context = context;
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.album_fragment, container, false);

        listAlbum = rootView.findViewById(R.id.listAlbum);
        LinkedList<Album> albums = AlbumController.getAlbums();
        AlbumAdapter albumAdapter = new AlbumAdapter(context, albums);
        listAlbum.setAdapter(albumAdapter);

        View footerV = new View(context);
        footerV.setMinimumHeight(350);
        footerV.setOnClickListener(null);
        listAlbum.addFooterView(footerV);

        albumController = new AlbumController(context, albumAdapter);

        FloatingActionButton fab = rootView.findViewById(R.id.fabAlbum);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                albumController.addNewAblbum();
            }
        });

        registerForContextMenu(listAlbum);

        return rootView;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if (v.getId() == R.id.listAlbum) {
            ListView lv = (ListView) v;
            AdapterView.AdapterContextMenuInfo acmi = (AdapterView.AdapterContextMenuInfo) menuInfo;
            Album album = (Album) lv.getItemAtPosition(acmi.position);
            menu.setHeaderTitle("Album " + album.getFile().getName());
            getActivity().getMenuInflater().inflate(R.menu.menu_album, menu);
        }
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        Album album = (Album) listAlbum.getItemAtPosition(info.position);
        switch (item.getItemId()) {
            case R.id.albumDelete:
                albumController.deleteAlbum(album);
                break;
            case R.id.albumRename:
                albumController.renameAlbum(album);
                break;
            case R.id.albumHide:
                albumController.hideAlbum(album);
                break;
            case R.id.albumMove:
                albumController.moveAlbum(album);
                break;
        }
        return super.onContextItemSelected(item);
    }
}
