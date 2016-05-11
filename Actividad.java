package com.micecop.singlepamplona;

import static android.widget.Toast.LENGTH_SHORT;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
//import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
//import android.support.v7.app.ActionBarActivity;
//import android.app.ActionBarActivity;
//import android.app.ActionBar
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
//import android.view.ViewConfiguration;
//import android.view.Window;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.widget.Toast;

@SuppressWarnings("deprecation")
public class Actividad extends Activity {
	private static boolean comprobadaSesion;
	private int menuUtilizar;
	private static WebView webView;
	private Menu mainMenu;
	private String urlCompartir;
	private String asuntoCompartir;
	private String textoCompartir;
    private ProgressDialog pDialog;
	 
	    // Progress dialog type (0 - for Horizontal progress bar)
	    public static final int progress_bar_type = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// requestWindowFeature(Window.FEATURE_NO_TITLE);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.mainMenu = menu;
		if (comprobarSesion(this)) {
			setMenuUtilizar(R.menu.conectado);

		} else {
			setMenuUtilizar(R.menu.main);
		}
		getMenuInflater().inflate(getMenuUtilizar(), menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		if (id == R.id.grupos) {
			finish();
			startActivity(new Intent(this, Grupos.class));
			return true;
			/*
			 * } else if (id == R.id.home) { irMenu(); return true;
			 */
		} else if (id == R.id.menu) {
			irMenu();
			return true;
		} else if (id == R.id.mipagina) {
			iniciarsesion();
		} else if (id == R.id.iniciar) {
			iniciarsesion();
			return true;
		} else if (id == R.id.actividades) {
			iractividades();
			return true;
		} else if (id == R.id.eventos) {
			ireventos();
			return true;
		} else if (id == R.id.discusiones) {
			irdiscusiones();
			return true;
		} else if (id == R.id.informacion) {
			irInfo();
			return true;
		} else if (id == R.id.compartir) {
			compartir();
			return true;
		} else if (id == R.id.whatsapp) {
			compartirWhatsapp();
			return true;
		} else if (id == R.id.cerrar) {
			// irWeb("/main/authorization/","signOut?target=http%3A%2F%2Fwww.singlespamplona.es%2Fm&xg_token=275513473bb61818fd3dbe4478f5a6e9",
			// 1, 1);
			cerrarSesion(this);
			return true;
		} else if (id == R.id.salir) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);

	}

	public void iniciarsesion() {
		if (comprobarSesion(this)) {
			irWeb("/m?id=3402611", ":MobilePage:49593", 1, 1);
		} else {
			irWeb("/m/signin?", "target=%2Fm&cancelUrl=%2Fm", 1, 1);
		}

	}

	public void irChat() {
		if (comprobarSesion(this)) {
			irWeb("/chat/index", "/popOutWindowV4", 11, 1);
		} else {
			noSesion();
		}
	}

	public void iropciones(View view) {
		// finish();
		Toast.makeText(Actividad.this, "No habilitado", Toast.LENGTH_LONG)
				.show();
		// startActivity(new Intent(this, Chat.class));
	}

	public void iractividades() {
		irWeb("/m?id=3402611", "%3AMobilePage%3A49587", 2, 1);
	}

	public void ireventos() {
		irWeb("/m?id=3402611", "%3AMobilePage%3A49592", 3, 1);
	}

	public void irdiscusiones() {
		irWeb("/m?id=3402611", "%3AMobilePage%3A49590", 4, 1);
	}

	public void salir(View view) {
		finish();
	}

	public static boolean comprobarSesion(Context context) {
		try {
			WebView webView = new WebView(context);
			CookieSyncManager cookieSyncManager = CookieSyncManager
					.createInstance(webView.getContext());
			CookieManager cookieManager = (CookieManager.getInstance());
			cookieManager.setAcceptCookie(true);
			cookieSyncManager.sync();
			webView.loadUrl("http://www.singlespamplona.es/m");
			String cookie = cookieManager
					.getCookie("http://www.singlespamplona.es/m");
			
			if (cookie == null) {
				return false;
			} else {
				Log.d("Cookie", cookie);
				if (cookie.indexOf("singlespamplona=") != -1) {
					return true;
				} else {
					return false;
				}
			}

			
		} catch (Exception e) {
			Log.e("Actividad/comprobarSesion()", "Error al comprobar sesión", e);
			return false;
		}

	}

	private void borrarTodosLosCookies() {

		CookieSyncManager cookieSyncMngr = CookieSyncManager
				.createInstance(this);
		cookieSyncMngr.startSync();
		CookieManager cookieManager = CookieManager.getInstance();
		cookieManager.removeAllCookie();
		cookieManager.removeSessionCookie();
		cookieSyncMngr.stopSync();
		// getWebView().clearCache(true);
		// getWebView().clearHistory();
	}

	public void cerrarSesion(Context context) {
		// Intent intent = getIntent();
		try {
			borrarTodosLosCookies();
			invalidateOptionsMenu();

			// startActivity(new Intent(Actividad.this,Principal.class));
			if (!comprobarSesion(this)) {
				Toast.makeText(getApplicationContext(),
						"Sesión cerrada con éxito", LENGTH_SHORT).show();
			} else {
				Toast.makeText(getApplicationContext(),
						"No se ha podido cerrar la sesión", LENGTH_SHORT)
						.show();
			}
		} catch (Exception e) {

			Log.e("Actividad/cerrarsesion()", "Error cerrarSesion()", e);
		}
		startActivity(new Intent(Actividad.this, Principal.class));
		finish();

	}

	public int getMenuUtilizar() {
		return menuUtilizar;
	}

	public void setMenuUtilizar(int menuUtilizar) {
		this.menuUtilizar = menuUtilizar;
	}

	public void noInternet() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Actividad.this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(getString(R.string.noconnection));
		builder.setCancelable(false);
		builder.setNeutralButton(R.string.ok, null);
		builder.setTitle(getString(R.string.error));
		builder.create().show();
	}

	public void noSesion() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Actividad.this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(getString(R.string.nosesion));
		builder.setCancelable(false);
		builder.setNegativeButton("No iniciar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						setComprobadaSesion(true);
						irMenu();
					}
				});
		builder.setPositiveButton(R.string.iniciar,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						irWeb("/m/signin?", "target=%2Fm&cancelUrl=%2Fm", 1, 1);
						setComprobadaSesion(true);
					}
				});

		builder.setTitle(getString(R.string.app_name));
		builder.create().show();
	}

	public void comprobarSesion() {
		setComprobadaSesion(false);
		try {
			if (!comprobarSesion(this)) {
				noSesion();
			} else {
				setComprobadaSesion(true);
				finish();
				startActivity(new Intent(this, Principal.class));
			}

		} catch (Exception e) {
			Log.e("Actividad/comprobarSesion()", "Error comprobar inicio", e);
		}

	}

	public void versionObsoleta() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Actividad.this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage("Existe una versión nueva de esta aplicación y recomendamos que la actualice.");
		builder.setCancelable(false);
		builder.setNegativeButton("Más tarde",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						setComprobadaSesion(true);
						comprobarSesion();
					}
				});
		builder.setPositiveButton("Actualizar",
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						//goToUrl("https://www.dropbox.com/s/r8lvh14q3at8ywa/Singlepamplona.apk?dl=1");
						(new DownloadAsyncTask(Actividad.this,"Singles","Singlepamplona.apk")).execute("https://drive.google.com/uc?export=download&id=0B9U91PNbQkoKUG1QMkZkaTNiTFE");
						//goToUrl("https://drive.google.com/uc?export=download&id=0B9U91PNbQkoKUG1QMkZkaTNiTFE");
						setComprobadaSesion(true);
					}
				});

		builder.setTitle(getString(R.string.app_name));
		builder.create().show();
	}

	public boolean checkConnectivity(Context context) {
		boolean enabled = true;

		ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = connectivityManager.getActiveNetworkInfo();

		if ((info == null || !info.isConnected() || !info.isAvailable())) {
			enabled = false;

		}
		return enabled;
	}

	public void irMenu() {
		finish();
		startActivity(new Intent(this, Principal.class));
	}
	public void verUsuarios(){
		finish();
		Usuarios.setActividad(this);
		startActivity(new Intent(this, Usuarios.class));
	}

	public void irWeb(String modulo, String web, int numeroMenu,
			int actividadAnterior) {
		if (checkConnectivity(this)) {
			finish();
			Navegador.setModuloWeb(modulo);
			Navegador.setUrlCompleta(null);
			Navegador.setDireccionWeb(web);
			Navegador.setActividadAnterior(actividadAnterior);
			Navegador.setNumeroMenu(numeroMenu);
			startActivity(new Intent(this, Navegador.class));
		} else {
			noInternet();
		}
	}

	public void irWeb(String web, int numeroMenu, int actividadAnterior) {
		if (checkConnectivity(this)) {
			finish();
			Navegador.setModuloWeb("");
			Navegador.setDireccionWeb("");
			Navegador.setUrlCompleta(web);
			Navegador.setNumeroMenu(numeroMenu);
			Navegador.setActividadAnterior(actividadAnterior);
			startActivity(new Intent(this, Navegador.class));
		} else {
			noInternet();
		}
	}

	public void irInfo() {
		/*
		 * finish(); startActivity(new Intent(this, HTMLView.class));
		 */
		irWeb("file:///android_asset/acercade.html", 5, 1);

	}

	public static boolean isComprobadaSesion() {
		return Actividad.comprobadaSesion;
	}

	public static void setComprobadaSesion(boolean comprobadaSesion) {
		Actividad.comprobadaSesion = comprobadaSesion;
	}

	public static WebView getWebView() {
		return webView;
	}

	public static void setWebView(WebView webView) {
		Actividad.webView = webView;
	}

	private void goToUrl(String url) {
		Uri uriUrl = Uri.parse(url);
		Intent launchBrowser = new Intent(Intent.ACTION_VIEW, uriUrl);
		startActivity(launchBrowser);
	}
    public void compartir() {
    	//setUrlCompartir("https://www.dropbox.com/s/r8lvh14q3at8ywa/Singlepamplona.apk?dl=1");
    	setUrlCompartir("https://drive.google.com/uc?export=download&id=0B9U91PNbQkoKUG1QMkZkaTNiTFE");
		setTextoCompartir("Compartir aplicación Singles Pamplona");
		setAsuntoCompartir("Descarga aplicación Singles Pamplona para Android (min Android 3.0)");
		compartir(getUrlCompartir(),getAsuntoCompartir(),getTextoCompartir());
    	
    }
    public void compartirWhatsapp() {
    	//setUrlCompartir("https://www.dropbox.com/s/r8lvh14q3at8ywa/Singlepamplona.apk?dl=1");
    	setUrlCompartir("https://drive.google.com/uc?export=download&id=0B9U91PNbQkoKUG1QMkZkaTNiTFE");
		setTextoCompartir("Compartir aplicación Singles Pamplona");
		setAsuntoCompartir("Descarga aplicación Singles Pamplona para Android (min Android 3.0)\n");
		compartirWhatsapp(getUrlCompartir(),getAsuntoCompartir(),getTextoCompartir());
    	
    }
	public void compartir(String url, String asunto, String texto) {
		Intent i = new Intent(Intent.ACTION_SEND);
		i.setType("text/plain");
		i.putExtra(Intent.EXTRA_SUBJECT, asunto);
		i.putExtra(Intent.EXTRA_TEXT, url);
		startActivity(Intent.createChooser(i, texto));
	}
	public void compartirWhatsapp(String url, String asunto, String texto) {
		Intent i = new Intent(Intent.ACTION_SEND);
		String textoAnuncio = asunto+url;
		i.setPackage("com.whatsapp");
		i.setType("text/plain");
		//i.putExtra(Intent.EXTRA_SUBJECT, asunto);
		i.putExtra(Intent.EXTRA_TEXT, textoAnuncio);
		startActivity(i);
	}

	@Override
	public boolean onKeyUp(int keycode, KeyEvent e) {
		switch (keycode) {
		case KeyEvent.KEYCODE_MENU:
			if (mainMenu != null) {
				mainMenu.performIdentifierAction(R.id.overflow, 0);
			}
		}

		return super.onKeyUp(keycode, e);
	}

	public String getUrlCompartir() {
		return urlCompartir;
	}

	public void setUrlCompartir(String urlCompartir) {
		this.urlCompartir = urlCompartir;
	}

	public String getAsuntoCompartir() {
		return asuntoCompartir;
	}

	public void setAsuntoCompartir(String asuntoCompartir) {
		this.asuntoCompartir = asuntoCompartir;
	}

	public String getTextoCompartir() {
		return textoCompartir;
	}

	public void setTextoCompartir(String textoCompartir) {
		this.textoCompartir = textoCompartir;
	}
	@Override
	protected Dialog onCreateDialog(int id) {
	   
		switch (id) {
	    case progress_bar_type:
	        pDialog = new ProgressDialog(this);
	        pDialog.setMessage("Downloading file. Please wait...");
	        pDialog.setIndeterminate(false);
	        pDialog.setMax(100);
	        pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
	        pDialog.setCancelable(true);
	        pDialog.show();
	        return pDialog;
	    default:
	        return null;
	    }
	}
}
