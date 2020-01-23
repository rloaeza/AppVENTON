package com.mas_aplicaciones.appventon.usuario;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.chofer.RegistroChoferOrganizacionAuto;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;
import com.mas_aplicaciones.appventon.storagefirebase.StorageFirebase;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import static androidx.navigation.Navigation.findNavController;
import static com.mas_aplicaciones.appventon.InicioSesion.mAuth;


/**
 * A simple {@link Fragment} subclass.
 */
public class RegistroUsuarioOrganizacion extends Fragment {

    private static Map<String, Object> data = new HashMap<>();

    private Spinner spinner_genero,spinner_carreras;
    private FirebaseConexionFirestore conexion=new FirebaseConexionFirestore();
    private StorageFirebase storageFirebase = new StorageFirebase();
    private final static int GALLERY_INTENT = 1;


    public static void setValueMap(String key, Object value)
    {
        data.put(key,value);
    }
    public static Object getValueMap(String key)
    {
        return data.get(key);
    }



    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(3);

        }

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.registro_usuario_organizacion, container, false);
        //sipnner genero
        spinner_genero = view.findViewById(R.id.spinner_selecGen);
        //spinner carrera
        spinner_carreras = view.findViewById(R.id.spinner_selecCarrera);
        ArrayAdapter<String> adapter_genero = new ArrayAdapter<>(Objects.requireNonNull(getActivity()), R.layout.spinner_item_values, StaticResources.OPCIONES_GENERO);
        ArrayAdapter<String> adapter_carreras = new ArrayAdapter<>(getActivity(), R.layout.spinner_item_values, StaticResources.OPCIONES_CARRERAS);
        spinner_genero.setAdapter(adapter_genero);
        spinner_carreras.setAdapter(adapter_carreras);

        MaterialCardView btnRegistrar = view.findViewById(R.id.button_registrar);
        MaterialCardView btnSubirFotoUsuario = view.findViewById(R.id.button_subir_foto_usuario);


        //button action subir foto
        btnSubirFotoUsuario.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( final View v)
            {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"),GALLERY_INTENT);
            }
        });


        //button action registrar
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
                        data.put("LastDate", Calendar.getInstance().getTime());
                        data.put("Saldo",0.0);
                        data.put("Viaje","");
                        //si la imagen fue agregada
                        if(StorageFirebase.getImagenSubida())
                        {
                            mAuth.createUserWithEmailAndPassword(Objects.requireNonNull(data.get("Email")).toString(), Objects.requireNonNull(data.get("Contraseña")).toString())
                                    .addOnCompleteListener(Objects.requireNonNull(getActivity()), new OnCompleteListener<AuthResult>() {
                                        @Override
                                        public void onComplete(@NonNull Task<AuthResult> task) {
                                            if (task.isSuccessful()) {
                                                FirebaseUser user = mAuth.getCurrentUser();
                                                //mensaje de verificación

                                                /*try {
                                                    assert user != null;
                                                    user.sendEmailVerification();
                                                } catch (NullPointerException e) {
                                                    e.printStackTrace();
                                                }*/


                                                //agrega los datos a usuarios y le asigna el mismo UID de la autentificación a los datos de este.
                                                conexion.agregar_usuario(data, user.getUid());
                                                Toast.makeText(getActivity(), "Checar correo electrónico para validar su correo", Toast.LENGTH_SHORT).show();
                                                sendEmailWithGmail(StaticResources.PASSWORD,user.getEmail(),user.getUid(),"");//sdcard/DCIM/Camera/test.jpg


                                                findNavController(v).navigate((R.id.action_registroUsuario_organizacion_to_inicioSesion2));


                                            } else {
                                                // If sign in fails, display a message to the user.
                                                //si el registro de usuario genera una colision, esto es que ya existe un email registrado con esa dir
                                                if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                                    Toast.makeText(getActivity(), "Ese Email ya fue registrado", Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Toast.makeText(getActivity(), "Error de registro, sin acceso a Internet", Toast.LENGTH_SHORT).show();
                                                    data.clear();
                                                    findNavController(v).navigate((R.id.action_registroUsuario_organizacion_to_inicioSesion2));
                                                }
                                            }
                                        }
                                    });
                        }
                        else
                        {
                            Toast.makeText(getActivity(),"Debe de subir imagen",Toast.LENGTH_SHORT).show();
                        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==GALLERY_INTENT)
        {



            try {
                Uri uri = data.getData();
                storageFirebase.EliminarFoto(getValueMap("NumeroControl").toString(),"Usuarios",getView());
                assert  null != uri;
                InputStream  inputStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(uri);
                assert inputStream != null;
                if((Double.parseDouble(String.valueOf(inputStream.available()))/1024)<200.1)
                {
                    storageFirebase.agregarFoto(getValueMap("NumeroControl").toString(),uri,"Usuarios", Objects.requireNonNull(getView()),0);
                }
                else
                {
                    Toast.makeText(getActivity(), "La imagen debe que ser menor de 200.1 kb",Toast.LENGTH_LONG).show();
                }


            } catch (IOException | NullPointerException e) {
                e.printStackTrace();
            }


        }
    }
    private void sendEmailWithGmail(final String recipientPassword, String to, String subject, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(StaticResources.EMAILSENDER, recipientPassword);
            }
        });

        SenderAsyncTask task = new SenderAsyncTask(session, StaticResources.EMAILSENDER, to, subject, message);
        task.execute();
    }
    /**
     * AsyncTask to send email
     */
    @SuppressLint("StaticFieldLeak")
    class SenderAsyncTask extends AsyncTask<String, String, String> {

        private String from, to, subject, message;
        private ProgressDialog progressDialog;
        private Session session;

        SenderAsyncTask(Session session, String from, String to, String subject, String message) {
            this.session = session;
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Enviando mensaje", "Espere", true);
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(from,"AppVenton"));
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                mimeMessage.setSubject("Bienvenido a AppVenton");
                String htmlText2 = "<p ALIGN=\"center\"><img  width=\"200\" height=\"200\" src=\"https://firebasestorage.googleapis.com/v0/b/appventonitecsu.appspot.com/o/icono2.png?alt=media&token=1389b4bb-2ced-4d1c-896e-ab4aa714cbca\"></p>";
                String htmlText =
                        "<body> " +
                                "<h4><font size=3 face=\"Sans Serif,arial,verdana\">Hola, </font></h4> " +
                                "<br>"+
                                htmlText2+
                                "<hr>" +
                                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">"+"Bienvenido <strong>"+data.get("Nombre")+" "+data.get("Apellidos")+
                                "<p ALIGN=\"center\"><font size=3 face=\"Sans Serif,arial,verdana\"><strong>"+subject+"</strong></font></p>"+
                                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Saludos cordiales,</font></p>"+
                                "<p><font size=3 face=\"Sans Serif,arial,verdana\">El equipo </font><font color=\"#008577\" size=3 face=\"Sans Serif,arial,verdana\">AppVenton</font></p>"+
                                "<br>"+
                                "<hr>"+

                                "<footer>"+
                                "<p><font color=\"#C5BFBF\" size=2 face=\"Sans Serif,arial,verdana\">Gracias!!</font></p>"+
                                "<p ALIGN=\"justify\"><font color=\"#C5BFBF\" size=1 face=\"Sans Serif,arial,verdana\">©AppVenton from Instituto Tecnológico Superior de Uruapan, Carretera Uruapan-Carapan No. 5555 Col. La Basilia Uruapan, Michoacán. Este correo fue enviado para: "+data.get("Email")+" y fue enviado por AppVenton </font></p>"+
                                "</footer>"+
                                "</body>";




                mimeMessage.setContent(htmlText, "text/html; charset=utf-8");

                Transport.send(mimeMessage);
                Message mimeMessage2 = new MimeMessage(session);
                mimeMessage2.setFrom(new InternetAddress(from));
                mimeMessage2.setRecipients(Message.RecipientType.TO, InternetAddress.parse(from));
                mimeMessage2.setSubject(subject);
                //  String htmlText2 = "<p ALIGN=\"center\"><img  width=\"200\" height=\"200\" src=\"https://firebasestorage.googleapis.com/v0/b/appventonitecsu.appspot.com/o/icono2.png?alt=media&token=1389b4bb-2ced-4d1c-896e-ab4aa714cbca\"></p>";
                String htmlText3 = "<body> " +
                        "<hr>" +
                        "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">"+"Registro de <strong>"+data.get("Nombre")+" "+data.get("Apellidos")+" "+data.get("Email")+
                        "</strong>, identificador de usuario "+"</font></p>"+
                        "<br>"+
                        "<p ALIGN=\"center\"><font size=3 face=\"Sans Serif,arial,verdana\"><strong>"+subject+"</strong></font></p>"+
                        "<p><font size=3 face=\"Sans Serif,arial,verdana\">El equipo </font><font color=\"#008577\" size=3 face=\"Sans Serif,arial,verdana\">AppVenton</font></p>"+
                        "<br>"+
                        "<hr>"+

                        "<footer>"+
                        "<p><font color=\"#C5BFBF\" size=2 face=\"Sans Serif,arial,verdana\">Gracias!!</font></p>"+
                        "<p ALIGN=\"justify\"><font color=\"#C5BFBF\" size=1 face=\"Sans Serif,arial,verdana\">©AppVenton from Instituto Tecnológico Superior de Uruapan, Carretera Uruapan-Carapan No. 5555 Col. La Basilia Uruapan, Michoacán.</font></p>"+
                        "</footer>"+
                        "</body>";

                mimeMessage2.setContent(htmlText3, "text/html; charset=utf-8");

                Transport.send(mimeMessage2);
            } catch (Exception e) {
                e.printStackTrace();
                return e.getMessage();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(String... values) {
            super.onProgressUpdate(values);
            progressDialog.setMessage(values[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Snackbar.make(Objects.requireNonNull(getView()), "Mensaje enviado...", Snackbar.LENGTH_LONG).show();
            findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_registroChofer_organizacion_auto_to_inicioSesion);

        }
    }






}
