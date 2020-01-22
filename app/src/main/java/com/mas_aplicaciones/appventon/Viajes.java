package com.mas_aplicaciones.appventon;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.mas_aplicaciones.appventon.Interfaces.CancelarChoferLugar;
import com.mas_aplicaciones.appventon.Interfaces.CargarDatos;
import com.mas_aplicaciones.appventon.Interfaces.NotificarToast;
import com.mas_aplicaciones.appventon.chofer.PrincipalChofer;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 */
public class Viajes extends Fragment {

    private EditText editText_comentario;
    private AlertDialog dialogo;
    private Spinner spinner_tiempo_espera;
    private ImageView image_view_lugar;
    private TextView textView_lugar_nombre,text_view_espacios_restantes;
    private List<String> espacios = new ArrayList<>();
    private TimePicker timePicker;
    private final FirebaseConexionFirestore firebaseConexionFirestore = new FirebaseConexionFirestore();
    private  String UUID_Puntos_recoleccion="";


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
        cargarDialogo(view);
        timePicker = view.findViewById(R.id.time);
        textView_lugar_nombre = view.findViewById(R.id.text_view_lugar);
        text_view_espacios_restantes = view.findViewById(R.id.text_view_espacios_restantes);
        editText_comentario = view.findViewById(R.id.edit_text_mensaje);
        image_view_lugar = view.findViewById(R.id.image_view_lugar);
        spinner_tiempo_espera = view.findViewById(R.id.spinner_tiempo_espera);
        ArrayAdapter<String> adapter_tiempo_espera = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_values_2, StaticResources.OPCIONES_TIEMPO_ESPERA);
        spinner_tiempo_espera.setAdapter(adapter_tiempo_espera);
        MaterialCardView button_cancelar = view.findViewById(R.id.button_cancelar);
        button_cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                dialogo.show();

                Toast.makeText(getActivity(),"adios",Toast.LENGTH_SHORT).show();
            }
        });
        firebaseConexionFirestore.getDatosViaje(FirebaseConexionFirestore.getValue("Viaje").toString());
        firebaseConexionFirestore.setCargarDatosViajeListener(new CargarDatos() {
            @Override
            public void getInformacion(Map<String, Object> datos) {
                Glide.with(view.getRootView().getContext())
                        .load(datos.get("URI_Lugar"))
                        .fitCenter()
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .into(image_view_lugar);

                textView_lugar_nombre.setText(datos.get("Nombre_Lugar").toString());
                text_view_espacios_restantes.setText("Espacios disponibles: "+datos.get("Espacios").toString());
                editText_comentario.setText(datos.get("Comentario").toString());
                spinner_tiempo_espera.setSelection(Integer.parseInt(datos.get("TiempoEspera").toString()));
                String []hora=datos.get("Hora").toString().split(":");
                timePicker.setHour(Integer.parseInt(hora[0]));
                timePicker.setMinute(Integer.parseInt(hora[1]));
                UUID_Puntos_recoleccion =datos.get("IdLugar").toString();

            }
        });




        return view;
    }
    private void cargarDialogo(final View view)
    {
        final String UUID_chofer_lugar=FirebaseConexionFirestore.getValue("Viaje").toString();

        dialogo = new AlertDialog
                .Builder(getActivity()) // getActivity() si es dentro de un fragmento
                .setPositiveButton("Cancelar Viaje", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        // Hicieron click en el botón positivo, así que la acción está confirmada
                        firebaseConexionFirestore.eliminar_chofer_lugar(UUID_chofer_lugar);
                        firebaseConexionFirestore.setNotificarToastListener(
                        (String mensaje)->
                        {
                            if(mensaje.equals("Eliminado el viaje"))
                            {
                                FirebaseConexionFirestore.actualizarViaje("");
                                firebaseConexionFirestore.consultarRelacionUsuarioChofer(FirebaseConexionFirestore.DOCUMENT);
                                firebaseConexionFirestore.consultarRelacionChoferLugar(UUID_Puntos_recoleccion);
                                firebaseConexionFirestore.setCancelarChoferLugarListener(new CancelarChoferLugar() {

                                    @Override
                                    public void getUsuariosRelacionados(int cantidadUsuarios) {
                                        if(cantidadUsuarios>0)//afecto ranking=ranking-(cantidadUsuarios*0.02)*2
                                        {
                                            Log.e("err","usuario valor");
                                            firebaseConexionFirestore.actualizarRating(cantidadUsuarios*0.02*2);
                                        }
                                        else//no lo afecto en -0.04
                                        {
                                            Log.e("err","0.04");
                                            firebaseConexionFirestore.actualizarRating(0.30);
                                        }
                                    }

                                    @Override
                                    public void getChoferesRelacionados(boolean hay)
                                    {

                                            Log.e("err",""+hay);
                                            firebaseConexionFirestore.actualizarHay(UUID_Puntos_recoleccion,hay);
                                    }
                                });

                                Toast.makeText(getActivity(),mensaje,Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(getActivity(),mensaje+" no se puede borrar error de conexion a internet",Toast.LENGTH_SHORT).show();
                            }

                        });
                        findNavController(view).navigate(R.id.action_viajes_to_principalChofer2);




                    }
                })
                .setNegativeButton("no cancelarlo", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Hicieron click en el botón negativo, no confirmaron
                        // Simplemente descartamos el diálogo
                        PrincipalChofer.lugar.clear();
                        dialog.dismiss();
                    }
                })
                .setTitle("Confirmar") // El título
                .setMessage("¿Deseas eliminar el viaje, después de aceptarlo," +
                        "afectará su reputación de servicio, aún más si es que ya hay usuarios en espera?") // El mensaje
                .create();
    }





}
