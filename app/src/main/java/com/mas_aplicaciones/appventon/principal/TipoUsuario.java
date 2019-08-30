package com.mas_aplicaciones.appventon.principal;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class TipoUsuario extends Fragment {


    public TipoUsuario() {
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
        View view = inflater.inflate(R.layout.fragment_tipo_usuario, container, false);
        Button btnConductor = view.findViewById(R.id.button_conductor);
        Button btnUsuario =view.findViewById(R.id.button_pasajero);
        btnConductor.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tipoUsuario_to_registroChofer));
        btnUsuario.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_tipoUsuario_to_registroUsuario));

        return view;
    }

}
