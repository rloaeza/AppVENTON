package com.mas_aplicaciones.appventon.usuarios;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.evaluacion_de_views;

import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroUsuario extends Fragment {

    evaluacion_de_views objeto_evaluacion_de_views = new evaluacion_de_views();
    private EditText editText_nombre;
    private EditText editText_apellidos;
    private EditText editText_edad ;
    private EditText editText_telefono;
    private EditText editText_email;
    private EditText editText_contrasena;
    private String nombre;
    private String apellidos;
    private String edad;
    private String telefono;
    private String email;
    private String contrasena;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_registro_usuario, container, false);
        Button btnSiguiente = view.findViewById(R.id.button_registrar);
        editText_nombre = view.findViewById(R.id.edit_text_nombre);
        editText_apellidos = view.findViewById(R.id.edit_text_apellidos);
        editText_edad = view.findViewById(R.id.edit_text_edad);
        editText_telefono = view.findViewById(R.id.edit_text_telefono);
        editText_email = view.findViewById(R.id.edit_text_email);
        editText_contrasena = view.findViewById(R.id.edit_text_contrasena2);



        btnSiguiente.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {

                nombre = editText_nombre.getText().toString();
                apellidos = editText_apellidos.getText().toString();
                edad = editText_edad.getText().toString();
                telefono = editText_telefono.getText().toString();
                email = editText_email.getText().toString();
                contrasena = editText_contrasena.getText().toString();

                if(!nombre.equals(""))
                {
                    if(!apellidos.equals(""))
                    {
                        if(!edad.equals("") && objeto_evaluacion_de_views.es_numero(edad,17))
                        {
                            if(!telefono.equals(""))
                            {
                                if(!email.equals("") && objeto_evaluacion_de_views.emailValidado(email))
                                {
                                    if(!contrasena.equals("") && objeto_evaluacion_de_views.contrasena_correcta(contrasena))
                                    {
                                            registroUsuario_organizacion.setValueMap("Nombre",nombre);
                                            registroUsuario_organizacion.setValueMap("Apellidos",apellidos);
                                            registroUsuario_organizacion.setValueMap("Edad",Integer.parseInt(edad));
                                            registroUsuario_organizacion.setValueMap("Teléfono",telefono);
                                            registroUsuario_organizacion.setValueMap("Email",email);
                                            registroUsuario_organizacion.setValueMap("Contraseña",contrasena);
                                            findNavController(v).navigate(R.id.action_registroUsuario_to_registroUsuario_organizacion);

                                    }
                                    else
                                    {
                                        editText_contrasena.setError("required");
                                        Toast.makeText(getActivity(),"Debe tener mínimo una letra mayuscula, un número y 8 caracteres",Toast.LENGTH_SHORT).show();
                                    }
                                }
                                else
                                {
                                    editText_email.setError("required");
                                    Toast.makeText(getActivity(),"Email incorrecto",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                editText_telefono.setError("required");
                            }
                        }
                        else
                        {
                            editText_edad.setError("required");
                            Toast.makeText(getActivity(),"La edad es incorrecta, o no está en el rango (17-100) años ",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        editText_apellidos.setError("required");
                    }

                }
                else
                {
                    editText_nombre.setError("required");
                }
            }
        });
        return view;
    }



}
