package com.mas_aplicaciones.appventon;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseUser;


/**
 * A simple {@link Fragment} subclass.
 */
public class proceso_de_validacion extends Fragment {



    private FirebaseUser currentUser;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view  =inflater.inflate(R.layout.fragment_proceso_de_validacion, container, false);
        // 0 si es usuario no esta verificado el email y la verificacion de datos %0
        // 1 si verifico el email  y los datos no 50%
        // 2 si están los datos y el e mail no
        // 3 si los datos estan verificados 100%

        boolean val = false;// define si es un usuario o chofer
        try
        {
            firebase_conexion_firestore.getValue("validacion");
            val = true;

        }
        catch (NullPointerException e)
        {
            val=false;
        }
         if(!InicioSesion.mAuth.getCurrentUser().isEmailVerified())

            Toast.makeText(getActivity(), "Email validado",Toast.LENGTH_SHORT).show();
         else
         {
            if(val && (boolean)firebase_conexion_firestore.getValue("validacion"))
            {
                Toast.makeText(getActivity(), "",Toast.LENGTH_SHORT).show();
            }
         }

         return view;

    }

}
