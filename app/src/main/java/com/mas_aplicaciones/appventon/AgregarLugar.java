package com.mas_aplicaciones.appventon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;
import com.mas_aplicaciones.appventon.chofer.PrincipalChofer;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.firebase.QueriesFirebase;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;


public class AgregarLugar extends Fragment
{
    private EditText editText_comentario;
    private Spinner spinner_espacios,spinner_tiempo_espera;
    private List<String> espacios = new ArrayList<>();
    private TimePicker timePicker;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(3);
        }
        View view = inflater.inflate(R.layout.fragment_agregar_lugar, container, false);
        for(int i=0;i<=Integer.parseInt(FirebaseConexionFirestore.getValue("CantidadPasajeros").toString());i++)
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
        editText_comentario = view.findViewById(R.id.edit_text_mensaje);
        Button button_agregar_lugar = view.findViewById(R.id.button_seleccionar_lugar);
        spinner_tiempo_espera = view.findViewById(R.id.spinner_tiempo_espera);
        spinner_espacios = view.findViewById(R.id.spinner_cantidad);
        ArrayAdapter<String> adapter_tiempo_espera = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_values_2, StaticResources.OPCIONES_TIEMPO_ESPERA);
        ArrayAdapter<String> adapter_cantidad_pasajeros = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_values_2,espacios );
        spinner_tiempo_espera.setAdapter(adapter_tiempo_espera);
        spinner_espacios.setAdapter(adapter_cantidad_pasajeros);
        button_agregar_lugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                List<String> agregarChoferes= (ArrayList<String>) PrincipalChofer.lugar.get("Choferes-lista");



                if (spinner_espacios.getSelectedItemPosition() >= 1) {
                    if (spinner_tiempo_espera.getSelectedItemPosition() >= 1) {
                        if (timePicker.getHour()>=6 && timePicker.getHour()<=20)
                        {

                                Map<String, Object> chofer_lugar = new HashMap<>();

                                chofer_lugar.put("IdChofer", FirebaseConexionFirestore.DOCUMENT);
                                chofer_lugar.put("Nombre", FirebaseConexionFirestore.getValue("Nombre").toString()+" "+FirebaseConexionFirestore.getValue("Apellidos").toString());
                                chofer_lugar.put("URI", FirebaseConexionFirestore.getValue("URI").toString());
                                chofer_lugar.put("URI_Coche", FirebaseConexionFirestore.getValue("URI_Coche").toString());
                                chofer_lugar.put("Hora", timePicker.getHour()+":"+timePicker.getMinute());
                                chofer_lugar.put("Fecha", Calendar.getInstance().getTime());
                                chofer_lugar.put("TiempoEspera", spinner_tiempo_espera.getSelectedItem());
                                chofer_lugar.put("Espacios", spinner_espacios.getSelectedItem());
                                chofer_lugar.put("Usuarios", new ArrayList<String>());
                                chofer_lugar.put("Lleno", false);
                                chofer_lugar.put("Comentario", editText_comentario.getText().toString());
                                chofer_lugar.put("IdLugar", Objects.requireNonNull(PrincipalChofer.lugar.get("id")).toString());
                                chofer_lugar.put("Ranking", Objects.requireNonNull(FirebaseConexionFirestore.getValue("Ranking")));
                                chofer_lugar.put("Nombre_Lugar", Objects.requireNonNull(PrincipalChofer.lugar.get("title")));
                                chofer_lugar.put("URI_Lugar",Objects.requireNonNull(PrincipalChofer.lugar.get("imagen").toString()));
                                String UUIDDChofer_lugar = UUID.randomUUID().toString();

                                Log.e("err",PrincipalChofer.lugar.get("id")+String.valueOf(PrincipalChofer.lugar.get("Choferes-lista")));
                                QueriesFirebase.BuscarChoferEnLugar(agregarChoferes,UUIDDChofer_lugar,chofer_lugar, Objects.requireNonNull(getView()));


                        }
                        else
                        {
                            Toast.makeText(getContext(),"La hora debe estan entre las  6 am y las 8:59 pm",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(getContext(),"Seleccione cuanto tiempo esperará en el lugar",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getContext(),"Seleccione cuántos asientos disponibles tiene",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return view;
    }



}
