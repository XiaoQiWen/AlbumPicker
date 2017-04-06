package gorden.album;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.ViewGroup;

import java.util.ArrayList;

import gorden.album.fragment.AlbumPickerFragment;
import gorden.album.fragment.AlbumPreViewFragment;
import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.cache.LruMemoryCache;
import me.xiaopan.sketch.display.TransitionImageDisplayer;
import me.xiaopan.sketch.drawable.SketchDrawable;
import me.xiaopan.sketch.process.GaussianBlurImageProcessor;
import me.xiaopan.sketch.state.DrawableStateImage;
import me.xiaopan.sketch.state.OldStateImage;
import me.xiaopan.sketch.util.SketchUtils;

import static gorden.album.AlbumPicker.EXTRA_MAX_COUNT;
import static gorden.album.AlbumPicker.EXTRA_SELECT_MODE;
import static gorden.album.fragment.AlbumPickerFragment.DEFAULT_MAX_COUNT;
import static gorden.album.fragment.AlbumPickerFragment.SINGLE_SELECT_MODE;

/**
 * Album图片选择器 页面
 * Created by Gordn on 2017/3/31.
 */

public class AlbumPickerActivity extends AppCompatActivity {
    private AlbumPickerFragment pickerFragment;
    private AlbumPreViewFragment preViewFragment;
    private FragmentManager fragmentManager;

    private SketchImageView backgroundImageView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_picker);

        backgroundImageView = (SketchImageView) findViewById(R.id.image_main_background);

        initViews();

        pickerFragment = new AlbumPickerFragment();
        pickerFragment.setArguments(getIntent().getExtras());
        fragmentManager = getSupportFragmentManager();
        pickerView();

        int newMemoryCacheMaxSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        Sketch.with(this).getConfiguration().setMemoryCache(new LruMemoryCache(this, newMemoryCacheMaxSize));
    }

    private void initViews() {
        ViewGroup.LayoutParams layoutParams = backgroundImageView.getLayoutParams();
        layoutParams.width = getResources().getDisplayMetrics().widthPixels;
        layoutParams.height = getResources().getDisplayMetrics().heightPixels;
        backgroundImageView.setLayoutParams(layoutParams);

        backgroundImageView.getOptions().setLoadingImage(new OldStateImage(new DrawableStateImage(R.drawable.shape_window_background)))
                .setImageProcessor(GaussianBlurImageProcessor.makeLayerColor(Color.parseColor("#66000000")))
                .setCacheProcessedImageInDisk(true)
                .setBitmapConfig(Bitmap.Config.ARGB_8888)   // 效果比较重要
                .setShapeSizeByFixedSize(true)
                .setMaxSize(getResources().getDisplayMetrics().widthPixels / 4,
                        getResources().getDisplayMetrics().heightPixels / 4)
                .setImageDisplayer(new TransitionImageDisplayer(true));
    }

    /**
     * 设置模糊背景
     * @param imgPath 图片地址
     */
    public void applyBackground(String imgPath) {
        Drawable drawable = SketchUtils.getLastDrawable(backgroundImageView.getDrawable());
        if (drawable instanceof SketchDrawable) {
            SketchDrawable sketchDrawable = (SketchDrawable) drawable;
            if (imgPath.contains(sketchDrawable.getUri())) return;//如果地址没变,则不设置
        }
        backgroundImageView.displayImage(imgPath);
    }

    private void pickerView() {
        fragmentManager.beginTransaction().add(R.id.frame_content, pickerFragment).commit();
    }

    public void preView(ArrayList<String> imglist, ArrayList<String> selected, int position, boolean backStack) {
        preViewFragment = new AlbumPreViewFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        bundle.putStringArrayList("imglist", imglist);
        bundle.putStringArrayList("selected", selected);
        int pickerModel = getIntent().getIntExtra(EXTRA_SELECT_MODE, SINGLE_SELECT_MODE);
        bundle.putInt(EXTRA_MAX_COUNT, getIntent().getIntExtra(EXTRA_MAX_COUNT, pickerModel == SINGLE_SELECT_MODE ? 1 : DEFAULT_MAX_COUNT));
        preViewFragment.setArguments(bundle);
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        if (backStack) fragmentTransaction.addToBackStack(null);
        fragmentTransaction.hide(pickerFragment).add(R.id.frame_content, preViewFragment, "preview").commit();
    }

    public void refreshSelected(ArrayList<String> selectPath) {
        if (pickerFragment != null) {
            pickerFragment.selectPath = selectPath;
            pickerFragment.notifySelected();
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        try {
            return super.dispatchTouchEvent(ev);
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
        return true;
    }

}
