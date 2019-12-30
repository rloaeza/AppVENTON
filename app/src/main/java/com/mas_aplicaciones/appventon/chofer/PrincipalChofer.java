package com.mas_aplicaciones.appventon.chofer;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import com.google.android.material.snackbar.Snackbar;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;

import static androidx.navigation.Navigation.findNavController;


public class PrincipalChofer extends Fragment {



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragments
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(1);
        }
        final View view = inflater.inflate(R.layout.fragment_principal_chofer, container, false);
        ImageButton btnMenu = view.findViewById(R.id.image_button_menu);
        btnMenu.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_principalChofer_to_menu2));

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(FirebaseConexionFirestore.getValue("URI_Coche").equals(""))
        {

            StaticResources.actionSnackbar(getView());

        }

    }
}
