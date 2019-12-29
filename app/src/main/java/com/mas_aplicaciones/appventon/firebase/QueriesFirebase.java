package com.mas_aplicaciones.appventon.firebase;

import android.provider.DocumentsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mas_aplicaciones.appventon.MainActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueriesFirebase
{
    private static FirebaseFirestore db = MainActivity.db;
    private static Map<String, Object> datos = new HashMap<>();
    public static boolean BuscarDocumento(String email, final String carrera, final String telefono, final String NumeroControl,String tipo_usuario)
    {

        final boolean[] key = {false};
        CollectionReference Collection = db.collection(tipo_usuario);

        // Create a query against the collection.
        Collection.whereEqualTo("Email", email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    Log.e("entre","sdfghj");
                    if(task.getResult().size()==1)
                    {

                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                           datos=document.getData();
                           if(document.getData().get("Carrera").toString().equals(carrera) && document.getData().get("Teléfono").toString().equals(telefono) && document.getData().get("NumeroControl").toString().equals(NumeroControl)){
                               key[0] =true;
                               Log.e("entre",document.getData().get("Contraseña").toString());
                           }
                        }
                    }
                    else
                    {
                        key[0] =false;
                    }


                }
                else
                {
                    key[0] =false;
                }
            }
        });


        return key[0];
    }
    public static boolean BuscarNumControl(final String NumeroControl, String Collections)
    {

        final boolean[] key = {false};
        CollectionReference Collection = db.collection(Collections);

        // Create a query against the collection.
        Collection.whereEqualTo("NumeroControl", NumeroControl).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful())
                {
                    if(task.getResult().size()==1)
                    {



                        for (QueryDocumentSnapshot document : task.getResult())
                        {
                            datos=document.getData();
                            if(document.getData().get("NumeroControl").toString().equals(NumeroControl))
                            {
                                Log.e("valor",document.getData().get("Email").toString());
                                key[0] =true;
                            }
                        }
                    }
                    else
                    {
                        key[0] =false;
                        Log.e("valor",String.valueOf(key[0]));
                    }


                }
                else
                {
                    key[0] =false;
                }
            }
        });


        return key[0];
    }
    public static void ClearMap() {

        datos.clear();
    }
    public static Object getValue(String Key) {

        return datos.get(Key);
    }
}
