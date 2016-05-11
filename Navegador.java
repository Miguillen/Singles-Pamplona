package com.micecop.singlepamplona;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.DownloadManager.Request;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.provider.SyncStateContract.Constants;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceResponse;
import android.webkit.JavascriptInterface;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebView;
import android.webkit.WebSettings;
import android.webkit.WebView.HitTestResult;
import android.webkit.WebViewClient;
import static android.widget.Toast.LENGTH_SHORT;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringBufferInputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;

import android.graphics.Bitmap;
import android.net.Uri;
import android.widget.Toast;
import android.widget.ProgressBar;
import android.webkit.JsResult;

public class Navegador extends Actividad {

	private static String raizWeb = "http://www.singlespamplona.es";
	private static String moduloWeb;
	private static String direccionWeb;
	private WebView myWebView;
	private String web;
	private static String urlCompleta;
	private String webInicio;
	private String webIntemt;
	private static int numeroMenu;
	private int menuUtilizar;
	private boolean iniciada;
	private ProgressBar progressBar;
	protected String phoneSelectedOnLongPress;
	private static int actividadAnterior;
	private String mUrl;
	private String mEnlace;
	private boolean isWeb;

	final class DemoJavaScriptInterface {

		private View mHandler;

		/**
		 * No es llamada en la interfaz. Crea un runnable para invocar loadUrl
		 * en la hebra de la interfaz.
		 */

		@JavascriptInterface
		public void clickInAndroid() {
			mHandler.post(new Runnable() {
				public void run() {
					getWebView().loadUrl("javascript:wave()");
					Log.d("interfaz descarga", "llamada");
				}
			});

		}
	}

	private class MyWebViewClient extends WebViewClient {

		private Context context;

		public MyWebViewClient(Context context) {
			this.context = context;
		}

		@Override
		public WebResourceResponse shouldInterceptRequest(final WebView view,
				String url) {

			String url1 = "http://api.ning.com/icons/appatar/3402611?default=3402611&width=32&height=32";
			if (url.startsWith(url1)) {
				return loadFromAssets(url, "logo.png", getMimeType(url), "");
			} else if (url.contains("avatarpam")) {
				return loadFromAssets(url, "logo.png", getMimeType(url), "");
			} /*
			 * else if (url.contains("min.js")){ return
			 * getWebResourceResponseFromString(url); }
			 */else {
				return super.shouldInterceptRequest(view, url);
			}
		}

		private WebResourceResponse getWebResourceResponseFromString(String url) {
			Log.d("JS anulado", url);
			return getUtf8EncodedWebResourceResponse(new StringBufferInputStream(

			"alert('" + url + "')"));

		}

		private WebResourceResponse getUtf8EncodedWebResourceResponse(
				InputStream data) {
			return new WebResourceResponse("text/css", "UTF-8", data);
		}

		private WebResourceResponse loadFromAssets(String url,
				String assetPath, String mimeType, String encoding) {

			AssetManager assetManager = Navegador.this.getAssets();
			InputStream input = null;
			try {
				Log.d(Constants.DATA, "Cargando de assets: " + assetPath);

				input = assetManager.open(assetPath);
				WebResourceResponse response = new WebResourceResponse(
						mimeType, encoding, input);

				return response;
			} catch (IOException e) {
				Log.e("WEB-APP", "Error durante la carga de " + assetPath
						+ " from assets: " + e.getMessage(), e);
			}

			return null;
		}

		// Url = ruta del archivo o lo que sea adecuado URL que want.
		public String getMimeType(String url) {
			String type = null;
			String extension = MimeTypeMap.getFileExtensionFromUrl(url);
			if (extension != null) {
				MimeTypeMap mime = MimeTypeMap.getSingleton();
				type = mime.getMimeTypeFromExtension(extension);
			}
			return type;
		}

		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url) {

			if (url.equals("mailto:mgs2000pna@gmail.com")) {
				mandarCorreo("mgs2000pna@gmail.com");
				return true;
			} else if (Uri.parse(url).getHost()
					.equals("www.singlespamplona.es")
					|| (Uri.parse(url).getHost()
							.equals("singlespamplona.networkauth.com"))
					|| (Uri.parse(url).getHost().equals("www.facebook.com"))
					|| (Uri.parse(url).getHost().equals("m.facebook.com"))
					|| (Uri.parse(url).getHost().equals("api.ning.com"))
					|| (Uri.parse(url).getHost().equals("www.clan-2000.com"))
					|| (Uri.parse(url).getHost().equals("www.gnu.org"))) {
				// This is my web site, so do not override; let my WebView load
				// the page
				return false;
			}
			// Otherwise, the link is not for a page on my site, so launch
			// another Activity that handles URLs
			Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
			startActivity(intent);
			return true;
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {

			super.onPageStarted(view, url, favicon);
			try {
				if (moduloWeb != null) {

					if (((moduloWeb.equals("/m/signin?")) || (moduloWeb
							.equals("/main/authorization/")))
							&& (url.equals("http://www.singlespamplona.es/m"))) {

						Toast.makeText(getApplicationContext(),
								"Sesión iniciada con éxito", LENGTH_SHORT)
								.show();
						SinglesPamplona.getContenidoUrl(Navegador.this);
						finish();
						startActivity(new Intent(context, Principal.class));

					} else {
						if (checkConnectivity(context)) {
							Toast.makeText(getApplicationContext(),
									"Cargando...", LENGTH_SHORT).show();
						}
					}
				}
			} catch (Exception e) {
				Log.e("onPageStarted", "Error al poner el Toast", e);
			}
		}

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			if (!iniciada) {
				webInicio = getWebView().getUrl();
				iniciada = true;

			}
			if (getWebView().getUrl().contains("http")) {
				isWeb = true;
			} else {
				isWeb = false;
			}
			invalidateOptionsMenu();
		}

		@Override
		public void onReceivedError(WebView view, int errorCode,
				String description, String failingUrl) {
			AlertDialog.Builder builder = new AlertDialog.Builder(
					Navegador.this);
			builder.setMessage(description).setPositiveButton("Aceptar", null)
					.setTitle("Problemas al cargar");
			builder.show();
		}
	}

	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.navegador);
		iniciada = false;
		try {
			if (getIntent().getData() != null) {
				webIntemt = getIntent().getData().toString();
				Log.d("webIntent", webIntemt);
			} else {
				webIntemt = null;
				Log.d("webIntent", "getData = null");
			}

		} catch (Exception e) {
			Log.d("webIntent", webIntemt, e);
		}
		try {

			myWebView = (WebView) this.findViewById(R.id.webView);
			setWebView(myWebView);
			WebSettings webSettings = getWebView().getSettings();
			webSettings.setBuiltInZoomControls(true);
			webSettings.setJavaScriptEnabled(true);
			webSettings.setDomStorageEnabled(true);
			// webSettings.setUseWideViewPort(true);
			// webSettings.setLoadWithOverviewMode(true);
			// webSettings.setPluginsEnabled(true);
			webSettings.setPluginState(PluginState.ON);
			webSettings.setAllowFileAccess(true);
			/*
			 * getWebView().addJavascriptInterface(new
			 * DemoJavaScriptInterface(), "android");
			 */

			getWebView().setWebViewClient(new MyWebViewClient(this));

			if (savedInstanceState != null) {
				getWebView().restoreState(savedInstanceState);

			} else {
				if (webIntemt != null) {
					web = getIntent().getData().toString();
					isWeb = true;

				} else if (getUrlCompleta() != null) {
					web = getUrlCompleta();

				} else if ((direccionWeb != null) && (moduloWeb != null)) {
					web = getDireccionWeb();
					isWeb = true;
				} else {
					web = getIntent().getData().toString();
					isWeb = true;
				}
				getWebView().loadUrl(web);
				Log.d("Navegador/onCreate()", "Entrando en página: " + web);
				webInicio = web;
			}

		} catch (Exception ie) {
			Log.d("Navegador/onCreate()", "Erro abrir pagina web", ie);
		}

		progressBar = (ProgressBar) findViewById(R.id.progressbar);

		getWebView().setWebChromeClient(new WebChromeClient() {
			@Override
			public void onProgressChanged(WebView view, int progress) {
				progressBar.setProgress(0);
				progressBar.setVisibility(View.VISIBLE);
				Navegador.this.setProgress(progress * 1000);
				progressBar.incrementProgressBy(progress);
				if (progress == 100) {
					progressBar.setVisibility(View.GONE);
				}
			}

			@Override
			public boolean onJsAlert(WebView view, String url, String message,
					JsResult result) {
				new AlertDialog.Builder(view.getContext()).setMessage(message)
						.setCancelable(true).show();
				result.confirm();
				return true;
			}
		});

		/*
		 * getWebView().setDownloadListener(new DownloadListener() { // private
		 * FileOutputStream fileOutputStream; public void onDownloadStart(String
		 * url,String userAgent,String contentDisposition, String mimetype, long
		 * contentLength) {
		 * 
		 * Request request = new Request(Uri.parse(url));
		 * request.allowScanningByMediaScanner();
		 * 
		 * request.setNotificationVisibility(DownloadManager.Request.
		 * VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
		 * 
		 * request.setDestinationInExternalPublicDir(
		 * Environment.DIRECTORY_DOWNLOADS, // Download folder "download"); //
		 * Name of file
		 * 
		 * DownloadManager dm = (DownloadManager)
		 * getSystemService(DOWNLOAD_SERVICE);
		 * 
		 * dm.enqueue(request);
		 * 
		 * }
		 * 
		 * });
		 */

		getWebView().setOnLongClickListener(new View.OnLongClickListener() {

			private String emailSelectedOnLongPress;
			private String urlSelectedOnLongPress;
			private String imageSelectedOnLongPress;
			private String geoSelectedOnLongPress;

			public boolean onLongClick(View v) {
				WebView.HitTestResult mResult = getWebView().getHitTestResult();
				final WebView webview = (WebView) v;

				if (mResult.getType() == WebView.HitTestResult.EMAIL_TYPE) {
					emailSelectedOnLongPress = mResult.getExtra();
					Log.d("e-mail", emailSelectedOnLongPress);
					final String[] items = { "Mandar correo",
							"Copiar dirección de correo" };
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Navegador.this);

					builder.setTitle("Selección").setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									Log.i("Dialogos", "Opción elegida: "
											+ items[item]);
									if (item == 0) {
										mandarCorreo(emailSelectedOnLongPress);
									} else if (item == 1) {
										ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
										clipboard
												.setText(emailSelectedOnLongPress);
										Toast.makeText(
												getApplicationContext(),
												"Copiada dirección de correo en portapapeles",
												LENGTH_SHORT).show();
									}
								}
							});
					builder.create().show();
					// onLongTapEmail();
					return true;
				} else if (mResult.getType() == WebView.HitTestResult.SRC_ANCHOR_TYPE) {
					urlSelectedOnLongPress = mResult.getExtra();
					Handler mHandler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							String texto = (String) msg.getData().get("title");
							mEnlace = texto;
							// Do something with it.
							Log.d("texto; ", texto);
						}
					};
					Message msg = mHandler.obtainMessage();
					getWebView().requestFocusNodeHref(msg);
					final String[] items = { "Abrir enlace", "Copiar URL",
							"Copiar texto", "Compartir" };
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Navegador.this);

					builder.setTitle("Selección").setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									Log.i("Dialogos", "Opción elegida: "
											+ items[item]);
									String textoEnlace = mEnlace;
									if (item == 0) {
										getWebView().loadUrl(
												urlSelectedOnLongPress);
									} else if (item == 1) {
										ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
										clipboard
												.setText(urlSelectedOnLongPress);
										Toast.makeText(getApplicationContext(),
												"Copiada URL en portapapeles",
												LENGTH_SHORT).show();
									} else if (item == 2) {
										ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
										clipboard.setText(textoEnlace);
										Toast.makeText(
												getApplicationContext(),
												"Copiado texto en portapapeles",
												LENGTH_SHORT).show();

									} else if (item == 3) {
										compartir(urlSelectedOnLongPress,
												textoEnlace, "Compartir enlace");
									}
								}
							});
					builder.create().show();
					Log.d("Ancla URL", urlSelectedOnLongPress);
					return true;
				} else if ((mResult.getType() == WebView.HitTestResult.IMAGE_TYPE)
						&& (getWebView().getUrl().contains("http"))) {

					final String[] items = { "Abrir imagen",
							"Descargar Imagen", "Copiar URL de la imagen" };
					imageSelectedOnLongPress = mResult.getExtra();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Navegador.this);

					builder.setTitle("Selección").setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									Log.i("Dialogos", "Opción elegida: "
											+ items[item]);
									if (item == 0) {
										getWebView().loadUrl(
												imageSelectedOnLongPress);

									} else if (item == 1) {
										(new DownloadAsyncTask(Navegador.this))
												.execute(imageSelectedOnLongPress);
										/*
										 * DownloadManager mdDownloadManager =
										 * (DownloadManager) Navegador.this
										 * .getSystemService
										 * (Context.DOWNLOAD_SERVICE);
										 * DownloadManager.Request request = new
										 * DownloadManager.Request(
										 * Uri.parse(imageSelectedOnLongPress));
										 * File destinationFile = new File(
										 * Environment
										 * .getExternalStorageDirectory(),
										 * getNameFile
										 * (imageSelectedOnLongPress));
										 * request.setDescription
										 * ("Descargando imagen ..");
										 * request.setNotificationVisibility
										 * (DownloadManager.Request.
										 * VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
										 * request.setDestinationUri(Uri
										 * .fromFile(destinationFile));
										 * mdDownloadManager.enqueue(request);
										 */

									} else if (item == 2) {
										ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
										clipboard
												.setText(imageSelectedOnLongPress);
										Toast.makeText(
												getApplicationContext(),
												"Copiada URL de imagen en portapapeles",
												LENGTH_SHORT).show();
									}
								}
							});
					builder.create().show();
					Log.d("Imagen", imageSelectedOnLongPress);
					return true;
					// onLongTapImage();
				} else if (mResult.getType() == WebView.HitTestResult.GEO_TYPE) {
					Handler mHandler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							String texto = (String) msg.getData().get("title");
							mEnlace = texto;
							// Do something with it.
							Log.d("texto; ", texto);
						}
					};
					Message msg = mHandler.obtainMessage();
					getWebView().requestFocusNodeHref(msg);
					final String[] items = { "Ir a Google Maps", "Copiar",
							"Compartir" };
					geoSelectedOnLongPress = mResult.getExtra();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Navegador.this);

					builder.setTitle("Selección").setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									Log.i("Dialogos", "Opción elegida: "
											+ items[item]);
									String textoEnlace = mEnlace;
									if (item == 0) {
										Intent i = new Intent(
												Intent.ACTION_VIEW,
												Uri.parse("google.navigation:q="
														+ geoSelectedOnLongPress));
										Navegador.this.startActivity(i);

									} else if (item == 1) {
										ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
										clipboard
												.setText(geoSelectedOnLongPress);
										Toast.makeText(
												getApplicationContext(),
												"Copiadas coordenadas en portapapeles",
												LENGTH_SHORT).show();
									} else if (item == 2) {
										compartir("google.navigation:q="
												+ geoSelectedOnLongPress,
												textoEnlace,
												"Compartir coordenadas");
									}

								}
							});
					builder.create().show();
					return true;

				} else if (mResult.getType() == WebView.HitTestResult.SRC_IMAGE_ANCHOR_TYPE) {
					Handler mHandler = new Handler() {

						@Override
						public void handleMessage(Message msg) {
							// Get link-URL.
							String url = (String) msg.getData().get("url");
							String texto = (String) msg.getData().toString();
							mUrl = url;
							mEnlace = texto;
							// Do something with it.
							Log.d("link; ", url);
							Log.d("texto; ", texto);
						}
					};

					Message msg = mHandler.obtainMessage();
					getWebView().requestFocusNodeHref(msg);

					final String[] items = { "Abrir enlace", "Abrir imagen",
							"Descargar Imagen", "Copiar URL del enlace",
							"Copiar URL de la imagen" };
					imageSelectedOnLongPress = mResult.getExtra();
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Navegador.this);

					builder.setTitle("Selección").setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									String url = mUrl;
									mUrl = null;

									Log.i("Dialogos", "Opción elegida: "
											+ items[item]);
									if (item == 0) {
										getWebView().loadUrl(url);
									} else if (item == 1) {
										getWebView().loadUrl(
												imageSelectedOnLongPress);

									} else if (item == 2) {
										(new DownloadAsyncTask(Navegador.this))
												.execute(imageSelectedOnLongPress);
										/*
										 * DownloadManager mdDownloadManager =
										 * (DownloadManager) Navegador.this
										 * .getSystemService
										 * (Context.DOWNLOAD_SERVICE);
										 * DownloadManager.Request request = new
										 * DownloadManager.Request(
										 * Uri.parse(imageSelectedOnLongPress));
										 * File destinationFile = new File(
										 * Environment
										 * .getExternalStorageDirectory(),
										 * getNameFile
										 * (imageSelectedOnLongPress));
										 * request.setDescription
										 * ("Descargando imagen ..");
										 * request.setNotificationVisibility
										 * (DownloadManager.Request.
										 * VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
										 * request.setDestinationUri(Uri
										 * .fromFile(destinationFile));
										 * mdDownloadManager.enqueue(request);
										 */

									} else if (item == 3) {
										ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
										clipboard.setText(url);
										Toast.makeText(
												getApplicationContext(),
												"Copiada URL de imagen en portapapeles",
												LENGTH_SHORT).show();
									} else if (item == 4) {
										ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
										clipboard
												.setText(imageSelectedOnLongPress);
										Toast.makeText(
												getApplicationContext(),
												"Copiada URL de imagen en portapapeles",
												LENGTH_SHORT).show();
									}
								}
							});
					builder.create().show();
					Log.d("Imagen", imageSelectedOnLongPress);

					// mUrl = null;
					return true;
					// onLongTapImage();
				} else if (mResult.getType() == WebView.HitTestResult.PHONE_TYPE) {
					phoneSelectedOnLongPress = mResult.getExtra();
					final String[] items = { "Copiar número de telefono" };
					AlertDialog.Builder builder = new AlertDialog.Builder(
							Navegador.this);

					builder.setTitle("Selección").setItems(items,
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int item) {
									Log.i("Dialogos", "Opción elegida: "
											+ items[item]);
									if (item == 0) {
										ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
										clipboard
												.setText(phoneSelectedOnLongPress);
										Toast.makeText(
												getApplicationContext(),
												"Copiado número de teléfono en portapapeles",
												LENGTH_SHORT).show();
									}
								}
							});
					builder.create().show();
					// onLongTapPhone();
					Log.d("Número de teléfono", phoneSelectedOnLongPress);
					return true;
				} else {
					Log.d("Otro tipo", mResult.toString());
					return false;
				}

			}
		});
	}

	@Override
	public void compartir() {
		if (getWebView().getUrl().contains("http")) {
			setUrlCompartir(getWebView().getUrl());
			setTextoCompartir("Compartir página");
			setAsuntoCompartir(getWebView().getTitle());
		} else {
			setUrlCompartir("https://drive.google.com/uc?export=download&id=0B9U91PNbQkoKUG1QMkZkaTNiTFE");
			setTextoCompartir("Compartir aplicación Singles Pamplona");
			setAsuntoCompartir("Descarga aplicación Singles Pamplona para Android (min Android 3.0)");
		}
		compartir(getUrlCompartir(), getAsuntoCompartir(), getTextoCompartir());
	}
	@Override
	 public void compartirWhatsapp() {
		if (getWebView().getUrl().contains("http")) {
			setUrlCompartir(getWebView().getUrl());
			setTextoCompartir("Compartir página");
			setAsuntoCompartir(getWebView().getTitle()+"\n");
		} else {
			setUrlCompartir("https://drive.google.com/uc?export=download&id=0B9U91PNbQkoKUG1QMkZkaTNiTFE");
			setTextoCompartir("Compartir aplicación Singles Pamplona");
			setAsuntoCompartir("Descarga aplicación Singles Pamplona para Android (min Android 3.0)");
		}
		compartirWhatsapp(getUrlCompartir(), getAsuntoCompartir(), getTextoCompartir());
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
		} else {
			return "imagen";
		}

		// Matcher matcher = patron.matcher(url);
		// matcher.find();
		// String nameFile = matcher.group(1);
		Log.d("Nombre archivo", nameFile);
		return nameFile;
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

	public void mandarCorreo(String direccion) {

		Intent intent = new Intent(Intent.ACTION_SEND);
		String[] addresses = { direccion };

		// intent.setData(Uri.parse("mailto:")); // only email apps
		// should handle this
		intent.putExtra(Intent.EXTRA_EMAIL, addresses);
		if (direccion.equals("mgs2000pna@gmail.com")) {
			intent.putExtra(Intent.EXTRA_SUBJECT, "Apicación Singles Pamplona");
			intent.putExtra(
					Intent.EXTRA_TEXT,
					"• Tipo de consulta (Error, ayuda, sugerencia, otro): \n\n• Tipo de dispositivo (teléfono, tableta, otro): \n\n• Marca y modelo: \n\n• Versión del sistema operativo Android: \n\n• Detalle del problema o sugerencia:");
		}
		intent.setType("message/rfc822");
		if (intent.resolveActivity(getPackageManager()) != null) {
			startActivity(intent);
		}
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		getWebView().saveState(outState);
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onResume() {
		super.onResume();
		/*
		 * if (!urlTemporal.equals("") && urlTemporal != null) {
		 * getWebView().loadUrl(urlTemporal); }
		 */

	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == Menu.FIRST) {
			actualizar();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		if (isWeb) {
			MenuItem actualizar = menu.add(0, Menu.FIRST, Menu.FIRST,
					R.string.actualizar);
			actualizar.setIcon(R.drawable.ic_action_actualizarmenu);
			actualizar.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
		}
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		if (getNumeroMenu() == 2) {
			menu.findItem(R.id.actividades).setEnabled(false);
		} else if (getNumeroMenu() == 3) {
			menu.findItem(R.id.eventos).setEnabled(false);
		} else if (getNumeroMenu() == 4) {
			menu.findItem(R.id.discusiones).setEnabled(false);
		} else if (getNumeroMenu() == 5) {
			menu.findItem(R.id.informacion).setEnabled(false);
		}

		return super.onPrepareOptionsMenu(menu);
	}

	public void actualizar() {
		if (checkConnectivity(this)) {
			web = getWebView().getUrl();
			System.out.println("Entrando en página: " + web);
			getWebView().loadUrl(web);
			// invalidateOptionsMenu();
		} else {
			noInternet();
		}
	}

	public void iratras(View view) {
		try {
			if ((webInicio != null)
					&& (getWebView().getUrl().equals(webInicio))) {
				iratras();

			} else if (checkConnectivity(this)) {
				web = getDireccionWeb();
				System.out.println("Entrando en página: " + web);
				getWebView().loadUrl(web);
			} else {
				iratras();
			}
		} catch (Exception e) {
			finish();
			startActivity(new Intent(this, Principal.class));
		}
	}

	public void iratras() {

		if (getActividadAnterior() == 1) {
			finish();
			startActivity(new Intent(this, Principal.class));
		} else if (getActividadAnterior() == 2){
			finish();
			startActivity(new Intent(this, Grupos.class));
			
		} else if (getActividadAnterior() == 3) {
			finish();
			startActivity(new Intent(this, Usuarios.class));

		} else {
			finish();
		}
	}

	public static void setDireccionWeb(String direccionWeb) {
		Navegador.direccionWeb = direccionWeb;

	}

	public static void setModuloWeb(String moduloWeb) {
		Navegador.moduloWeb = moduloWeb;

	}

	public static String getDireccionWeb() {
		String urlPag = raizWeb + moduloWeb + direccionWeb;
		return urlPag;

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		// Checks the orientation of the screen
		/*
		 * if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
		 * // Toast.makeText(this, "landscape", Toast.LENGTH_SHORT).show(); }
		 * else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
		 * { // Toast.makeText(this, "portrait", Toast.LENGTH_SHORT).show(); }
		 */
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent evento) {
		if (keyCode == KeyEvent.KEYCODE_BACK && evento.getRepeatCount() == 0) {
			// Esto es lo que hace mi botón al pulsar ir a atrás
			try {
				if (webInicio == null){
					finish();
				} else if (getWebView().getUrl().equals(webInicio)) {
					iratras();
				} else {
					getWebView().goBack();
				}
				return true;
			} catch (Exception e) {
				finish();
				startActivity(new Intent(this, Principal.class));
			}
		}
		return super.onKeyDown(keyCode, evento);
	}

	public static int getActividadAnterior() {
		return actividadAnterior;
	}

	public static void setActividadAnterior(int actividadAnterior) {
		Navegador.actividadAnterior = actividadAnterior;
	}

	public int getMenuUtilizar() {
		return menuUtilizar;
	}

	public void setMenuUtilizar(int menuUtilizar) {
		this.menuUtilizar = menuUtilizar;
	}

	public static int getNumeroMenu() {
		return numeroMenu;
	}

	public static void setNumeroMenu(int numeroMenu) {
		Navegador.numeroMenu = numeroMenu;
	}

	public static String getUrlCompleta() {
		return urlCompleta;
	}

	public static void setUrlCompleta(String urlCompleta) {
		Navegador.urlCompleta = urlCompleta;
	}

}
