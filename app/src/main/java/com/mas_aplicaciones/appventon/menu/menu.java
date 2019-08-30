package com.mas_aplicaciones.appventon.menu;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mas_aplicaciones.appventon.InicioSesion;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.firebase_conexion_firestore;

import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;


public class menu extends Fragment {


    private TextView textUser;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        Button btnAyuda = view.findViewById(R.id.button_ayuda);
        Button btnQuejas = view.findViewById(R.id.button_quejas_sugerencias);
        Button btnConfiguracion = view.findViewById(R.id.button_configurar_datos);
        Button btnCerrarSesion = view.findViewById(R.id.button_cerrar_sesion);
        textUser = view.findViewById(R.id.text_view_nombre);

        btnAyuda.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_ayuda3));
        btnQuejas.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_quejas));
        btnConfiguracion.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_configurar));


        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase_conexion_firestore.ClearMap();
                InicioSesion.mAuth.signOut();
                clearPreferencias();
                firebase_conexion_firestore.ClearMap();
                findNavController(v).navigate(R.id.inicioSesion);

            }
        });
        return view;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {
        super.onStart();
        Object ob_nombre = firebase_conexion_firestore.getValue("Nombre");
        Object ob_apellidos = firebase_conexion_firestore.getValue("Apellidos");
        textUser.setText(ob_nombre +" "+ ob_apellidos);
    }
    private void clearPreferencias() {


        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("credenciales" , Context.MODE_PRIVATE); //nombre de mi file y el modo de visualizacion
        //Eliminar credeciales
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


    }
}
