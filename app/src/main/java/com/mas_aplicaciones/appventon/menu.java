package com.mas_aplicaciones.appventon;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import static androidx.navigation.Navigation.findNavController;


public class menu extends Fragment {



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
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
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


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales" , Context.MODE_PRIVATE); //nombre de mi file y el modo de visualizacion
        //Eliminar credeciales
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();


    }
}
