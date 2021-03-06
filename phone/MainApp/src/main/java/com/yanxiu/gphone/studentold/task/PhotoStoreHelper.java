package com.yanxiu.gphone.studentold.task;

import android.app.Activity;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;

import com.yanxiu.gphone.studentold.R;
import com.yanxiu.gphone.studentold.bean.PhotoDirectory;
import com.yanxiu.gphone.studentold.utils.PhotoLoaderConstant;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import static android.provider.BaseColumns._ID;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME;
import static android.provider.MediaStore.Images.ImageColumns.BUCKET_ID;
import static android.provider.MediaStore.MediaColumns.DATA;
import static android.provider.MediaStore.MediaColumns.DATE_ADDED;
import static android.provider.MediaStore.MediaColumns.SIZE;

/**
 * Created by sunpeng on 2016/12/30.
 */
public class PhotoStoreHelper {

  public final static int INDEX_ALL_PHOTOS = 0;


  public static void getPhotoDirs(Activity activity, Bundle args, PhotosResultCallback resultCallback) {
    activity.getLoaderManager()
        .initLoader(0, args, new PhotoDirLoaderCallbacks(activity, resultCallback));
  }

  private static class PhotoDirLoaderCallbacks implements LoaderManager.LoaderCallbacks<Cursor> {

    private WeakReference<Context> context;
    private PhotosResultCallback resultCallback;

    public PhotoDirLoaderCallbacks(Context context, PhotosResultCallback resultCallback) {
      this.context = new WeakReference<>(context);
      this.resultCallback = resultCallback;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
      return new PhotoDirectoryLoader(context.get(), args.getBoolean(PhotoLoaderConstant.EXTRA_SHOW_GIF, false));
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

      if (data == null)  return;
      List<PhotoDirectory> directories = new ArrayList<>();
      PhotoDirectory photoDirectoryAll = new PhotoDirectory();
      photoDirectoryAll.setName(context.get().getString(R.string.picker_all_image));
      photoDirectoryAll.setId("ALL");

      while (data.moveToNext()) {

        int imageId  = data.getInt(data.getColumnIndexOrThrow(_ID));
        String bucketId = data.getString(data.getColumnIndexOrThrow(BUCKET_ID));
        String name = data.getString(data.getColumnIndexOrThrow(BUCKET_DISPLAY_NAME));
        String path = data.getString(data.getColumnIndexOrThrow(DATA));
        long size = data.getInt(data.getColumnIndexOrThrow(SIZE));

        if (size < 1) continue;

        PhotoDirectory photoDirectory = new PhotoDirectory();
        photoDirectory.setId(bucketId);
        photoDirectory.setName(name);

        if (!directories.contains(photoDirectory)) {
          photoDirectory.setCoverPath(path);
          photoDirectory.addPhoto(imageId, path);
          photoDirectory.setDateAdded(data.getLong(data.getColumnIndexOrThrow(DATE_ADDED)));
          directories.add(photoDirectory);
        } else {
          directories.get(directories.indexOf(photoDirectory)).addPhoto(imageId, path);
        }

        photoDirectoryAll.addPhoto(imageId, path);
      }
      if (photoDirectoryAll.getPhotoPaths().size() > 0) {
        photoDirectoryAll.setCoverPath(photoDirectoryAll.getPhotoPaths().get(0));
      }
      directories.add(INDEX_ALL_PHOTOS, photoDirectoryAll);
      if (resultCallback != null) {
        resultCallback.onResultCallback(directories);
      }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
  }


  public interface PhotosResultCallback {
    void onResultCallback(List<PhotoDirectory> directories);
  }

}
