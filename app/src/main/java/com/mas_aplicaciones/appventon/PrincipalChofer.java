package com.mas_aplicaciones.appventon;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;



public class PrincipalChofer extends Fragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(1);
        }
        View view = inflater.inflate(R.layout.fragment_principal_chofer, container, false);
        ImageButton btnMenu = view.findViewById(R.id.image_button_menu);
        btnMenu.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_principalChofer_to_menu2));

        return view;
    }


}
