package com.mas_aplicaciones.appventon.holders;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.mas_aplicaciones.appventon.Interfaces.I_card_View_Comunicate;
import com.mas_aplicaciones.appventon.R;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;


public class HolderChoferes extends RecyclerView.ViewHolder implements View.OnClickListener
{

    public ImageView imageView;
    public TextView title;
    public TextView hora;
    public TextView fecha;
    I_card_View_Comunicate i_card_view_comunicate;

    public HolderChoferes(@NonNull View itemView) {
        super(itemView);

        this.imageView = itemView.findViewById(R.id.image_view_Photo);
        this.title = itemView.findViewById(R.id.text_view_nombre);
        this.hora = itemView.findViewById(R.id.text_view_hora);
        this.fecha = itemView.findViewById(R.id.text_view_fecha);

        itemView.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        this.i_card_view_comunicate.onClickListener(view, getLayoutPosition());
    }

    public void setClickListener(I_card_View_Comunicate i_card_view_comunicate)
    {
        this.i_card_view_comunicate=i_card_view_comunicate;
    }
}
