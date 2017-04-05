package gorden.album;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import gorden.album.fragment.AlbumPickerFragment;
import gorden.album.loader.ImageLoader;
import gorden.album.utils.PermissionsUtils;

/**
 * 默认单选模式、显示gif 每排3张图 显示相机
 * TODO cropFilePath(String) 添加启动配置
 */
@SuppressWarnings({"unused","WeakerAccess"})
public class AlbumPicker {
    public static final int REQUEST_CODE = 10233;
    public static final int REQUEST_CAMERA = 10234;

    public static final String KEY_IMAGES = "KEY_IMAGES";

    public final static String EXTRA_SELECT_MODE = "EXTRA_SELECT_MODE"; //选择模式、单选 多选
    public final static String EXTRA_MAX_COUNT = "MAX_COUNT";   //多选模式,最多选择多少张
    public final static String EXTRA_SHOW_CAMERA = "SHOW_CAMERA";//显示相机
    public final static String EXTRA_SHOW_GIF = "SHOW_GIF";//显示gif
    public final static String EXTRA_GRID_COLUMN = "GRID_COLUMN";//每排显示多少张图
    public final static String EXTRA_SELECTED_PATH = "SELECTED_PATH";//已选择图片的地址
    public final static String EXTRA_PREVIEW_ENABLED = "PREVIEW_ENABLED";//是否可以预览

    public final static String EXTRA_IMAGE_LOADER = "EXTRA_IMAGE_LOADER";//图片加载器

    public static String mCurrentPhotoPath = null;

    public static AlbumPickerBuilder builder() {
        return new AlbumPickerBuilder();
    }

    public static class AlbumPickerBuilder {
        private Bundle optionsBundle;
        private Intent pickerIntent;

        public AlbumPickerBuilder() {
            this.optionsBundle = new Bundle();
            this.pickerIntent = new Intent();
        }

        public void start(@NonNull Activity activity, int requestCode) {
            if (PermissionsUtils.checkReadStoragePermission(activity)) {
                activity.startActivityForResult(getIntent(activity), requestCode);
            }
        }

        public void start(@NonNull android.support.v4.app.Fragment fragment, int requestCode) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                fragment.startActivityForResult(getIntent(fragment.getActivity()), requestCode);
            }
        }

        public void start(@NonNull android.support.v4.app.Fragment fragment) {
            if (PermissionsUtils.checkReadStoragePermission(fragment.getActivity())) {
                fragment.startActivityForResult(getIntent(fragment.getActivity()), REQUEST_CODE);
            }
        }

        public void start(@NonNull Activity activity) {
            start(activity, REQUEST_CODE);
        }

        /**
         * 单选模式
         */
        public AlbumPickerBuilder single() {
            optionsBundle.putInt(EXTRA_SELECT_MODE, AlbumPickerFragment.SINGLE_SELECT_MODE);
            return this;
        }

        /**
         * 多选模式
         *
         * @param count 可选择的图片数量
         */
        public AlbumPickerBuilder multi(int count) {
            optionsBundle.putInt(EXTRA_SELECT_MODE, AlbumPickerFragment.MULTI_SELECT_MODE);
            optionsBundle.putInt(EXTRA_MAX_COUNT, count);
            return this;
        }

        /**
         * 是否显示相机,默认显示
         *
         * @param showCamera if true show
         */
        public AlbumPickerBuilder showCamera(boolean showCamera) {
            optionsBundle.putBoolean(EXTRA_SHOW_CAMERA, showCamera);
            return this;
        }

        /**
         * 是否显示gif图,默认显示
         *
         * @param showGif if true show
         */
        public AlbumPickerBuilder showGif(boolean showGif) {
            optionsBundle.putBoolean(EXTRA_SHOW_GIF, showGif);
            return this;
        }

        /**
         * @param count 单排展示多少张图
         */
        public AlbumPickerBuilder gridColumns(int count) {
            optionsBundle.putInt(EXTRA_GRID_COLUMN, count);
            return this;
        }

        /**
         * 已经选择的图片地址
         *
         * @param pathArray 图片地址
         */
        public AlbumPickerBuilder selectedPaths(String[] pathArray) {
            return selectedPaths(new ArrayList<>(Arrays.asList(pathArray)));
        }

        public AlbumPickerBuilder selectedPaths(ArrayList<String> pathList) {
            optionsBundle.putStringArrayList(EXTRA_SELECTED_PATH, pathList);
            return this;
        }

        /**
         * 是否支持预览
         */
        public AlbumPickerBuilder previewEnabled(boolean previewEnabled) {
            optionsBundle.putBoolean(EXTRA_PREVIEW_ENABLED, previewEnabled);
            return this;
        }


        public AlbumPickerBuilder setImageLoader(ImageLoader imageLoader){
            optionsBundle.putSerializable(EXTRA_IMAGE_LOADER,imageLoader);
            return this;
        }

        Intent getIntent(@NonNull Context context) {
            pickerIntent.setClass(context, AlbumPickerActivity.class);
            pickerIntent.putExtras(optionsBundle);
            return pickerIntent;
        }

        public void openCamera(Activity activity){
            mCurrentPhotoPath = null;
            if (PermissionsUtils.checkCameraPermission(activity)){
                try {
                    Intent intent = dispatchTakePictureIntent(activity);
                    activity.startActivityForResult(intent,REQUEST_CAMERA);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void openCamera(Fragment fragment){
            mCurrentPhotoPath = null;
            if (PermissionsUtils.checkCameraPermission(fragment)){
                try {
                    Intent intent = dispatchTakePictureIntent(fragment.getContext());
                    fragment.startActivityForResult(intent,REQUEST_CAMERA);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private Intent dispatchTakePictureIntent(Context mContext) throws IOException {
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(mContext.getPackageManager()) != null) {
                File file = createImageFile();
                Uri photoFile;
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    String authority = mContext.getApplicationInfo().packageName+".fileprovider";
                    photoFile = FileProvider.getUriForFile(mContext.getApplicationContext(), authority, file);
                } else {
                    photoFile = Uri.fromFile(file);
                }

                if (photoFile != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoFile);
                }
            }
            return takePictureIntent;
        }

        private File createImageFile() throws IOException {
            // Create an image file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.ENGLISH).format(new Date());
            String imageFileName = "IMG_" + timeStamp + ".jpg";
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM),"/Camera");

            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {
                    Log.e("TAG", "Throwing Errors....");
                    throw new IOException();
                }
            }

            File image = new File(storageDir, imageFileName);
            mCurrentPhotoPath = image.getAbsolutePath();
            return image;
        }
    }
}
