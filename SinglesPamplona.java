package com.micecop.singlepamplona;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.NetworkInterface;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;

import org.apache.http.conn.util.InetAddressUtils;

import android.app.Application;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.text.format.Formatter;
//import android.net.ParseException;
import android.util.Log;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SinglesPamplona extends Application {
	private static String userName, contrasenna, email;
	private Actividad actividad;
	private static Context context;
	private WifiManager wm;
	public static String contenido;
	private static String userURL;
	private String ip;
	private static ParseUser user;
	public static boolean anonimo;

	@Override
	public void onCreate() {
		super.onCreate();
		// wm = (WifiManager) getSystemService(WIFI_SERVICE);

		// ip =
		// Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
		// ip = getLocalIpAddress();
		SinglesPamplona.context = getApplicationContext();
		actividad = new Actividad();
		Parse.initialize(this, "pa3G6NgAnFFa8hgrmLMl7oJb6gQ48knBu6dt0l3J",
				"g1sGtpM2lADYCXKeseuk7AVaZbejhUotwJoPC5lE");
		ParseInstallation.getCurrentInstallation().saveInBackground();
		if (ParseUser.getCurrentUser() == null) {			
			ParseUser.enableAutomaticUser();
			ParseUser.getCurrentUser().saveInBackground();
			user = ParseUser.getCurrentUser();
			/*Toast.makeText(SinglesPamplona.this ,"Sesión anónima: "+ ParseUser.getCurrentUser().getString(
					"username"),Toast.LENGTH_LONG).show();*/
			if (actividad.checkConnectivity(this)) {
				registro();
				anonimo = false;
			}
		} else {
			Log.d("InicioParse","Ya está registrado usuario: "+ ParseUser.getCurrentUser().getString(
					"username"));
			/*Toast.makeText(SinglesPamplona.this ,"Ya está registrado usuario: "+ ParseUser.getCurrentUser().getString(
					"username"),Toast.LENGTH_LONG).show();*/
		}

	}

	public void registro() {
		if (Actividad.comprobarSesion(SinglesPamplona.context)) {
			getContenidoUrl(this);
			
		} else {
			// anonimos();
			anonimo = true;
		}
	}

	public static void conCuenta() {
		user.setUsername(contenido);
		user.setPassword("singles");
		// user.setEmail(getEmail());

		// other fields can be set just like with ParseObject //
		// user.put("phone", "");
		user.put("pageurl", userURL);
		user.put("anonimo", true);
		user.signUpInBackground(new SignUpCallback() {

			public void done(com.parse.ParseException e) {
				if (e == null) {
					anonimo = false;
					
					Log.e("Parse", "Perfecto! El usuario está conectado.");
				} else {
					Log.e("Parse",
							"Registro fallido. Mira el ParseException a ver qué pasa",
							e);
				}
			}
		});
		
	}

	public void LoginIn() {
		ParseUser.logInInBackground(userURL, "singles");
	}

	public void anonimos() {
		ParseAnonymousUtils.logIn(new LogInCallback() {
			public void done(ParseUser user, com.parse.ParseException e) {
				if (e != null) {
					Log.d("MyApp", "Anonymous login failed.");
				} else {
					Log.d("MyApp", "Anonymous user logged in.");
				}
			}
		});
	}

	public static void getContenidoUrl(final Context context) {

		try {
			final WebView webView = new WebView(context);
			webView.loadUrl("http://www.singlespamplona.es/m?id=3402611:MobilePage:49593");
			webView.setWebViewClient(new WebViewClient() {
				@Override
				public void onPageFinished(WebView view, String url) {
					String contenidoTotal = "";
					contenidoTotal = webView.getTitle().replaceAll(
							"Página de ", "");
					contenido = contenidoTotal.replaceAll(
							" – Singles Pamplona", "");
					userURL = webView.getUrl();
					conCuenta();
					Log.d("login", contenido);
					Toast.makeText(context ,"Registrado como: "+ contenido,Toast.LENGTH_LONG).show();
				}
			});

		} catch (Exception e) {
			Log.e("login", "Error al comprobar titulo", e);

		}
	}

	public String getUserName() {
		if (Actividad.comprobarSesion(SinglesPamplona.context)) {
			return contenido;
		} else {
			// return userName;
			if (getLocalIpAddress() != null) {
				return ip;
			} else {
				return "no conectado";
			}
		}
	}

	public static void setUserName(String userName) {

		SinglesPamplona.userName = userName;
	}

	public String getLocalIpAddress() {
		String ipv4;
		try {
			for (Enumeration<NetworkInterface> en = NetworkInterface
					.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf
						.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					System.out.println("ip1--:" + inetAddress);
					System.out.println("ip2--:" + inetAddress.getHostAddress());

					// for getting IPV4 format
					if (!inetAddress.isLoopbackAddress()
							&& InetAddressUtils
									.isIPv4Address(ipv4 = inetAddress
											.getHostAddress())) {

						String ip = inetAddress.getHostAddress().toString();
						System.out.println("ip---::" + ip);
						// EditText tv = (EditText) findViewById(R.id.ipadd);
						// tv.setText(ip);
						// return inetAddress.getHostAddress().toString();
						return ipv4;
					}
				}
			}
		} catch (Exception ex) {
			Log.e("IP Address", ex.toString());
		}
		return null;
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
