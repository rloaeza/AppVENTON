package com.mas_aplicaciones.appventon;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.navigation.Navigation;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static androidx.navigation.Navigation.findNavController;


public class menu extends Fragment {

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    firebase_conexion_firestore  objeto_firebase_conexion_firestore = new firebase_conexion_firestore();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        Button btnAyuda = view.findViewById(R.id.button_ayuda);
        Button btnQuejas = view.findViewById(R.id.button_quejas_sugerencias);
        Button btnConfiguracion = view.findViewById(R.id.button_configurar_datos);
        Button btnCerrarSesion = view.findViewById(R.id.button_cerrar_sesion);
        TextView textUser =view.findViewById(R.id.text_view_nombre);
        objeto_firebase_conexion_firestore.getNombre(InicioSesion.mAuth.getCurrentUser().getUid());
//        textUser.setText(objeto_firebase_conexion_firestore.getValue("Nombre").toString());
        btnAyuda.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_ayuda3));
        btnQuejas.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_quejas));
        btnConfiguracion.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_configurar));


        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InicioSesion.mAuth.signOut();
                findNavController(v).navigate(R.id.action_menu2_to_inicioSesion);
            }
        });
        return view;
    }


}
