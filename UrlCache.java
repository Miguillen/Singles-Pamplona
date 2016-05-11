package com.micecop.singlepamplona;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.content.Context;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.webkit.WebResourceResponse;

public class UrlCache {

	public static final long ONE_SECOND = 1000L;
	public static final long ONE_MINUTE = 60L * ONE_SECOND;
	public static final long ONE_HOUR = 60L * ONE_MINUTE;
	public static final long ONE_DAY = 24 * ONE_HOUR;

	private static class CacheEntry {
		private String url;
		public String fileName;
		public String mimeType;
		public String encoding;
		public long maxAgeMillis;

		private CacheEntry(String url, String fileName, String mimeType,
				String encoding, long maxAgeMillis) {

			this.setUrl(url);
			this.fileName = fileName;
			this.mimeType = mimeType;
			this.encoding = encoding;
			this.maxAgeMillis = maxAgeMillis;
		}

		@SuppressWarnings("unused")
		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}
	}

	protected Map<String, CacheEntry> cacheEntries = new HashMap<String, CacheEntry>();
	protected Activity activity = null;
	protected File rootDir = null;

	public UrlCache(Activity activity) {
		this.activity = activity;
		this.rootDir = this.activity.getFilesDir();
	}

	public UrlCache(Activity activity, File rootDir) {
		this.activity = activity;
		this.rootDir = rootDir;
	}

	public void register(String url, String cacheFileName, String mimeType,
			String encoding, long maxAgeMillis) {

		CacheEntry entry = new CacheEntry(url, cacheFileName, mimeType,
				encoding, maxAgeMillis);

		this.cacheEntries.put(url, entry);
	}

	public WebResourceResponse load(String url) {
		CacheEntry cacheEntry = this.cacheEntries.get(url);

		if (cacheEntry == null)
			return null;

		File cachedFile = new File(this.rootDir.getPath() + File.separator
				+ cacheEntry.fileName);

		if (cachedFile.exists()) {
			long cacheEntryAge = System.currentTimeMillis()
					- cachedFile.lastModified();
			if (cacheEntryAge > cacheEntry.maxAgeMillis) {
				cachedFile.delete();

				// Archivo en caché borrado, carga load() de nuevo.
				Log.d(Constants._COUNT, "Borrado de la memoria caché: " + url);
				return load(url);
			}

			// Existe el archivo en caché y no es demasiado antiguo. Devuelve el
			// archivo.
			Log.d(Constants._COUNT, "leyendo de caché: " + url);
			try {
				return new WebResourceResponse(cacheEntry.mimeType,
						cacheEntry.encoding, new FileInputStream(cachedFile));
			} catch (FileNotFoundException e) {
				Log.d(Constants._COUNT, "Error al cargar el archivo en caché:"
						+ cachedFile.getPath() + " : " + e.getMessage(), e);
			}

		} else {
			try {
				downloadAndStore(url, cacheEntry, cachedFile);

				// Ahora existe el archivo en la memoria caché, por lo que sólo
				// se puede llamar a este
				// Método nuevo para leerlo.
				return load(url);
			} catch (Exception e) {
				Log.d(Constants._COUNT, "Error al leer el archivo por la red: "
						+ cachedFile.getPath(), e);
			}
		}

		return null;
	}

	private void downloadAndStore(String url, CacheEntry cacheEntry,
			File cachedFile) throws IOException {

		URL urlObj = new URL(url);
		URLConnection urlConnection = urlObj.openConnection();
		InputStream urlInput = urlConnection.getInputStream();

		FileOutputStream fileOutputStream = this.activity.openFileOutput(
				cacheEntry.fileName, Context.MODE_PRIVATE);

		int data = urlInput.read();
		while (data != -1) {
			fileOutputStream.write(data);

			data = urlInput.read();
		}

		urlInput.close();
		fileOutputStream.close();
		Log.d(Constants._COUNT, "Cache file: " + cacheEntry.fileName
				+ " stored. ");
	}
}
