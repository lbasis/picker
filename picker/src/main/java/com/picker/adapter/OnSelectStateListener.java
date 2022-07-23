package com.picker.adapter;

public interface OnSelectStateListener<T> {
    void OnSelectStateChanged(boolean state, T file);
}
