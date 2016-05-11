package com.micecop.singlepamplona;

//import static android.widget.Toast.LENGTH_SHORT;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

//import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
//import android.widget.Toast;
import java.net.URLConnection;

public class MainActivity extends Actividad {

	private float version = (float) 1.8;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
				.permitAll().build();
		StrictMode.setThreadPolicy(policy);
		setContentView(R.layout.activity_main);
		WebView myWebView = (WebView) this.findViewById(R.id.webView);
		setWebView(myWebView);
		if (checkConnectivity(this)) {
			comprobarVersion();
		} else if (checkConnectivity(this)) {
			comprobarSesion();
		} else {
			startActivity(new Intent(MainActivity.this, Principal.class));
			finish();
		}

	}

	public void comprobarVersion() {
		String contenido = getContenidoUrl("http://tools.wmflabs.org/mg-bot/ver.php");
		Log.d("comprobarVersion()", contenido);

		try {
			float UltimaVersion = Float.valueOf(contenido.trim()).floatValue();
			// Toast.makeText(getApplicationContext(),"Última versión: "+UltimaVersion
			// + ", tu versión: " + version, LENGTH_SHORT).show();
			if (UltimaVersion > version) {
				// Toast.makeText(getApplicationContext(),"tienes una versión obsoleta",
				// LENGTH_SHORT).show();
				versionObsoleta();
			} else {
				// Toast.makeText(getApplicationContext(),"Tienes la última versión",
				// LENGTH_SHORT).show();
				comprobarSesion();
			}
		} catch (Exception e) {
			comprobarSesion();
		}

	}

	public static String getContenidoUrl(String url) {
		String contents = "";

		try {
			URLConnection conn = new URL(url).openConnection();

			InputStream in = conn.getInputStream();
			contents = convertStreamToString(in);
		} catch (MalformedURLException e) {
			Log.v("comprobarVersion()", "MALFORMED URL EXCEPTION");
		} catch (IOException e) {
			Log.d("comprobarVersion()", "error", e);
		}

		return contents;
	}

	static String convertStreamToString(InputStream is) {

		BufferedReader reader = new BufferedReader(new InputStreamReader(is));
		StringBuilder sb = new StringBuilder();

		String line = null;
		try {
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

}
