package com.mas_aplicaciones.appventon.chofer;


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
public class RegistroChofer extends Fragment {

    private EditText editText_nombre;
    private EditText editText_apellidos;
    private EditText editText_edad;
    private EditText editText_telefono;
    private EditText editText_email;
    private EditText editText_contrasena;
    private String nombre;
    private String apellidos;
    private String edad;
    private String telefono;
    private String email;
    private String contrasena;
    evaluacion_de_views objeto_evaluacion_de_views = new evaluacion_de_views();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_registro_chofer, container, false);
        editText_nombre = view.findViewById(R.id.edit_text_nombre);
        editText_apellidos = view.findViewById(R.id.edit_text_apellidos);
        editText_edad = view.findViewById(R.id.edit_text_edad);
        editText_telefono = view.findViewById(R.id.edit_text_telefono);
        editText_email = view.findViewById(R.id.edit_text_email);
        editText_contrasena = view.findViewById(R.id.edit_text_contrasena2);
        Button btnSiguiente = view.findViewById(R.id.button_registrar);

        btnSiguiente.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
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
                                        registroChofer_organizacion_auto.setValueMap("Nombre",nombre);
                                        registroChofer_organizacion_auto.setValueMap("Apellidos",apellidos);
                                        registroChofer_organizacion_auto.setValueMap("Edad",Integer.parseInt(edad));
                                        registroChofer_organizacion_auto.setValueMap("Teléfono",telefono);
                                        registroChofer_organizacion_auto.setValueMap("Email",email);
                                        registroChofer_organizacion_auto.setValueMap("Contraseña",contrasena);
                                        findNavController(v).navigate(R.id.action_registroChofer_to_registroChofer_organizacion_auto);
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
