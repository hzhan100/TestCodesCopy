package com.example.listtestfrominternet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyListAdapterForOneURL extends BaseAdapter {
	private static int IMAGE_MAX_WIDTH = 480;
	private static int IMAGE_MAX_HEIGHT = 960;
	private Activity activity;
	private static LayoutInflater inflater = null;
	private ArrayList<HashMap<String, String>> data;
	private Thread photeloadThread;
	private File mDir;

	public MyListAdapterForOneURL(Activity activity,
			ArrayList<HashMap<String, String>> d) {

		this.activity = activity;
		data = d;
		inflater = (LayoutInflater) this.activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mDir = new File(Environment.getExternalStorageDirectory().getPath()
				+ "/Downloaded_image");

		if (!mDir.exists()) {
			mDir.mkdirs();
		}
		// imageLoader = new ImageLoader(activity.getApplicationContext());
	}

	@Override
	public int getCount() {
		return data.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		View tempView = arg1;
		if (tempView == null)
			tempView = inflater.inflate(R.layout.list_raw, null);
		TextView title = (TextView) tempView.findViewById(R.id.list_span_title);
		TextView summary = (TextView) tempView
				.findViewById(R.id.list_span_summary);

		ImageView image = (ImageView) tempView
				.findViewById(R.id.list_span_image1);
		HashMap<String, String> hash = new HashMap<String, String>();
		hash = data.get(arg0);

		// retrieve data from hashmap
		title.setText(hash.get("Title").toString());
		String url = hash.get("Image").toString();
		// start to download thread and upload ui thread

		// photeloadThread = new Thread(new ImageDownloadThread(url, image));
		// photeloadThread.start();
		// imageLoader.DisplayImage(url, thumb_image);

		return tempView;
	}

	//
	// public static Bitmap getBitmap(Bitmap bitmap) {
	// return bitmap;
	//
	// }

	// private Bitmap decodeFile(File f) {
	// try {
	// // 解码图像大小
	// BitmapFactory.Options o = new BitmapFactory.Options();
	// o.inJustDecodeBounds = true;
	// BitmapFactory.decodeStream(new FileInputStream(f), null, o);
	//
	// // 找到正确的刻度值，它应该是2的幂。
	// final int REQUIRED_SIZE = 70;
	// int width_tmp = o.outWidth, height_tmp = o.outHeight;
	// int scale = 1;
	// while (true) {
	// if (width_tmp / 2 < REQUIRED_SIZE
	// || height_tmp / 2 < REQUIRED_SIZE)
	// break;
	// width_tmp /= 2;
	// height_tmp /= 2;
	// scale *= 2;
	// }
	//
	// BitmapFactory.Options o2 = new BitmapFactory.Options();
	// o2.inSampleSize = scale;
	// return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
	// } catch (FileNotFoundException e) {
	// }
	// return null;
	// }

	class ImageDownloadThread implements Runnable {

		private String url;
		private ImageView image;
		private PhotoHolder photo;
		private Bitmap bitmap = null;

		public ImageDownloadThread(String url, ImageView image) {
			this.url = url;
			this.image = image;
			photo = new PhotoHolder(url, image);

		}

		@Override
		public void run() {
			String imagePath = downloadImage(url);

			while (imagePath != null)
				bitmap = bitmapProvider(imagePath);

			if (bitmap != null) {
				Activity activity = (Activity) photo.image.getContext();
				// although it uses a runnable objective, we do not open a
				// new
				// thread to upload UI.
				activity.runOnUiThread(new ImageUploadPost(bitmap, photo));
			}
		}

		private String downloadImage(String url) {

			// String imagePath = Environment.getExternalStorageDirectory()
			// .toString() + "/Downloaded_image";

			File file = new File(mDir + "/"
					+ url.substring(url.lastIndexOf(".")));
			Thread thread = new Thread(new realDownloadThread(url, file));
			thread.start();
			//
			// URL path = new URL(url);
			//
			// HttpURLConnection conn = (HttpURLConnection)
			// path.openConnection();
			// conn.setConnectTimeout(5000);
			// conn.setRequestMethod("GET");
			// conn.setDoInput(true);
			while (thread.getState() == Thread.State.NEW
					|| thread.getState() == Thread.State.RUNNABLE
					|| thread.getState() != Thread.State.TERMINATED) {
				try {
					Thread.sleep(9000);

				} catch (InterruptedException e) {
					e.printStackTrace();
					return null;
				}

			}
			return file.getAbsolutePath();

		}

		private Bitmap bitmapProvider(String imagePath) {
			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inSampleSize = getImageScale(imagePath);
			Bitmap bitmap = BitmapFactory.decodeFile(imagePath, option);
			return bitmap;

		}

		private int getImageScale(String ImagePath) {

			BitmapFactory.Options option = new BitmapFactory.Options();
			option.inJustDecodeBounds = true;
			BitmapFactory.decodeFile(ImagePath, option);
			int scale = 1;
			while (option.outWidth / scale >= IMAGE_MAX_WIDTH
					|| option.outHeight / scale >= IMAGE_MAX_HEIGHT) {
				scale *= 2;
			}
			return scale;

		}
	}

	class realDownloadThread implements Runnable {
		private String url;
		private File file;

		public realDownloadThread(String url, File file) {
			this.url = url;
			this.file = file;
		}

		@Override
		public void run() {
			try {
				URL path = new URL(url);

				HttpURLConnection conn = (HttpURLConnection) path
						.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				if (conn.getResponseCode() == 200) {
					InputStream is = conn.getInputStream();

					FileOutputStream os = new FileOutputStream(file);
					byte[] buffer = new byte[1024 * 2];
					int len = 0;
					while ((len = is.read(buffer, 0, 1024 * 2)) != -1) {
						os.write(buffer, 0, len);

					}
					is.close();
					os.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
				// Toast.makeText(activity, mDir.getAbsolutePath(),
				// Toast.LENGTH_SHORT).show();
			}
		}
	}

	class ImageUploadPost implements Runnable {
		private Bitmap bitmap;
		private PhotoHolder photo;

		ImageUploadPost(Bitmap bitmap, PhotoHolder photo) {
			this.bitmap = bitmap;
			this.photo = photo;
		}

		@Override
		public void run() {
			photo.image.setImageBitmap(bitmap);
			if (bitmap.isRecycled()) {
				bitmap.recycle();
			}
		}
	}

	class PhotoHolder {
		public String url;
		public ImageView image;

		PhotoHolder(String url, ImageView image) {

			this.url = url;
			this.image = image;

		}

	}
}

/*
 * class ImageDownloadThread implements Runnable { private String url;
 * 
 * ImageDownloadThread(String path) { url = path; }
 * 
 * @Override public void run() { try { URL imageUrl; imageUrl = new URL(url);
 * 
 * HttpURLConnection conn = (HttpURLConnection) imageUrl .openConnection();
 * 
 * conn.setConnectTimeout(5000); conn.setReadTimeout(30000);
 * conn.setDoInput(true); conn.setRequestMethod("GET"); if
 * (conn.getResponseCode() == 200) { InputStream is = conn.getInputStream();
 * byte[] data = Utils.read(is); bitmap = BitmapFactory .decodeByteArray(data,
 * 0, data.length); image.setImageBitmap(bitmap);
 * 
 * // Toast.makeText(activity, url, Toast.LENGTH_LONG).show(); }
 * 
 * File f = new File(Environment.getExternalStorageDirectory() .toString() +
 * "/LazyList", url.toString()); OutputStream os = new FileOutputStream(f);
 * Utils.CopyStream(is, os); os.close(); bitmap = decodeFile(f);
 * 
 * } catch (Exception e) { // TODO Auto-generated catch block
 * 
 * e.printStackTrace(); } }
 * 
 * }
 */

