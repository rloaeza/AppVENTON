package com.mas_aplicaciones.appventon;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mas_aplicaciones.appventon.Interfaces.Choferes;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.firebase.FirestoreConection;
import com.mas_aplicaciones.appventon.usuario.PrincipalUsuario;

import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;

import static androidx.navigation.Navigation.findNavController;


public class LugarUsuario extends Fragment
{
    private View view;
    private TextView textView_titulo;
    private ImageView imageView_lugar;
    private ListView listView_choferesLugar;
    AdaptadorChofer arrayAdapter=null;
    FirestoreConection firestoreConection = new FirestoreConection();
    FirebaseConexionFirestore firebaseConexionFirestore = new FirebaseConexionFirestore();



    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Glide.with(getView().getContext())
                .load(PrincipalUsuario.lugar.get("imagen"))
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(imageView_lugar);


        firestoreConection.setListenerCambiosChoferes(new Choferes() {
            @Override
            public void getDatos(QuerySnapshot elementos) {


                ArrayList<EntidadChofer> entidadChoferList = new ArrayList<>();
                for (QueryDocumentSnapshot document : elementos)
                {
                    String id = document.getId();
                    String idChofer = document.getData().get("IdChofer").toString();
                    String nombreChofer=document.getData().get("Nombre").toString();
                    String imagenChofer=document.getData().get("URI").toString();
                    String imagenCoche = document.getData().get("URI_Coche").toString();
                    String comentario = document.getData().get("Comentario").toString();
                    String espacios = document.getData().get("Espacios").toString();
                    String tiempoEspera=document.getData().get("TiempoEspera").toString();
                    String hora = document.getData().get("Hora").toString();
                    Timestamp timestamp = (Timestamp) document.getData().get("Fecha");
                    String horaarray[] = hora.split(":");
                    if(horaarray[1].length()==1)
                    {
                        horaarray[1]="0"+horaarray[1];

                    }
                    if(Integer.parseInt(horaarray[0])>12)
                    {
                        hora=(Integer.parseInt(horaarray[0])-12)+":"+horaarray[1]+"pm";
                    }
                    else
                    {
                        hora=horaarray[0]+":"+horaarray[1]+"am";
                    }
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(timestamp.toDate());
                    calendar.set(Calendar.HOUR,Integer.parseInt(horaarray[0]));
                    calendar.set(Calendar.MINUTE,Integer.parseInt(horaarray[1]));
                    Calendar calendar1 = Calendar.getInstance();
                    String fecha= calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+1+"/"+calendar.get(Calendar.YEAR);


                    if(calendar1.getTime().after(calendar.getTime()))
                    {
                        EntidadChofer entidadChofer = new EntidadChofer();
                        entidadChofer.setId(id);
                        entidadChofer.setIdChofer(idChofer);
                        entidadChofer.setNombre(nombreChofer);
                        entidadChofer.setImagen(imagenChofer);
                        entidadChofer.setImagen_Coche(imagenCoche);
                        entidadChofer.setFecha("Fecha: " + fecha);

                        entidadChofer.setHora("Hora: " + hora);
                        if (!comentario.equals("")) {
                            entidadChofer.setComentario("Comentario:\n" + comentario);
                        } else {
                            entidadChofer.setComentario("Ningún comentario disponible");
                        }
                        entidadChofer.setEspacio("Hay " + espacios + " espacios disponibles");
                        entidadChofer.setTiempoEspera("Tiempo de espera en el lugar es de: " + tiempoEspera + " min");

                        entidadChoferList.add(entidadChofer);
                    }

                }

               arrayAdapter = new AdaptadorChofer(view.getContext(),entidadChoferList);
                listView_choferesLugar.setAdapter(arrayAdapter);


            }

            @Override
            public void getUpdateData(ArrayList<EntidadChofer> elementosAdd,ArrayList<EntidadChofer> elementosMod,ArrayList<EntidadChofer> elementosDel, int opcion)
            {
                if(arrayAdapter!=null) {
                    if(elementosAdd.size()>0)//necesito agregar
                    {
                        if(arrayAdapter.getCount()>0)
                        {

                            arrayAdapter.add(elementosAdd);

                            /*ArrayList<EntidadChofer> entidadesOld=arrayAdapter.getAllEntidades();
                            arrayAdapter.delete(arrayAdapter.getAllEntidades());
                            arrayAdapter=null;

                            listView_choferesLugar.setAdapter(null);
                            entidadesOld.addAll(elementosAdd);
                            arrayAdapter = new AdaptadorChofer(view.getContext(),entidadesOld);
                            listView_choferesLugar.setAdapter(arrayAdapter);*/

                        }
                        else
                        {
                            Log.d("err", "else");
                            arrayAdapter = new AdaptadorChofer(view.getContext(),elementosAdd);
                            listView_choferesLugar.setAdapter(arrayAdapter);
                        }


                       // StaticResources.actionSnackbar(getView(),"Se agregó un nuevo chofer");

                    }
                    if(elementosMod.size()>0)//necesito actualizar
                    {
                        arrayAdapter.update(elementosMod);

                    }
                    if(elementosDel.size()>0)//necesito borrar
                    {
                        arrayAdapter.delete(elementosDel);
                        checarCantidadChoferes();


                    }
                    listView_choferesLugar.setAdapter(arrayAdapter);
                    Log.e("err", ""+elementosAdd.size()+" "+elementosDel.size()+" "+elementosMod.size()+" "+opcion);

                }
                else
                {

                }


            }
        });

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(3);
        }
        view= inflater.inflate(R.layout.fragment_lugar_usuario, container, false);


        textView_titulo = view.findViewById(R.id.text_view_lugar);
        imageView_lugar = view.findViewById(R.id.image_view_lugar);
        textView_titulo.setText(PrincipalUsuario.lugar.get("title").toString());
        listView_choferesLugar = view.findViewById(R.id.list_view_choferesLugar);
        firestoreConection.obtenerChoferes(PrincipalUsuario.lugar.get("id").toString(),getActivity());
        firestoreConection.cambiosChoferes(PrincipalUsuario.lugar.get("id").toString());
        return view;
    }


    @Override
    public void onPause()
    {
        firebaseConexionFirestore.cargarLugaresUsuarios(null);
        super.onPause();
    }

    public void checarCantidadChoferes()
    {
        if(arrayAdapter.getCount()==0)
        {

            Log.e("err", " soy 0");
            FirebaseConexionFirestore.featuresUsuarios.clear();
            getActivity().onBackPressed();
            //Toast.makeText(getActivity(),"No hay más viajes en este punto",Snackbar.LENGTH_LONG).show();

        }
        else
        {
            Log.e("err", "soy 1");
            FirebaseConexionFirestore.featuresUsuarios.clear();
            // Snackbar.make(Objects.requireNonNull(view),"Se canceló un viaje",Snackbar.LENGTH_LONG).show();
        }

    }
    //create an interface
    //modify the class holder
    //create another activity



}
