package com.mas_aplicaciones.appventon;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class datos_personales_usuario extends Fragment {

    public datos_personales_usuario() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        return inflater.inflate(R.layout.fragment_datos_personales_usuario, container, false);
    }


}
