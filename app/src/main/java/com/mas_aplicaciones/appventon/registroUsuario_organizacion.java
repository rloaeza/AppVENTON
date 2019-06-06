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

import java.util.HashMap;
import java.util.Map;

import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 */
public class registroUsuario_organizacion extends Fragment {

    private static Map<String, Object> data = new HashMap<>();
    private Spinner spinner_genero,spinner_carreras;
    private Button btnRegistrar;

    private ArrayAdapter<String> adapter;
    private ArrayAdapter <String> adapter2;

    private final String [] OPCIONES_GENERO =  {"Género","Masculino","Femenino"};
    private final String [] OPCIONES_CARRERAS =  {"Carrera","Ing. Sistemas","Ing. Administración","Ing. Industrial","Ing. Alimientarias","Ing. Electrónica","Ing. Mecatrónica","Ing. Mécanica","Ing. Civil"};

    public registroUsuario_organizacion()
    {


    }
    public static void setValueMap(String key, Object value)
    {
        data.put(key,value);
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.registro_usuario_organizacion, container, false);

        //sipnner genero
        spinner_genero = view.findViewById(R.id.spinner_selecGen);
        //spinner carrera
        spinner_carreras = view.findViewById(R.id.spinner_selecCarrera);
        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_values, OPCIONES_GENERO);
        adapter2 = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_values, OPCIONES_CARRERAS);
        spinner_genero.setAdapter(adapter);
        spinner_carreras.setAdapter(adapter2);

        btnRegistrar = view.findViewById(R.id.button_registrar);

        //button action
        btnRegistrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if(spinner_carreras.getSelectedItemPosition()>=0)
                Toast.makeText(getActivity(),"Checar correo electrónico para validar su correo",Toast.LENGTH_SHORT).show();
                findNavController(v).navigate(R.id.action_registroUsuario_organizacion_to_inicioSesion2);

            }
        });
        return  view;
    }

}
