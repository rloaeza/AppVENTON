package com.mas_aplicaciones.appventon.firebase;


import android.location.Location;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static androidx.navigation.Navigation.findNavController;


public class FirebaseConexionFirestore {
    private static FirebaseFirestore db = MainActivity.db;

    private static Map<String, Object> datos = new HashMap<>();
    public static List<Feature> features = new ArrayList<>();
    private static ArrayList<String> numeroControlUsuarios = new ArrayList<>();
    public static String PERSONA;//almacena el valor de la coleccion donde encontro las credenciales.
    public static String DOCUMENT;//almacena el valor de la coleccion donde encontro las credenciales.
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
                    if (document.exists())
                    {
                        PERSONA="Usuarios";
                        DOCUMENT=document.getId();
                        FirebaseConexionFirestore.setMap(document.getData());
                        features=cargarLugares();
                       // Toast.makeText(view.getContext(), "Iniciando... ", Toast.LENGTH_SHORT).show();
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

                        FirebaseConexionFirestore.setMap(document.getData());
                        if((boolean) Objects.requireNonNull(document.getData()).get("Validacion"))
                        {
                            PERSONA="Choferes";
                            DOCUMENT=document.getId();
                            features=cargarLugares();
                           // Toast.makeText(view.getContext(), "Iniciando... ", Toast.LENGTH_SHORT).show();
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

    public static void actualizarData(String nombre, String apellido, String telefono, View view)
    {
        if(!apellido.equals(datos.get("Apellidos")))
        {
            db.collection(PERSONA).document(DOCUMENT).update("Apellidos",apellido);
            datos.put("Apellidos",apellido);
        }
        if(!nombre.equals(datos.get("Nombre")))
        {
            db.collection(PERSONA).document(DOCUMENT).update("Nombre",nombre );
            datos.put("Nombre",nombre);
        }
        if(!telefono.equals(datos.get("Teléfono")))
        {
            db.collection(PERSONA).document(DOCUMENT).update("Teléfono",telefono);
            datos.put("Teléfono",apellido);
        }

        Snackbar.make(view,"Datos actualizados",Snackbar.LENGTH_SHORT).show();
    }
    public static  void actualizarDate()
    {
        db.collection(PERSONA).document(DOCUMENT).update("LastDate", Calendar.getInstance().getTime());
        datos.put("LastDate", Calendar.getInstance().getTime());

    }
    public static void actualizarImagen(String URI, int actualizar) {
        if (actualizar == 0)//imagen de perfil
        {
            db.collection(PERSONA).document(DOCUMENT).update("URI",URI);
            datos.put("URI",URI);
        }
        else//la del coche
        {
            db.collection(PERSONA).document(DOCUMENT).update("URI_Coche",URI);
            datos.put("URI_Coche",URI);
        }
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
    public static List<Feature> cargarLugares()
    {
        final List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        db.collection("Puntos_recoleccion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.addProperty("title", document.getData().get("nombre").toString());
                                GeoPoint geoPoint = (GeoPoint) document.getData().get("location");

                                symbolLayerIconFeatureList.add(
                                        Feature.fromGeometry(Point.fromLngLat(geoPoint.getLongitude(), geoPoint.getLatitude()), jsonObject));

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return  symbolLayerIconFeatureList;


    }



}
