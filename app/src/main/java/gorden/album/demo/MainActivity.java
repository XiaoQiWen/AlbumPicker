package gorden.album.demo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.soundcloud.android.crop.Crop;
import com.theartofdev.edmodo.cropper.CropImage;
import com.yalantis.ucrop.UCrop;
import com.yalantis.ucrop.UCropActivity;
import com.yalantis.ucrop.model.AspectRatio;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

import gorden.album.AlbumPicker;
import gorden.album.demo.util.XLog;
import gorden.album.demo.widget.BottomMenuDialog;
import gorden.album.utils.SingleMediaScanner;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.util.SketchUtils;

import static gorden.album.AlbumPicker.KEY_DEL_IMAGES;
import static gorden.album.AlbumPicker.KEY_IMAGES;
import static gorden.album.AlbumPicker.REQUEST_CAMERA;
import static gorden.album.AlbumPicker.REQUEST_CODE;
import static gorden.album.AlbumPicker.REQUEST_DEL_CODE;
import static gorden.album.AlbumPicker.mCurrentPhotoPath;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    TextView textContent;
    ArrayList<String> imgs = new ArrayList<>();
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        textContent = (TextView) findViewById(R.id.textContent);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        imgs.add("/storage/emulated/0/tencent/QQfile_recv/qmsht.jpg");
        imgs.add("/storage/emulated/0/Tencent/QQ_Images/-8b8080f0cef83fa.jpg");
        adapter = new MyAdapter();
        recyclerView.setAdapter(adapter);
    }

    public void showBottomMenu(final View view) {
        new BottomMenuDialog.BottomMenuBuilder().addItem("拍照", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumPicker.builder().openCamera(MainActivity.this);
            }
        }).addItem("相册中选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumPicker.builder()
                        .multi(9)
                        .gridColumns(3)
                        .previewEnabled(true)
                        .showGif(true)
                        .showCamera(true)
                        .start(MainActivity.this);
            }
        }).addItem("继续选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumPicker.builder()
                        .multi(9)
                        .gridColumns(3)
                        .selectedPaths(imgs)
                        .previewEnabled(true)
                        .showGif(true)
                        .showCamera(false)
                        .start(MainActivity.this);
            }
        }).addItem("单选", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumPicker.builder().single().selectedPaths(imgs).start(MainActivity.this);
            }
        }).addItem("删除选择", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlbumPicker.builder().previewDelete(imgs, 0).start(MainActivity.this);
            }
        })
                .addItem("取消", null).build().show(getSupportFragmentManager());
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        XLog.e("requestCode is " + requestCode + "     resultCode  is " + resultCode);
        if (requestCode == REQUEST_CAMERA && resultCode == RESULT_OK) {
            imgs.clear();
            XLog.e(AlbumPicker.mCurrentPhotoPath);
            imgs.add(AlbumPicker.mCurrentPhotoPath);
            textContent.setText("拍照  :" + mCurrentPhotoPath);
            adapter.notifyDataSetChanged();
            new SingleMediaScanner(this).scanFile(AlbumPicker.mCurrentPhotoPath);
        }
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            imgs.clear();
            imgs.addAll(data.getStringArrayListExtra(KEY_IMAGES));
            textContent.setText("图片地址  :" + Arrays.toString(imgs.toArray()));
            XLog.e(Arrays.toString(imgs.toArray()));
            adapter.notifyDataSetChanged();
        }
        if (requestCode == REQUEST_DEL_CODE && resultCode == RESULT_OK && data != null) {
            ArrayList<String> del = data.getStringArrayListExtra(KEY_DEL_IMAGES);
            imgs.removeAll(del);
            textContent.setText("删除  :" + Arrays.toString(del.toArray()));
            XLog.e(Arrays.toString(imgs.toArray()));
            adapter.notifyDataSetChanged();
        }


    }

    class MyAdapter extends RecyclerView.Adapter {

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ImageHolder(new SketchImageView(MainActivity.this)) {

            };
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            ((SketchImageView) holder.itemView).setScaleType(ImageView.ScaleType.CENTER_CROP);
            ((SketchImageView) holder.itemView).displayImage(imgs.get(position));
        }

        @Override
        public int getItemCount() {
            return imgs.size();
        }


        class ImageHolder extends RecyclerView.ViewHolder {

            public ImageHolder(View itemView) {
                super(itemView);
                itemView.setLayoutParams(new RecyclerView.LayoutParams(SketchUtils.dp2px(getBaseContext(), 100), SketchUtils.dp2px(getBaseContext(), 100)));
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        UCrop.Options options = new UCrop.Options();
//                        options.setActiveWidgetColor(Color.BLACK);
//                        options.setShowCropGrid(false);
//                        options.setAllowedGestures(UCropActivity.ALL, UCropActivity.ALL, UCropActivity.ALL);
//                        options.setFreeStyleCropEnabled(false);
//                        options.setToolbarColor(Color.BLACK);
//                        options.setStatusBarColor(Color.BLACK);
                        options.setHideBottomControls(true);

//                        UCrop.of(Uri.fromFile(new File(imgs.get(getLayoutPosition()))), Uri.parse(""))
////                                .withMaxResultSize(800, 800)
//                                .withAspectRatio(4,4)
//                                .withOptions(options)
//                                .start(MainActivity.this);
                        Crop.of(Uri.fromFile(new File(imgs.get(getLayoutPosition()))), Uri.parse("")).asSquare().start(MainActivity.this);
//                        CropImage.activity(Uri.fromFile(new File(imgs.get(getLayoutPosition()))))
//                                .start(MainActivity.this);
//                        AlbumPicker.builder().preview(imgs, getLayoutPosition()).start(MainActivity.this);
                    }
                });
            }
        }
    }


    public void crop(String img) {
        File tempFile = new File("");
        Intent intent1 = new Intent("com.android.camera.action.CROP");
        intent1.setDataAndType(Uri.fromFile(new File(img)), "image/*");
        intent1.putExtra("crop", "true");
        intent1.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));//
        intent1.putExtra("aspectX", 1);
        intent1.putExtra("aspectY", 1);
        intent1.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent1.putExtra("outputX", 720);
        intent1.putExtra("outputY", 720);
        intent1.putExtra("circleCrop ", false);
        intent1.putExtra("return-data", false);
        intent1.putExtra("noFaceDetection", true);
        intent1.putExtra("scale", true);
        intent1.putExtra("scaleUpIfNeeded", true);
        startActivityForResult(intent1, 0x222);
    }

}
