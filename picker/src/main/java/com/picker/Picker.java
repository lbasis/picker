package com.picker;

import android.app.Activity;
import android.content.Intent;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.Toolbar;

import com.picker.activity.AudioPickActivity;
import com.picker.activity.ImagePickActivity;
import com.picker.activity.NormalFilePickActivity;
import com.picker.activity.VideoPickActivity;

/**
 * 文件选择器
 */
public class Picker {
    public static final int DEFAULT_MAX_COUNT = 9;

    public static final String SHOW_FOLDER_LIST = "ShowFolderList";
    public static final String MAX_COUNT = "Max_Count";
    public static final String SHOW_CAMERA = "Show_Camera";

    public static final String ENABLE_PREVIEW = "preview";
    public static final String IS_TAKEN_AUTO_SELECTED = "IsTakenAutoSelected";
    public static final int REQUEST_CODE_PICK_IMAGE = 0x100;
    public static final String RESULT_PICK_IMAGE = "ResultPickImage";
    public static final int REQUEST_CODE_TAKE_IMAGE = 0x101;

    public static final int REQUEST_CODE_BROWSER_IMAGE = 0x102;
    public static final String RESULT_BROWSER_IMAGE = "ResultBrowserImage";

    public static final int REQUEST_CODE_PICK_VIDEO = 0x200;
    public static final String RESULT_PICK_VIDEO = "ResultPickVideo";
    public static final int REQUEST_CODE_TAKE_VIDEO = 0x201;

    public static final String IS_NEED_RECORDER = "IsNeedRecorder";
    public static final int REQUEST_CODE_PICK_AUDIO = 0x300;
    public static final String RESULT_PICK_AUDIO = "ResultPickAudio";
    public static final int REQUEST_CODE_TAKE_AUDIO = 0x301;

    public static final int REQUEST_CODE_PICK_FILE = 0x400;
    public static final String RESULT_PICK_FILE = "ResultPickFILE";
    public static final String SUFFIX = "Suffix";

    private Picker() {
    }

    public static class Config {
        @DrawableRes
        public int navigationIcon;
        @ColorRes
        public int titleBarTextColor;
        @ColorRes
        public int titleBarBackground;
        @ColorRes
        public int contentBackground;
    }

    private static Config config = new Config();

    public static void setConfig(Config config) {
        Picker.config = config;
    }

    public static void configTooleBar(Toolbar toolbar) {
        if (null == config) return;
        if (config.navigationIcon > 0) {
            toolbar.setNavigationIcon(config.navigationIcon);
        }
        if (config.titleBarBackground > 0) {
            toolbar.setBackgroundResource(config.titleBarBackground);
        }
    }

    public static void init(Config config) {

    }

    /**
     * @param activity   Activity
     * @param max        最大数
     * @param showFolder 是否显示文件夹
     * @param suffix     选择的文件后缀名
     */
    public static void pickFile(Activity activity, int max, boolean showFolder, String[] suffix) {
        Intent intent = new Intent(activity, NormalFilePickActivity.class);
        intent.putExtra(MAX_COUNT, max);
        intent.putExtra(SHOW_FOLDER_LIST, showFolder);
        intent.putExtra(SUFFIX, suffix);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    /**
     * 选择音频图片
     *
     * @param activity      Activity
     * @param max           最大数
     * @param showCamera    是否显示相机拍照
     * @param showFolder    是否显示文件夹
     * @param enablePreview 是可以预览
     * @param autoSelected  拍照后是否自动选择
     */
    public static void pickImage(Activity activity, int max, boolean showCamera, boolean showFolder, boolean enablePreview, boolean autoSelected) {
        Intent intent = new Intent(activity, ImagePickActivity.class);
        intent.putExtra(SHOW_CAMERA, showCamera);
        intent.putExtra(MAX_COUNT, max);
        intent.putExtra(SHOW_FOLDER_LIST, showFolder);
        intent.putExtra(IS_TAKEN_AUTO_SELECTED, autoSelected);
        intent.putExtra(ENABLE_PREVIEW, true);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_IMAGE);
    }

    /**
     * 选择音频文件
     *
     * @param activity     Activity
     * @param max          最大数
     * @param showRecord   是否显示麦克风录制
     * @param showFolder   是否显示文件夹
     * @param autoSelected 录制后是否自动选择
     */
    public static void pickAudio(Activity activity, int max, boolean showRecord, boolean showFolder, boolean autoSelected) {
        Intent intent = new Intent(activity, AudioPickActivity.class);
        intent.putExtra(MAX_COUNT, max);
        intent.putExtra(IS_NEED_RECORDER, showRecord);
        intent.putExtra(SHOW_FOLDER_LIST, showFolder);
        intent.putExtra(IS_TAKEN_AUTO_SELECTED, autoSelected);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_AUDIO);
    }

    /**
     * @param activity     Activity
     * @param max          最大数
     * @param showCamera   是否显示相机录制
     * @param showFolder   是否显示文件夹
     * @param autoSelected 录制后是否自动选择
     */
    public static void pickVideo(Activity activity, int max, boolean showCamera, boolean showFolder, boolean autoSelected) {
        Intent intent = new Intent(activity, VideoPickActivity.class);
        intent.putExtra(MAX_COUNT, max);
        intent.putExtra(SHOW_CAMERA, showCamera);
        intent.putExtra(SHOW_FOLDER_LIST, showFolder);
        intent.putExtra(IS_TAKEN_AUTO_SELECTED, autoSelected);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO);
    }
}
