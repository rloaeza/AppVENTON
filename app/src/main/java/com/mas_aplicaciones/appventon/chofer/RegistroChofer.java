package com.mas_aplicaciones.appventon.chofer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.firebase.EvaluacionDeViews;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.firebase.QueriesFirebase;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;
import com.mas_aplicaciones.appventon.storagefirebase.StorageFirebase;
import com.mas_aplicaciones.appventon.usuario.RegistroUsuarioOrganizacion;

import java.util.Calendar;

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
    private Spinner spinner_carrera;
    private String nombre;
    private String apellidos;
    private String edad;
    private String telefono;
    private String email;
    private String contrasena;
    private String numero_control;
    private EvaluacionDeViews evaluacionDeViews = new EvaluacionDeViews();
    private StorageFirebase storageFirebase = new StorageFirebase();
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_registro_chofer, container, false);
        editText_nombre = view.findViewById(R.id.edit_text_nombre);
        editText_apellidos = view.findViewById(R.id.edit_text_apellidos);
        editText_edad = view.findViewById(R.id.edit_text_edad);
        editText_telefono = view.findViewById(R.id.edit_text_telefono);
        editText_email = view.findViewById(R.id.edit_text_email);
        editText_contrasena = view.findViewById(R.id.edit_text_contrasena2);
        editText_numero_control = view.findViewById(R.id.edit_text_num_control);
        spinner_carrera = view.findViewById(R.id.spinner_selecCarrera);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item_values, StaticResources.OPCIONES_CARRERAS);
        spinner_carrera.setAdapter(arrayAdapter);
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


                    if (!nombre.equals(""))
                    {
                        if (!apellidos.equals(""))
                        {
                            if (!edad.equals("") && evaluacionDeViews.es_numero(edad, 17))
                            {
                                if (!telefono.equals("") && evaluacionDeViews.telefonoValido(telefono))
                                {
                                    if (!email.equals("") && evaluacionDeViews.emailValidado(email))
                                    {
                                        if (!contrasena.equals("") && evaluacionDeViews.contrasena_correcta(contrasena))
                                        {
                                            if(spinner_carrera.getSelectedItemPosition()>=1)
                                            {


                                                if (!numero_control.equals("") && evaluacionDeViews.numControlValido(numero_control))
                                                {

                                                        RegistroChoferOrganizacionAuto.setValueMap("Nombre", nombre.trim());
                                                        RegistroChoferOrganizacionAuto.setValueMap("Apellidos", apellidos.trim());
                                                        RegistroChoferOrganizacionAuto.setValueMap("Edad", Integer.parseInt(edad));
                                                        RegistroChoferOrganizacionAuto.setValueMap("Teléfono", telefono);
                                                        RegistroChoferOrganizacionAuto.setValueMap("Email", email.trim());
                                                        RegistroChoferOrganizacionAuto.setValueMap("Contraseña", contrasena);
                                                        RegistroChoferOrganizacionAuto.setValueMap("LastDate", Calendar.getInstance().getTime());
                                                        RegistroChoferOrganizacionAuto.setValueMap("Ranking",0.00);
                                                        QueriesFirebase.BuscarNumControl(numero_control,"Choferes",view,0);

                                                } else {
                                                    editText_numero_control.setError("required");
                                                    Toast.makeText(getActivity(), "Comienza con 1X04XXXX 8 caracteres", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(),"Carrera no seleccionada",Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            editText_contrasena.setError("required");
                                            Toast.makeText(getActivity(), "Debe tener mínimo una letra mayuscula, un número y 8 caracteres", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        editText_email.setError("required");
                                        Toast.makeText(getActivity(), "Email incorrecto", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    editText_telefono.setError("required");
                                    Toast.makeText(getActivity(), "Teléfono nulo o incorrecto", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                editText_edad.setError("required");
                                Toast.makeText(getActivity(), "La edad es incorrecta, o no está en el rango (17-100) años ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            editText_apellidos.setError("required");
                        }

                    } else {
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
        if(RegistroUsuarioOrganizacion.getValueMap("NumeroControl")!=null)
        {
            storageFirebase.EliminarFoto(RegistroUsuarioOrganizacion.getValueMap("NumeroControl").toString(),"Choferes",getView());
            Toast.makeText(getContext(), RegistroUsuarioOrganizacion.getValueMap("NumeroControl").toString(),Toast.LENGTH_SHORT).show();
        }
    }

}
