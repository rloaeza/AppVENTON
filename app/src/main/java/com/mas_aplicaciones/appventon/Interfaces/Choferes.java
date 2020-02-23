package com.mas_aplicaciones.appventon.Interfaces;

import com.google.firebase.firestore.QuerySnapshot;
import com.mas_aplicaciones.appventon.EntidadChofer;

import java.util.ArrayList;


public interface Choferes
{

    void getDatos(QuerySnapshot elementos);
    void getUpdateData(ArrayList<EntidadChofer> elementosAdd,ArrayList<EntidadChofer> elementosMod,ArrayList<EntidadChofer> elementosDel,int opcion);
}
