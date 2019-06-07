package com.mas_aplicaciones.appventon;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.XmlRes;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.navigation.Navigation;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import static androidx.navigation.Navigation.findNavController;


/**
 * A simple {@link Fragment} subclass.
 */
public class InicioSesion extends Fragment {

    // Initialize Firebase Auth
    public static FirebaseAuth mAuth = FirebaseAuth.getInstance() ;
    private View view;
    private EditText editText_email,editText_contrasena;
    private String email,contrasena;
    private evaluacion_de_views objeto_evaluacion_de_views = new evaluacion_de_views();




    public InicioSesion() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
        editText_email = view.findViewById(R.id.edit_text_email);
        editText_contrasena = view.findViewById(R.id.edit_text_contrasena);
        Button btnRegistrar = view.findViewById(R.id.button_registrar);
        Button btnIniciarSesion = view.findViewById(R.id.button_iniciar);
        //listener para entrar al tipo usuario
        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {//acomoda y luego muestra y realiza todo lo necesario validad
            @Override
            public void onClick(View v) {
                email=editText_email.getText().toString();
                contrasena=editText_contrasena.getText().toString();
                if(objeto_evaluacion_de_views.emailValidado(email))
                {
                    mAuth.signInWithEmailAndPassword(email, contrasena)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        // Sign in success, update UI with the signed-in user's information
                                        //Log.d(TAG, "signInWithEmail:success");

                                        FirebaseUser user = mAuth.getCurrentUser();
                                        Toast.makeText(getActivity(),"Iniciando...",Toast.LENGTH_SHORT).show();
                                        findNavController(view).navigate(R.id.action_inicioSesion_to_principalUsuario);
                                        //updateUI(user);
                                    } else {
                                        // If sign in fails, display a message to the user.
                                        //Log.w(TAG, "signInWithEmail:failure", task.getException());
                                        if(task.getException() instanceof FirebaseAuthEmailException)
                                        {
                                            Toast.makeText(getActivity(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {
                                            Toast.makeText(getActivity(), "Contraseña Incorrecta", Toast.LENGTH_SHORT).show();
                                        }


                                    }
                                }
                            });
                }
                else
                {
                    Toast.makeText(getActivity(), "Email invalido", Toast.LENGTH_SHORT).show();
                    editText_email.setError("required");
                }

                //findNavController(v).navigate(R.id.action_inicioSesion_to_principalUsuario);
               //findNavController(v).navigate(R.id.action_inicioSesion_to_principalChofer);
            }
        });
        //listener para ingresar a tipo de usuario
        btnRegistrar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_inicioSesion_to_tipoUsuario));// metodo para pasar al siguiente fragment  sin validar

        return view;
    }

    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        /*FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser.isEmailVerified())
        {
            Toast.makeText(getActivity(),"Iniciando...",Toast.LENGTH_SHORT).show();
        }
        else
            {}*/


    }
    private void iniciar()
    {

    }




    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {//pesado spinner hace todo despues de que carga el layout facebook muestra el layout pero carga poco a poquito
        super.onViewCreated(view, savedInstanceState);
        Button btnRegistrar = view.findViewById(R.id.button_registrar);
        btnRegistrar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_inicioSesion_to_tipoUsuario));// metodo para pasar al siguiente fragment  sin validar
    }*/
}
