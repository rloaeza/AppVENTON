package com.mas_aplicaciones.appventon;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class AdaptadorChofer extends BaseAdapter
{
    private Context context;
    private ArrayList<EntidadChofer> entidadChoferes;

    public AdaptadorChofer(Context context, ArrayList<EntidadChofer> entidadChoferes) {
        this.context = context;
        this.entidadChoferes = entidadChoferes;
    }
    public ArrayList<EntidadChofer> getAllEntidades()
    {
        if(entidadChoferes.isEmpty())
        {
            return null;
        }
        else
        {
            return entidadChoferes;
        }
    }
    public void add(ArrayList<EntidadChofer> entidadChoferAdd)
    {

        ArrayList<Integer> pos = new ArrayList<>();
        for(int i=0;i<entidadChoferAdd.size();i++)
        {
            for(int j=0;j<entidadChoferes.size();j++)
            {
                if (entidadChoferes.get(j).getId().equals(entidadChoferAdd.get(i).getId()))
                {
                    pos.add(j);
                    break;
                }
            }
        }
        if(pos.size()!=0)
        {
            for (int x: pos)
            {
                entidadChoferAdd.remove(x);
            }
        }

        this.entidadChoferes.addAll(entidadChoferAdd);
    }

    public void delete(ArrayList<EntidadChofer> entidadChoferDel)
    {
        for(int i=0;i<entidadChoferDel.size();i++)
        {
            for(int j=0;j<entidadChoferes.size();j++)
            {
                if (entidadChoferes.get(j).getIdChofer().equals(entidadChoferDel.get(i).getIdChofer())) {
                    entidadChoferes.remove(j);
                    break;
                }
            }
        }

    }

    public void update(ArrayList<EntidadChofer> entidadChoferMod)
    {
        for(int i=0;i<entidadChoferMod.size();i++)
        {
            for(int j=0;j<entidadChoferes.size();j++)
            {
                if (entidadChoferes.get(j).getIdChofer().equals(entidadChoferMod.get(i).getIdChofer())) {
                    entidadChoferes.set(j, entidadChoferMod.get(i));
                    break;
                }
            }

        }
    }


    @Override
    public int getCount() {

        return entidadChoferes.size();
    }

    @Override
    public Object getItem(int position) {
        return entidadChoferes.get(position);
    }

    @Override
    public long getItemId(int position) {

        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        EntidadChofer entidadChofer = (EntidadChofer)getItem(position);
        convertView = LayoutInflater.from(context).inflate(R.layout.row,null);

        TextView textView_nombre = convertView.findViewById(R.id.text_view_nombre);
        TextView textView_hora = convertView.findViewById(R.id.text_view_hora);
        TextView textView_fecha = convertView.findViewById(R.id.text_view_fecha);
        TextView textView_comentario = convertView.findViewById(R.id.text_view_comentario);
        TextView textView_espacios_restantes = convertView.findViewById(R.id.text_view_espacios_restantes);
        TextView textView_tiempo_espera = convertView.findViewById(R.id.text_view_tiempo_espera);
        ImageView imageView_chofer = convertView.findViewById(R.id.image_view_photo);
        ImageView imageView_coche = convertView.findViewById(R.id.image_view_photo_coche);
        Button  button_seleccionar = convertView.findViewById(R.id.button_seleccionar_chofer);

        textView_nombre.setText(entidadChofer.getNombre());
        textView_hora.setText(entidadChofer.getHora());
        textView_fecha.setText(entidadChofer.getFecha());
        textView_comentario.setText(entidadChofer.getComentario());
        textView_espacios_restantes.setText(entidadChofer.getEspacio());
        textView_tiempo_espera.setText(entidadChofer.getTiempoEspera());
        Glide.with(context)
                .load(entidadChofer.getImagen())
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into((imageView_chofer));
        Glide.with(context)
                .load(entidadChofer.getImagen_Coche())
                .fitCenter()
                .centerCrop()
                .apply(RequestOptions.circleCropTransform())
                .into((imageView_coche));
        button_seleccionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context,entidadChofer.getId()+"",Toast.LENGTH_SHORT).show();
            }
        });

        return convertView;
    }
}
