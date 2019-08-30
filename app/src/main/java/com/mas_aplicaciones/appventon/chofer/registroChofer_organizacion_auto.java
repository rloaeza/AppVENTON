package com.mas_aplicaciones.appventon.chofer;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.firebase_conexion_firestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import static androidx.navigation.Navigation.findNavController;
import static com.mas_aplicaciones.appventon.InicioSesion.mAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class registroChofer_organizacion_auto extends Fragment {

    private View view;
    private Spinner spinner_organizacion,spinner_genero;
    private String [] OPCIONES_ORGANIZACION = {"Organización","ITSU"};
    private final String [] OPCIONES_GENERO =  {"Género","Masculino","Femenino"};
    private static Map<String,Object> data = new HashMap<>();
    firebase_conexion_firestore conexion=new firebase_conexion_firestore();

    public registroChofer_organizacion_auto() {
        // Required empty public constructor
    }
    public static void setValueMap(String key, Object value)
    {
        data.put(key,value);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_registro_chofer_organizacion_auto, container, false);

        spinner_organizacion = view.findViewById(R.id.spinner_organizacion);
        spinner_genero = view.findViewById(R.id.spinner_selecGen);
        ArrayAdapter<String> adapter_organizacion = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_values, OPCIONES_ORGANIZACION);
        ArrayAdapter<String> adapter_genero = new ArrayAdapter<>(Objects.requireNonNull(getActivity()),R.layout.spinner_item_values,OPCIONES_GENERO);
        spinner_organizacion.setAdapter(adapter_organizacion);
        spinner_genero.setAdapter(adapter_genero);
        Button btnSiguiente = view.findViewById(R.id.button_registrar);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( final View v)
            {
                if(spinner_organizacion.getSelectedItemPosition()>=1)
                {
                    if(spinner_genero.getSelectedItemPosition()>=1)
                    {
                        data.put("Organización",spinner_organizacion.getSelectedItem().toString());
                        data.put("Género",spinner_genero.getSelectedItem().toString());

                        mAuth.createUserWithEmailAndPassword(Objects.requireNonNull(data.get("Email")).toString(), Objects.requireNonNull(data.get("Contraseña")).toString())
                                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();

                                            try {
                                                assert user != null;
                                                user.sendEmailVerification();
                                            }
                                            catch(NullPointerException e)
                                            {
                                                e.printStackTrace();
                                            }

                                            Toast.makeText(getActivity(),"Checar correo electrónico para validar su correo",Toast.LENGTH_SHORT).show();

                                            //agrega los datos a usuarios y le asigna el mismo UID de la autentificación a los datos de este.
                                            data.put("validacion",false);
                                            conexion.agregar_chofer(data,user.getUid());

                                            findNavController(v).navigate(R.id.action_registroChofer_organizacion_auto_to_inicioSesion);

                                        }
                                        else {
                                            // If sign in fails, display a message to the user.
                                            //si el registro de usuario genera una colision, esto es que ya existe un email registrado con esa dir
                                            if(task.getException() instanceof FirebaseAuthUserCollisionException)
                                            {
                                                Toast.makeText(getActivity(), "Ese Email ya fue registrado",Toast.LENGTH_SHORT).show();
                                            }
                                            else
                                            {
                                                Toast.makeText(getActivity(), "Error de registro, sin acceso a Internet",Toast.LENGTH_SHORT).show();
                                                data.clear();
                                                findNavController(view).navigate(R.id.action_registroChofer_organizacion_auto_to_inicioSesion);

                                            }
                                        }
                                    }
                                });
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Género no seleccionado",Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Carrera no seleccionada",Toast.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }


}
