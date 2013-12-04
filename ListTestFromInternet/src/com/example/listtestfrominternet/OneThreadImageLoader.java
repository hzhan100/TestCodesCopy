package com.example.listtestfrominternet;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class OneThreadImageLoader {
	private HandlerThread oneThread;
	private ImageDownLoadHandler mHandler;

	private MemoryCache memoryCache = new MemoryCache();
	private FileCache fileCache;
	// ���������������е�image������url��Ӧ����Ϣ�������ˢ��ui��ʱ�������˶��ǲ���һ����
	// �����������ʮ�����ã���������ʹ��һ��handler������Ϣ��ʱ��
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	private Context context;

	public OneThreadImageLoader(Context context) {
		this.context = context;
		fileCache = new FileCache(context);
		oneThread = new HandlerThread("ImageDownloadOneThread");
		oneThread.start();
		mHandler = new ImageDownLoadHandler(oneThread.getLooper());

	}

	public void sendDownloadMessage(PhotoHolder photo) {
		Message msg = mHandler.obtainMessage();
		msg.obj = photo;

		mHandler.sendMessage(msg);
	}

	// the main function of loading image
	public void DisplayImage(String url, ImageView imageView,
			RelativeLayout relativeLayout) {
		InsertProgressbar mProgressbar = new InsertProgressbar(relativeLayout,
				context);
		imageViews.put(imageView, url);
		// memoryCache�����þ����������Ѿ����еĳ����б�����һ���ڴ����ڱ������ݣ�������ʹ���ǿ��ٵĻ�ȡ������������ȥ��ȡfile��download��
		// Ϊ�˷�ֹ�ڴ����������һ���ʹ��java�ṩ��softReference������ʱ�洢���ݡ�
		// ����fileCache��ͬ�������̽������ڴ���Ȼ���ͷ���
		Bitmap bitmap = memoryCache.get(url);
		mProgressbar.insertBar();
		PhotoHolder photo = new PhotoHolder(url, imageView, mProgressbar);
		if (bitmap != null) {
			imageView.setImageBitmap(bitmap);
			mProgressbar.stopBar();

		} else {

			sendDownloadMessage(photo);
			// queuePhoto(url, imageView);
			imageView.setImageResource(R.drawable.moren);
		}
	}

	@SuppressLint("HandlerLeak")
	class ImageDownLoadHandler extends Handler {

		public ImageDownLoadHandler(Looper loop) {
			super(loop);
		}

		public ImageDownLoadHandler() {
			super();
		}

		@Override
		public void handleMessage(Message msg) {
			Bitmap bitmap;
			// ��Ϊ�������������һ��photoholder�������մ������ݣ����е�message�и�����ͬ��photoholder�������������������ڴ���ͬ����Ϣ��ʱ�����holder�е������ǲ�ͬ��
			// Ȼ�������������holderȥ����ui��
			PhotoHolder photo = (PhotoHolder) msg.obj;
			String url = photo.url;
			ImageView image = photo.image;
			bitmap = downloadImage(url);
			// MemoryCache��������ֹ�ڴ������ͬʱ�ֲ���̫Ӱ���ȡ������ٶ�
			memoryCache.put(url, bitmap);
			BitmapDisplayer displayer = new BitmapDisplayer(bitmap, photo);
			// photo.mProgressbar.stopBar();
			// �������������ã�����Ϊ����δ��ͨ��uithread��ǿ�и���ui�ϵĿؼ���
			Activity activity = (Activity) image.getContext();
			activity.runOnUiThread(displayer);
		}
	}

	private Bitmap downloadImage(String url) {
		Bitmap bitmap;
		bitmap = getBitmap(url);
		return bitmap;

	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// �ȴ��ļ������в����Ƿ���
		Bitmap b = null;
		if (f != null && f.exists()) {
			b = decodeFile(f);
		}
		if (b != null) {
			return b;
		}
		// ����ָ����url������ͼƬ
		try {
			// BufferedOutputStream bufferStream;
			Bitmap bitmap = null;
			URL imageUrl;
			Log.w("URLURL", url);
			imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(5000);
			conn.setReadTimeout(10000);
			conn.setDoInput(true);
			// conn.setInstanceFollowRedirects(true);
			conn.setRequestMethod("GET");
			conn.connect();
			InputStream is = new BufferedInputStream(conn.getInputStream());
			// OutputStream os = new BufferedOutputStream(new
			// FileOutputStream(f));
			// OutputStream os = new FileOutputStream(f);
			// Utils.CopyStream(is, os);
			// os.close();
			Utils.CopyStream(is, f);/*
									 * I spent a long time on this part,
									 * although I do not know why it could work,
									 * I know another CopyStream doesn't work
									 */
			bitmap = decodeFile(f);
			return bitmap;
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("getBitmap URL", e.getMessage());
			return null;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Log.w("getBitmap IO", e.getMessage());
			return null;
		}

	}

	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	class PhotoHolder {
		public ImageView image;
		public String url;
		public InsertProgressbar mProgressbar;

		public PhotoHolder(String url, ImageView image,
				InsertProgressbar mProgressbar) {
			this.mProgressbar = mProgressbar;
			this.url = url;
			this.image = image;
		}
	}

	// �������Ҫ����Ŀǰ��Ϊ������android��listView��ˢ��ʱ���̵߳ģ�����������getView��û�м�synchronized�ĺ������Կ��ܻ�������������⣬�����⻹�Ǻ���ֵ�
	boolean imageViewReused(PhotoHolder photoholder) {
		String tag = imageViews.get(photoholder.image);
		if (tag == null || !tag.equals(photoholder.url))
			return true;
		return false;
	}

	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoHolder photoholder;
		InsertProgressbar mProgreebar;

		public BitmapDisplayer(Bitmap bitmap, PhotoHolder photoholder) {
			this.bitmap = bitmap;
			this.photoholder = photoholder;
		}

		public void run() {
			if (imageViewReused(photoholder)) {
				photoholder.mProgressbar.stopBar();
				return;
			}
			if (bitmap != null) {

				photoholder.image.setImageBitmap(bitmap);
				// bitmap.recycle();

			} else {
				photoholder.image.setImageResource(R.drawable.ic_launcher);
			}
			photoholder.mProgressbar.stopBar();
		}
	}
}
