package gorden.album.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import gorden.album.R;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.display.TransitionImageDisplayer;
import me.xiaopan.sketch.state.StateImage;

/**
 * Created by Gorden on 2017/4/4.
 */

public class ImagePagerAdapter extends PagerAdapter {
    private Context mContext;
    private ArrayList<String> imgList;

    public ImagePagerAdapter(Context context, ArrayList<String> imgList) {
        this.mContext = context;
        this.imgList = imgList;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        SketchImageView imageView = new SketchImageView(mContext);
        imageView.setSupportZoom(true);
        imageView.setSupportLargeImage(true);
        imageView.getOptions().setDecodeGifImage(true);
        imageView.displayImage(imgList.get(position));
        container.addView(imageView);
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return imgList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
}
