package com.mas_aplicaciones.appventon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.mas_aplicaciones.appventon.Interfaces.I_card_View_Comunicate;
import com.mas_aplicaciones.appventon.firebase.FirebaseConexionFirestore;
import com.mas_aplicaciones.appventon.holders.HolderChoferes;
import com.mas_aplicaciones.appventon.models.ModelChoferes;
import com.mas_aplicaciones.appventon.R;

import java.util.ArrayList;

public class AdapterChoferes extends RecyclerView.Adapter<HolderChoferes> {

    Context context;
    ArrayList<ModelChoferes> modelArrayList; // this arraylist create a llist of array which parameters define in our model class
    View view;
    RecyclerView pareView;
    String idLugar;


    public AdapterChoferes(Context context, View view, ArrayList<ModelChoferes> modelArrayList, String idLugar)
    {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.view =view;
        this.idLugar =idLugar;
    }


    @NonNull
    @Override
    public HolderChoferes onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
        this.pareView= (RecyclerView) parent;
        return new HolderChoferes(view);// this will return out view to holder cllass
    }

    @Override
    public void onBindViewHolder(@NonNull final HolderChoferes holder, final int position)
    {



        holder.title.setText(modelArrayList.get(position).getTitle());
        holder.hora.setText(modelArrayList.get(position).getHora());
        holder.fecha.setText(modelArrayList.get(position).getFecha());
        Glide.with(context)
                .load(modelArrayList.get(position).getImage())
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageView);


        //FirebaseConexionFirestore.actualizarCardViewChoferes(modelArrayList.get(position).getId(),holder.title,holder.fecha,holder.hora,holder.imageView,view,idLugar,position,pareView,modelArrayList,this);



        //this method you can use it when you use an activity
        holder.setClickListener(new I_card_View_Comunicate() {
            @Override
            public void onClickListener(View v, int position) {

                Navigation.findNavController(view).navigate(R.id.action_lugarUsuario_to_descripcionViaje);


            }
        });

    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }




}
