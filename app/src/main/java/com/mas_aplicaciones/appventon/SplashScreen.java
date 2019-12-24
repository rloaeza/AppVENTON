package com.mas_aplicaciones.appventon;



import android.media.MediaPlayer;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Objects;


/**
 * A simple {@link Fragment} subclass.
 */
public class SplashScreen extends Fragment {


    //Drawable image  = ContextCompat.getDrawable(getContext(), R.drawable.icono_splash_screen);

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final MediaPlayer sound = MediaPlayer.create(getContext(),R.raw.sound_av);
        //ImageView imageView_splash_screen = view.findViewById(R.id.image_view_splash_screen);
        final TextView textView_title = view.findViewById(R.id.textView_title);



        new Handler().postDelayed(new Runnable() {
            @Override
            public void run()
            {
                try {

                    textView_title.setVisibility(View.VISIBLE);
                    sound.start();
                    Navigation.findNavController(Objects.requireNonNull(getView())).navigate(R.id.action_splashScreen_to_inicioSesion);// metodo para pasar al siguiente fragment  sin validar
                }
                catch (Exception ex){
                    ///Toast.makeText(getContext(), "Error al cargar vista principal.", Toast.LENGTH_SHORT).show();
                }


            }
        },2000);
    }



    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return  inflater.inflate(R.layout.fragment_splash_screen, container, false);
    }


}
