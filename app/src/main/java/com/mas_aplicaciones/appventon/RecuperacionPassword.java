package com.mas_aplicaciones.appventon;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;
import com.mas_aplicaciones.appventon.firebase.EvaluacionDeViews;
import com.mas_aplicaciones.appventon.firebase.QueriesFirebase;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;

import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecuperacionPassword extends Fragment {


    private EditText editText_email,editText_telefono,editText_numero_control;
    private RadioButton radioButton_prestado_servicios, radioButton_pasajero;
    private RadioGroup radioGroup;
    private Spinner spinner_carrera;
    private String email, telefono,numeroControl,carrera,tipo_usuario;
    private EvaluacionDeViews evaluacionDeViews = new EvaluacionDeViews();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recuperacion_password, container, false);
        editText_email = view.findViewById(R.id.edit_text_email);
        editText_numero_control= view.findViewById(R.id.edit_text_num_control);
        editText_telefono = view.findViewById(R.id.edit_text_telefono);
        radioGroup = view.findViewById(R.id.radioGroup_peticion);
        radioButton_prestado_servicios = view.findViewById(R.id.radioButton_prestador_servicios);
        radioButton_pasajero = view.findViewById(R.id.radioButton_pasajero);
        spinner_carrera = view.findViewById(R.id.spinner_selecCarrera);
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()),R.layout.spinner_item_values_2, StaticResources.OPCIONES_CARRERAS);
        spinner_carrera.setAdapter(arrayAdapter);
        Button button_recuperar = view.findViewById(R.id.button_recuperar_cuenta);


        button_recuperar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                email=editText_email.getText().toString();
                numeroControl = editText_numero_control.getText().toString();
                telefono= editText_telefono.getText().toString();

                if(!email.equals("") && evaluacionDeViews.emailValidado(email))
                {
                    if(!telefono.equals("") && evaluacionDeViews.telefonoValido(telefono))
                    {
                        if(!numeroControl.equals("") && evaluacionDeViews.numControlValido(numeroControl))
                        {
                            if(spinner_carrera.getSelectedItemPosition()>=1)
                            {
                                if(radioButton_pasajero.isChecked() || radioButton_prestado_servicios.isChecked())
                                {
                                    carrera = spinner_carrera.getSelectedItem().toString().trim();
                                    tipo_usuario=(radioButton_prestado_servicios.isChecked())?"Choferes":"Usuarios";

                                     new QueriesFirebase().BuscarDocumento(email.trim(),carrera,telefono,numeroControl,tipo_usuario,view);

                                     editText_numero_control.setText("");
                                     editText_email.requestFocus();
                                     editText_email.setText("");
                                     editText_telefono.setText("");
                                     radioGroup.clearCheck();
                                     spinner_carrera.setSelection(0);
                                }
                                else
                                {
                                    Toast.makeText(getContext(),"Seleccione qué tipo de usuario es",Toast.LENGTH_SHORT).show();
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(),"Carrera no seleccionada",Toast.LENGTH_SHORT).show();
                            }


                        }
                        else
                        {
                            editText_numero_control.setError("required");
                            Toast.makeText(getActivity(), "Comienza con 1X04XXXX y tiene 8 caracteres", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        editText_telefono.setError("required");
                        Toast.makeText(getActivity(), "Teléfono nulo o incorrecto", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    editText_email.setError("required");
                    Toast.makeText(getActivity(), "Email incorrecto", Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }




}