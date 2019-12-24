package com.mas_aplicaciones.appventon.menu;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;


import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;


/**
 * A simple {@link quejas} subclass.
 * Activities that contain this fragment must implement the
 * {@link } interface
 * to handle interaction events.
 * Use the {@link } factory method to
 * create an instance of this fragment.
 */
public class quejas extends Fragment {
    private RadioButton radioButton1,radioButton2;
    private Button button_enviar;
    private EditText editText_queja;
    private TextView textView_emailreceiver;
    private final String EMAILSENDER ="appventonitsu@gmail.com";
    private final String SERVERDEFULT ="gmail.com";
    private final String PASSWORD ="AppVenton1234";
    private String user,subject,body;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if(getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_quejas, container, false);

        button_enviar =view.findViewById(R.id.button_enviar_queja);
        textView_emailreceiver = view.findViewById(R.id.textView_Email);
        editText_queja = view.findViewById(R.id.edit_text_mensaje);
        radioButton1 = view.findViewById(R.id.radioButton1);
        radioButton2 = view.findViewById(R.id.radioButton2);
        textView_emailreceiver.setText(FirebaseConexionFirestore.getValue("Email").toString());
        button_enviar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                user=textView_emailreceiver.getText().toString();

                subject=(radioButton1.isChecked())?"Queja":"Sugerencia";
                body =editText_queja.getText().toString();

                // Perform action on click

                //Snackbar.make(getView(),"Hola",Snackbar.LENGTH_SHORT).show();


                sendEmailWithGmail(EMAILSENDER,PASSWORD,user,subject,body);//sdcard/DCIM/Camera/test.jpg

            }
        });

        return view;
    }

    private void sendEmailWithGmail(final String recipientEmail, final String recipientPassword,String to, String subject, String message) {
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

        SenderAsyncTask task = new SenderAsyncTask(session, recipientEmail, to, subject, message);
        task.execute();
    }

    /**
     * AsyncTask to send email
     */
    class SenderAsyncTask extends AsyncTask<String, String, String> {

        private String from, to, subject, message;
        private ProgressDialog progressDialog;
        private Session session;

        public SenderAsyncTask(Session session, String from, String to, String subject, String message) {
            this.session = session;
            this.from = from;
            this.to = to;
            this.subject = subject;
            this.message = message;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(getActivity(), "Enviando", "Espere", true);
            progressDialog.setCancelable(false);
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                Message mimeMessage = new MimeMessage(session);
                mimeMessage.setFrom(new InternetAddress(from));
                mimeMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
                mimeMessage.setSubject("Gracias por ayudarnos a mejorar AppVenton");
                String htmlText2 = "<p ALIGN=\"center\"><img  width=\"200\" height=\"200\" src=\"https://firebasestorage.googleapis.com/v0/b/appventonitecsu.appspot.com/o/icono2.png?alt=media&token=1389b4bb-2ced-4d1c-896e-ab4aa714cbca\"></p>";
                String htmlText =
                         "<body> " +
                                "<h4><font size=3 face=\"Sans Serif,arial,verdana\">Hola, </font></h4> " +
                                 "<br>"+
                                 htmlText2+
                                "<hr>" +
                                 "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">"+"Agradecemos tu colaboración <strong>"+FirebaseConexionFirestore.getValue("Nombre")+" "+FirebaseConexionFirestore.getValue("Apellidos")+
                                 "</strong> evaluaremos la petición y tendremos pronta respuesta a tu petición"+"</font></p>"+
                                 "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Saludos cordiales,</font></p>"+
                                 "<p><font size=3 face=\"Sans Serif,arial,verdana\">El equipo </font><font color=\"#008577\" size=3 face=\"Sans Serif,arial,verdana\">AppVenton</font></p>"+
                                 "<br>"+
                                 "<hr>"+

                                    "<footer>"+
                                        "<p><font color=\"#C5BFBF\" size=2 face=\"Sans Serif,arial,verdana\">Gracias!!</font></p>"+
                                        "<p ALIGN=\"justify\"><font color=\"#C5BFBF\" size=1 face=\"Sans Serif,arial,verdana\">©AppVenton from Instituto Tecnológico Superior de Uruapan, Carretera Uruapan-Carapan No. 5555 Col. La Basilia Uruapan, Michoacán.</font></p>"+
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
                                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">"+"Petición de <strong>"+FirebaseConexionFirestore.getValue("Nombre")+" "+FirebaseConexionFirestore.getValue("Apellidos")+" "+FirebaseConexionFirestore.getValue("Email")+
                                "</strong>, la cual menciona que: "+"</font></p>"+
                                "<p ALIGN=\"center\"><font size=3 face=\"Sans Serif,arial,verdana\">"+message+"</font></p>"+
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
            Snackbar.make(getView(), "Mensaje enviado...", Snackbar.LENGTH_LONG).show();

        }
    }






}
