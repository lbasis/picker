package com.picker.filter.loader;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;

import androidx.loader.content.CursorLoader;

public class FileLoader extends CursorLoader {
    private static final String[] FILE_PROJECTION = {
            //Base File
            MediaStore.Files.FileColumns._ID,
            MediaStore.Files.FileColumns.TITLE,
            MediaStore.Files.FileColumns.DATA,
            MediaStore.Files.FileColumns.SIZE,
            MediaStore.Files.FileColumns.DATE_ADDED,

            //Normal File
            MediaStore.Files.FileColumns.MIME_TYPE
    };

    private FileLoader(Context context, Uri uri, String[] projection, String selection,
                       String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }

    public FileLoader(Context context) {
        super(context);
        setProjection(FILE_PROJECTION);
        setUri(MediaStore.Files.getContentUri("external"));
        setSortOrder(MediaStore.Files.FileColumns.DATE_ADDED + " DESC");

//        setSelection(MIME_TYPE + "=? or "
////                + MIME_TYPE + "=? or "
////                + MIME_TYPE + "=? or "
//                + MIME_TYPE + "=?");
//
//        String[] selectionArgs;
//        selectionArgs = new String[] { "text/txt", "text/plain" };
//        setSelectionArgs(selectionArgs);
        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "!=" + MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE +
                " AND " + MediaStore.Files.FileColumns.MEDIA_TYPE + "!=" + MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
        setSelection(selection);
    }
}
