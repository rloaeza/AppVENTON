package com.mas_aplicaciones.appventon.menu;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mas_aplicaciones.appventon.MainActivity;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.EvaluacionDeViews;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.storagefirebase.StorageFirebase;
import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class Configurar_choferes extends Fragment
{


    private EditText editText_nombre, editText_apellidos, editText_celular;
    private String nombre, apellido,celular,foto_auto,foto_persona;
    private EvaluacionDeViews objeto_evaluacion_de_views = new EvaluacionDeViews();
    private final static int GALLERY_INTENT = 1;
    private final static int GALLERY_INTENT2 = 3;
    private ImageView imageView_carro,imageView_persona;
    private StorageFirebase storageFirebase = new StorageFirebase();


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {

        if (getActivity() instanceof MainActivity)
        {
            ((MainActivity) getActivity()).activado(3);
        }
        final View view = inflater.inflate(R.layout.fragment_configurar, container, false);

        Button button_actualizar = view.findViewById(R.id.button_actualizar);
        Button button_subir_foto_auto = view.findViewById(R.id.button_subir_foto_auto);
        editText_nombre = view.findViewById(R.id.edit_text_nombre);
        editText_apellidos = view.findViewById(R.id.edit_text_apellidos);
        editText_celular = view.findViewById(R.id.edit_text_celular);
        editText_nombre.setText(FirebaseConexionFirestore.getValue("Nombre").toString());
        editText_apellidos.setText(FirebaseConexionFirestore.getValue("Apellidos").toString());
        editText_celular.setText(FirebaseConexionFirestore.getValue("Teléfono").toString());
        imageView_carro = view.findViewById(R.id.image_view_auto);
        imageView_persona = view.findViewById(R.id.image_view_persona);
        RatingBar ratingBar = view.findViewById(R.id.rating);
        int cantidad = (int) (Float.parseFloat(FirebaseConexionFirestore.getValue("Ranking").toString())/0.5);
        ratingBar.setRating((float)(cantidad*0.5));

        foto_auto = FirebaseConexionFirestore.getValue("URI_Coche").toString();
        foto_persona = FirebaseConexionFirestore.getValue("URI").toString();
        TextView recompensa1 = view.findViewById(R.id.recompensa1);
        TextView recompensa2 = view.findViewById(R.id.recompensa2);
        TextView recompensa3 = view.findViewById(R.id.recompensa3);
        TextView[] textViews = {recompensa1, recompensa2, recompensa3};

        //listeners


        FirebaseConexionFirestore.actualizarRatingAutomatico("Choferes",textViews, ratingBar,getActivity());
        FirebaseConexionFirestore.actualizarFotoAutomatica("Choferes",view,imageView_persona,imageView_carro);


        button_subir_foto_auto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick( final View v)
            {

                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"),GALLERY_INTENT2);
            }
        });
        imageView_persona.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

                startActivityForResult(Intent.createChooser(intent,"Selecciona una imagen"),GALLERY_INTENT);


            }
        });

        button_actualizar.setOnClickListener(new View.OnClickListener()
        {


            @Override
            public void onClick(View v)
            {
                nombre = editText_nombre.getText().toString();
                apellido=editText_apellidos.getText().toString();
                celular=editText_celular.getText().toString();

                if(!nombre.equals(""))
                {
                    if(!apellido.equals("") )
                    {
                        if(!celular.equals("") && objeto_evaluacion_de_views.telefonoValido(celular))
                        {

                                FirebaseConexionFirestore.actualizarData(nombre.trim(),apellido.trim(),celular,getView());
                        }
                        else
                        {
                            editText_celular.setError("required");
                            Toast.makeText(getActivity(), "Teléfono nulo o incorrecto", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        editText_apellidos.setError("requered");
                    }

                }
                else
                {
                    editText_nombre.setError("required");
                }
            }
        });


        // Inflate the layout for this fragment
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState)
    {
        Glide.with(Objects.requireNonNull(getView()).getContext())
                .load(foto_persona)
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(imageView_persona);
        if(!foto_auto.equals(""))
        {

            Glide.with(getView().getContext())
                    .load(foto_auto)
                    .fitCenter()
                    .centerCrop()
                    .apply(RequestOptions.circleCropTransform())
                    .into(imageView_carro);
        }
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);


            if(requestCode==GALLERY_INTENT)
            {
                try
                {
                    Uri uri = data.getData();
                    storageFirebase.EliminarFoto(FirebaseConexionFirestore.getValue("NumeroControl").toString(), FirebaseConexionFirestore.PERSONA,getView());
                    assert null!=uri;
                    InputStream inputStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(uri);
                    assert inputStream != null;
                    if((Double.parseDouble(String.valueOf(inputStream.available()))/1024)<200.1)
                    {
                        storageFirebase.agregarFoto(FirebaseConexionFirestore.getValue("NumeroControl").toString(),uri, FirebaseConexionFirestore.PERSONA, Objects.requireNonNull(getView()),1);

                    }
                    else
                    {
                        Toast.makeText(getActivity(), "La imagen debe que ser menor de 200.1 kb",Toast.LENGTH_LONG).show();
                    }
                }
                catch (IOException | NullPointerException e)
                {
                    e.printStackTrace();
                }
            }
            if(requestCode==GALLERY_INTENT2)
            {
                try
                {
                    Uri uri = data.getData();
                    storageFirebase.EliminarFoto(FirebaseConexionFirestore.getValue("NumeroControl").toString(), "Coches",getView());
                    assert null!=uri;
                    InputStream inputStream = Objects.requireNonNull(getActivity()).getContentResolver().openInputStream(uri);
                    assert inputStream != null;
                    if((Double.parseDouble(String.valueOf(inputStream.available()))/1024)<200.1)
                    {
                        storageFirebase.agregarFotoCoche(FirebaseConexionFirestore.getValue("NumeroControl").toString(),uri, Objects.requireNonNull(getView()),1);
                    }
                    else
                    {
                        Toast.makeText(getActivity(), "La imagen debe que ser menor de 200.1 kb",Toast.LENGTH_LONG).show();
                    }
                }
                catch (IOException | NullPointerException e)
                {
                    e.printStackTrace();
                }
            }



    }



}
