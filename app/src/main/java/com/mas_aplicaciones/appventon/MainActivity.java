package com.mas_aplicaciones.appventon;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Objects.requireNonNull(getSupportActionBar()).hide();


    }
    /*@Override
    public void onBackPressed() {

       // Fragment f = this.getSupportFragmentManager().findFragmentById(R.id.fragment);


        /*if ( f instanceof InicioSesion) {//the fragment on which you want to handle your back press
            Toast.makeText(this,f.getFragmentManager().getBackStackEntryCount(), Toast.LENGTH_SHORT).show();
        }
        else{
          //  Toast.makeText(this,String.valueOf(getSupportFragmentManager().getBackStackEntryCount()), Toast.LENGTH_SHORT).show();
            super.onBackPressed();

        }*/
    /*}*/


}
