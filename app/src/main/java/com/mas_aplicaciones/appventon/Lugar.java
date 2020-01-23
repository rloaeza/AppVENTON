package com.mas_aplicaciones.appventon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.card.MaterialCardView;
import com.mas_aplicaciones.appventon.chofer.PrincipalChofer;

import java.util.Objects;


public class Lugar extends Fragment {

    private ImageView imageView_lugar;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        Glide.with(Objects.requireNonNull(getView()).getContext())
                .load(Objects.requireNonNull(PrincipalChofer.lugar.get("imagen")).toString())
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(imageView_lugar);
       // Toast.makeText(getContext(),PrincipalChofer.lugar.get("id"),Toast.LENGTH_LONG).show();

        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(3);
        }
        final View view = inflater.inflate(R.layout.fragment_lugar, container, false);

        TextView textView_titulo = view.findViewById(R.id.text_view_lugar);
        imageView_lugar = view.findViewById(R.id.image_view_lugar);
        textView_titulo.setText(Objects.requireNonNull(PrincipalChofer.lugar.get("nombre")).toString());
        MaterialCardView button_agregar_lugar = view.findViewById(R.id.button_seleccionar_lugar);
        button_agregar_lugar.setOnClickListener(v -> Navigation.findNavController(view).navigate(R.id.action_lugar_to_agregarLugar));

        return view;

    }

    @Override
    public void onPause()
    {

        super.onPause();
    }

    //create an interface
    //modify the class holder
    //create another activity

}
