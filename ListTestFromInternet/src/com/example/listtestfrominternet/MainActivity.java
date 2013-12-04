package com.example.listtestfrominternet;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.ListView;

public class MainActivity extends Activity {
	private ListView list;
	private ArrayList<HashMap<String, String>> data;
	private NewListAdapter adapter;
	private ImageView image;
	private Thread thread;
	private Bitmap bitmap;
	private FileCache fileCache;

	private String path = "http://news.xinhuanet.com/ent/2006-12/20/xinsrc_55212032022096192355310.jpg";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		data = dataProvider();
		list = (ListView) findViewById(R.id.testList1);
		// adapter = new MyListAdapterForOneURL(this, data);
		// fileCache = new FileCache(this);

		adapter = new NewListAdapter(data, this);
		list.setAdapter(adapter);
		// image = (ImageView) findViewById(R.id.imageview1);
		// try {
		// File f = fileCache
		// .getFile(String
		// .valueOf(new URL(
		// "http://news.xinhuanet.com/ent/2006-12/20/xinsrc_55212032022096192355310.jpg")
		// .hashCode()));
		//
		// Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(f));
		// image.setImageResource(R.drawable.ic_launcher);
		// } catch (FileNotFoundException e) {
		// // TODO Auto-generated catch block
		// Log.w("FileCache", "Notfound");
		// e.printStackTrace();
		//
		// } catch (MalformedURLException e) {
		// // TODO Auto-generated catch block
		// Log.w("URL", "it is not a url");
		// e.printStackTrace();
		// }

		/*
		 * thread = new Thread(new ImageDownload()); thread.start();
		 */
		/*
		 * Bitmap bitmap = MyListAdapterForOneURL .getBitMap(
		 * "http://news.xinhuanet.com/ent/2006-12/20/xinsrc_55212032022096192355310.jpg"
		 * );
		 */
	}

	private ArrayList<HashMap<String, String>> dataProvider() {
		ArrayList<HashMap<String, String>> data = new ArrayList<HashMap<String, String>>();

		for (int i = 1; i <= 7; i++) {
			HashMap<String, String> hash = new HashMap<String, String>();
			hash.put("Title", "This is a test");
			if (i == 1) {
				hash.put("Image",
						"http://news.xinhuanet.com/ent/2006-12/20/xinsrc_55212032022096192355310.jpg");
			}
			if (i == 2) {
				hash.put("Image",
						"http://imge.gmw.cn/attachement/jpg/site2/20130411/bc305bbef94412d0e7ea09.jpg");
			}
			if (i == 3) {
				hash.put(
						"Image",
						"http://img001.photo.21cn.com/photos/album/20080716/o/592FB7C36922F0E774AA0EEEFD5C7253.jpg");
			}
			if (i == 4) {
				hash.put(
						"Image",
						"http://img001.photo.21cn.com/photos/album/20120926/o/F28A1CD7F827F4A1DDBBD8BE778C93F9.jpg");
			}
			if (i == 5) {
				hash.put(
						"Image",
						"http://img001.photo.21cn.com/photos/album/20120926/o/C4B57B368801A0583AB5B7803F7869E8.jpg");
			}
			if (i == 6) {
				hash.put("Image",
						"http://imgnews.mingxing.com/upload/picture/2013/04/05OEnYD.jpg");
			}
			if (i == 7) {
				hash.put("Image",
						"http://m1.aboluowang.com/ent/data/uploadfile/200804/20080405074036940.jpg");
			}

			data.add(hash);
		}
		return data;
	}
	/*
	 * private Bitmap getBitmap(String path) {
	 * 
	 * }
	 */
	//
	// class ImageDownload implements Runnable {
	//
	// @Override
	// public void run() {
	// URL url;
	// try {
	// url = new URL(path);
	// HttpURLConnection conn = (HttpURLConnection) url
	// .openConnection();
	// conn.setConnectTimeout(5000);
	// conn.setRequestMethod("GET");
	// if (conn.getResponseCode() == 200) {
	// ByteArrayOutputStream os = new ByteArrayOutputStream();
	// InputStream is = conn.getInputStream();
	// byte[] buffer = new byte[1024 * 5];
	// int len = 0;
	// while ((len = is.read(buffer)) != -1) {
	// os.write(buffer, 0, len);
	// }
	// bitmap = BitmapFactory.decodeByteArray(os.toByteArray(), 0,
	// os.toByteArray().length);
	// image.setImageBitmap(bitmap);
	// }
	// } catch (IOException e) {
	// e.printStackTrace();
	// }
	//
	// }
	// }
	//
	// @Override
	// protected void onDestroy() {
	// thread.stop();
	// super.onDestroy();
	//
	// }
}
