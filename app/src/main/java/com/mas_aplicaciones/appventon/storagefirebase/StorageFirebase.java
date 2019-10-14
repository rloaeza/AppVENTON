package com.mas_aplicaciones.appventon.storagefirebase;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import dmax.dialog.SpotsDialog;

public class StorageFirebase
{
    private StorageReference mStorage = FirebaseStorage.getInstance().getReference();//instancia de clase para le storage
    //iniciación de la instancia storage
    // dialogo
    private AlertDialog alertDialog;
    public void EliminarFoto(String id, final View view)
    {

        StorageReference desertRef = mStorage.child("Usuarios").child(id);

        // Delete the file
        desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(view.getContext(), "Eliminado" , Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Uh-oh, an error occurred!
               // Toast.makeText(view.getContext(), "ERROR" , Toast.LENGTH_SHORT).show();
            }
        });
    }
    public boolean agregarFoto(final String id, Uri uri, final View view)
    {
        alertDialog = new SpotsDialog.Builder().setContext(view.getContext()).setMessage("Cargando").build();
        alertDialog.show();
        final boolean[] sucessful = {false};
        StorageReference storageReference = mStorage.child("Usuarios").child(id);
        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(view.getContext(), "Imagen agregada al sistema con el identificador " + id, Toast.LENGTH_SHORT).show();
                sucessful[0] = true;
                alertDialog.cancel();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Error de subida \n verifique si conexión \n contacte al administrador", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
            }
        });
        return sucessful[0];
    }
}