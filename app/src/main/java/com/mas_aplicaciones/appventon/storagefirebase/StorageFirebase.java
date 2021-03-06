package com.mas_aplicaciones.appventon.storagefirebase;

import android.app.AlertDialog;
import android.net.Uri;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.chofer.RegistroChoferOrganizacionAuto;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.usuario.RegistroUsuarioOrganizacion;

import dmax.dialog.SpotsDialog;

public class StorageFirebase
{
    private static StorageReference mStorage = MainActivity.mStorage;//instancia de clase para le storage
    //iniciación de la instancia storage
    // dialogo
    private AlertDialog alertDialog;
    private final static boolean[] sucessful = {false};

    public void EliminarFoto(String id,String child, final View view)
    {

        StorageReference desertRef = mStorage.child(child).child(id);
        if(child.equals("Choferes"))
        {
            RegistroChoferOrganizacionAuto.setValueMap("URI","");
        }
        else if(child.equals("Usuarios"))
        {
            RegistroUsuarioOrganizacion.setValueMap("URI","");
        }
        else
        {
            RegistroUsuarioOrganizacion.setValueMap("URI_Coche","");
        }
        // Delete the file
        desertRef.delete().addOnSuccessListener(aVoid -> {

        }).addOnFailureListener(exception -> {
            // Uh-oh, an error occurred!
            //
        });
    }

    public void agregarFoto(final String id, Uri uri, final String child, final View view, final int num)
    {
        alertDialog = new SpotsDialog.Builder().setContext(view.getContext()).setMessage("Cargando").build();
        alertDialog.show();

        final StorageReference storageReference = mStorage.child(child).child(id);

        storageReference.putFile(uri).addOnSuccessListener(taskSnapshot -> {
            //agrega el uri a el database


            storageReference.getDownloadUrl().addOnSuccessListener(uri1 -> {

                if(child.equals("Choferes"))
                {

                    if(num==0)//significa que se esta registrando
                    {
                        RegistroChoferOrganizacionAuto.setValueMap("URI", uri1.toString());
                    }
                    else//actualizando
                    {
                        FirebaseConexionFirestore.actualizarImagen(uri1.toString(), 0);
                        Snackbar.make(view,"La imagen puede durar algunos segundos en salir, depende de tu conexion a internet",Snackbar.LENGTH_LONG).show();
                    }


                }
                else
                {
                    if(num==0)//significa que se esta registrando
                    {
                        RegistroUsuarioOrganizacion.setValueMap("URI", uri1.toString());


                    }
                    else//actualizando
                    {
                        FirebaseConexionFirestore.actualizarImagen(uri1.toString(), 0);
                        Snackbar.make(view,"La imagen puede durar algunos segundos en salir, depende de tu conexion a internet",Snackbar.LENGTH_LONG).show();
                    }

                }

            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                    Toast.makeText(view.getContext(), "Error de subida" , Toast.LENGTH_SHORT).show();

                }
        });

            //Toast.makeText(view.getContext(), "Imagen agregada al sistema con el identificador " + id, Toast.LENGTH_SHORT).show();
            sucessful[0] = true;
            alertDialog.cancel();
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Error de subida \n verifique si conexión \n contacte al administrador", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
                sucessful[0] = false;
                e.printStackTrace();
            }
        });

    }


    public void agregarFotoCoche(final String id, Uri uri, final View view, final int num)
    {
        alertDialog = new SpotsDialog.Builder().setContext(view.getContext()).setMessage("Cargando").build();
        alertDialog.show();

        final StorageReference storageReference = mStorage.child("Coches").child(id);

        storageReference.putFile(uri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot)
            {
                //agrega el uri a el database

                storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri)
                    {
                                FirebaseConexionFirestore.actualizarImagen(uri.toString(),1);
                                Snackbar.make(view,"La imagen puede durar algunos segundos en salir, depende de tu conexion a internet",Snackbar.LENGTH_LONG).show();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
                        Toast.makeText(view.getContext(), "Error de subida" , Toast.LENGTH_SHORT).show();

                    }
                });

                //Toast.makeText(view.getContext(), "Imagen agregada al sistema con el identificador " + id, Toast.LENGTH_SHORT).show();
                sucessful[0] = true;
                alertDialog.cancel();
            }

        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(view.getContext(), "Error de subida \n verifique si conexión \n contacte al administrador", Toast.LENGTH_SHORT).show();
                alertDialog.cancel();
                sucessful[0] = false;
                e.printStackTrace();
            }
        });

    }
    public static boolean getImagenSubida()
    {
        return sucessful[0];
    }
    public static void setImagenSubida()
    {
         sucessful[0]=false;
    }
}
