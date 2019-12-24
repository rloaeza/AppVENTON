package com.mas_aplicaciones.appventon.menu;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.email.EmailSender;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;


/**
 * A simple {@link Fragment} subclass.
 */
public class quejas extends Fragment {



   private RadioButton radioButton1,radioButton2;
   private Button button_enviar;
   private EditText editText_queja;
   private TextView textView_emailreceiver;
   private final String EMAILSENDER ="appventonitsu@gmail.com";
   private final String SERVERDEFULT ="gmail.com";
   private final String PASSWORD ="AppVenton1234";
   private String user,server,subject,body;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_quejas, container, false);

        textView_emailreceiver = view.findViewById(R.id.textView_Email);
        editText_queja = view.findViewById(R.id.edit_text_mensaje);
        radioButton1 = view.findViewById(R.id.radioButton1);
        radioButton2 = view.findViewById(R.id.radioButton2);
        textView_emailreceiver.setText(FirebaseConexionFirestore.getValue("Email").toString());


        return view;
    }

    public void onClick(View view)
    {
        user=textView_emailreceiver.getText().toString();
        server = user.split("@")[1];
        subject=(radioButton1.isChecked())?"Queja":"Sugerencia";
        body =editText_queja.getText().toString();

        // Perform action on click


        EmailSender emailSender=new EmailSender();

        emailSender.execute(EMAILSENDER,PASSWORD,user,subject,body,"",SERVERDEFULT);//sdcard/DCIM/Camera/test.jpg

    }

}
