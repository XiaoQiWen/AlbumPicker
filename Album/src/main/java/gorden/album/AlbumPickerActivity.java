package gorden.album;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import java.util.ArrayList;

import gorden.album.fragment.AlbumPickerFragment;
import gorden.album.fragment.AlbumPreViewFragment;
import me.xiaopan.sketch.Configuration;
import me.xiaopan.sketch.Sketch;
import me.xiaopan.sketch.cache.LruDiskCache;
import me.xiaopan.sketch.cache.LruMemoryCache;

import static gorden.album.AlbumPicker.EXTRA_MAX_COUNT;
import static gorden.album.AlbumPicker.EXTRA_SELECT_MODE;
import static gorden.album.fragment.AlbumPickerFragment.DEFAULT_MAX_COUNT;
import static gorden.album.fragment.AlbumPickerFragment.SINGLE_SELECT_MODE;

/**
 * document
 * Created by Gordn on 2017/3/31.
 */

public class AlbumPickerActivity extends AppCompatActivity {
    private AlbumPickerFragment pickerFragment;
    private AlbumPreViewFragment preViewFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album_picker);

        pickerFragment = new AlbumPickerFragment();
        pickerFragment.setArguments(getIntent().getExtras());
        fragmentManager = getSupportFragmentManager();
        pickerView();

        int newMemoryCacheMaxSize = (int) (Runtime.getRuntime().maxMemory() / 5);
        Sketch.with(this).getConfiguration().setMemoryCache(new LruMemoryCache(this,newMemoryCacheMaxSize));
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
        fragmentTransaction.add(R.id.frame_content, preViewFragment, "preview").commit();
    }

    public void refreshSelected(ArrayList<String> selectPath){
        if (pickerFragment!=null){
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
