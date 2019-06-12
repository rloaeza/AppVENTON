package com.mas_aplicaciones.appventon;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.navigation.Navigation;

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
