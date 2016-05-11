package com.micecop.singlepamplona;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
//import android.view.Window;

public class Grupos extends Actividad {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.grupos);
	}
		
	@Override
    public boolean onPrepareOptionsMenu(Menu menu) {
		//menu.getItem(0).setEnabled(false); 
		menu.findItem(R.id.grupos).setEnabled(false);
		return super.onPrepareOptionsMenu(menu);
    }
		
	public void  treintaytantos(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A49469", 11, 2);
	}
	
	public void  nuevos(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A76", 11, 2);        
	}
	
	public void  cuarentaanneros(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A49660", 11, 2);
	}
	
	public void  cuarentones(View view) {
		irWeb("/m/group?id=3402611", "%3AGroup%3A288403", 11, 2);
	}
	
	public void  sebuscaamor(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A79", 11, 2);
	}
	
	public void  cincuentanneros(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A242219", 11, 2);
	}
	
	public void  viajeros(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A2105", 11, 2);
	}
	
	public void  singlestudela(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A225113", 11, 2);
	}
	
	public void  veintitantos(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A250193", 11, 2);
	}
	
	public void  rochapeanos(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A233078", 11, 2);
	}
	
	public void  montanneros(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A148979", 11, 2);
	}
	
	public void  lastminute(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A101084", 11, 2);
	}
	
	public void  fiestadelclan(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A94", 11, 2);
	}
	
	public void  actividadesculturales(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A91", 11, 2);
        
	}
	
	public void  actividadesalairelibre(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A88", 11, 2);
	}
	
	public void  papasymamassingleconninnos(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A85", 11, 2);
	}
	
	public void  conciertos(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A82", 11, 2);
	}
	
	public void  literaturaarteylibros(View view) {	
	    irWeb("/m/group?id=3402611","%3AGroup%3A66", 11, 2);
	}
	
	public void  rockeros(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A238254", 11, 2);
	}
	public void  grupodecocina(View view) {
		irWeb("/m/group?id=3402611","%3AGroup%3A70", 11, 2);
	}
	
	@Override
    public boolean onKeyDown(int keyCode, KeyEvent evento){
        if (keyCode == KeyEvent.KEYCODE_BACK && evento.getRepeatCount() == 0) {
          // Esto es lo que hace mi botón al pulsar ir a atrás
            finish();
	    	startActivity(new Intent(this, Principal.class)); 
            return true;
        }
        return super.onKeyDown(keyCode, evento);
    }
	
}
