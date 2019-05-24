package com.mas_aplicaciones.appventon;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.Navigation;

import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 */
public class InicioSesion extends Fragment {


    public InicioSesion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
        Button btnRegistrar = view.findViewById(R.id.button_registrar);
        Button btnIniciarSesion = view.findViewById(R.id.button_iniciar);
        //listener para entrar al tipo usuario
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {//acomoda y luego muestra y realiza todo lo necesario validad
            @Override
            public void onClick(View v) {


                findNavController(v).navigate(R.id.action_inicioSesion_to_principalUsuario);
                findNavController(v).navigate(R.id.action_inicioSesion_to_principalChofer);
            }
        });
        //listener para ingresar a tipo de usuario
        btnRegistrar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_inicioSesion_to_tipoUsuario));// metodo para pasar al siguiente fragment  sin validar
        return view;
    }

    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {//pesado spinner hace todo despues de que carga el layout facebook muestra el layout pero carga poco a poquito
        super.onViewCreated(view, savedInstanceState);
        Button btnRegistrar = view.findViewById(R.id.button_registrar);
        btnRegistrar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_inicioSesion_to_tipoUsuario));// metodo para pasar al siguiente fragment  sin validar
    }*/
}
