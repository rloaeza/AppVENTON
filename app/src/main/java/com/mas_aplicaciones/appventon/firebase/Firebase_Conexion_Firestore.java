package com.mas_aplicaciones.appventon.firebase;


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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static androidx.navigation.Navigation.findNavController;


public class Firebase_Conexion_Firestore {
    private static FirebaseFirestore db = MainActivity.db;

    private static Map<String, Object> datos = new HashMap<>();
    private static ArrayList<String> numeroControlUsuarios = new ArrayList<>();
    private static ArrayList<String> numeroControlChoferes = new ArrayList<>();

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
                        Firebase_Conexion_Firestore.setMap(document.getData());

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

                        Firebase_Conexion_Firestore.setMap(document.getData());
                        if((boolean) Objects.requireNonNull(document.getData()).get("validacion"))
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
    //verifica si el numero de control existe en el sistema en Usuarios
    public static void getNumeroControlUsuarios()
    {

        db.collection("Usuarios")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult()))
                                numeroControlUsuarios.add(Objects.requireNonNull(document.getData().get("NumeroControl")).toString());

                            
                        
                        }
                        
                    }
                });

    }
    //verifica si el numero de control existe en el sistema en Choferes
    public static  void getNumeroControlChoferes()
    {


        db.collection("Choferes")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful())
                        {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                numeroControlChoferes.add(document.getData().get("NumeroControl").toString());
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
    public static boolean existKeyInMap(String key)
    {
        boolean b = false;
        if(datos.containsKey(key))
        {
            b=true;

        }
        return b ;

    }



}
