package com.mas_aplicaciones.appventon.menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class quejas extends Fragment {


    public quejas() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment

        return inflater.inflate(R.layout.fragment_quejas, container, false);
    }

}
