package com.micecop.singlepamplona;


import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
//import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

public class Principal extends Actividad {
private String conectado;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
	    //getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.principal);
		//invalidateOptionsMenu();
		try {
			if (comprobarSesion(this)){
				System.out.println("Sesión comprobada");
				conectado = "Mi página";
				TextView tv =  (TextView) findViewById(R.id.iniciar); 
			    tv.setText (conectado); 
			}
		} catch (Exception e){
			System.out.println("Error comprobar"+e);
		}

	}
	
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
		menu.findItem(R.id.menu).setEnabled(false);
		//menu.getItem(1).setEnabled(false); 
		return super.onPrepareOptionsMenu(menu);
    }
	

	public void  irGrupos(View view) {
		finish();
        startActivity(new Intent(this, Grupos.class));
	}
	
	public void  iniciarsesion(View view) {
		iniciarsesion();
	}
	
	public void  irchat(View view) {
		irChat();
       
	}
	public void verUsuarios(View view) {
		verUsuarios();
	}

	public void iropciones(View view) {
		//finish();
		Toast.makeText(Principal.this ,"No habilitado",Toast.LENGTH_LONG).show();
        //startActivity(new Intent(this, Chat.class));
	}
	
	public void iractividades(View view) {		   
		iractividades();
	}
	 	
	public void ireventos(View view) {
		ireventos();	
	}
		
	public void irdiscusiones(View view) {
		irdiscusiones();
		
	}

	public void  salir(View view) {
		finish();
	}	
		
}	

