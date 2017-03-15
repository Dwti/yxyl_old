package com.common.core.utils;

import android.annotation.SuppressLint;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

/**
 * Created by Administrator on 2015/6/26.
 */
public class PictureHelper {
    // get the absolute path from the uri
    private static final String PRIMARY="primary";
    private static final String DOWNLOADS_PROVIDER_URL="content://downloads/public_downloads";
    private static final String VIDEO_TYPE="video";
    private static final String AUDIO_TYPE="audio";
    private static final String IMAGE_TYPE="image";
    private static final String CONTENT_SCHEME="content";
    private static final String FILE_SCHEME="file";
    private static final String DOCUMENT_AUTHORITY="com.android.externalstorage.documents";
    private static final String DOWNLOAD_AUTHORITY="com.android.providers.downloads.documents";
    private static final String MEDIA_AUTHORITY="com.android.providers.media.documents";
    private static final String PHOTO_AUTHORITY="com.google.android.apps.photos.content";
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {
        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if (PRIMARY.equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse(DOWNLOADS_PROVIDER_URL),
                        Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                Uri contentUri = null;
                if (IMAGE_TYPE.equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if (VIDEO_TYPE.equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if (AUDIO_TYPE.equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                final String selection = "_id=?";
                final String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if (CONTENT_SCHEME.equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if (FILE_SCHEME.equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {
        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection,
                    selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    public static boolean isExternalStorageDocument(Uri uri) {
        return DOCUMENT_AUTHORITY.equals(uri
                .getAuthority());
    }

    public static boolean isDownloadsDocument(Uri uri) {
        return DOWNLOAD_AUTHORITY.equals(uri
                .getAuthority());
    }

    public static boolean isMediaDocument(Uri uri) {
        return MEDIA_AUTHORITY.equals(uri
                .getAuthority());
    }

    private static boolean isGooglePhotosUri(Uri uri) {
        return PHOTO_AUTHORITY.equals(uri
                .getAuthority());
    }
}
