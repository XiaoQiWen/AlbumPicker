package gorden.album.loader;

import android.app.Activity;
import android.widget.ImageView;

import java.io.Serializable;

/**
 * Created by Gorden on 2017/4/2.
 */

public interface ImageLoader extends Serializable{
    void displayImage(Activity activity, ImageView imageView, String path, int width, int height);
}
