package com.mas_aplicaciones.appventon;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;



public class Principal extends Fragment {

    public Principal() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        //modifique el fragment a fragment_inicio_sesion de fragment principal
        return inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
    }


}
