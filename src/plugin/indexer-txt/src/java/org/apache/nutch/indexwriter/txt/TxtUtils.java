package org.apache.nutch.indexwriter.txt;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TxtUtils {

	public static AtomicInteger ai = new AtomicInteger(1000);
	public static Logger LOG = LoggerFactory.getLogger(TxtUtils.class);

	public static void write(String dataDir, String fileName, String content) throws IOException {
		if (content == null || fileName == null) {
			return;
		}

		String date = TxtUtils.getStringFromDate("yyyyMMdd", new Date());
		String dataDirTmp = dataDir + "/" + date;
		File dir = new File(dataDirTmp);
		if (!dir.exists()) {
			dir.mkdirs();
		}

		File file = new File(dataDirTmp + "/" + fileName);
		if (file.exists()) {
			writeAppend(file, content);
		} else {
			FileOutputStream fos = null;
			FileChannel chnanel = null;
			try {
				fos = new FileOutputStream(file, true);
				chnanel = fos.getChannel();
				ByteBuffer buf = ByteBuffer.wrap(content.getBytes());
				buf.put(content.getBytes());
				buf.flip();
				chnanel.write(buf);
			} finally {
				if (null != chnanel) {
					chnanel.close();
				}
				if (null != fos) {
					fos.close();
				}
			}
		}

	}

	private static void writeAppend(File file, String content) throws IOException {
		RandomAccessFile raf = null;
		FileChannel fc = null;
		try {
			raf = new RandomAccessFile(file, "rw");
			fc = raf.getChannel();
			fc.position(fc.size());// 定位到文件末尾
			fc.write(ByteBuffer.wrap(content.getBytes()));
		} finally {
			if (null != fc) {
				fc.close();
			}
			if (null != raf) {
				raf.close();
			}
		}

	}

	public static String getStringFromDate(String pattern, Date date) {
		if (pattern == null) {
			pattern = "yyyyMMdd";
		}
		if (date != null) {
			DateFormat dateFormat = new SimpleDateFormat(pattern);
			return dateFormat.format(date);
		}
		return null;
	}

	public static String getStringFromDate(String pattern, long millis) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(millis);
		Date date = calendar.getTime();
		return getStringFromDate(pattern, date);
	}

	public static int getIndex() {
		return ai.incrementAndGet();
	}

}
