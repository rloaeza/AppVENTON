package com.mas_aplicaciones.appventon.Interfaces;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.mas_aplicaciones.appventon.EntidadChofer;

import java.util.ArrayList;


public interface Choferes
{

    void getDatos(ArrayList<EntidadChofer> elementos);
    void getUpdateData(ArrayList<EntidadChofer> elementosAdd,ArrayList<EntidadChofer> elementosMod,ArrayList<EntidadChofer> elementosDel,int opcion);
}
