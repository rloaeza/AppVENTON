package com.mas_aplicaciones.appventon;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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

import com.google.android.material.card.MaterialCardView;
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

import static androidx.navigation.Navigation.findNavController;


public class AgregarLugar extends Fragment
{
    private EditText editText_comentario;
    private Spinner spinner_espacios,spinner_tiempo_espera;
    private List<String> espacios = new ArrayList<>();
    private TimePicker timePicker;
    private AlertDialog dialogo;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {

        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(3);
        }
        final View view = inflater.inflate(R.layout.fragment_agregar_lugar, container, false);
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
        cargarDialogo(view);
        timePicker = view.findViewById(R.id.time);
        editText_comentario = view.findViewById(R.id.edit_text_mensaje);
        MaterialCardView button_agregar_lugar = view.findViewById(R.id.button_seleccionar_lugar);
        spinner_tiempo_espera = view.findViewById(R.id.spinner_tiempo_espera);
        spinner_espacios = view.findViewById(R.id.spinner_cantidad);
        ArrayAdapter<String> adapter_tiempo_espera = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_values_2, StaticResources.OPCIONES_TIEMPO_ESPERA);
        ArrayAdapter<String> adapter_cantidad_pasajeros = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_values_2,espacios );
        spinner_tiempo_espera.setAdapter(adapter_tiempo_espera);
        spinner_espacios.setAdapter(adapter_cantidad_pasajeros);
        button_agregar_lugar.setOnClickListener(v -> {

            if (spinner_espacios.getSelectedItemPosition() >= 1) {
                if (spinner_tiempo_espera.getSelectedItemPosition() >= 1) {
                    if (timePicker.getHour()>=6 && timePicker.getHour()<=20)
                    {

                                dialogo.show();
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

        });
        return view;
    }

    private void cargarDialogo(final View view)
    {
        dialogo = new AlertDialog
                .Builder(getActivity()) // getActivity() si es dentro de un fragmento
                .setPositiveButton("Crear Viaje y aceptar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Hicieron click en el botón positivo, así que la acción está confirmada
                        insertarDatos(view);
                    }
                })
                .setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Hicieron click en el botón negativo, no confirmaron
                        // Simplemente descartamos el diálogo
                        PrincipalChofer.lugar.clear();
                        findNavController(view).navigate(R.id.action_agregarLugar_to_principalChofer);
                        dialog.dismiss();
                    }
                })
                .setTitle("Confirmar") // El título
                .setMessage("¿Deseas Crear el viaje por la ruta especificada, después de aceptarlo, no podrá editarlo solo cancelarlo pero " +
                        "afectará su reputación de servicio, si es que ya hay usuarios en espera?") // El mensaje
                .create();
    }

    private void insertarDatos(View view)
    {
        Map<String, Object> map_chofer_lugar = new HashMap<>();
        map_chofer_lugar.put("IdChofer", FirebaseConexionFirestore.DOCUMENT);
        map_chofer_lugar.put("Nombre", FirebaseConexionFirestore.getValue("Nombre").toString()+" "+FirebaseConexionFirestore.getValue("Apellidos").toString());
        map_chofer_lugar.put("URI", FirebaseConexionFirestore.getValue("URI").toString());
        map_chofer_lugar.put("URI_Coche", FirebaseConexionFirestore.getValue("URI_Coche").toString());
        map_chofer_lugar.put("Hora", timePicker.getHour()+":"+timePicker.getMinute());
        map_chofer_lugar.put("Fecha", Calendar.getInstance().getTime());
        map_chofer_lugar.put("TiempoEspera", spinner_tiempo_espera.getSelectedItem());
        map_chofer_lugar.put("Espacios", spinner_espacios.getSelectedItem());
        map_chofer_lugar.put("Lleno", false);
        map_chofer_lugar.put("Comentario", editText_comentario.getText().toString());
        map_chofer_lugar.put("IdLugar", Objects.requireNonNull(PrincipalChofer.lugar.get("id")).toString());
        map_chofer_lugar.put("Ranking", Objects.requireNonNull(FirebaseConexionFirestore.getValue("Ranking")));
        map_chofer_lugar.put("Nombre_Lugar", Objects.requireNonNull(PrincipalChofer.lugar.get("nombre")));
        map_chofer_lugar.put("URI_Lugar",Objects.requireNonNull(PrincipalChofer.lugar.get("imagen").toString()));
        //string que contiene el uuid generado
        String UUIDD_chofer_lugar = UUID.randomUUID().toString();
        //objeto de la clase QueriesFirebase
        QueriesFirebase queriesFirebase = new QueriesFirebase();
        //método para agregar servicio
        queriesFirebase.AgregarLugar(UUIDD_chofer_lugar,map_chofer_lugar,view);
    }



}
