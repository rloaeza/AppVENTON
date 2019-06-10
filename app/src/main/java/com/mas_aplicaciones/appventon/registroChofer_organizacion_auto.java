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
public class registroChofer_organizacion_auto extends Fragment {


    public registroChofer_organizacion_auto() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_chofer_organizacion_auto, container, false);

        Spinner spinner_organizacion = view.findViewById(R.id.spinner_organizacion);
        String [] opciones = {"ITSU"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_values, opciones);
        spinner_organizacion.setAdapter(adapter);
        Button btnSiguiente = view.findViewById(R.id.button_siguiente);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Checar correo electrónico para validar su correo", Toast.LENGTH_LONG).show();
                findNavController(v).navigate(R.id.action_registroChofer_organizacion_auto_to_inicioSesion);
            }
        });
        return view;
    }

}
