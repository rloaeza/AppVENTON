package com.mas_aplicaciones.appventon.staticresources;

import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.mas_aplicaciones.appventon.R;

public class StaticResources
{
    public final static String [] OPCIONES_GENERO =  {"Género","Masculino","Femenino"};
    public final static String [] OPCIONES_CARRERAS =  {"Carrera","Ing. Administración","Ing. Sistemas","Ing. Industrial","Ing. Alimientarias","Ing. Electrónica","Ing. Mecatrónica","Ing. Mécanica","Ing. Civil"};
    public final static String [] OPCIONES_ORGANIZACION = {"Organización","ITSU"};
    public final static String [] OPCIONES_TIEMPO_ESPERA = {"Tiempo espera (min)","1","2","3","4"};
    public final static String EMAILSENDER ="appventonitsu@gmail.com";
    public final static String PASSWORD ="AppVenton1234";

    public static void actionSnackbar(final View view){
       Snackbar.make(view,"No podrá ver el mapa hasta que suba uma imagen de su auto",Snackbar.LENGTH_LONG).setAction(R.string.modificar,
                new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_principalChofer_to_configurar);
                }
            }).show();

    }

}
