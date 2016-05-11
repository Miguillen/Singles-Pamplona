package com.micecop.singlepamplona;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;

public class DialogoSeleccion extends DialogFragment {
	private static int elemento;
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
 
     final String[] items = {"Abrir imagen", "Descargar Imagen", "Copiar URL de la imagen"};
 
        AlertDialog.Builder builder =
                new AlertDialog.Builder(getActivity());
 
        builder.setTitle("Selección")
           .setItems(items, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int item) {
                    Log.i("Dialogos", "Opción elegida: " + items[item]);
                    setElemento(item);
                }
            });
 
        return builder.create();
    }
	public static int getElemento() {
		return elemento;
	}
	public static void setElemento(int elemento) {
		DialogoSeleccion.elemento = elemento;
	}
}
