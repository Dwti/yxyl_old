package com.yanxiu.gphone.studentold.view.picsel.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.provider.MediaStore.Audio.Albums;
import android.provider.MediaStore.Images.Media;
import android.provider.MediaStore.Images.Thumbnails;
import android.util.Log;

import com.common.core.utils.LogInfo;
import com.common.core.utils.StringUtils;
import com.yanxiu.gphone.studentold.view.picsel.bean.ImageBucket;
import com.yanxiu.gphone.studentold.view.picsel.bean.ImageItem;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

/**
 * 专辑帮助类
 *
 * @author Administrator
 *
 */
public class AlbumHelper {
	private final String TAG = getClass().getSimpleName();
	private Context context;
	private ContentResolver cr;

	// 缩略图列表
	private final HashMap<String, String> thumbnailList = new HashMap<String, String>();
	// 专辑列表
	private final List<HashMap<String, String>> albumList = new ArrayList<HashMap<String, String>>();
	private final HashMap<String, ImageBucket> bucketList = new HashMap<>();

	private static AlbumHelper instance;

	private AlbumHelper() {
	}

	public static AlbumHelper getHelper() {
		if (instance == null) {
			instance = new AlbumHelper();
		}
		return instance;
	}

	/**
	 * 初始化
	 *
	 * @param context
	 */
	public void init(Context context) {
		if (this.context == null) {
			this.context = context;
			cr = context.getContentResolver();
		}
	}

	/**
	 * 得到缩略图
	 */
	private void getThumbnail() {
		String[] projection = { Thumbnails._ID, Thumbnails.IMAGE_ID,
				Thumbnails.DATA };
		Cursor cursor = cr.query(Thumbnails.EXTERNAL_CONTENT_URI, projection,
				null, null, null);
		getThumbnailColumnData(cursor);
	}

	/**
	 * 从数据库中得到缩略图
	 *
	 * @param cur
	 */
	private void getThumbnailColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int _id;
			int image_id;
			String image_path;
			int _idColumn = cur.getColumnIndex(Thumbnails._ID);
			int image_idColumn = cur.getColumnIndex(Thumbnails.IMAGE_ID);
			int dataColumn = cur.getColumnIndex(Thumbnails.DATA);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				image_id = cur.getInt(image_idColumn);
				image_path = cur.getString(dataColumn);

				// Do something with the values.
				// Log.i(TAG, _id + " image_id:" + image_id + " path:"
				// + image_path + "---");
				// HashMap<String, String> hash = new HashMap<String, String>();
				// hash.put("image_id", image_id + "");
				// hash.put("path", image_path);
				// thumbnailList.add(hash);
				thumbnailList.put("" + image_id, image_path);
			} while (cur.moveToNext());
		}
		cur.close();
	}

	/**
	 * 得到原图
	 */
	void getAlbum() {
		String[] projection = { Albums._ID, Albums.ALBUM, Albums.ALBUM_ART,
				Albums.ALBUM_KEY, Albums.ARTIST, Albums.NUMBER_OF_SONGS };
		Cursor cursor = cr.query(Albums.EXTERNAL_CONTENT_URI, projection, null,
				null, null);
		getAlbumColumnData(cursor);

	}

	/**
	 * 从本地数据库中得到原图
	 *
	 * @param cur
	 */
	private void getAlbumColumnData(Cursor cur) {
		if (cur.moveToFirst()) {
			int _id;
			String album;
			String albumArt;
			String albumKey;
			String artist;
			int numOfSongs;

			int _idColumn = cur.getColumnIndex(Albums._ID);
			int albumColumn = cur.getColumnIndex(Albums.ALBUM);
			int albumArtColumn = cur.getColumnIndex(Albums.ALBUM_ART);
			int albumKeyColumn = cur.getColumnIndex(Albums.ALBUM_KEY);
			int artistColumn = cur.getColumnIndex(Albums.ARTIST);
			int numOfSongsColumn = cur.getColumnIndex(Albums.NUMBER_OF_SONGS);

			do {
				// Get the field values
				_id = cur.getInt(_idColumn);
				album = cur.getString(albumColumn);
				albumArt = cur.getString(albumArtColumn);
				albumKey = cur.getString(albumKeyColumn);
				artist = cur.getString(artistColumn);
				numOfSongs = cur.getInt(numOfSongsColumn);

				// Do something with the values.
				Log.i(TAG, _id + " album:" + album + " albumArt:" + albumArt
						+ "albumKey: " + albumKey + " artist: " + artist
						+ " numOfSongs: " + numOfSongs + "---");
				HashMap<String, String> hash = new HashMap<String, String>();
				hash.put("_id", _id + "");
				hash.put("album", album);
				hash.put("albumArt", albumArt);
				hash.put("albumKey", albumKey);
				hash.put("artist", artist);
				hash.put("numOfSongs", numOfSongs + "");
				albumList.add(hash);

			} while (cur.moveToNext());

		}
	}

	/**
	 * 是否创建了图片集
	 */
	private boolean hasBuildImagesBucketList = false;

	/**
	 * 得到图片集
	 */
	private void buildImagesBucketList() throws ParseException {
		// 构造缩略图索引
		getThumbnail();
		// 构造相册索引
		String columns[] = new String[] { Media._ID, Media.BUCKET_ID,
				Media.PICASA_ID, Media.DATA, Media.DISPLAY_NAME, Media.TITLE,
				Media.SIZE, Media.BUCKET_DISPLAY_NAME,Media.DATE_TAKEN };
		// 得到一个游标
		Cursor cur = cr.query(Media.EXTERNAL_CONTENT_URI, columns, null, null,
				Media.DATE_TAKEN +" desc");
		if (cur != null && cur.moveToFirst()) {
			// 获取指定列的索引
			int photoIDIndex = cur.getColumnIndexOrThrow(Media._ID);
			int photoPathIndex = cur.getColumnIndexOrThrow(Media.DATA);
			int photoNameIndex = cur.getColumnIndexOrThrow(Media.DISPLAY_NAME);
			int photoTitleIndex = cur.getColumnIndexOrThrow(Media.TITLE);
			int photoSizeIndex = cur.getColumnIndexOrThrow(Media.SIZE);
			int bucketDisplayNameIndex = cur
					.getColumnIndexOrThrow(Media.BUCKET_DISPLAY_NAME);
			int bucketIdIndex = cur.getColumnIndexOrThrow(Media.BUCKET_ID);
			int picasaIdIndex = cur.getColumnIndexOrThrow(Media.PICASA_ID);
			int dateTokenIndex=cur.getColumnIndexOrThrow(Media.DATE_TAKEN);
			// 获取图片总数
			int totalNum = cur.getCount();
			do {
				String _id = cur.getString(photoIDIndex);
				String name = cur.getString(photoNameIndex);
				String path = cur.getString(photoPathIndex);
				String title = cur.getString(photoTitleIndex);
				String size = cur.getString(photoSizeIndex);
				String bucketName = cur.getString(bucketDisplayNameIndex);
				String bucketId = cur.getString(bucketIdIndex);
				String picasaId = cur.getString(picasaIdIndex);
				String dateToken=cur.getString(dateTokenIndex);
				LogInfo.log(TAG, _id + ", bucketId: " + bucketId + ", picasaId: "
						+ picasaId + " name:" + name + " path:" + path
						+ " title: " + title + " size: " + size + " bucket: "
						+ bucketName + "---"+"datemilliseconds: "+dateToken);
				ImageBucket bucket = bucketList.get(bucketId);
				if (bucket == null) {
					bucket = new ImageBucket();
					bucketList.put(bucketId, bucket);
					bucket.setImageList(new ArrayList<ImageItem>());
					bucket.setBucketName(bucketName);
					if(StringUtils.isEmpty(dateToken)){
						bucket.setDateToken("0");
					}else{
						bucket.setDateToken(dateToken);
					}
				}
				bucket.count++;
				ImageItem imageItem = new ImageItem();
				imageItem.setImageId(_id);
				imageItem.setDateToken(dateToken);
				imageItem.setImagePath(path);
				imageItem.setThumbnailPath(thumbnailList.get(_id));
				bucket.getImageList().add(imageItem);

			} while (cur.moveToNext());


		}

		for (Entry<String, ImageBucket> entry : bucketList.entrySet()) {
			ImageBucket bucket = entry.getValue();
			LogInfo.log(TAG, entry.getKey() + ", " + bucket.getBucketName() + ", "
					+ bucket.count + " ---------- ");
			int size = bucket.getImageList().size();
			for (int i = 0; i < size; ++i) {
				ImageItem image = bucket.getImageList().get(i);
				Log.d(TAG, "----- " + image.getImageId() + ", " + image.getImagePath()
						+ ", " + image.getThumbnailPath());
			}
		}
		hasBuildImagesBucketList = true;

		cur.close();
	}

	/**
	 * 得到图片集
	 *
	 * @param refresh
	 * @return
	 */
	public List<ImageBucket> getImagesBucketList(boolean refresh) throws ParseException {
		if (refresh || (!refresh && !hasBuildImagesBucketList)) {
			try {
				buildImagesBucketList();
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}
		List<ImageBucket> tmpList = new ArrayList<>();
		for (Entry<String, ImageBucket> entry : bucketList.entrySet()) {
			tmpList.add(entry.getValue());
		}
		return AlbumSortUtils.sortListDesc(tmpList);
	}

	/**
	 * 得到原始图像路径
	 *
	 * @param image_id
	 * @return
	 */
	String getOriginalImagePath(String image_id) {
		String path = null;
		Log.i(TAG, "---(^o^)----" + image_id);
		String[] projection = { Media._ID, Media.DATA };
		Cursor cursor = cr.query(Media.EXTERNAL_CONTENT_URI, projection,
				Media._ID + "=" + image_id, null, null);
		if (cursor != null) {
			cursor.moveToFirst();
			path = cursor.getString(cursor.getColumnIndex(Media.DATA));

		}
		cursor.close();
		return path;
	}


	public void resetParmas(){
		thumbnailList.clear();
		albumList .clear();
		bucketList .clear();
		hasBuildImagesBucketList=false;
	}

}
