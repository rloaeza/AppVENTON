package com.mas_aplicaciones.appventon.about;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;

public class About extends Fragment {
    //private Button button_politicas;
   // private Button button_terminos;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_about, container, false);


        Button button_desarrolladores = view.findViewById(R.id.button_desarrollado_por);
       // button_politicas = view.findViewById(R.id.button_politicas_privacidad);
       // button_terminos = view.findViewById(R.id.button_terminos_uso);

        button_desarrolladores.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigation.findNavController(v).navigate(R.id.action_about_to_desarrolladores);
            }
        });
        /*button_politicas.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigation.findNavController(v).navigate(R.id.action_about_to_politicasPrivacidad);
            }
        });
        button_terminos.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Navigation.findNavController(v).navigate(R.id.action_about_to_terminos);
            }
        });*/
        return view;
    }


}
