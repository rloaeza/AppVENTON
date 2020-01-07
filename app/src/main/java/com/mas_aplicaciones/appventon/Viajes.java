package com.mas_aplicaciones.appventon;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class Viajes extends Fragment {

    private EditText editText_comentario;
    private RecyclerView recycler_usuarios;
    private Spinner spinner_tiempo_espera;
    private ImageView image_view_persona;
    private TextView textView_lugar_nombre,text_view_espacios_restantes;
    private List<String> espacios = new ArrayList<>();
    private TimePicker timePicker;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(3);
        }
        final View view = inflater.inflate(R.layout.fragment_viajes, container, false);

        for(int i = 0; i<=Integer.parseInt(FirebaseConexionFirestore.getValue("CantidadPasajeros").toString()); i++)
        {
            if( i==0){
                espacios.add("Asientos disponibles");
            }
            else
            {
                espacios.add(String.valueOf(i));
            }

        }
        timePicker = view.findViewById(R.id.time);
        textView_lugar_nombre = view.findViewById(R.id.text_view_lugar);
        text_view_espacios_restantes = view.findViewById(R.id.text_view_espacios_restantes);
        editText_comentario = view.findViewById(R.id.edit_text_mensaje);
        image_view_persona = view.findViewById(R.id.image_view_persona);
        spinner_tiempo_espera = view.findViewById(R.id.spinner_tiempo_espera);
        ArrayAdapter<String> adapter_tiempo_espera = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_values_2, StaticResources.OPCIONES_TIEMPO_ESPERA);
        spinner_tiempo_espera.setAdapter(adapter_tiempo_espera);
        recycler_usuarios = view.findViewById(R.id.recycler_usuarios);
        FirebaseConexionFirestore.actualizarViajeAutomatica(view,image_view_persona,spinner_tiempo_espera,recycler_usuarios,textView_lugar_nombre,text_view_espacios_restantes,editText_comentario,timePicker);
        Button button_actualizar = view.findViewById(R.id.button_actualizar);
        Button button_cancelar = view.findViewById(R.id.button_cancelar);
        button_actualizar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(spinner_tiempo_espera.getSelectedItemId()>0)
                {
                    if (timePicker.getHour()>=6 && timePicker.getHour()<=20) {
                        FirebaseConexionFirestore.actualizarDatosViajeChofer(editText_comentario.getText().toString(), spinner_tiempo_espera.getSelectedItem().toString(), timePicker.getHour() + ":" + timePicker.getMinute());
                    }
                    else
                    {
                        Toast.makeText(getContext(),"La hora debe estan entre las  6 am y las 8:59 pm",Toast.LENGTH_SHORT).show();
                    }

                }
                else
                {
                    Toast.makeText(getContext(),"Seleccione un tiempo de espera",Toast.LENGTH_SHORT).show();
                }

            }
        });
        button_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                FirebaseConexionFirestore.eliminarViajeChofer(view);
            }
        });


        return view;
    }
}
