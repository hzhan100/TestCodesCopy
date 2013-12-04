package com.example.listtestfrominternet;

import java.io.File;

import android.content.Context;
import android.util.Log;

public class FileCache {
	private File cacheDir;

	public FileCache(Context context) {
		// ��һ����������ͼƬ��·��
		if (android.os.Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED))
			cacheDir = new File(
					android.os.Environment.getExternalStorageDirectory(),
					"Downloaded_image");
		else
			cacheDir = context.getCacheDir();
		if (!cacheDir.exists())
			cacheDir.mkdirs();
		Log.w("FilePahFilePath", cacheDir.getAbsolutePath());
	}

	public File getFile(String url) {

		String filename = String.valueOf(url.hashCode());

		File f = new File(cacheDir.getAbsolutePath() + "/" + filename);
		return f;

	}

	public void clear() {
		File[] files = cacheDir.listFiles();
		if (files == null)
			return;
		for (File f : files)
			f.delete();
	}

}
