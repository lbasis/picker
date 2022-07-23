package com.picker.filter.callback;

import com.picker.filter.entity.BaseFile;
import com.picker.filter.entity.Directory;

import java.util.List;

public interface FilterResultCallback<T extends BaseFile> {
    void onResult(List<Directory<T>> directories);
}
