package com.mas_aplicaciones.appventon;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.navigation.Navigation;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalUsuario extends Fragment {




    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_usuario, container, false);
        Button btnMenu = view.findViewById(R.id.image_button_menu);
        btnMenu.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_principalUsuario_to_menu2));

        return view;
    }

}
