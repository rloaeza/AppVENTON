package com.mas_aplicaciones.appventon.usuario;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalUsuario extends Fragment {




    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(1);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_usuario, container, false);



        ImageButton btnMenu = view.findViewById(R.id.image_button_menu);
        btnMenu.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_principalUsuario_to_menu2));

        return view;
    }

}
