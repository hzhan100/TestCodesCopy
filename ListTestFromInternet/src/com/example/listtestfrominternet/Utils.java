package com.example.listtestfrominternet;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

class Utils {

	public static void CopyStream(InputStream inStream, File f) {
		try {
			ByteArrayOutputStream outByteStream = new ByteArrayOutputStream();
			//通过file来穿件一个outputStream，这样往outputStream中写数据就是往file中写数据
			//似乎outputStream就是一种数据流的概念，具体写到哪儿怎么写都是根据具体情况来定的，所以应该有多种创建它的方法
			OutputStream os = new BufferedOutputStream(new FileOutputStream(f));
			byte[] buffer = new byte[1024];
			int len = -1;
			while ((len = inStream.read(buffer)) != -1) {
				outByteStream.write(buffer, 0, len);
			}
			outByteStream.close();
			inStream.close();
			outByteStream.writeTo(os);
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void CopyStream(InputStream is, OutputStream os) {
		final int buffer_size = 1024;
		try {
			byte[] bytes = new byte[buffer_size];
			for (;;) {
				int count = is.read(bytes, 0, buffer_size);
				if (count == -1)
					break;
				os.write(bytes, 0, count);

			}
			is.close();
			os.close();
		} catch (Exception ex) {
		}
	}

	public static byte[] read(InputStream instream) throws Exception {
		ByteArrayOutputStream outstream = new ByteArrayOutputStream();
		byte[] buffer = new byte[1024];
		int len = 0;
		while ((len = instream.read(buffer)) != -1) {
			outstream.write(buffer, 0, len);
		}
		instream.close();
		return outstream.toByteArray();
	}
	
	
	
}