package gorden.album.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;

import gorden.album.ImageType;

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
        PhotoView imageView = new PhotoView(mContext);
        imageView.enable();
        container.addView(imageView);
        if (ImageType.getType(imgList.get(position)) == ImageType.GIF) {
            Glide.with(mContext).load(imgList.get(position)).asGif().into(imageView);
        } else {
            Glide.with(mContext).load(imgList.get(position)).asBitmap().into(imageView);
        }
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
