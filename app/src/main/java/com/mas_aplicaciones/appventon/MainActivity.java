package com.mas_aplicaciones.appventon;



import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mas_aplicaciones.appventon.R;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {


    private int activar = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();



    }
    public  void activado(int activar)
    {
        this.activar=activar;
    }
    public  void activado(int activar, String ImagenDeletePeople)
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
