package gorden.album.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import gorden.album.AlbumPickerActivity;
import gorden.album.R;
import gorden.album.adapter.ImagePagerAdapter;
import gorden.album.utils.AnimationUtils;
import gorden.album.widget.ZoomOutPageTransformer;

import static android.app.Activity.RESULT_OK;
import static gorden.album.AlbumPicker.EXTRA_MAX_COUNT;
import static gorden.album.AlbumPicker.KEY_IMAGES;

/**
 * 预览界面
 */

public class AlbumPreViewFragment extends Fragment {
    private ViewPager pager_image;
    private TextView btn_confirm;
    private ImageButton btn_back;
    private TextView text_position;
    private CheckBox checkbox;
    private View viewClicked;

    private ImagePagerAdapter pagerAdapter;
    private View rootView;
    private ArrayList<String> selectPath = new ArrayList<>();
    private int pickerMaxCount;
    private int currentPosition;

    private boolean show = true;
    private View rel_bottom,rel_top;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.fragment_album_preview, container, false);
            pager_image = (ViewPager) rootView.findViewById(R.id.pager_image);
            btn_confirm = (TextView) rootView.findViewById(R.id.btn_confirm);
            btn_back = (ImageButton) rootView.findViewById(R.id.btn_back);
            text_position = (TextView) rootView.findViewById(R.id.text_position);
            checkbox = (CheckBox) rootView.findViewById(R.id.checkbox);
            viewClicked = rootView.findViewById(R.id.viewClicked);
            rel_top = rootView.findViewById(R.id.rel_top);
            rel_bottom = rootView.findViewById(R.id.rel_bottom);
        }
        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        final ArrayList<String> imgList = getArguments().getStringArrayList("imglist");
        selectPath.addAll(getArguments().getStringArrayList("selected"));
        int position = getArguments().getInt("position", 0);
        pickerMaxCount = getArguments().getInt(EXTRA_MAX_COUNT);
        pager_image.setOffscreenPageLimit(2);
        pager_image.setPageTransformer(false,new ZoomOutPageTransformer());
        pagerAdapter = new ImagePagerAdapter(this, imgList);
        pager_image.setAdapter(pagerAdapter);

        pager_image.setCurrentItem(position);
        currentPosition = position;
        text_position.setText((position + 1) + "/" + imgList.size());
        ((AlbumPickerActivity)getActivity()).applyBackground(imgList.get(position));
        refreshConfirm();
        checkbox.setChecked(selectPath.contains(imgList.get(position)));

        pager_image.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                text_position.setText((position + 1) + "/" + imgList.size());
                checkbox.setChecked(selectPath.contains(imgList.get(position)));
                ((AlbumPickerActivity)getActivity()).applyBackground(imgList.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        viewClicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isChecked = !checkbox.isChecked();
                if (isChecked && selectPath.size() >= pickerMaxCount) return;
                checkbox.setChecked(isChecked);

                if (isChecked && !selectPath.contains(imgList.get(currentPosition))) {
                    selectPath.add(imgList.get(currentPosition));
                } else if (!isChecked && selectPath.contains(imgList.get(currentPosition))) {
                    selectPath.remove(imgList.get(currentPosition));
                }
                ((AlbumPickerActivity) getActivity()).refreshSelected(selectPath);
                refreshConfirm();
            }
        });

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent resultData = new Intent();
                resultData.putStringArrayListExtra(KEY_IMAGES, selectPath);
                getActivity().setResult(RESULT_OK, resultData);
                getActivity().finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().popBackStack();
            }
        });
    }

    /**
     * 刷新完成按钮
     */
    public void refreshConfirm() {
        btn_confirm.setText(selectPath.size() > 0 && pickerMaxCount > 1 ? "完成(" + selectPath.size() + "/" + pickerMaxCount + ")" : "完成");
        btn_confirm.setEnabled(selectPath.size() > 0);
        btn_confirm.setTextColor(ContextCompat.getColor(getContext(), (selectPath.size() > 0 ? R.color.album_btn_textcolor : R.color.album_btn_textcolor_e)));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void toggleToolbarVisibleState() {
        show = !show;
        if (show) {
            AnimationUtils.visibleViewByAlpha(rel_top);
            AnimationUtils.visibleViewByAlpha(rel_bottom);
        } else {
            AnimationUtils.goneViewByAlpha(rel_top);
            AnimationUtils.goneViewByAlpha(rel_bottom);
        }
    }
}
