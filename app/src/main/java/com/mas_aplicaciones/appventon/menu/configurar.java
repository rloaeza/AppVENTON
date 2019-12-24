package com.mas_aplicaciones.appventon.menu;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.Evaluacion_De_Views;
import com.mas_aplicaciones.appventon.firebase.Firebase_Conexion_Firestore;
import com.mas_aplicaciones.appventon.storagefirebase.StorageFirebase;


/**
 * A simple {@link Fragment} subclass.
 */
public class configurar extends Fragment
{


    private EditText editText_nombre, editText_apellidos, editText_celular;
    private Button button_camara,button_actualizar;
    private String nombre, apellido,celular;
    private Evaluacion_De_Views objeto_evaluacion_de_views = new Evaluacion_De_Views();
    private final static int GALLERY_INTENT = 1;
    private StorageFirebase storageFirebase = new StorageFirebase();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        final View view = inflater.inflate(R.layout.fragment_configurar, container, false);

        button_actualizar = view.findViewById(R.id.button_actualizar);
        button_camara = view.findViewById(R.id.button_camara);
        editText_nombre = view.findViewById(R.id.edit_text_nombre);
        editText_apellidos = view.findViewById(R.id.edit_text_apellidos);
        editText_celular = view.findViewById(R.id.edit_text_celular);
        editText_nombre.setText(Firebase_Conexion_Firestore.getValue("Nombre").toString());
        editText_apellidos.setText(Firebase_Conexion_Firestore.getValue("Apellidos").toString());
        editText_celular.setText(Firebase_Conexion_Firestore.getValue("Teléfono").toString());

        //listeners
        button_camara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");

                startActivityForResult(intent.createChooser(intent,"Selecciona una imagen"),GALLERY_INTENT);


            }
        });

        button_actualizar.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View v)
            {
                nombre = editText_nombre.getText().toString();
                apellido=editText_apellidos.getText().toString();
                celular=editText_celular.getText().toString();

                if(!nombre.equals(""))
                {
                    if(!apellido.equals("") )
                    {
                        if(!celular.equals("") && objeto_evaluacion_de_views.telefonoValido(celular))
                        {

                                Firebase_Conexion_Firestore.actualizarData(Firebase_Conexion_Firestore.PERSONA,Firebase_Conexion_Firestore.DOCUMENT,
                                        nombre.trim(),apellido.trim(),celular,getView());
                        }
                        else
                        {
                            editText_celular.setError("required");
                            Toast.makeText(getActivity(), "Teléfono nulo o incorrecto", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        editText_apellidos.setError("requered");
                    }

                }
                else
                {
                    editText_nombre.setError("required");
                }
            }
        });


        // Inflate the layout for this fragment
        return view;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        storageFirebase.EliminarFoto(Firebase_Conexion_Firestore.getValue("NumeroControl").toString(),Firebase_Conexion_Firestore.PERSONA,getView());

        if(requestCode==GALLERY_INTENT)
        {

            Uri uri = data.getData();
            storageFirebase.agregarFoto(Firebase_Conexion_Firestore.getValue("NumeroControl").toString(),uri,Firebase_Conexion_Firestore.PERSONA,getView(), configurar.class);



        }
    }


}
