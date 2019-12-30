package com.mas_aplicaciones.appventon.menu;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mas_aplicaciones.appventon.InicioSesion;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.storagefirebase.StorageFirebase;

import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;


public class menu extends Fragment {


    private TextView textPersona;
    private ImageView imageView_persona;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        //Button btnAyuda = view.findViewById(R.id.button_ayuda);
        Button btnQuejas = view.findViewById(R.id.button_quejas_sugerencias);
        Button btnConfiguracion = view.findViewById(R.id.button_configurar_datos);
        Button btnAcerca = view.findViewById(R.id.button_about);
        Button btnCerrarSesion = view.findViewById(R.id.button_cerrar_sesion);
        textPersona = view.findViewById(R.id.text_view_nombre);
        imageView_persona = view.findViewById(R.id.image_view_persona);
        //btnAyuda.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_ayuda3));
        btnQuejas.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_quejas));
        btnAcerca.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_about));
        if(FirebaseConexionFirestore.PERSONA.equals("Choferes"))
        {
            btnConfiguracion.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_configurar));
        }
        else
        {
            btnConfiguracion.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_menu2_to_configurar_usuarios));
        }



        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseConexionFirestore.actualizarDate();

                InicioSesion.mAuth.signOut();
                clearPreferencias();
                FirebaseConexionFirestore.ClearMap();
                findNavController(v).navigate(R.id.inicioSesion);

            }
        });

        return view;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onStart() {


        super.onStart();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        Object ob_nombre = FirebaseConexionFirestore.getValue("Nombre");
        Object ob_apellidos = FirebaseConexionFirestore.getValue("Apellidos");
        Object ob_uri = FirebaseConexionFirestore.getValue("URI");
        textPersona.setText(ob_nombre + " " + ob_apellidos);
        StorageReference mStorage = FirebaseStorage.getInstance().getReference();
        Glide.with(getView().getContext())
                .load(ob_uri.toString())
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(imageView_persona);








            // estoy en usuarios
            /*StorageReference storageReference = mStorage.child("Usuarios").child(ob_num_Control.toString());
            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    // Got the download URL for 'users/me/profile.png'
                    /*Glide.with(getView().getContext())
                            .load(uri)
                            .fitCenter().
                            apply(RequestOptions.circleCropTransform())
                            .centerCrop()
                            .into(imageView_user);*/


           /*     }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors

                }
            });*/

        super.onActivityCreated(savedInstanceState);

    }

    private void clearPreferencias() {


        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("credenciales" , Context.MODE_PRIVATE); //nombre de mi file y el modo de visualizacion
        //Eliminar credeciales
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();


    }
}