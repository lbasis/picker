package com.bcq.demo;

import android.content.Intent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.picker.Picker;
import com.picker.filter.entity.AudioFile;
import com.picker.filter.entity.ImageFile;
import com.picker.filter.entity.NormalFile;
import com.picker.filter.entity.VideoFile;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView mTvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTvResult = (TextView) findViewById(R.id.tv_result);
        Picker.init(7,9,false);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.btn_pick_image:
                Picker.pickImage(this, true, true);
                break;
            case R.id.btn_pick_video:
                Picker.pickVideo(this,  true);
                break;
            case R.id.btn_pick_audio:
                Picker.pickAudio(this, true);
                break;
            case R.id.btn_pick_file:
                Picker.pickFile(this, new String[]{"xlsx", "xls", "doc", "dOcX", "ppt", "pptx", "pdf", "txt"});
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Picker.REQUEST_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    ArrayList<ImageFile> list = data.getParcelableArrayListExtra(Picker.RESULT_PICK_IMAGE);
                    StringBuilder builder = new StringBuilder();
                    for (ImageFile file : list) {
                        String path = file.getPath();
                        builder.append(path + "\n");
                    }
                    mTvResult.setText(builder.toString());
                }
                break;
            case Picker.REQUEST_CODE_PICK_VIDEO:
                if (resultCode == RESULT_OK) {
                    ArrayList<VideoFile> list = data.getParcelableArrayListExtra(Picker.RESULT_PICK_VIDEO);
                    StringBuilder builder = new StringBuilder();
                    for (VideoFile file : list) {
                        String path = file.getPath();
                        builder.append(path + "\n");
                    }
                    mTvResult.setText(builder.toString());
                }
                break;
            case Picker.REQUEST_CODE_PICK_AUDIO:
                if (resultCode == RESULT_OK) {
                    ArrayList<AudioFile> list = data.getParcelableArrayListExtra(Picker.RESULT_PICK_AUDIO);
                    StringBuilder builder = new StringBuilder();
                    for (AudioFile file : list) {
                        String path = file.getPath();
                        builder.append(path + "\n");
                    }
                    mTvResult.setText(builder.toString());
                }
                break;
            case Picker.REQUEST_CODE_PICK_FILE:
                if (resultCode == RESULT_OK) {
                    ArrayList<NormalFile> list = data.getParcelableArrayListExtra(Picker.RESULT_PICK_FILE);
                    StringBuilder builder = new StringBuilder();
                    for (NormalFile file : list) {
                        String path = file.getPath();
                        builder.append(path + "\n");
                    }
                    mTvResult.setText(builder.toString());
                }
                break;
        }
    }
}
