package com.mas_aplicaciones.appventon;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mapbox.mapboxsdk.Mapbox;
import com.mas_aplicaciones.appventon.chofer.RegistroChoferOrganizacionAuto;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.storagefirebase.StorageFirebase;
import com.mas_aplicaciones.appventon.usuario.RegistroUsuarioOrganizacion;

import java.util.Objects;

import static com.mas_aplicaciones.appventon.usuario.RegistroUsuarioOrganizacion.getValueMap;

public class MainActivity extends AppCompatActivity {
   
    public static FirebaseFirestore db ;
    //instancia de clase para le storage
    public static StorageReference mStorage;


    private int activar = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        // metodos para consultar los numeros de control
        FirebaseApp.initializeApp(this);
        db  = FirebaseFirestore.getInstance();
        mStorage = FirebaseStorage.getInstance().getReference();

    }
    public  void activado(int activar)
    {
        this.activar=activar;
    }

    @Override
    public void onBackPressed() {

        //utilizado para definir cuando si puedo regresar y cuando no


        if ( activar==1 )//se asigna 1 en el fragment principal chofer y usuario
        {//the fragment on which you want to handle your back press
            finish();
        }
        else{// si es diferente a inicio, principal usuario o principa chofer
           super.onBackPressed();


        }
    }


}
