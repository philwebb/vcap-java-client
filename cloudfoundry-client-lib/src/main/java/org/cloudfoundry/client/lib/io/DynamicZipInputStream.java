package org.cloudfoundry.client.lib.io;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class DynamicZipInputStream extends DynamicInputStream {

	private static final int BUFFER_SIZE = 4096;
	private static InputStream EMPTY_STREAM = new InputStream() {

		@Override
		public int read() throws IOException {
			return -1;
		}
	};

	private ZipOutputStream zipStream;
	private Iterator<Entry> entries;
	private InputStream entryStream = EMPTY_STREAM;

	private byte[] buffer = new byte[BUFFER_SIZE];

	public DynamicZipInputStream(Iterable<Entry> entries) {
		this.zipStream = new ZipOutputStream(getOutputStream());
		this.entries = entries.iterator();
	}

	@Override
	protected boolean writeMoreData() throws IOException {
		int count = entryStream.read(buffer);
		if (count != -1) {
			zipStream.write(buffer, 0, count);
			return true;
		}

		zipStream.closeEntry();
		entryStream.close();

		if (entries.hasNext()) {
			Entry entry = entries.next();
			zipStream.putNextEntry(new ZipEntry(entry.getName()));
			entryStream = entry.getInputStream();
			return true;
		}

		zipStream.flush();
		zipStream.close();
		return false;
	}

	@Override
	public void close() throws IOException {
		super.close();
		zipStream.close();
	}

	public static interface Entry {
		String getName();

		InputStream getInputStream();
	}
}
