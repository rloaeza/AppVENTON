package com.mas_aplicaciones.appventon;


import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import static androidx.navigation.Navigation.findNavController;


public class firebase_conexion_firestore {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static Map<String, Object> datos = new HashMap<>();

    public void agregar_usuario(Map<String, Object> datos, String UUID) {

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
    public void agregar_chofer(Map<String, Object> datos, String UUID) {

        db.collection("Choferes").document(UUID)
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
    public void buscarUsuario(String UIDD, final View view) {
        DocumentReference docRef = FirebaseFirestore.getInstance().collection("Usuarios").document(UIDD);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists()) {
                        firebase_conexion_firestore.setMap(document.getData());

                        findNavController(view).navigate(R.id.action_inicioSesion_to_principalUsuario);


                    }
                }
            }
        });
    }
    public void buscarChofer(String UIDD, final View view) {
        final DocumentReference docRef = FirebaseFirestore.getInstance().collection("Choferes").document(UIDD);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>()
        {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    assert document != null;
                    if (document.exists())
                    {

                        firebase_conexion_firestore.setMap(document.getData());
                        if((boolean)document.getData().get("validacion"))
                        {
                            Toast.makeText(view.getContext(), "Iniciando...", Toast.LENGTH_SHORT).show();
                            findNavController(view).navigate(R.id.action_inicioSesion_to_principalChofer);
                        }
                        else
                        {
                            Toast.makeText(view.getContext(),"Tus datos estan siendo validados en la organización "+document.getData().get("Organización"),Toast.LENGTH_LONG).show();
                        }


                        //vaciar();

                    }

                }
            }
        });
    }
    public static Object getValue(String Key) {

        return datos.get(Key);
    }
    public static void ClearMap() {

        datos.clear();
    }
    public static void setMap(Map<String,Object> setData)
    {
        datos=setData;
    }



}
