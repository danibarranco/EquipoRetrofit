package com.example.equiporetrofit.View;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.example.equiporetrofit.Model.Data.Equipo;
import com.example.equiporetrofit.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.equiporetrofit.MainActivity.URL;

public class EquipoViewAdapter extends RecyclerView.Adapter <EquipoViewAdapter.ItemHolder>{
    private LayoutInflater inflater;
    private List<Equipo> equipoList;
    private EquipoViewAdapter.OnItemClickListenner listener;
    private Context context;

    public interface OnItemClickListenner{
        void onItemClick(Equipo equipo, View v, ItemHolder holder);
    }

    public EquipoViewAdapter(EquipoViewAdapter.OnItemClickListenner listener,Context context) {
        this.listener=listener;
        this.context=context;
        inflater=LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.item_equipo,parent,false);

        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemHolder holder, int position) {
        if(equipoList !=null){
            final Equipo curent = equipoList.get(position);
            holder.tvNomE.setText(holder.tvNomE.getText().toString().split(" ")[0]+" "+curent.getNombre());
            holder.tvCiudad.setText(holder.tvCiudad.getText().toString().split(" ")[0]+" "+curent.getCiudad());
            holder.tvEstadio.setText(holder.tvEstadio.getText().toString().split(" ")[0]+" "+curent.getEstadio());
            holder.tvAforo.setText(holder.tvAforo.getText().toString().split(" ")[0]+" "+String.valueOf(curent.getAforo()));
            Glide.with(context)
                    .load(Uri.parse("http://"+URL+curent.getEscudo()))
                    .override(300, 300)// prueba de escalado
                    .into(holder.ivEscudo);
            holder.cl.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(curent,view,holder);
                }
            });
            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(curent,view,holder);
                }
            });
            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(curent,view,holder);
                }
            });

        }
    }
    public void setData(List<Equipo>typeList){
        this.equipoList =typeList;
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        equipoList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        int elements=0;
        if(equipoList !=null){
            elements= equipoList.size();
        }
        return elements;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomE,tvCiudad,tvEstadio,tvAforo;
        private final ImageView ivEscudo,ivEdit,ivDelete;
        private ConstraintLayout cl;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvNomE=itemView.findViewById(R.id.tvNomJ);
            tvCiudad=itemView.findViewById(R.id.tvCiudad);
            ivEscudo=itemView.findViewById(R.id.ivFoto);
            ivEdit=itemView.findViewById(R.id.ivEditJ);
            ivDelete=itemView.findViewById(R.id.ivDeleJ);
            tvEstadio=itemView.findViewById(R.id.tvApe);
            tvAforo=itemView.findViewById(R.id.tvAforo);
            cl=itemView.findViewById(R.id.cl);
        }
    }
}
