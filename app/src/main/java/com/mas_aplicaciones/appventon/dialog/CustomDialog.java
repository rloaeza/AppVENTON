package com.mas_aplicaciones.appventon.dialog;


import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.mas_aplicaciones.appventon.R;

import java.util.Objects;


public class CustomDialog {


    public static void show(final Context context, int idTituloError, int idMensajeError)
    {
        final android.app.Dialog dialog = new android.app.Dialog(context);
        dialog.setCancelable(false);
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setContentView(R.layout.dialog_design);
        dialog.show();
        View btnAceptar = dialog.findViewById(R.id.btn_aceptar_error);
        TextView txtTitulo = dialog.findViewById(R.id.textView_tituloEror);

        TextView txtMensaje = dialog.findViewById(R.id.textView_mensajeError);

        txtTitulo.setText(idTituloError);
        txtMensaje.setText(idMensajeError);

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnAceptar.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                Button botonAyuda = (Button) v;
                switch(event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        botonAyuda.setTextColor(context.getResources().getColor(R.color.colorBotones));
//                        botonAyuda.setBackgroundColor(Color.parseColor("#FF6634"));
                        botonAyuda.setBackgroundColor(context.getResources().getColor(R.color.colorPrimary));

                        return false;
                    case MotionEvent.ACTION_UP:
                        botonAyuda.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                        botonAyuda.setBackground(context.getResources().getDrawable(R.drawable.transparent_button_inactive));

                        return false;
                }
                return false;

            }
        });
    }



}
