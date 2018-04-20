package com.example.thaib.testtab.album;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.example.thaib.testtab.R;
import com.example.thaib.testtab.Utils.FolderFileFilter;
import com.example.thaib.testtab.dialog.ConfirmDialog;
import com.example.thaib.testtab.dialog.InputDialog;

import java.io.File;
import java.util.LinkedList;

/**
 * Created by thaib on 17/04/2018.
 */

public class AlbumController {

    private Context context;
    private AlbumAdapter albumAdapter;
    public static final  File albumsLocation = new File(Environment.getExternalStorageDirectory(), "Albums06/");

    public AlbumController(Context context,AlbumAdapter albumAdapter){
        this.context=context;
        this.albumAdapter=albumAdapter;
        if(!albumsLocation.exists())
           albumsLocation.mkdirs();

    }

    public static LinkedList<Album> getAlbums(){
        LinkedList<Album> albums = new LinkedList<>();
        File[] folders = albumsLocation.listFiles(new FolderFileFilter());
        for (File folder : folders){
            albums.add(new Album(folder));
        }
        return albums;
    }

    public void addNewAblbum(){
        InputDialog inputDialog=new InputDialog(context,"Nhập tên album mới",
                "Thêm","Hủy","New Album") {
            @Override
            public void onPositiveButtonClick(AlertDialog inputDialog,String output) {
                if(output.isEmpty()){
                    Toast.makeText(context,"Tên album không được rỗng",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(output.startsWith(".")){
                    Toast.makeText(context,"Tên album không được bắt đầu bằng dấu ."
                            ,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(albumAdapter.containsAlbum(output)){
                    Toast.makeText(context,"Tên album đã tồn tại",Toast.LENGTH_SHORT).show();
                    return;
                }
                File newAlbumFile = new File(albumsLocation,output+"/");
                Album newAlbum = new Album(newAlbumFile);
                if(!albumAdapter.addAlbum(newAlbum)){
                    Toast.makeText(context,"Không thể tạo album",Toast.LENGTH_SHORT).show();
                    return;
                }
                inputDialog.cancel();
            }
        };
        inputDialog.showDialog();
    }

    public void deleteAlbum(final Album album){
        ConfirmDialog confirmDialog=new ConfirmDialog(context,
                "Bạn có muốn xóa album: "+album.getName()
                ,"Xóa","hủy") {
            @Override
            public void onPositiveButtonClick() {
                if(!albumAdapter.deleteAlbum(album)){
                    Toast.makeText(context,"Không ẩn tạo album",Toast.LENGTH_SHORT).show();
                }
            }
        };
        confirmDialog.showDialog();
    }

    public void renameAlbum(final Album album){
        InputDialog inputDialog=new InputDialog(context,"Nhập tên album:",
                "Đổi Tên","Hủy",album.getName()) {
            @Override
            public void onPositiveButtonClick(AlertDialog inputDialog,String output) {
                if(output.isEmpty()){
                    Toast.makeText(context,"Tên album không được rỗng",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(output.startsWith(".")){
                    Toast.makeText(context,"Tên album không được bắt đầu bằng dấu ."
                            ,Toast.LENGTH_SHORT).show();
                    return;
                }
                if(output.equals(album.getName())){
                    inputDialog.cancel();
                    return;
                }
                if(albumAdapter.containsAlbum(output)){
                    Toast.makeText(context,"Tên album đã tồn tại",Toast.LENGTH_SHORT).show();
                    return;
                }
                if(!albumAdapter.renameAlbum(album,output)){
                    Toast.makeText(context,"Không thể đổi tên album",Toast.LENGTH_SHORT).show();
                    return;
                }
                inputDialog.cancel();
            }
        };
        inputDialog.showDialog();
    }

    public void hideAlbum(final Album album){
        ConfirmDialog confirmDialog=new ConfirmDialog(context,
                "Bạn có muốn ẩn album: "+album.getName()
                ,"Ẩn","Hủy") {
            @Override
            public void onPositiveButtonClick() {
                if(!albumAdapter.hideAlbum(album)){
                    Toast.makeText(context,"Không ẩn tạo album",Toast.LENGTH_SHORT).show();
                }
            }
        };
        confirmDialog.showDialog();
    }

    public void moveAlbum(final Album album){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.select_album_dialog);
        View view=dialog.getWindow().getDecorView();

        final ListView listView=view.findViewById(R.id.listAlbumSelect);

        Button btnCancelMove = view.findViewById(R.id.btnCancelMove);
        btnCancelMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.cancel();
            }
        });

        SelectAlbumAdapter albumAdapter_= new SelectAlbumAdapter(context, albumAdapter.getAlbums()) {
            @Override
            void onRowClick(Album clickedAlbum) {
                //albumAdapter.
                albumAdapter.moveAlbum(album,clickedAlbum);
                dialog.cancel();
            }
        };
        listView.setAdapter(albumAdapter_);
        dialog.show();
    }
}
