package gorden.album.demo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Arrays;

import gorden.album.AlbumPicker;
import gorden.album.demo.util.XLog;
import gorden.album.demo.widget.BottomMenuDialog;

import static gorden.album.AlbumPicker.KEY_IMAGES;
import static gorden.album.AlbumPicker.REQUEST_CAMERA;
import static gorden.album.AlbumPicker.REQUEST_CODE;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    ArrayList<String> imgs=new ArrayList<>();
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void showBottomMenu(final View view){
        new BottomMenuDialog.BottomMenuBuilder().addItem("拍照", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumPicker.builder().openCamera(MainActivity.this);
            }
        }).addItem("相册中选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumPicker.builder().multi(9).gridColumns(3).showGif(true).showCamera(true).start(MainActivity.this);
            }
        }).addItem("取消",null).build().show(getSupportFragmentManager());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XLog.e("requestCode is "+requestCode+"     resultCode  is "+resultCode);
        if (requestCode == REQUEST_CAMERA && resultCode==RESULT_OK){
            imgs.clear();
            XLog.e(AlbumPicker.mCurrentPhotoPath);
            imgs.add(AlbumPicker.mCurrentPhotoPath);
            adapter.notifyDataSetChanged();
        }
        if (requestCode == REQUEST_CODE && resultCode==RESULT_OK && data!=null){
            imgs.clear();
            imgs.addAll(data.getStringArrayListExtra(KEY_IMAGES));
            XLog.e(Arrays.toString(imgs.toArray()));
            adapter.notifyDataSetChanged();
        }

    }

    class MyAdapter extends RecyclerView.Adapter{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new RecyclerView.ViewHolder(new ImageView(MainActivity.this)) {
            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            Glide.with(MainActivity.this).load(imgs.get(position)).asBitmap().into((ImageView) holder.itemView);
        }

        @Override
        public int getItemCount() {
            return imgs.size();
        }
    }

}
