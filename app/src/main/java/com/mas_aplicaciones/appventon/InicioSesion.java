package com.mas_aplicaciones.appventon;



import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseUser;
import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;

/**
 * A simple {@link Fragment} subclass.
 */
public class InicioSesion extends Fragment {

    // Initialize Firebase Auth
    public static FirebaseAuth mAuth ;
    private FirebaseUser currentUser;
    private View view;
    private EditText editText_email,editText_contrasena;
    private String email,contrasena;
    evaluacion_de_views objeto_evaluacion_de_views = new evaluacion_de_views();
    firebase_conexion_firestore objeto_firebase_conexion_firestore = new firebase_conexion_firestore();







    private boolean isNetDisponible() {

        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(getActivity().CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
    public Boolean isOnlineNet()
    {

        try {
            Process p = java.lang.Runtime.getRuntime().exec("ping -c 1 www.google.com");

            int val = p.waitFor();
            return (val == 0);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }



    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {



            if(getActivity() instanceof MainActivity)
            {
                ((MainActivity) getActivity()).activado(1);
            }
            mAuth = FirebaseAuth.getInstance() ;


            // Inflate the layout for this fragment
            view = inflater.inflate(R.layout.fragment_inicio_sesion, container, false);
            editText_email = view.findViewById(R.id.edit_text_email);
            editText_contrasena = view.findViewById(R.id.edit_text_contrasena2);
            Button btnRegistrar = view.findViewById(R.id.button_registrar);
            final Button btnIniciarSesion = view.findViewById(R.id.button_iniciar);
            //listener para entrar al tipo usuario

            btnIniciarSesion.setOnClickListener(new View.OnClickListener() {//acomoda y luego muestra y realiza todo lo necesario validad
                @Override
                public void onClick( final View v) {
                    if(isNetDisponible() && isOnlineNet())
                    {
                        email = editText_email.getText().toString();
                        contrasena = editText_contrasena.getText().toString();
                        if (objeto_evaluacion_de_views.emailValidado(email))
                        {
                            mAuth.signInWithEmailAndPassword(email, contrasena)
                                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful())
                                            {
                                                // Sign in success, update UI with the signed-in user's information
                                                currentUser = mAuth.getCurrentUser();
                                                guardarPreferencias();
                                                if(currentUser!= null && currentUser.isEmailVerified())
                                                {

                                                    objeto_firebase_conexion_firestore.buscarUsuario(currentUser.getUid(),view);
                                                    objeto_firebase_conexion_firestore.buscarChofer(currentUser.getUid(),view);
                                                    editText_contrasena.setText("");
                                                    editText_email.setText("");
                                                }
                                                else
                                                {
                                                    Toast.makeText(getActivity(), "Email no validado, revisa el email registrado",Toast.LENGTH_LONG).show();
                                                }

                                            }

                                            else
                                            {
                                                // If sign in fails, display a message to the user.
                                                if (task.getException() instanceof FirebaseAuthEmailException) {
                                                    Toast.makeText(getActivity(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                                }


                                            }
                                        }
                                    });
                        }
                        else {
                            Toast.makeText(getActivity(), "Email invalido", Toast.LENGTH_SHORT).show();
                            editText_email.setError("required");
                        }
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Compruebe su conexión de Internet",Toast.LENGTH_SHORT).show();
                    }
                }
            });
            //listener para ingresar a tipo de usuario

                btnRegistrar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick( View v) {
                        if(isOnlineNet() && isNetDisponible())
                        {

                            findNavController(v).navigate(R.id.action_inicioSesion_to_tipoUsuario);// metodo para pasar al siguiente fragment  sin validar
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Compruebe su conexión de Internet",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            return view;
    }



    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
            if(isNetDisponible() && isOnlineNet())
            {
                leerPreferencias();
            }
            else
            {
                Toast.makeText(getActivity(),"Compruebe su conexión de Internet",Toast.LENGTH_SHORT).show();
            }
    }



    private void guardarPreferencias() {


        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales" , Context.MODE_PRIVATE); //nombre de mi file y el modo de visualizacion
        //asignarle los datos al archivo credenciales
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario",editText_email.getText().toString());
        editor.putString("password",editText_contrasena.getText().toString());
        editor.commit();

    }
    private void leerPreferencias()
    {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("credenciales" , Context.MODE_PRIVATE); //nombre de mi file y el modo de visualizacion
        if(!sharedPreferences.getString("usuario","null").equals("null") && !sharedPreferences.getString("password","null").equals("null")) {
            String usuario = sharedPreferences.getString("usuario", "null");
            String password = sharedPreferences.getString("password", "null");
            mAuth.signInWithEmailAndPassword(usuario, password)
                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                currentUser = mAuth.getCurrentUser();


                                if (currentUser != null && currentUser.isEmailVerified()) {

                                    objeto_firebase_conexion_firestore.buscarUsuario(currentUser.getUid(), view);
                                    objeto_firebase_conexion_firestore.buscarChofer(currentUser.getUid(), view);
                                    editText_contrasena.setText("");
                                    editText_email.setText("");
                                }
                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Email no validado, revisa el email registrado",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }

    }





    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {//pesado spinner hace todo despues de que carga el layout facebook muestra el layout pero carga poco a poquito
        super.onViewCreated(view, savedInstanceState);
        Button btnRegistrar = view.findViewById(R.id.button_registrar);
        btnRegistrar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_inicioSesion_to_tipoUsuario));// metodo para pasar al siguiente fragment  sin validar
    }*/
}
