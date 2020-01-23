package com.mas_aplicaciones.appventon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.snackbar.Snackbar;
import com.mas_aplicaciones.appventon.Interfaces.Choferes;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.firebase.FirestoreConection;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;
import com.mas_aplicaciones.appventon.usuario.PrincipalUsuario;

import java.util.ArrayList;
import java.util.Objects;

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
        firestoreConection.obtenerChoferes(PrincipalUsuario.lugar.get("id").toString());
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
            //
            // Snackbar.make(Objects.requireNonNull(view),"Se canceló un viaje",Snackbar.LENGTH_LONG).show();
        }

    }
    //create an interface
    //modify the class holder
    //create another activity



}
