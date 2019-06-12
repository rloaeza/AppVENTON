package com.mas_aplicaciones.appventon;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;


import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;


public class menu extends Fragment {


    firebase_conexion_firestore  objeto_firebase_conexion_firestore = new firebase_conexion_firestore();
    Button btnAyuda;
    Button btnQuejas;
    Button btnConfiguracion;
    Button btnCerrarSesion;
    TextView textUser;
    View view;

    @Override
    public void onViewCreated(@NonNull View views, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(views, savedInstanceState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_menu, container, false);
        btnAyuda = view.findViewById(R.id.button_ayuda);
        btnQuejas = view.findViewById(R.id.button_quejas_sugerencias);
        btnConfiguracion = view.findViewById(R.id.button_configurar_datos);
        btnCerrarSesion = view.findViewById(R.id.button_cerrar_sesion);
        textUser =view.findViewById(R.id.text_view_nombre);

        btnAyuda.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_ayuda3));
        btnQuejas.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_quejas));
        btnConfiguracion.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_configurar));


        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebase_conexion_firestore.ClearMap();
                InicioSesion.mAuth.signOut();
                firebase_conexion_firestore.ClearMap();
                //findNavController(v).popBackStack(R.id.inicioSesion,true);
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
}
