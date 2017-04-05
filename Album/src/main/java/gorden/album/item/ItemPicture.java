package gorden.album.item;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.Gravity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;

import gorden.album.R;
import me.xiaopan.sketch.SketchImageView;
import me.xiaopan.sketch.decode.ImageFormat;
import me.xiaopan.sketch.display.TransitionImageDisplayer;

/**
 * Created by Gorden on 2017/4/2.
 */

public class ItemPicture extends FrameLayout{
    public SketchImageView imgPicture;
    public CheckBox imgCheck;
    public View viewClicked;
    public View viewShadow;
    public ItemPicture(@NonNull Context context,int imgSize) {
        super(context);
        initView(context,imgSize);
    }

    private void initView(Context context,int imgSize) {
        imgPicture = new SketchImageView(context);
        imgPicture.setShowGifFlag(R.drawable.ic_gif);
        imgPicture.getOptions().setImageDisplayer(new TransitionImageDisplayer())
                  .setMaxSize(imgSize,imgSize).setResize(imgSize,imgSize).setLowQualityImage(true)
                  .setThumbnailMode(true).setCacheProcessedImageInDisk(true);
        imgPicture.setScaleType(ImageView.ScaleType.CENTER_CROP);
        addView(imgPicture,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

        viewShadow = new View(context);
        viewShadow.setBackgroundColor(Color.parseColor("#88000000"));
        viewShadow.setVisibility(GONE);
        addView(viewShadow,LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);

        imgCheck = new CheckBox(context);
        LayoutParams paramsCheckbox = new LayoutParams(dip2px(29),dip2px(29));
        paramsCheckbox.gravity = Gravity.RIGHT|Gravity.TOP;
        paramsCheckbox.rightMargin = 5;
        paramsCheckbox.topMargin = 5;
        addView(imgCheck,paramsCheckbox);

        viewClicked = new View(context);
        LayoutParams paramsView = new LayoutParams(dip2px(32),dip2px(32));
        paramsView.gravity = Gravity.RIGHT|Gravity.TOP;
        addView(viewClicked,paramsView);

    }

    private int dip2px(int dip) {
        return (int) (dip * getResources().getDisplayMetrics().density + 0.5f);
    }
}
