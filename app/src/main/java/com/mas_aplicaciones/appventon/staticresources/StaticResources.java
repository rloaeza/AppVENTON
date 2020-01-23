package com.mas_aplicaciones.appventon.staticresources;

import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.mas_aplicaciones.appventon.R;

import java.util.Objects;

public class StaticResources
{
    public final static String [] OPCIONES_GENERO =  {"Género","Masculino","Femenino"};
    public final static String [] OPCIONES_CARRERAS =  {"Carrera","Ing. Administración","Ing. Sistemas","Ing. Industrial","Ing. Alimientarias","Ing. Electrónica","Ing. Mecatrónica","Ing. Mécanica","Ing. Civil"};
    public final static String [] OPCIONES_ORGANIZACION = {"Organización","ITSU"};
    public final static String [] OPCIONES_TIEMPO_ESPERA = {"Tiempo espera (min)","1","2","3","4"};
    public final static String EMAILSENDER ="appventonitsu@gmail.com";
    public final static String PASSWORD ="AppVenton1234";

    public static void actionSnackbar(final View view,String mensaje,int id_button,int id_fragment){
       Snackbar.make(Objects.requireNonNull(view),mensaje,Snackbar.LENGTH_LONG).setAction(
              id_button,
               v -> Navigation.findNavController(view).navigate(id_fragment)).show();

    }
    public static void actionSnackbar(final View view,String mensaje){
        Snackbar.make(Objects.requireNonNull(view),mensaje,Snackbar.LENGTH_LONG).show();

    }

}
