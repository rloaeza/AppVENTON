package com.mas_aplicaciones.appventon;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;
import com.google.android.material.snackbar.Snackbar;
import com.mas_aplicaciones.appventon.firebase.EvaluacionDeViews;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.firebase.QueriesFirebase;
import com.mas_aplicaciones.appventon.staticresources.StaticResources;
import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import dmax.dialog.SpotsDialog;


/**
 * A simple {@link Fragment} subclass.
 */
public class RecuperacionPassword extends Fragment {


    private EditText editText_email,editText_telefono,editText_numero_control;
    private RadioButton radioButton_prestado_servicios, radioButton_pasajero;
    private Spinner spinner_carrera;
    private Button button_recuperar;
    private String email, telefono,numeroControl,carrera,tipo_usuario;
    private EvaluacionDeViews evaluacionDeViews = new EvaluacionDeViews();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).activado(3);
        }
        // Inflate the layout for this fragment
        final View view = inflater.inflate(R.layout.fragment_recuperacion_password, container, false);
        editText_email = view.findViewById(R.id.edit_text_email);
        editText_numero_control= view.findViewById(R.id.edit_text_num_control);
        editText_telefono = view.findViewById(R.id.edit_text_telefono);
        radioButton_prestado_servicios = view.findViewById(R.id.radioButton_prestador_servicios);
        radioButton_pasajero = view.findViewById(R.id.radioButton_pasajero);
        spinner_carrera = view.findViewById(R.id.spinner_selecCarrera);
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(),R.layout.spinner_item_values_2, StaticResources.OPCIONES_CARRERAS);
        spinner_carrera.setAdapter(arrayAdapter);
        button_recuperar = view.findViewById(R.id.button_recuperar_cuenta);


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
                                if(!radioButton_pasajero.isChecked() || !radioButton_prestado_servicios.isChecked())
                                {
                                    carrera = spinner_carrera.getSelectedItem().toString();
                                    tipo_usuario=(radioButton_prestado_servicios.isChecked())?"Choferes":"Usuarios";

                                    if(!QueriesFirebase.BuscarDocumento(email.trim(),carrera,telefono,numeroControl,tipo_usuario))
                                    {

                                      Toast.makeText(getContext(),"Se enviará un correo a el email con las credenciales",Toast.LENGTH_SHORT).show();
                                      sendEmailWithGmail2(StaticResources.EMAILSENDER,StaticResources.PASSWORD,"vicente_prez@hotmail.com","","");
                                    }

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



    private void sendEmailWithGmail2(final String recipientEmail, final String recipientPassword,String to, String subject, String message) {
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

        SenderAsyncTask2 task = new SenderAsyncTask2(session, recipientEmail, subject, message);
        task.execute();
    }
    /**
     * AsyncTask to send email
     */
    class SenderAsyncTask2 extends AsyncTask<String, String, String> {

        private String from, to, subject, message;
        private ProgressDialog progressDialog;
        private Session session;

        public SenderAsyncTask2(Session session, String from, String subject, String message) {
            this.session = session;
            this.from = from;
            this.to = QueriesFirebase.getValue("Email").toString();
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
                                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">" + "Tus credenciales <strong>" + QueriesFirebase.getValue("Nombre") + " " + QueriesFirebase.getValue("Apellidos") +
                                "</strong> son:" + "</font></p>" +
                                "<p ALIGN=\"center\"><font size=3 face=\"Sans Serif,arial,verdana\">" + "<br><strong>"+to+"<br>"+QueriesFirebase.getValue("Contraseña")+"</strong>" + "</font></p>" +
                                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Si tú no solisitaste esto, no tienes por que preocuparte tus datos están protegidos</p>" +
                                "<p ALIGN=\"justify\"><font size=3 face=\"Sans Serif,arial,verdana\">Saludos cordiales,</font></p>" +
                                "<p><font size=3 face=\"Sans Serif,arial,verdana\">El equipo </font><font color=\"#008577\" size=3 face=\"Sans Serif,arial,verdana\">AppVenton</font></p>" +
                                "<br>" +
                                "<hr>" +
                                "<footer>" +
                                "<p><font color=\"#C5BFBF\" size=2 face=\"Sans Serif,arial,verdana\">Gracias!!</font></p>" +
                                "<p ALIGN=\"justify\"><font color=\"#C5BFBF\" size=1 face=\"Sans Serif,arial,verdana\">©AppVenton from Instituto Tecnológico Superior de Uruapan, Carretera Uruapan-Carapan No. 5555 Col. La Basilia Uruapan, Michoacán. Este correo fue enviado para: "+FirebaseConexionFirestore.getValue("Email")+" y fue enviado por AppVenton</font></p>" +
                                "</footer>" +
                                "</body>";
                mimeMessage.setContent(htmlText, "text/html; charset=utf-8");



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