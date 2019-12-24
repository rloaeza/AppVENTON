package com.mas_aplicaciones.appventon;



import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    public static StorageReference mStorage = FirebaseStorage.getInstance().getReference();//instancia de clase para le storage
    @SuppressLint("StaticFieldLeak")
    public static FirebaseFirestore db  = FirebaseFirestore.getInstance();



    private int activar = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        // metodos para consultar los numeros de control
        FirebaseConexionFirestore.getNumeroControlUsuarios();
        FirebaseConexionFirestore.getNumeroControlChoferes();



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
