package com.picker.filter;

import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;

import com.picker.filter.callback.FileLoaderCallbacks;
import com.picker.filter.callback.FilterResultCallback;
import com.picker.filter.entity.AudioFile;
import com.picker.filter.entity.ImageFile;
import com.picker.filter.entity.NormalFile;
import com.picker.filter.entity.VideoFile;

import static com.picker.filter.callback.FileLoaderCallbacks.TYPE_AUDIO;
import static com.picker.filter.callback.FileLoaderCallbacks.TYPE_FILE;
import static com.picker.filter.callback.FileLoaderCallbacks.TYPE_IMAGE;
import static com.picker.filter.callback.FileLoaderCallbacks.TYPE_VIDEO;

public class FileFilter {
    public static void getImages(FragmentActivity activity, FilterResultCallback<ImageFile> callback) {
        LoaderManager.getInstance(activity).initLoader(0, null,
                new FileLoaderCallbacks(activity, callback, TYPE_IMAGE));
    }

    public static void getVideos(FragmentActivity activity, FilterResultCallback<VideoFile> callback) {
        LoaderManager.getInstance(activity).initLoader(1, null,
                new FileLoaderCallbacks(activity, callback, TYPE_VIDEO));
    }

    public static void getAudios(FragmentActivity activity, FilterResultCallback<AudioFile> callback) {
        LoaderManager.getInstance(activity).initLoader(2, null,
                new FileLoaderCallbacks(activity, callback, TYPE_AUDIO));
    }

    public static void getFiles(FragmentActivity activity,
                                FilterResultCallback<NormalFile> callback, String[] suffix) {
        LoaderManager.getInstance(activity).initLoader(3, null,
                new FileLoaderCallbacks(activity, callback, TYPE_FILE, suffix));
    }
}
