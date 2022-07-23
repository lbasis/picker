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
        // 显示的列数
        public int column = 3;

        // 是否显示文件夹
        public boolean showFolder = true;

        // 最大选择个数
        public int maxCount = DEFAULT_MAX_COUNT;
    }

    public final static Config config = new Config();

    /**
     * 初始化默认配置
     *
     * @param column     显示的列数
     * @param maxCount   选择最大个数
     * @param showFolder 是否显示文件夹目录
     */
    public static void init(int column, int maxCount, boolean showFolder) {
        config.column = Math.min(column, 5);
        config.maxCount = maxCount;
        config.showFolder = showFolder;
    }

    /**
     * @param activity Activity
     * @param suffix   选择的文件后缀名
     */
    public static void pickFile(Activity activity, String[] suffix) {
        pickFile(activity, suffix, config.maxCount, config.showFolder);
    }

    /**
     * @param activity   Activity
     * @param max        最大数
     * @param showFolder 是否显示文件夹
     * @param suffix     选择的文件后缀名
     */
    public static void pickFile(Activity activity, String[] suffix, int max, boolean showFolder) {
        Intent intent = new Intent(activity, NormalFilePickActivity.class);
        intent.putExtra(MAX_COUNT, max);
        intent.putExtra(SHOW_FOLDER_LIST, showFolder);
        intent.putExtra(SUFFIX, suffix);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_FILE);
    }

    /**
     * 选择图片
     *
     * @param activity      Activity
     * @param showCamera    是否显示相机拍照
     * @param enablePreview 是可以预览
     */
    public static void pickImage(Activity activity, boolean enablePreview, boolean showCamera) {
        pickImage(activity, enablePreview, true, showCamera, config.maxCount, config.showFolder);
    }

    /**
     * 选择图片
     *
     * @param activity      Activity
     * @param max           最大数
     * @param showCamera    是否显示相机拍照
     * @param showFolder    是否显示文件夹
     * @param enablePreview 是可以预览
     * @param autoSelected  拍照后是否自动选择
     */
    public static void pickImage(Activity activity, boolean enablePreview, boolean showCamera, boolean autoSelected, int max, boolean showFolder) {
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
     * @param activity   Activity
     * @param showRecord 是否显示麦克风录制
     */
    public static void pickAudio(Activity activity, boolean showRecord) {
        pickAudio(activity, showRecord, true, config.maxCount, config.showFolder);
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
    public static void pickAudio(Activity activity, boolean showRecord, boolean autoSelected, int max, boolean showFolder) {
        Intent intent = new Intent(activity, AudioPickActivity.class);
        intent.putExtra(MAX_COUNT, max);
        intent.putExtra(IS_NEED_RECORDER, showRecord);
        intent.putExtra(SHOW_FOLDER_LIST, showFolder);
        intent.putExtra(IS_TAKEN_AUTO_SELECTED, autoSelected);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_AUDIO);
    }

    /**
     * 选择视频文件
     *
     * @param activity   Activity
     * @param showCamera 是否显示相机录制
     */
    public static void pickVideo(Activity activity, boolean showCamera) {
        pickVideo(activity, showCamera, true, config.maxCount, config.showFolder);
    }

    /**
     * 选择视频文件
     *
     * @param activity     Activity
     * @param max          最大数
     * @param showCamera   是否显示相机录制
     * @param showFolder   是否显示文件夹
     * @param autoSelected 录制后是否自动选择
     */
    public static void pickVideo(Activity activity, boolean showCamera, boolean autoSelected, int max, boolean showFolder) {
        Intent intent = new Intent(activity, VideoPickActivity.class);
        intent.putExtra(MAX_COUNT, max);
        intent.putExtra(SHOW_CAMERA, showCamera);
        intent.putExtra(SHOW_FOLDER_LIST, showFolder);
        intent.putExtra(IS_TAKEN_AUTO_SELECTED, autoSelected);
        activity.startActivityForResult(intent, REQUEST_CODE_PICK_VIDEO);
    }
}
