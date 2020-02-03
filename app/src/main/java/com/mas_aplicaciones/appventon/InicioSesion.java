package com.mas_aplicaciones.appventon;



import android.app.AlertDialog;
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
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseUser;
import com.mas_aplicaciones.appventon.firebase.EvaluacionDeViews;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import java.util.Objects;
import dmax.dialog.SpotsDialog;
import static androidx.navigation.Navigation.findNavController;

/**
 *
 * A simple {@link Fragment} subclass.
 */
public class InicioSesion extends Fragment {

    // Initialize Firebase Auth
    public static FirebaseAuth mAuth ;
    private ImageButton imageButton_about;
    private FirebaseUser currentUser;
    private View view;
    private AlertDialog alertDialog;
    private EditText editText_email,editText_contrasena;
    private String email,contrasena;
    private EvaluacionDeViews objeto_evaluacion_de_views = new EvaluacionDeViews();
    private FirebaseConexionFirestore objeto_firebase_conexion_firestore = new FirebaseConexionFirestore();
    private boolean click=false;


    private boolean isNetDisponible() {

        getActivity();
        ConnectivityManager connectivityManager = (ConnectivityManager) Objects.requireNonNull(getActivity()).getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo actNetInfo = connectivityManager.getActiveNetworkInfo();

        return (actNetInfo != null && actNetInfo.isConnected());
    }
    private Boolean isOnlineNet()
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
            MaterialCardView btnRegistrar = view.findViewById(R.id.button_registrar);
            MaterialCardView btnOlvidaContrasena = view.findViewById(R.id.button_olvidar_contraseña);
            imageButton_about = view.findViewById(R.id.imageButton_about);
            MaterialCardView btnIniciarSesion = view.findViewById(R.id.button_iniciar);
            //listener para entrar al tipo usuario

            btnIniciarSesion.setOnClickListener(v ->
            {//acomoda y luego muestra y realiza todo lo necesario validad
                    if(!click)
                    {
                        imageButton_about.setVisibility(View.INVISIBLE);
                        if (isNetDisponible() && isOnlineNet())
                        {
                            imageButton_about.setVisibility(View.INVISIBLE);
                            email = editText_email.getText().toString();
                            contrasena = editText_contrasena.getText().toString();
                            if (objeto_evaluacion_de_views.emailValidado(email))
                            {
                                imageButton_about.setVisibility(View.INVISIBLE);
                                if(!contrasena.equals("")) {
                                    imageButton_about.setVisibility(View.INVISIBLE);
                                    btnRegistrar.setEnabled(false);
                                    btnOlvidaContrasena.setEnabled(false);
                                    mAuth.signInWithEmailAndPassword(email, contrasena)
                                            .addOnCompleteListener(Objects.requireNonNull(getActivity()), task -> {
                                                if (task.isSuccessful()) {
                                                    alertDialog = new SpotsDialog.Builder().setContext(view.getContext()).setMessage("Iniciando").build();
                                                    alertDialog.show();
                                                    // Sign in success, update UI with the signed-in user's information
                                                    currentUser = mAuth.getCurrentUser();
                                                    guardarPreferencias();
                                                    if (currentUser != null /*&& currentUser.isEmailVerified()*/) {

                                                        objeto_firebase_conexion_firestore.buscarUsuario(currentUser.getUid(), view);
                                                        objeto_firebase_conexion_firestore.buscarChofer(currentUser.getUid(), view);

                                                        editText_contrasena.setText("");
                                                        editText_email.setText("");
                                                    } else {
                                                        Toast.makeText(getActivity(), "Email no validado, revisa el email registrado", Toast.LENGTH_LONG).show();
                                                    }

                                                } else {
                                                    // If sign in fails, display a message to the user.
                                                    if (task.getException() instanceof FirebaseAuthEmailException) {
                                                        Toast.makeText(getActivity(), "Usuario no registrado", Toast.LENGTH_SHORT).show();
                                                    } else {
                                                        Toast.makeText(getActivity(), "Email o contraseña incorrectos", Toast.LENGTH_SHORT).show();
                                                    }


                                                }
                                            });
                                    click = true;
                                }
                                else
                                {
                                    Toast.makeText(getActivity(), "Contraseña vacia", Toast.LENGTH_SHORT).show();
                                    editText_contrasena.setError("required");
                                    imageButton_about.setVisibility(View.VISIBLE);
                                }

                            }
                            else
                            {
                                Toast.makeText(getActivity(), "Email invalido", Toast.LENGTH_SHORT).show();
                                editText_email.setError("required");
                                imageButton_about.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            Toast.makeText(getActivity(), "Compruebe su conexión de Internet", Toast.LENGTH_SHORT).show();
                            imageButton_about.setVisibility(View.VISIBLE);
                        }

                    }
                    else
                    {
                        Snackbar.make(Objects.requireNonNull(getView()),"Ingresando al sistema espere...",Snackbar.LENGTH_LONG).show();
                        imageButton_about.setVisibility(View.VISIBLE);
                    }

            });
            //listener para ingresar a tipo de usuario

                btnRegistrar.setOnClickListener(v ->
                {
                    if(isOnlineNet() && isNetDisponible())
                    {

                        findNavController(v).navigate(R.id.action_inicioSesion_to_tipoUsuario);// metodo para pasar al siguiente fragment  sin validar
                    }
                    else
                    {
                        Toast.makeText(getActivity(),"Compruebe su conexión de Internet",Toast.LENGTH_SHORT).show();
                    }
                });
                btnOlvidaContrasena.setOnClickListener(v->
                {
                        Navigation.findNavController(v).navigate(R.id.action_inicioSesion_to_recuperacionPassword);
                });
                imageButton_about.setOnClickListener(v -> Navigation.findNavController(v).navigate(R.id.action_inicioSesion_to_about));
            return view;
    }

    @Override
    public void onStart()
    {
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

    private void guardarPreferencias()
    {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("credenciales" , Context.MODE_PRIVATE); //nombre de mi file y el modo de visualizacion
        //asignarle los datos al archivo credenciales
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("usuario",editText_email.getText().toString());
        editor.putString("password",editText_contrasena.getText().toString());
        editor.apply();
    }
    private void leerPreferencias()
    {
        SharedPreferences sharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("credenciales" , Context.MODE_PRIVATE); //nombre de mi file y el modo de visualizacion
        if(!Objects.equals(sharedPreferences.getString("usuario", "null"), "null") && !Objects.equals(sharedPreferences.getString("password", "null"), "null")) {
            String usuario = sharedPreferences.getString("usuario", "null");
            String password = sharedPreferences.getString("password", "null");
            assert usuario != null;
            assert password != null;
            mAuth.signInWithEmailAndPassword(usuario, password)
                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                alertDialog = new SpotsDialog.Builder().setContext(view.getContext()).setMessage("Iniciando").build();
                                alertDialog.show();
                                currentUser = mAuth.getCurrentUser();
                                if (currentUser != null /*&& currentUser.isEmailVerified()*/) {
                                    objeto_firebase_conexion_firestore.buscarUsuario(currentUser.getUid(), view);
                                    objeto_firebase_conexion_firestore.buscarChofer(currentUser.getUid(), view);
                                    editText_contrasena.setText("");
                                    editText_email.setText("");
                                    editText_email.requestFocus();
                                }
                                else
                                {
                                    alertDialog.cancel();
                                   // Toast.makeText(getActivity(), "Revice su correo electrónico para validar el email",Toast.LENGTH_LONG).show();
                                }
                            }
                            else
                            {
                               // Toast.makeText(getActivity(), "Email no validado, revisa el email registrado",Toast.LENGTH_LONG).show();
                            }

                        }
                    });
        }
        else
        {
            imageButton_about.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onPause() {
        super.onPause();
        if(alertDialog!=null)
        {
            alertDialog.cancel();
        }
    }
    /*@Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {//pesado spinner hace todo despues de que carga el layout facebook muestra el layout pero carga poco a poquito
        super.onViewCreated(view, savedInstanceState);
        Button btnRegistrar = view.findViewById(R.id.button_registrar);
        btnRegistrar.setOnClickListener(Navigation.createNavigateOnClickListener(R.id.action_inicioSesion_to_tipoUsuario));// metodo para pasar al siguiente fragment  sin validar
    }*/
}
