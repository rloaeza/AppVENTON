package com.mas_aplicaciones.appventon;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mas_aplicaciones.appventon.Interfaces.Choferes;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.firebase.FirestoreConection;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;
import com.mas_aplicaciones.appventon.usuario.PrincipalUsuario;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

import static androidx.navigation.Navigation.findNavController;


public class LugarUsuario extends Fragment
{
    private View view;
    private TextView textView_titulo;
    private ImageView imageView_lugar;
    private CardView cardView_disponibles;
    private TextView textView_diponibles;
    private ListView listView_choferesLugar;
    AdaptadorChofer arrayAdapter=null;
    FirestoreConection firestoreConection = new FirestoreConection();



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
            public void getDatos(ArrayList<EntidadChofer> elementos) {


                Log.e("err", "entre");
                arrayAdapter = new AdaptadorChofer(view.getContext(),elementos);
                listView_choferesLugar.setAdapter(arrayAdapter);
            }

            @Override
            public void getUpdateData(ArrayList<EntidadChofer> elementosAdd,ArrayList<EntidadChofer> elementosMod,ArrayList<EntidadChofer> elementosDel, int opcion)
            {
                if(arrayAdapter!=null) {
                    if(elementosAdd.size()>0)//necesito agregar
                    {
                        arrayAdapter.add(elementosAdd);
                        arrayAdapter = new AdaptadorChofer(view.getContext(),arrayAdapter.getAllEntidades());
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
                    Log.e("err", "soy null");
                    Log.e("err", ""+opcion);
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
        textView_diponibles = view.findViewById(R.id.text_view_disponibles);
        textView_titulo.setText(PrincipalUsuario.lugar.get("title").toString());
        cardView_disponibles = view.findViewById(R.id.cardView);
        listView_choferesLugar = view.findViewById(R.id.list_view_choferesLugar);
        firestoreConection.obtenerChoferes(PrincipalUsuario.lugar.get("id").toString());
        firestoreConection.cambiosChoferes(PrincipalUsuario.lugar.get("id").toString());
        return view;
    }


    @Override
    public void onPause()
    {

        super.onPause();
    }

    public void checarCantidadChoferes()
    {
        if(arrayAdapter.getCount()==0)
        {
            StaticResources.actionSnackbar(view.getRootView(),"No hay más viajes disponibles",R.string.regresar_al_mapa,R.id.action_lugarUsuario_to_principalUsuario);
            Log.e("err", " soy 0");
            FirebaseConexionFirestore.featuresUsuarios.clear();
            FirebaseConexionFirestore.cargarLugaresUsuarios();
        }
        else
        {
            Log.e("err", "soy 1");
            FirebaseConexionFirestore.featuresUsuarios.clear();
            FirebaseConexionFirestore.cargarLugaresUsuarios();
            StaticResources.actionSnackbar(view.getRootView(),"Se canceló un viaje");
        }

    }
    //create an interface
    //modify the class holder
    //create another activity



}
