package com.mas_aplicaciones.appventon;

import android.app.AlertDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mas_aplicaciones.appventon.adapters.AdapterChoferes;
import com.mas_aplicaciones.appventon.chofer.PrincipalChofer;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.models.ModelChoferes;
import com.mas_aplicaciones.appventon.usuario.PrincipalUsuario;

import java.util.ArrayList;

import dmax.dialog.SpotsDialog;


public class LugarUsuario extends Fragment
{
    private View view;
    private TextView textView_titulo;
    private ImageView imageView_lugar;
    private RecyclerView recyclerView_choferes;
    private CardView cardView_disponibles;
    private TextView textView_diponibles;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {

        Glide.with(getView().getContext())
                .load(PrincipalUsuario.lugar.get("imagen"))
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(imageView_lugar);
        FirebaseConexionFirestore.actualizarViajeAutomaticoUsuariosView(PrincipalUsuario.lugar.get("id").toString(),view,recyclerView_choferes,textView_diponibles);


        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(3);
        }
        view= inflater.inflate(R.layout.fragment_lugar_usuario, container, false);

        recyclerView_choferes = view.findViewById(R.id.recyclerView);
        recyclerView_choferes.setLayoutManager(new LinearLayoutManager(getContext()));
        textView_titulo = view.findViewById(R.id.text_view_lugar);
        imageView_lugar = view.findViewById(R.id.image_view_lugar);
        textView_diponibles = view.findViewById(R.id.text_view_disponibles);
        textView_titulo.setText(PrincipalUsuario.lugar.get("title").toString());
        // we will create recycler view in linear layout
        cardView_disponibles = view.findViewById(R.id.cardView);
        cardView_disponibles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView_diponibles.setText("Disponibles");
                AdapterChoferes adapter = new AdapterChoferes(view.getContext(),view, FirebaseConexionFirestore.choferModels,PrincipalUsuario.lugar.get("id").toString());
                recyclerView_choferes.setAdapter(adapter);
                recyclerView_choferes.setVisibility(View.VISIBLE);
            }
        });




        return view;

    }





    @Override
    public void onPause()
    {

        super.onPause();
    }

    //create an interface
    //modify the class holder
    //create another activity



}
