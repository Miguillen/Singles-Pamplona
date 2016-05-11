package com.micecop.singlepamplona;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class Usuarios extends ListActivity {
	public static class Node implements Parcelable {
		/**
		 * 
		 */
		public String mtitle;
		public String mURL;
		public String mfoto;

		public Node() {

		}

		public Node(Parcel in) {
			String[] data = new String[3];

			in.readStringArray(data);
			this.mtitle = data[0];
			this.mURL = data[1];
			this.mfoto = data[2];

		}

		public int describeContents() {
			// TODO Auto-generated method stub
			return 0;
		}

		public void writeToParcel(Parcel dest, int flags) {
			// TODO Auto-generated method stub
			dest.writeString(mtitle);
			dest.writeString(mURL);
			dest.writeString(mfoto);

		}

		public String toString() {
			return mtitle;
		}

		public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
			public Node createFromParcel(Parcel in) {
				return new Node(in);
			}

			public Node[] newArray(int size) {
				return new Node[size];
			}
		};
	}

	private MyAdapter adaptador = null;
	private static Actividad actividad;
	private String mUser;
	private Menu mainMenu;
	private List<ParseObject> listaUsuarios;

	private static ArrayList<Node> mArray = new ArrayList<Node>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ParseQuery<ParseObject> query = ParseQuery.getQuery("_User");
		// query.whereEqualTo("objectId",ParseUser.getCurrentUser().getObjectId());
		try {
			if (actividad.checkConnectivity(this)) {
				listaUsuarios = query.find();
				Log.d("ObjetoParse", "Lista creada con éxito");
				for (int i = 0; i < listaUsuarios.size(); i++) {
					System.out.println(listaUsuarios.get(i).getString(
							"username"));
				}
				setData();
			} else if (savedInstanceState != null) {
				mArray = savedInstanceState.getParcelableArrayList("Old");
			} else {
				Log.e("Restauración", "objeto nulo");
			}
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			Log.e("onCreate()", "Error: ", e);
		}

		/*
		 * query.findInBackground(new FindCallback<ParseObject>() { public void
		 * done(List<ParseObject> objects, ParseException e) { if (e == null) {
		 * // row of Object Id "Current USer"
		 * 
		 * Log.d("ObjetoParse", "Objetos importados con éxito"); } else { //
		 * error Log.e("ObjetoParse", "Error al importar objeto", e); } } });
		 */
		/*
		 * ParseQuery<ParseUser> query = ParseQuery.getQuery("_User");
		 * query.getInBackground("MoxU5eI79i", new GetCallback<ParseUser>() {
		 * public void done(ParseUser usuario, com.parse.ParseException e) { if
		 * (e == null) { // object will be your Singles Pamplona
		 * Log.d("ObjetoParse", "Objetos importados con éxito"+
		 * usuario.toString()); } else { // something went wrong
		 * Log.e("ObjetoParse", "Error al importar objeto", e); } }
		 * 
		 * }); // requestWindowFeature(Window.FEATURE_NO_TITLE);
		 */

		adaptador = new MyAdapter(this);
		setListAdapter(adaptador);
	}

	@Override
	public void onSaveInstanceState(Bundle savedInstanceState) {
		super.onSaveInstanceState(savedInstanceState);
		savedInstanceState.putParcelableArrayList("Old", mArray);
		Log.d("Restauración", "Salvado objeto");

	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);
		mArray = savedInstanceState.getParcelableArrayList("Old");
		Log.d("Registrado valor", mArray.toString());

	}

	public static void setActividad(Actividad actividad) {
		Usuarios.actividad = actividad;
	}

	public void setData() {
		mArray.clear();
		Node arraynodes[] = new Node[listaUsuarios.size()];
		for (int i = 0; i < listaUsuarios.size(); i++) {
			if (listaUsuarios.get(i).getBoolean("anonimo")) {
				arraynodes[i] = new Node();
				arraynodes[i].mtitle = listaUsuarios.get(i).getString(
						"username");
				arraynodes[i].mURL = listaUsuarios.get(i).getString("pageurl");
				mArray.add(arraynodes[i]);
			}
		}

	}

	public static class MyAdapter extends BaseAdapter {

		private Context mContext;

		public MyAdapter(Context c) {
			mContext = c;

		}

		public int getCount() {
			return mArray.size();
		}

		public Object getItem(int position) {
			return mArray.get(position);
		}

		public long getItemId(int position) {
			return 0;

		}

		@SuppressLint("InflateParams")
		public View getView(final int position, View convertView,
				ViewGroup parents) {
			View view = null;
			if (convertView == null) {
				LayoutInflater inflater = (LayoutInflater) mContext
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(R.layout.usuario, null);

			} else {
				view = convertView;
			}
			// ImageView img = (ImageView) view.findViewById(R.id.logo);
			// img.setImageDrawable(mContext.getResources().getDrawable(
			// mArray.get(position).mImageResource));

			TextView mTitle = (TextView) view.findViewById(R.id.nombre);
			mTitle.setText(mArray.get(position).mtitle);
			mTitle.setOnClickListener(new View.OnClickListener() {

				public void onClick(View v) {
					 MyAdapter.this.irWeb(position);
				}
			});
			return view;

		}
		public void irWeb(int p) {
			((Activity) mContext).finish();
			actividad.irWeb(mArray.get(p).mURL, 11, 3);
		}
	}

	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.mainMenu = menu;
		if (Actividad.comprobarSesion(this)) {
			actividad.setMenuUtilizar(R.menu.conectado);

		} else {
			actividad.setMenuUtilizar(R.menu.main);
		}
		getMenuInflater().inflate(actividad.getMenuUtilizar(), menu);
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
			finish();
			startActivity(new Intent(this, Principal.class));
			return true;
		} else if (id == R.id.mipagina) {
			finish();
			actividad.iniciarsesion();
		} else if (id == R.id.iniciar) {
			if (actividad.checkConnectivity(this)) {
				finish();
				actividad.iniciarsesion();
			} else {
				noInternet();
			}
			return true;
		} else if (id == R.id.actividades) {
			if (actividad.checkConnectivity(this)) {
				finish();
				actividad.iractividades();
			} else {
				noInternet();
			}
			return true;
		} else if (id == R.id.eventos) {
			if (actividad.checkConnectivity(this)) {
				finish();
				actividad.ireventos();
			} else {
				noInternet();
			}
			return true;
		} else if (id == R.id.discusiones) {
			if (actividad.checkConnectivity(this)) {
				finish();
				actividad.irdiscusiones();
			} else {
				noInternet();
			}
			return true;
		} else if (id == R.id.informacion) {
			finish();
			actividad.irInfo();
			return true;
		} else if (id == R.id.compartir) {
			actividad.compartir();
			return true;
		} else if (id == R.id.cerrar) {
			finish();
			// irWeb("/main/authorization/","signOut?target=http%3A%2F%2Fwww.singlespamplona.es%2Fm&xg_token=275513473bb61818fd3dbe4478f5a6e9",
			// 1, 1);
			actividad.cerrarSesion(this);
			return true;
		} else if (id == R.id.salir) {
			finish();
			return true;
		} else {
			return super.onOptionsItemSelected(item);
		}
		return super.onOptionsItemSelected(item);

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent evento) {
		if (keyCode == KeyEvent.KEYCODE_BACK && evento.getRepeatCount() == 0) {
			// Esto es lo que hace mi botón al pulsar ir a atrás
			finish();
			startActivity(new Intent(this, Principal.class));
			return true;
		}
		return super.onKeyDown(keyCode, evento);
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

	public void noInternet() {
		AlertDialog.Builder builder = new AlertDialog.Builder(Usuarios.this);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		builder.setMessage(getString(R.string.noconnection));
		builder.setCancelable(false);
		builder.setNeutralButton(R.string.ok, null);
		builder.setTitle(getString(R.string.error));
		builder.create().show();
	}
}
