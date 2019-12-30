package com.mas_aplicaciones.appventon;



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.mas_aplicaciones.appventon.chofer.RegistroChoferOrganizacionAuto;
import com.mas_aplicaciones.appventon.storagefirebase.StorageFirebase;
import com.mas_aplicaciones.appventon.usuario.RegistroUsuarioOrganizacion;

import java.util.Objects;

import static com.mas_aplicaciones.appventon.usuario.RegistroUsuarioOrganizacion.getValueMap;

public class MainActivity extends AppCompatActivity {

    public static StorageReference mStorage = FirebaseStorage.getInstance().getReference();//instancia de clase para le storage
    @SuppressLint("StaticFieldLeak")
    public static FirebaseFirestore db  = FirebaseFirestore.getInstance();


    /*public void actionSnackbar(){
       Snackbar.make(view,"No podrá ver el mapa hasta que suba uma imagen de su auto",Snackbar.LENGTH_LONG).setAction(R.string.modificar,
                new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Navigation.findNavController(view).navigate(R.id.action_principalChofer_to_menu2);
                }
            }).show();
        Snackbar.make(, "hola", Snackbar.LENGTH_LONG)
                .setAction("hola", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MainActivity.this, "goas", Toast.LENGTH_SHORT).show();
                    }
                }).show();
    }*/


    private int activar = 3;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();
        // metodos para consultar los numeros de control




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
