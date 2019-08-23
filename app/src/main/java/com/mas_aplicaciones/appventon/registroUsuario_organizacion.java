package com.mas_aplicaciones.appventon;


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
import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;
import static com.mas_aplicaciones.appventon.InicioSesion.mAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class registroUsuario_organizacion extends Fragment {

    private static Map<String, Object> data = new HashMap<>();
    private Spinner spinner_genero,spinner_carreras;
    private final String [] OPCIONES_GENERO =  {"Género","Masculino","Femenino"};
    private final String [] OPCIONES_CARRERAS =  {"Carrera","Ing. Administración","Ing. Sistemas","Ing. Industrial","Ing. Alimientarias","Ing. Electrónica","Ing. Mecatrónica","Ing. Mécanica","Ing. Civil"};
    firebase_conexion_firestore conexion=new firebase_conexion_firestore();

    public registroUsuario_organizacion()
    {


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
        View view = inflater.inflate(R.layout.registro_usuario_organizacion, container, false);
        //sipnner genero
        spinner_genero = view.findViewById(R.id.spinner_selecGen);
        //spinner carrera
        spinner_carreras = view.findViewById(R.id.spinner_selecCarrera);
        ArrayAdapter<String> adapter_genero = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_values, OPCIONES_GENERO);
        ArrayAdapter<String> adapter_carreras = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_values, OPCIONES_CARRERAS);
        spinner_genero.setAdapter(adapter_genero);
        spinner_carreras.setAdapter(adapter_carreras);

        Button btnRegistrar = view.findViewById(R.id.button_registrar);

        //button action
        btnRegistrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( final View v) {
                if(spinner_carreras.getSelectedItemPosition()>=1)
                {
                    if(spinner_genero.getSelectedItemPosition()>=1)
                    {
                        data.put("Carrera",spinner_carreras.getSelectedItem().toString());
                        data.put("Género",spinner_genero.getSelectedItem().toString());

                        mAuth.createUserWithEmailAndPassword(Objects.requireNonNull(data.get("Email")).toString(), Objects.requireNonNull(data.get("Contraseña")).toString())
                                .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>()
                                {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        if (task.isSuccessful()) {
                                            FirebaseUser user = mAuth.getCurrentUser();
                                            //mensaje de verificación

                                            try {
                                                assert user != null;
                                                user.sendEmailVerification();
                                            }
                                            catch (NullPointerException e)
                                            {
                                                e.printStackTrace();
                                            }


                                            //agrega los datos a usuarios y le asigna el mismo UID de la autentificación a los datos de este.
                                            conexion.agregar_usuario(data,user.getUid());
                                            Toast.makeText(getActivity(),"Checar correo electrónico para validar su correo",Toast.LENGTH_SHORT).show();


                                            findNavController(v).navigate((R.id.action_registroUsuario_organizacion_to_inicioSesion2));
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
                                                findNavController(v).navigate((R.id.action_registroUsuario_organizacion_to_inicioSesion2));
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
