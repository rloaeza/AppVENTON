package com.mas_aplicaciones.appventon.firebase;


import android.app.AlertDialog;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.JsonObject;
import com.mapbox.geojson.Feature;
import com.mapbox.geojson.Point;
import com.mas_aplicaciones.appventon.Lugar;
import com.mas_aplicaciones.appventon.LugarUsuario;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.adapters.AdapterChoferes;
import com.mas_aplicaciones.appventon.chofer.PrincipalChofer;
import com.mas_aplicaciones.appventon.holders.HolderChoferes;
import com.mas_aplicaciones.appventon.models.ModelChoferes;
import com.mas_aplicaciones.appventon.usuario.PrincipalUsuario;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import dmax.dialog.SpotsDialog;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static androidx.navigation.Navigation.findNavController;


public class FirebaseConexionFirestore {
    private static FirebaseFirestore db = MainActivity.db;

    private static Map<String, Object> datos = new HashMap<>();
    private static Map<String, Object> datosChoferLugar = new HashMap<>();
    private static Map<String, Object> datosPuntosColeccion = new HashMap<>();
    private static List<String> listaAux = new ArrayList<>();
    public static ArrayList<ModelChoferes> choferModels = new ArrayList<>();

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
    public void agregar_chofer_lugar(Map<String, Object> datos, String UUID) {

        db.collection("Chofer_lugar").document(UUID)
                .set(datos)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

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
                            Toast.makeText(view.getContext(),"Tus datos están siendo validados en la organización "+document.getData().get("Organización")+" hasta que termine la validación podrás iniciar sesión",Toast.LENGTH_LONG).show();
                        }


                        //vaciar();

                    }

                }
            }
        });
    }

    public static void buscarListaChoferes(final View view, final int vista, final String chofer_lugar, final String UUID)
    {
        final AlertDialog alertDialog;
        DocumentReference docRef;
        if(vista==1)
        {
            docRef  = db.collection("Puntos_recoleccion").document(UUID);
            alertDialog = new SpotsDialog.Builder().setContext(view.getContext()).setMessage("Cargando...").build();
            alertDialog.show();
        }
        else
        {
            docRef  = db.collection("Puntos_recoleccion").document(UUID);
            alertDialog = new SpotsDialog.Builder().setContext(view.getContext()).setMessage("Eliminando...").build();
            alertDialog.show();
        }

        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                if (documentSnapshot.exists())
                {
                    if(vista ==1)
                    {
                        if(!PrincipalChofer.lugar.equals(null))
                        {
                            PrincipalChofer.lugar.put("Choferes-lista",documentSnapshot.getData().get("chofer-lugar"));
                        }
                        else
                        {
                            PrincipalUsuario.lugar.put("Choferes-lista",documentSnapshot.getData().get("chofer-lugar"));

                        }

                        alertDialog.cancel();
                    }
                    else
                    {
                        listaAux=(ArrayList<String>)Objects.requireNonNull(documentSnapshot.getData().get("chofer-lugar"));
                        listaAux.remove(chofer_lugar);
                        actualizarArrayChoferesPuntosRecoleccion(UUID,listaAux,"",2,view);

                        alertDialog.cancel();
                    }

                }
            }
        });

    }

    public static void actualizarFotoAutomatica(String collection, final View view, final ImageView imageView_persona, final ImageView imageView_carro)
    {
        DocumentReference docRef = db.collection(collection).document(DOCUMENT);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists())
                {
                    datos.put("URI",snapshot.getData().get("URI").toString());
                    datos.put("URI_Coche",snapshot.getData().get("URI_Coche").toString());
                    String foto_persona = snapshot.getData().get("URI").toString();
                    String foto_auto = snapshot.getData().get("URI_Coche").toString();
                    Glide.with(view.getContext())
                        .load(foto_persona)
                        .fitCenter()
                        .centerCrop()
                        .apply(RequestOptions.circleCropTransform())
                        .into(imageView_persona);
                    Glide.with(view.getContext())
                            .load(foto_auto)
                            .fitCenter()
                            .centerCrop()
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageView_carro);
                    if(!datos.get("Viaje").equals("")) {
                        db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("URI", foto_persona);
                        db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("URI_Coche",foto_auto);
                    }

                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }
    public static void actualizarViajeAutomatica(final View view, final ImageView imageView_lugar, final Spinner spinner_tiempo_espera, RecyclerView recycler_usuarios, final TextView textView_lugar_nombre, final TextView text_view_espacios_restantes, final EditText editText_comentario, final TimePicker timePicker)
    {

        DocumentReference docRef = db.collection("Chofer_lugar").document(datos.get("Viaje").toString());
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists())
                {
                    datosChoferLugar = snapshot.getData();
                    Glide.with(view.getRootView().getContext())
                            .load(datosChoferLugar.get("URI_Lugar"))
                            .fitCenter()
                            .centerCrop()
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageView_lugar);

                    textView_lugar_nombre.setText(datosChoferLugar.get("Nombre_Lugar").toString());
                    text_view_espacios_restantes.setText("Espacios disponibles: "+datosChoferLugar.get("Espacios").toString());
                    editText_comentario.setText(datosChoferLugar.get("Comentario").toString());
                    spinner_tiempo_espera.setSelection(Integer.parseInt(datosChoferLugar.get("TiempoEspera").toString()));
                    String []hora=datosChoferLugar.get("Hora").toString().split(":");
                    timePicker.setHour(Integer.parseInt(hora[0]));
                    timePicker.setMinute(Integer.parseInt(hora[1]));






                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }
    public static void actualizarViajeAutomaticoUsuariosView(String UUID, final View view, final RecyclerView recyclerView_choferes, final TextView textView_disponibles)
    {


       choferModels.clear();
       datosChoferLugar.clear();
       listaAux.clear();
        LinearLayoutManager linearLayoutManager =new LinearLayoutManager(view.getContext());
        recyclerView_choferes.setLayoutManager(linearLayoutManager);

        DocumentReference docRef = db.collection("Puntos_recoleccion").document(UUID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>()
        {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists())
                {

                      datosPuntosColeccion = snapshot.getData();
                      listaAux = (List<String>) datosPuntosColeccion.get("chofer-lugar");
                      //recyclerView_choferes.setAdapter(null);




                    if((listaAux+"").length()!=4) {

                        Log.e("err", (listaAux.size())+"");
                        Log.e("err", (choferModels.size())+"");
                        choferModels.clear();

                        Log.e("err", (choferModels.size())+"");
                        for (int i = 0; i < listaAux.size(); i++)
                        {

                            DocumentReference docRef = db.collection("Chofer_lugar").document(listaAux.get(i));
                            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()) {
                                        DocumentSnapshot document = task.getResult();
                                        if (document.exists()) {

                                            ModelChoferes modelcardView = new ModelChoferes();


                                            String id = document.getId();
                                            String nombreChofer=document.getData().get("Nombre").toString();
                                            String imagenChofer=document.getData().get("URI").toString();
                                            String hora = document.getData().get("Hora").toString()+" "+document.getData().get("TiempoEspera").toString();
                                            Timestamp timestamp = (Timestamp) document.getData().get("Fecha");
                                            Calendar calendar = Calendar.getInstance();
                                            calendar.setTime(timestamp.toDate());
                                            String fecha= calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+1+"/"+calendar.get(Calendar.YEAR);
                                            modelcardView.setId(id);
                                            modelcardView.setTitle(nombreChofer);
                                            modelcardView.setImage(imagenChofer);
                                            Log.e("err", "hola llegué");
                                            modelcardView.setFecha(fecha);
                                            modelcardView.setHora(hora);
                                            choferModels.add(modelcardView);

                                        } else {
                                            Log.d(TAG, "No such document");
                                        }
                                    } else {
                                        Log.d(TAG, "get failed with ", task.getException());
                                    }
                                }
                            });

                        }

                        textView_disponibles.setText("Actualización diponible nuevos choferes");

                       /* recyclerView_choferes.getAdapter().notifyDataSetChanged();

                        adapter.notifyItemRangeInserted(0, items.size);
                        //Toast.makeText(view.getContext(),"Termine"+(adapterChoferes+"").toString().length(),Toast.LENGTH_SHORT).show();
                        if((adapterChoferes+"").toString().length()==4)
                        {

                            Toast.makeText(view.getContext(),"Termine",Toast.LENGTH_SHORT).show();
                            AdapterChoferes adapterChoferes2 = new AdapterChoferes(view.getContext(),view,choferModels,PrincipalUsuario.lugar.get("id").toString());
                            recyclerView_choferes.setAdapter(adapterChoferes2);
                            recyclerView_choferes.setVisibility(View.VISIBLE);
                        }*/






                    }
                    else{
                        Toast.makeText(view.getContext(),"No hay choferes en esta ruta",Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Log.d(TAG, "Current data: null");
                }

            }
        });






    }
    /*public static void actualizarCardViewChoferes(final String UUID, final TextView title, final TextView fecha, final TextView hora, final ImageView imageView, final View view, final String idLugar, final int position, final RecyclerView pareView, final ArrayList<ModelChoferes> modelArrayList, final AdapterChoferes adapterChoferes)
    {
        DocumentReference docRef = db.collection("Chofer_lugar").document(UUID);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists())
                {




                    ModelChoferes modelcardView = new ModelChoferes();
                    String id = snapshot.getId();
                    String nombreChofer=snapshot.getData().get("Nombre").toString();
                    String imagenChofer=snapshot.getData().get("URI").toString();
                    String horat = snapshot.getData().get("Hora").toString()+" "+snapshot.getData().get("TiempoEspera").toString();
                    Timestamp timestamp = (Timestamp) snapshot.getData().get("Fecha");
                    Calendar calendar = Calendar.getInstance();
                    calendar.setTime(timestamp.toDate());
                    String fechat= calendar.get(Calendar.DAY_OF_MONTH)+"/"+calendar.get(Calendar.MONTH)+1+"/"+calendar.get(Calendar.YEAR);
                    modelcardView.setId(id);
                    modelcardView.setTitle(nombreChofer);
                    modelcardView.setImage(imagenChofer);

                    modelcardView.setFecha(fechat);
                    modelcardView.setHora(horat);


                    title.setText(nombreChofer);
                    hora.setText(horat);
                    fecha.setText(fechat);
                    Glide.with(view.getRootView().getContext())
                            .load(imagenChofer)
                            .fitCenter()
                            .centerCrop()
                            .apply(RequestOptions.circleCropTransform())
                            .into(imageView);
                    Log.e("err", "entre"+position);
                    //adapterChoferes.update(position,modelcardView);



                }
                else
                {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }*/

    public static void actualizarRatingAutomatico(String collection, final View view, final TextView[] textViews, final RatingBar ratingBar, final FragmentActivity activity)
    {
        DocumentReference docRef = db.collection(collection).document(DOCUMENT);
        docRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists())
                {
                    datos.put("URI",snapshot.getData().get("URI").toString());
                    datos.put("URI_Coche",snapshot.getData().get("URI_Coche").toString());
                    datos.put("Ranking",snapshot.getData().get("Ranking").toString());
                    int cantidad = (int) (Float.parseFloat(FirebaseConexionFirestore.getValue("Ranking").toString())/0.5);
                    ratingBar.setRating((float)(cantidad*0.5));
                    recompensas(ratingBar,textViews,activity);
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });
    }
    public static void actualizarData(String nombre, @NonNull String apellido, String telefono, View view)
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
        //actualizara el viaje
        if(!datos.get("Viaje").equals(""))
        {
            db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("Nombre",nombre+" "+apellido);
            db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("Ranking",datos.get("Ranking"));
        }
        Snackbar.make(view,"Datos actualizados",Snackbar.LENGTH_SHORT).show();
    }
    public static void actualizarDate()
    {
        db.collection(PERSONA).document(DOCUMENT).update("LastDate", Calendar.getInstance().getTime());
        datos.put("LastDate", Calendar.getInstance().getTime());

    }
    public static void actualizarDatosViajeChofer(String comentario,String espera,String time)
    {


        if(!datosChoferLugar.get("Comentario").equals(comentario))
        {
            FirebaseConexionFirestore.setValueChoferLugar("Comentario",comentario);
            db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("Comentario",datosChoferLugar.get("Comentario"));

        }
        if(!datosChoferLugar.get("TiempoEspera").equals(espera))
        {
            setValueChoferLugar("TiempoEspera",espera);
            db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("TiempoEspera",datosChoferLugar.get("TiempoEspera"));

        }
        if(!datosChoferLugar.get("Hora").equals(time))
        {
            setValueChoferLugar("Hora",time);
            db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("Hora", time);

        }
        if(!datos.get("Viaje").equals(""))
        {
            db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("Comentario", comentario);
            db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("Hora", time);
            db.collection("Chofer_lugar").document(datos.get("Viaje").toString()).update("TiempoEspera",espera);

        }


    }
    public static  void actualizarViaje(String viaje)
    {
        db.collection(PERSONA).document(DOCUMENT).update("Viaje", viaje);
        datos.put("Viaje", viaje);

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

    public static void actualizarArrayChoferesPuntosRecoleccion(String UIDD, List<String> choferes, String UUIDDChofer_lugar, int vista,View view) {

        if(vista==1) {
            choferes.add(UUIDDChofer_lugar);
            db.collection("Puntos_recoleccion").document(UIDD).update("chofer-lugar", choferes);
            PrincipalChofer.lugar.put("Choferes", choferes);
        }
        else
        {
            Log.e("err","entre");
            db.collection("Puntos_recoleccion").document(UIDD).update("chofer-lugar", choferes);
            Navigation.findNavController(view).navigate(R.id.action_viajes_to_principalChofer2);
        }
    }

    public static void eliminarViajeChofer(final View view)
    {
        final String id = datos.get("Viaje").toString();
        final String idLugar =datosChoferLugar.get("IdLugar").toString();
        Log.e("err",idLugar);
        db.collection("Chofer_lugar").document(id)
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                        buscarListaChoferes(view,2,id,idLugar);
                        actualizarViaje("");
                        Navigation.findNavController(view).navigate(R.id.action_viajes_to_principalChofer2);
                        Toast.makeText(view.getContext(),"Cancelado",Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                    }
                });
    }
    //cargar lugares para colocar en el mapa
    public static List<Feature> cargarLugares()
    {
        final List<Feature> symbolLayerIconFeatureList = new ArrayList<>();
        db.collection("Puntos_recoleccion")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult())
                            {

                                if(((document.getData().get("chofer-lugar"))+"").length()>4)
                                {
                                    JsonObject jsonObject = new JsonObject();
                                    jsonObject.addProperty("title", document.getData().get("nombre").toString());
                                    jsonObject.addProperty("id", document.getId());
                                    GeoPoint geoPoint = (GeoPoint) document.getData().get("location");
                                    jsonObject.addProperty("lat", geoPoint.getLatitude());
                                    jsonObject.addProperty("lon", geoPoint.getLongitude());
                                    jsonObject.addProperty("imagen", document.getData().get("imagen").toString());


                                    symbolLayerIconFeatureList.add(
                                            Feature.fromGeometry(Point.fromLngLat(geoPoint.getLongitude(), geoPoint.getLatitude()), jsonObject));
                                }

                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
        return  symbolLayerIconFeatureList;
    }




    private static void recompensas(@NonNull RatingBar ratingBar, TextView[] textViews, FragmentActivity activity)
    {
        {
            if(ratingBar.getRating()>=3 && ratingBar.getRating()<5)
            {
                textViews[0].setTextColor(activity.getColor(R.color.colorBackBotones));
                textViews[1].setTextColor(activity.getColor(R.color.colorBackBotones3));
                textViews[2].setTextColor(activity.getColor(R.color.colorBackBotones3));
            }
            else if(ratingBar.getRating()>=5 && ratingBar.getRating()<6)
            {
                textViews[0].setTextColor(activity.getColor(R.color.colorBackBotones3));
                textViews[1].setTextColor(activity.getColor(R.color.colorBackBotones));
                textViews[2].setTextColor(activity.getColor(R.color.colorBackBotones3));
            }
            else if(ratingBar.getRating()==6)
            {
                textViews[0].setTextColor(activity.getColor(R.color.colorBackBotones3));
                textViews[1].setTextColor(activity.getColor(R.color.colorBackBotones3));
                textViews[2].setTextColor(activity.getColor(R.color.colorBackBotones));
            }
            else{
                textViews[0].setTextColor(activity.getColor(R.color.colorBackBotones3));
                textViews[1].setTextColor(activity.getColor(R.color.colorBackBotones3));
                textViews[2].setTextColor(activity.getColor(R.color.colorBackBotones3));
            }

        }
    }
    public static Object getValue(String Key) {

        return datos.get(Key);
    }


    public static void ClearMap() {

        datos.clear();
    }
    public static void setValueChoferLugar (String key,Object value) {

        datosChoferLugar.put(key,value);
    }
    public static void setMap(Map<String,Object> setData)
    {
        datos=setData;
    }



}
