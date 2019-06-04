package com.mas_aplicaciones.appventon;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 */
public class registroUsuario_organizacion extends Fragment {


    public registroUsuario_organizacion() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view =  inflater.inflate(R.layout.registro_usuario_organizacion, container, false);
        //sipnner genero
        Spinner spinner_genero = view.findViewById(R.id.spinner_selecGen);
        String [] opciones =  {"Masculino","Femenino"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_values, opciones);
        spinner_genero.setAdapter(adapter);
        //
        //sipnner carrera
        Spinner spinner_carreras = view.findViewById(R.id.spinner_selecCarrera);
        String [] opciones_carreras =  {"Ing. Sistemas","Ing. Administración","Ing. Industrial","Ing. Alimientarias","Ing. Electrónica","Ing. Mecatrónica","Ing. Mécanica","Ing. Civil"};
        ArrayAdapter <String> adapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_values, opciones_carreras);
        spinner_carreras.setAdapter(adapter2);
        //
        Button btnRegistrar = view.findViewById(R.id.button_registrar);
        System.out.println("holaaaa"+btnRegistrar);
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Checar correo electrónico para validar su correo",Toast.LENGTH_SHORT).show();
                findNavController(v).navigate(R.id.action_registroUsuario_organizacion_to_inicioSesion2);

            }
        });
        return  view;
    }

}
