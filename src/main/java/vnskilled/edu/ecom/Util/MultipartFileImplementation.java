package vnskilled.edu.ecom.Util;

import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class MultipartFileImplementation implements MultipartFile  {


		private final byte[] bytes;
		private final String filename;

		public MultipartFileImplementation(byte[] bytes, String filename) {
			this.bytes = bytes;
			this.filename = filename;
		}

		@Override
		public String getName() {
			return filename;
		}

		@Override
		public String getOriginalFilename() {
			return filename;
		}

		@Override
		public String getContentType() {
			return "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"; // MIME type cho Excel
		}

		@Override
		public boolean isEmpty() {
			return bytes.length == 0;
		}

		@Override
		public long getSize() {
			return bytes.length;
		}

		@Override
		public byte[] getBytes() {
			return bytes;
		}

		@Override
		public InputStream getInputStream() throws IOException {
			return new ByteArrayInputStream(bytes);
		}

		@Override
		public void transferTo(File file) throws IOException, IllegalStateException {
			// Không cần implement cho mục đích này
			throw new UnsupportedOperationException("Not implemented");
		}
	}