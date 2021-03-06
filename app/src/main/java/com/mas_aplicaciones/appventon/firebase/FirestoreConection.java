package com.mas_aplicaciones.appventon.firebase;


import android.app.AlertDialog;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mas_aplicaciones.appventon.EntidadChofer;
import com.mas_aplicaciones.appventon.Interfaces.Choferes;
import com.mas_aplicaciones.appventon.MainActivity;

import java.util.ArrayList;
import java.util.Calendar;

import dmax.dialog.SpotsDialog;


public class FirestoreConection
{
    Choferes choferes;
    FirebaseFirestore db = MainActivity.db;
    public  void obtenerChoferes(final String UUID, final Context context)
    {

        db.collection("Chofer_lugar")
                .whereEqualTo("IdLugar", UUID)
                .get()
                .addOnCompleteListener((Task<QuerySnapshot> task) -> {
                    if (task.isComplete())
                    {


                        new Handler().post(() -> {
                            try
                            {
                                choferes.getDatos(task.getResult());
                            }
                            catch (Exception ex){
                                ///
                            }


                        });

                        Log.d("err", task+"120");
                    }

                });
    }
    public void cambiosChoferes(final String UUID)
    {
        final ArrayList<EntidadChofer> entidadChoferListAdd = new ArrayList<>();
        final ArrayList<EntidadChofer> entidadChoferListMod = new ArrayList<>();
        final ArrayList<EntidadChofer> entidadChoferListDel = new ArrayList<>();
        final int[] opcion = {-1};
        db.collection("Chofer_lugar").whereEqualTo("IdLugar",UUID )
        .addSnapshotListener((value, e) -> {
            if (e != null) {
                    return;
                }
            entidadChoferListAdd.clear();
            entidadChoferListDel.clear();
            entidadChoferListMod.clear();
            for (DocumentChange documentChange : value.getDocumentChanges())
            {

                String id = documentChange.getDocument().getId();
                String idChofer = documentChange.getDocument().getData().get("IdChofer").toString();
                String nombreChofer=documentChange.getDocument().getData().get("Nombre").toString();
                String imagenChofer=documentChange.getDocument().getData().get("URI").toString();
                String imagenCoche = documentChange.getDocument().getData().get("URI_Coche").toString();
                String comentario = documentChange.getDocument().getData().get("Comentario").toString();
                String espacios = documentChange.getDocument().getData().get("Espacios").toString();
                String tiempoEspera=documentChange.getDocument().getData().get("TiempoEspera").toString();
                String hora = documentChange.getDocument().getData().get("Hora").toString();
                Timestamp timestamp = (Timestamp) documentChange.getDocument().getData().get("Fecha");
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(timestamp.toDate());
                String fecha= calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+1+"/"+calendar.get(Calendar.YEAR);
                String horaarray[] = hora.split(":");
                if(Integer.parseInt(horaarray[0])>12)
                {

                    hora=(Integer.parseInt(horaarray[0])-12)+":";
                }
                else
                {
                    hora=horaarray[0]+":";
                }
                if(Integer.parseInt(horaarray[1])<10)
                {
                    hora=hora+"0"+horaarray[1]+"pm";
                }else
                {
                    hora=hora+horaarray[1]+"pm";
                }

                EntidadChofer entidadChofer = new EntidadChofer();
                entidadChofer.setId(id);
                entidadChofer.setIdChofer(idChofer);
                entidadChofer.setNombre(nombreChofer);
                entidadChofer.setImagen(imagenChofer);
                entidadChofer.setImagen_Coche(imagenCoche);
                entidadChofer.setFecha("Fecha: "+fecha);

                entidadChofer.setHora("Hora: "+hora);
                if(!comentario.equals(""))
                {
                    entidadChofer.setComentario("Comentario:\n"+comentario);
                }else
                {
                    entidadChofer.setComentario("Ningún comentario disponible");
                }
                entidadChofer.setEspacio("Hay "+espacios+" espacios disponibles");
                entidadChofer.setTiempoEspera("Tiempo de espera en el lugar es de: "+tiempoEspera+" min");
                switch (documentChange.getType())
                {
                    case ADDED:
                        Log.d("err", "onEvent: add");
                        entidadChoferListAdd.add(entidadChofer);
                        opcion[0] =0;
                        break;
                    case MODIFIED:
                        Log.d("err", "onEvent: modify ");
                        entidadChoferListMod.add(entidadChofer);
                        opcion[0] =1;
                        break;
                    case REMOVED:
                        Log.d("err", "onEvent: remove ");
                        opcion[0]=2;
                        entidadChoferListDel.add(entidadChofer);
                        break;
                    default:
                        Log.d("err", "onEvent: nothing");
                        break;

                }
            }
            new Handler().postDelayed(() ->
            {
                try
                {
                    choferes.getUpdateData(entidadChoferListAdd,entidadChoferListMod,entidadChoferListDel, opcion[0]);
                }
                catch (Exception ex){
                    ///
                }
            },1000);

            });




    }
    public void setListenerCambiosChoferes(Choferes choferes)
    {
        this.choferes=choferes;
    }

}
