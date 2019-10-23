package com.mas_aplicaciones.appventon.chofer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.firebase.evaluacion_de_views;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.storagefirebase.StorageFirebase;
import com.mas_aplicaciones.appventon.usuario.registroUsuario_organizacion;

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
    private EditText editText_numero_control;
    private String nombre;
    private String apellidos;
    private String edad;
    private String telefono;
    private String email;
    private String contrasena;
    private String numero_control;
    evaluacion_de_views objeto_evaluacion_de_views = new evaluacion_de_views();
    StorageFirebase storageFirebase = new StorageFirebase();

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
        editText_numero_control = view.findViewById(R.id.edit_text_num_control);
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
                numero_control = editText_numero_control.getText().toString();
                if(!nombre.equals(""))
                {
                    if(!apellidos.equals(""))
                    {
                        if(!edad.equals("") && objeto_evaluacion_de_views.es_numero(edad,17))
                        {
                            if(!telefono.equals("") && objeto_evaluacion_de_views.telefonoValido(telefono))
                            {
                                if(!email.equals("") && objeto_evaluacion_de_views.emailValidado(email))
                                {
                                    if(!contrasena.equals("") && objeto_evaluacion_de_views.contrasena_correcta(contrasena))
                                    {
                                        if(!numero_control.equals(""))
                                        {
                                            registroChofer_organizacion_auto.setValueMap("Nombre", nombre);
                                            registroChofer_organizacion_auto.setValueMap("Apellidos", apellidos);
                                            registroChofer_organizacion_auto.setValueMap("Edad", Integer.parseInt(edad));
                                            registroChofer_organizacion_auto.setValueMap("Teléfono", telefono);
                                            registroChofer_organizacion_auto.setValueMap("Email", email);
                                            registroChofer_organizacion_auto.setValueMap("Contraseña", contrasena);
                                            registroChofer_organizacion_auto.setValueMap("NumeroControl", numero_control);
                                            findNavController(v).navigate(R.id.action_registroChofer_to_registroChofer_organizacion_auto);
                                        }
                                        else
                                        {
                                            editText_contrasena.setError("required");
                                            Toast.makeText(getActivity(),"Comienza con 1X04XXXX 8 caracteres",Toast.LENGTH_SHORT).show();
                                        }
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
                                Toast.makeText(getActivity(),"Teléfono nulo o incorrecto",Toast.LENGTH_SHORT).show();
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
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        delete_photo();//metodo local


    }

    private void delete_photo()
    {
        if(registroUsuario_organizacion.getValueMap("NumeroControl")!=null)
        {
            storageFirebase.EliminarFoto(registroUsuario_organizacion.getValueMap("NumeroControl").toString(),"Choferes",getView());
            Toast.makeText(getContext(),registroUsuario_organizacion.getValueMap("NumeroControl").toString(),Toast.LENGTH_SHORT).show();
        }
        else
        {
           // Toast.makeText(getActivity(),"No entro" ,Toast.LENGTH_SHORT).show();
        }

    }

}
