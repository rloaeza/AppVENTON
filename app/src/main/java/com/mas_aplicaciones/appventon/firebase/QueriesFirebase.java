package com.mas_aplicaciones.appventon.firebase;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.chofer.RegistroChoferOrganizacionAuto;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;
import com.mas_aplicaciones.appventon.usuario.RegistroUsuarioOrganizacion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dmax.dialog.SpotsDialog;

import static androidx.navigation.Navigation.findNavController;

public class QueriesFirebase
{
    private static FirebaseFirestore db = MainActivity.db;
    private static Map<String, Object> datos = new HashMap<>();
    private static View views;
    public void BuscarDocumento(final String email, final String carrera, final String telefono, final String NumeroControl, final String tipo_usuario, final View view)
    {


        CollectionReference Collection = db.collection(tipo_usuario);
        views=view;
        // Create a query against the collection.
        final AlertDialog alertDialog = new SpotsDialog.Builder().setContext(view.getContext()).setMessage("Evaluando Existencia...").build();
        alertDialog.show();
        Collection.whereEqualTo("Email", email).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>()
        {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {


                if(queryDocumentSnapshots.getDocuments().size()>0)
                {
                    datos=queryDocumentSnapshots.getDocuments().get(0).getData();
                    alertDialog.cancel();

                        if (datos.get("Teléfono").toString().equals(telefono) && datos.get("NumeroControl").toString().equals(NumeroControl) && datos.get("Carrera").toString().equals(carrera)) {
                            usar();
                        } else {
                            Toast.makeText(view.getContext(), "No existe registro con esos datos", Toast.LENGTH_SHORT).show();
                        }
                }
                else {
                    alertDialog.cancel();
                    Toast.makeText(view.getContext(), "No existe registro con esos datos", Toast.LENGTH_SHORT).show();
                }

            }

            private void usar()
            {
                sendEmailWithGmail2(StaticResources.EMAILSENDER,StaticResources.PASSWORD,datos.get("Email").toString(),"","");
            }
        });




    }



    public static void BuscarNumControl(final String NumeroControl, String Collections, final View view, final int i)
    {


        CollectionReference Collection = db.collection(Collections);
        final AlertDialog alertDialog = new SpotsDialog.Builder().setContext(view.getContext()).setMessage("Evaluando Datos en el sistema...").build();
        alertDialog.show();
        // Create a query against the collection.
        Collection.whereEqualTo("NumeroControl", NumeroControl).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                if(queryDocumentSnapshots.getDocuments().size()>0)
                {
                        alertDialog.cancel();
                        Toast.makeText(view.getContext(), "Número de control registrado. \n Debe de utilizar el suyo, si el alguien más está usando el suyo, repórtelo", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    alertDialog.cancel();
                    MainActivity.view=view;
                    MainActivity.NumeroControl=NumeroControl;
                    if (i == 1)//es registro usuario
                    {
                        RegistroUsuarioOrganizacion.setValueMap("NumeroControl", NumeroControl.trim());
                        MainActivity.Collection="Usuarios";
                        findNavController(view).navigate(R.id.action_registroUsuario_to_registroUsuario_organizacion);
                    } else // es registro chofer
                    {
                        RegistroChoferOrganizacionAuto.setValueMap("NumeroControl", NumeroControl.trim());
                        MainActivity.Collection="Choferes";
                        findNavController(view).navigate(R.id.action_registroChofer_to_registroChofer_organizacion_auto);
                    }
                }

            }

        });



    }

    public static void ClearMap() {

        datos.clear();
    }


    public  void sendEmailWithGmail2(final String recipientEmail, final String recipientPassword,String to, String subject, String message) {
        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.port", "465");

        Session session = Session.getDefaultInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(recipientEmail, recipientPassword);
            }
        });

        SenderAsyncTask2 task = new SenderAsyncTask2(session, recipientEmail,to);
        task.execute();
    }
    /**
     * AsyncTask to send email
     */
    class SenderAsyncTask2 extends AsyncTask<String, String, String> {

        private String from, to ;
        private ProgressDialog progressDialog;
        private Session session;

        public SenderAsyncTask2(Session session, String from,String to) {
            this.session = session;
            this.from = from;
            this.to=to;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(views.getContext(), "Enviando", "Espere", true);
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(from,"AppVenton"));
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                mimeMessage.setSubject("Recuperación de credenciales");
                String htmlText2 = "<p ALIGN=\"center\"><img  width=\"200\" height=\"200\" src=\"https://firebasestorage.googleapis.com/v0/b/appventonitecsu.appspot.com/o/icono2.png?alt=media&token=1389b4bb-2ced-4d1c-896e-ab4aa714cbca\"></p>";
                String htmlText =
                        "<body> " +
                                "<h4><font size=3 face=\"Sans Serif,arial,verdana\">Hola, </font></h4> " +
                                "<br>" +
                                htmlText2 +
                                "<hr>" +
                                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">" + "Tus credenciales <strong>" +datos.get("Nombre").toString() + " " + datos.get("Apellidos").toString()
                                +
                                "</strong> son:" + "</font></p>" +
                                "<p ALIGN=\"center\"><font size=3 face=\"Sans Serif,arial,verdana\">" + "<br><strong>"+to+"<br>"+datos.get("Contraseña").toString()+"</strong>" + "</font></p>" +
                                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Si tú no solisitaste esto no tienes porque preocuparte tus datos están protegidos</p>" +
                                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Saludos cordiales,</font></p>" +
                                "<p><font size=3 face=\"Sans Serif,arial,verdana\">El equipo </font><font color=\"#008577\" size=3 face=\"Sans Serif,arial,verdana\">AppVenton</font></p>" +
                                "<br>" +
                                "<hr>" +
                                "<footer>" +
                                "<p><font color=\"#C5BFBF\" size=2 face=\"Sans Serif,arial,verdana\">Gracias!!</font></p>" +
                                "<p ALIGN=\"justify\"><font color=\"#C5BFBF\" size=1 face=\"Sans Serif,arial,verdana\">©AppVenton from Instituto Tecnológico Superior de Uruapan, Carretera Uruapan-Carapan No. 5555 Col. La Basilia Uruapan, Michoacán. Este correo fue enviado para: "+to+" y fue enviado por AppVenton</font></p>" +
                                "</footer>" +
                                "</body>";
                mimeMessage.setContent(htmlText, "text/html; charset=utf-8");
                Transport.send(mimeMessage);


            } catch (MessagingException e) {
                e.printStackTrace();
                return e.getMessage();
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
            Toast.makeText(views.getContext(),
                    "Se enviará un correo al email encontrado en el sistema con las credenciales", Toast.LENGTH_LONG).show();
            ClearMap();
        }
    }
}
