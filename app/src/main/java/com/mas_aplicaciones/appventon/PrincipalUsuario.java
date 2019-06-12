package com.mas_aplicaciones.appventon;


import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.navigation.Navigation;

import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 */
public class PrincipalUsuario extends Fragment {




    @RequiresApi(api = Build.VERSION_CODES.P)
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_principal_usuario, container, false);



        ImageButton btnMenu = view.findViewById(R.id.image_button_menu);
        btnMenu.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_principalUsuario_to_menu2));

        return view;
    }

   /* @Override
    public void onResume() {
        super.onResume();*/
    /*@Override
    public void onDetach() {
       super.onDetach();

        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(event.getKeyCode() == KeyEvent.KEYCODE_BACK )
                {
                    Toast.makeText(getActivity(),"Necesita cerrar Sesión",Toast.LENGTH_SHORT);
                    getActivity().finish();
                    return true;
                }
                return false;
            }
        });
    }*/
}
