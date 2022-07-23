package com.picker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.picker.DividerListItemDecoration;
import com.picker.Picker;
import com.picker.R;
import com.picker.ToastUtil;
import com.picker.Util;
import com.picker.adapter.AudioPickAdapter;
import com.picker.adapter.FolderListAdapter;
import com.picker.adapter.OnSelectStateListener;
import com.picker.filter.FileFilter;
import com.picker.filter.callback.FilterResultCallback;
import com.picker.filter.entity.AudioFile;
import com.picker.filter.entity.Directory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class AudioPickActivity extends BaseActivity {
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private RecyclerView mRecyclerView;
    private AudioPickAdapter mAdapter;
    private boolean isNeedRecorder;
    private boolean isTakenAutoSelected;
    private ArrayList<AudioFile> mSelectedList = new ArrayList<>();
    private List<Directory<AudioFile>> mAll;
    private String mAudioPath;

    private TextView tv_count;
    private TextView tv_folder;
    private LinearLayout ll_folder;
    private RelativeLayout rl_done;
    private RelativeLayout tb_pick;
    private RelativeLayout rl_rec_aud;

    @Override
    void permissionGranted() {
        loadData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picker_activity_audio_pick);

        mMaxNumber = getIntent().getIntExtra(Picker.MAX_COUNT, Picker.DEFAULT_MAX_COUNT);
        isNeedRecorder = getIntent().getBooleanExtra(Picker.IS_NEED_RECORDER, false);
        isTakenAutoSelected = getIntent().getBooleanExtra(Picker.IS_TAKEN_AUTO_SELECTED, true);
        initView();
    }

    private void initView() {
        tv_count = (TextView) findViewById(R.id.tv_count);
        tv_count.setText(mCurrentNumber + "/" + mMaxNumber);

        mRecyclerView = (RecyclerView) findViewById(R.id.rv_audio_pick);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerListItemDecoration(this,
                LinearLayoutManager.VERTICAL, R.drawable.picker_divider_rv_file));
        mAdapter = new AudioPickAdapter(this, mMaxNumber);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnSelectStateListener(new OnSelectStateListener<AudioFile>() {
            @Override
            public void OnSelectStateChanged(boolean state, AudioFile file) {
                if (state) {
                    mSelectedList.add(file);
                    mCurrentNumber++;
                } else {
                    mSelectedList.remove(file);
                    mCurrentNumber--;
                }
                tv_count.setText(mCurrentNumber + "/" + mMaxNumber);
            }
        });

        rl_done = (RelativeLayout) findViewById(R.id.rl_done);
        rl_done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putParcelableArrayListExtra(Picker.RESULT_PICK_AUDIO, mSelectedList);
                setResult(RESULT_OK, intent);
                finish();
            }
        });

        tb_pick = (RelativeLayout) findViewById(R.id.tb_pick);
        ll_folder = (LinearLayout) findViewById(R.id.ll_folder);
        if (isNeedFolderList) {
            ll_folder.setVisibility(View.VISIBLE);
            ll_folder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mFolderHelper.toggle(tb_pick);
                }
            });
            tv_folder = (TextView) findViewById(R.id.tv_folder);
            tv_folder.setText(getResources().getString(R.string.picker_all));

            mFolderHelper.setFolderListListener(new FolderListAdapter.FolderListListener() {
                @Override
                public void onFolderListClick(Directory directory) {
                    mFolderHelper.toggle(tb_pick);
                    tv_folder.setText(directory.getName());

                    if (TextUtils.isEmpty(directory.getPath())) { //All
                        refreshData(mAll);
                    } else {
                        for (Directory<AudioFile> dir : mAll) {
                            if (dir.getPath().equals(directory.getPath())) {
                                List<Directory<AudioFile>> list = new ArrayList<>();
                                list.add(dir);
                                refreshData(list);
                                break;
                            }
                        }
                    }
                }
            });
        }

        if (isNeedRecorder) {
            rl_rec_aud = (RelativeLayout) findViewById(R.id.rl_rec_aud);
            rl_rec_aud.setVisibility(View.VISIBLE);
            rl_rec_aud.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
                    if (Util.detectIntent(AudioPickActivity.this, intent)) {
                        startActivityForResult(intent, Picker.REQUEST_CODE_TAKE_AUDIO);
                    } else {
                        ToastUtil.getInstance(AudioPickActivity.this).showToast(getString(R.string.picker_no_audio_app));
                    }
                }
            });
        }
    }

    private void loadData() {
        FileFilter.getAudios(this, new FilterResultCallback<AudioFile>() {
            @Override
            public void onResult(List<Directory<AudioFile>> directories) {
                // Refresh folder list
                if (isNeedFolderList) {
                    ArrayList<Directory> list = new ArrayList<>();
                    Directory all = new Directory();
                    all.setName(getResources().getString(R.string.picker_all));
                    list.add(all);
                    list.addAll(directories);
                    mFolderHelper.fillData(list);
                }

                mAll = directories;
                refreshData(directories);
            }
        });
    }

    private void refreshData(List<Directory<AudioFile>> directories) {
        boolean tryToFindTaken = isTakenAutoSelected;

        // if auto-select taken file is enabled, make sure requirements are met
        if (tryToFindTaken && !TextUtils.isEmpty(mAudioPath)) {
            File takenFile = new File(mAudioPath);
            tryToFindTaken = !mAdapter.isUpToMax() && takenFile.exists(); // try to select taken file only if max isn't reached and the file exists
        }

        List<AudioFile> list = new ArrayList<>();
        for (Directory<AudioFile> directory : directories) {
            list.addAll(directory.getFiles());

            // auto-select taken file?
            if (tryToFindTaken) {
                tryToFindTaken = findAndAddTaken(directory.getFiles());   // if taken file was found, we're done
            }
        }

        for (AudioFile file : mSelectedList) {
            int index = list.indexOf(file);
            if (index != -1) {
                list.get(index).setSelected(true);
            }
        }
        mAdapter.refresh(list);
    }

    private boolean findAndAddTaken(List<AudioFile> list) {
        for (AudioFile audioFile : list) {
            if (audioFile.getPath().equals(mAudioPath)) {
                mSelectedList.add(audioFile);
                mCurrentNumber++;
                mAdapter.setCurrentNumber(mCurrentNumber);
                tv_count.setText(mCurrentNumber + "/" + mMaxNumber);

                return true;   // taken file was found and added
            }
        }
        return false;    // taken file wasn't found
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Picker.REQUEST_CODE_TAKE_AUDIO:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        mAudioPath = data.getData().getPath();
                    }
                    loadData();
                }
                break;
        }
    }
}
