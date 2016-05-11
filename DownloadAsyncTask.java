package com.micecop.singlepamplona;

import static android.widget.Toast.LENGTH_SHORT;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.HttpURLConnection;
import java.net.URL;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

class DownloadAsyncTask extends AsyncTask<String, Void, String> {

	private Context context;
	private String directorio;
	private FileOutputStream fileOutputStream;
	private String nombreArchivo;
	private boolean hecho;
	private ProgressDialog pDialog;
	private boolean directa;

	public DownloadAsyncTask(Context context) {

		this.context = context;
		directorio = "Pictures/Singles";
		this.nombreArchivo = null;
		this.directa = false;

	}

	public DownloadAsyncTask(Context context, String directorio,
			String nombreArchivo) {
		this.context = context;
		this.directorio = directorio;
		//this.nombreArchivo = null;
		this.nombreArchivo = nombreArchivo;
		this.directa = true;
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		Dialog dialogo = new Dialog(context);
		showDialog(0);
	}

	private void showDialog(int id) {
		// TODO Auto-generated method stub
		switch (id) {
		case 0:
			pDialog = new ProgressDialog(context);
			pDialog.setMessage("Archivo descargándose. Por favor espere...");
			pDialog.setIndeterminate(false);
			pDialog.setMax(100);
			pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			pDialog.setCancelable(true);
			pDialog.show();
		}

	}

	@Override
	protected String doInBackground(String... arg0) {
		String result = null;
		String url = arg0[0];
		Log.d("URL: ", url);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			URL urlObject = null;
			InputStream inputStream = null;
			HttpURLConnection urlConnection = null;

			try {
				urlObject = new URL(url);
				urlConnection = (HttpURLConnection) urlObject.openConnection();
				System.out.println("URL admitida: "+urlConnection.getURL());
				//Log.d("URL codificada: ", urlConnection.getURL());
				// urlConnection . setRequestMethod ( "GET" );
				// urlConnection . setDoOutput ( true );
				// urlConnection . connect ();
				inputStream = urlConnection.getInputStream();
				System.out.println("ImputStream: "+inputStream.toString());
				// inputStream = new BufferedInputStream(urlObject.openStream(),
				// 8192);

				String fileName = Environment.getExternalStorageDirectory()
						.getAbsolutePath() + "/" + directorio;
				File directory = new File(fileName);
				/*
				 * File file = new File(directory, url.substring(url
				 * .lastIndexOf("/")));
				 */
				if (nombreArchivo == null) {
					nombreArchivo = getNameFile(url);
				}
				File file = new File(directory, nombreArchivo);
				directory.mkdirs();

				fileOutputStream = new FileOutputStream(file);
				ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
				byte[] buffer = new byte[1024];
				int available = inputStream.available();
				int lenghtOfFile = urlConnection.getContentLength();
				if (lenghtOfFile < 0) {
					lenghtOfFile = 2452599;
					available = 1;
					
					Log.d("Descarga","tamaño defecto" + " bufer: "+inputStream.available());
				} else {
					Log.d("Descarga", "tamaño: " + lenghtOfFile  + " bufer: "+inputStream.available());
				}
				int len = 0;
				long total = 0;

				while ((available >0)
						&& (len = inputStream.read(buffer)) != -1) {
					total += len;
					Log.d("Descargando", "len: "+len + ", total: " + total + " de "+ lenghtOfFile+ "bufer: "+inputStream.available());
					onProgressUpdate("" + (int) ((total * 100) / lenghtOfFile));
					byteArrayOutputStream.write(buffer, 0, len);
				}

				fileOutputStream.write(byteArrayOutputStream.toByteArray());
				fileOutputStream.flush();
				result = "Guardado en : " + file.getAbsolutePath();
				hecho = true;
			} catch (Exception ex) {
				result = ex.getClass().getSimpleName() + " " + ex.getMessage();
				hecho = false;
			} finally {
				if (inputStream != null) {
					try {
						inputStream.close();
					} catch (IOException ex) {
						result = ex.getClass().getSimpleName() + " "
								+ ex.getMessage();
						hecho = false;
					}
				}
				if (fileOutputStream != null) {
					try {
						fileOutputStream.close();
					} catch (IOException ex) {
						result = ex.getClass().getSimpleName() + " "
								+ ex.getMessage();
						hecho = false;
					}
				}
				if (urlConnection != null) {
					urlConnection.disconnect();
				}
			}
		} else {
			result = "Almacenamiento no disponible";
			hecho = false;
		}

		return result;
	}

	/**
	 * Updating progress bar
	 * */
	protected void onProgressUpdate(String... progress) {
		// setting progress percentage
		pDialog.setProgress(Integer.parseInt(progress[0]));
	}

	@Override
	protected void onPostExecute(String result) {
		String mensage;
		if (hecho) {
			mensage = "Desacarga completada";
		} else {
			mensage = "Ha fallado la descarga";
		}
		pDialog.dismiss();
		// String imagePath =
		// Environment.getExternalStorageDirectory().toString() +
		// "/downloadedfile.jpg";
		if (!directa) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(result).setNegativeButton("Aceptar", null)
					.setTitle(mensage);
			if (hecho) {
				addImageGallery();
				builder.setPositiveButton("Abrir",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								abrirAchivo();
							}
						});
			}
			builder.show();
		} else if (hecho) {
			abrirAchivo();
			((Actividad) context).finish();

		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(result)
					.setTitle(mensage);
			builder.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								((Actividad) context).comprobarSesion();
							}
						});
			
			builder.show();
		}
	}

	public void abrirAchivo() {
		String direccion = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + directorio + "/" + nombreArchivo;
		File myFile = new File(direccion);
		try {
			FileOpen.openFile(context, myFile);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getNameFile(String url) {
		String nameFile = "";
		String[] arrayPartes = url.split("/");

		if (url.contains("jpeg")) {
			nameFile = buscarsubcadena(arrayPartes, "jpeg");
		} else if (url.contains("jpg")) {
			nameFile = buscarsubcadena(arrayPartes, "jpg");
		} else if (url.contains("png")) {
			nameFile = buscarsubcadena(arrayPartes, "png");
		} else if (url.contains("gif")) {
			nameFile = buscarsubcadena(arrayPartes, "gif");
		} else if (url.contains("apk")) {
				nameFile = buscarsubcadena(arrayPartes, "apk");
		} else if (nombreArchivo == null) {
			return "imagen";
		} else {
			return nombreArchivo;
		}

		// Matcher matcher = patron.matcher(url);
		// matcher.find();
		// String nameFile = matcher.group(1);
		Log.d("Nombre archivo", nameFile);
		return nameFile;
	}
	private void addImageGallery() {
		String direccion = Environment.getExternalStorageDirectory()
				.getAbsolutePath() + "/" + directorio + "/" + nombreArchivo;
		File file = new File(direccion);
	    ContentValues values = new ContentValues();
	    values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
	    values.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg"); // setar isso
	    context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
	}
	
	private String buscarsubcadena(String[] cadena, String busqueda) {
		String subcadena = "";
		for (int k = 0; k < cadena.length; k++) {
			if (cadena[k].contains(busqueda)) {
				subcadena = cadena[k].substring(0, cadena[k].indexOf(busqueda))
						+ busqueda;
			}

		}
		return subcadena;
	}

}
