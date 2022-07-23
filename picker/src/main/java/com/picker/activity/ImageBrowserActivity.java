package com.picker.activity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bm.library.PhotoView;
import com.bumptech.glide.Glide;
import com.picker.Picker;
import com.picker.R;
import com.picker.ToastUtil;
import com.picker.filter.FileFilter;
import com.picker.filter.callback.FilterResultCallback;
import com.picker.filter.entity.Directory;
import com.picker.filter.entity.ImageFile;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

public class ImageBrowserActivity extends BaseActivity {
    public static final String IMAGE_BROWSER_INIT_INDEX = "ImageBrowserInitIndex";
    public static final String IMAGE_BROWSER_SELECTED_LIST = "ImageBrowserSelectedList";
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private int initIndex = 0;
    private int mCurrentIndex = 0;

    private ViewPager mViewPager;
    private Toolbar mTbImagePick;
    private ArrayList<ImageFile> mList = new ArrayList<>();
    private ImageView mSelectView;
    private ArrayList<ImageFile> mSelectedFiles;

    @Override
    void permissionGranted() {
        loadData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setContentView(R.layout.picker_activity_image_browser);

        mMaxNumber = getIntent().getIntExtra(Picker.MAX_COUNT, Picker.DEFAULT_MAX_COUNT);
        initIndex = getIntent().getIntExtra(IMAGE_BROWSER_INIT_INDEX, 0);
        mCurrentIndex = initIndex;
        mSelectedFiles = getIntent().getParcelableArrayListExtra(IMAGE_BROWSER_SELECTED_LIST);
        mCurrentNumber = mSelectedFiles.size();

        super.onCreate(savedInstanceState);
    }

    private void initView() {
        mTbImagePick = (Toolbar) findViewById(R.id.tb_image_pick);
        mTbImagePick.setTitle(mCurrentNumber + "/" + mMaxNumber);
        setSupportActionBar(mTbImagePick);
        mTbImagePick.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishThis();
            }
        });

        View bottom = findViewById(R.id.layout_bottom_bar);
        bottom.setAlpha(0.4f);
        mSelectView = (ImageView) findViewById(R.id.cbx);
        mSelectView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!v.isSelected() && isUpToMax()) {
                    ToastUtil.getInstance(ImageBrowserActivity.this).showToast(R.string.picker_up_to_max);
                    return;
                }

                if (v.isSelected()) {
                    mList.get(mCurrentIndex).setSelected(false);
                    mCurrentNumber--;
                    v.setSelected(false);
                    mSelectedFiles.remove(mList.get(mCurrentIndex));
                } else {
                    mList.get(mCurrentIndex).setSelected(true);
                    mCurrentNumber++;
                    v.setSelected(true);
                    mSelectedFiles.add(mList.get(mCurrentIndex));
                }

                mTbImagePick.setTitle(mCurrentNumber + "/" + mMaxNumber);
            }
        });

        mViewPager = (ViewPager) findViewById(R.id.vp_image_pick);
        mViewPager.setPageMargin((int) (getResources().getDisplayMetrics().density * 15));
        mViewPager.setAdapter(new ImageBrowserAdapter());
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mCurrentIndex = position;
                mSelectView.setSelected(mList.get(mCurrentIndex).isSelected());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mViewPager.setCurrentItem(initIndex, false);
        mSelectView.setSelected(mList.get(mCurrentIndex).isSelected());
    }

    private void loadData() {
        FileFilter.getImages(this, new FilterResultCallback<ImageFile>() {
            @Override
            public void onResult(List<Directory<ImageFile>> directories) {
                mList.clear();
                for (Directory<ImageFile> directory : directories) {
                    mList.addAll(directory.getFiles());
                }

                for (ImageFile file : mList) {
                    if (mSelectedFiles.contains(file)) {
                        file.setSelected(true);
                    }
                }

                initView();
                mViewPager.getAdapter().notifyDataSetChanged();
            }
        });
    }

    private class ImageBrowserAdapter extends PagerAdapter {
        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            PhotoView view = new PhotoView(ImageBrowserActivity.this);
            view.enable();
            view.setScaleType(ImageView.ScaleType.CENTER_INSIDE);

            Glide.with(ImageBrowserActivity.this)
                    .load(mList.get(position).getPath())
                    .transition(withCrossFade())
                    .into(view);
            container.addView(view);
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.picker_menu_image_pick, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_done) {
            finishThis();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isUpToMax() {
        return mCurrentNumber >= mMaxNumber;
    }

    private void finishThis() {
        Intent intent = new Intent();
        intent.putParcelableArrayListExtra(Picker.RESULT_BROWSER_IMAGE, mSelectedFiles);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        finishThis();
    }
}
