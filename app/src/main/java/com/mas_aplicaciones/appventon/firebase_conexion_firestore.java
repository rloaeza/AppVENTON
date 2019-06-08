package com.mas_aplicaciones.appventon;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;


public class firebase_conexion_firestore
{
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static Map<String,Object> datos = new HashMap<>();

    public void agregar_usuario(Map<String,Object> datos,String UUID)
    {
        Object valores[] =  datos.values().toArray();
        System.out.println();
        for( int i=0;i<valores.length;i++)
        {
            System.out.println("valores: "+valores[i].toString());
        }
        db.collection("Usuarios").document(UUID)
                .set(datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        //Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                       // Log.w(TAG, "Error writing document", e);
                    }
                });


    }
    /*public void getNombre(String id)
    {
        DocumentReference docRef = db.collection("Usuarios").document(id);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        //Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        datos=document.getData();
                    } else {
                        //Log.d(TAG, "No such document");
                    }
                } else {
                   // Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

    }
    public Object getValue(String Key)
    {
        return datos.get(Key);
    }*/
}
