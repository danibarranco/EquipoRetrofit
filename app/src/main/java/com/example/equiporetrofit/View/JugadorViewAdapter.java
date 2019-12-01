package com.example.equiporetrofit.View;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.equiporetrofit.EditJugador;
import com.example.equiporetrofit.JugadorActivity;
import com.example.equiporetrofit.MainActivity;
import com.example.equiporetrofit.Model.Data.Equipo;
import com.example.equiporetrofit.Model.Data.Jugador;
import com.example.equiporetrofit.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import static com.example.equiporetrofit.MainActivity.URL;

public class JugadorViewAdapter extends RecyclerView.Adapter <JugadorViewAdapter.ItemHolder>{
    private LayoutInflater inflater;
    private List<Jugador> jugadorList;
    private Context context;
    private MainViewModel viewModel;


    public JugadorViewAdapter( Context context ,MainViewModel viewModel) {
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.viewModel=viewModel;

    }

    @NonNull
    @Override
    public ItemHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView= inflater.inflate(R.layout.item_jugador,parent,false);

        return new ItemHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemHolder holder, final int position) {
        if(jugadorList !=null){
            final Jugador curent = jugadorList.get(position);
            holder.tvNomJ.setText(holder.tvNomJ.getText().toString().split(" ")[0]+" "+curent.getNombre());
            holder.tvApe.setText(holder.tvApe.getText().toString().split(" ")[0]+" "+curent.getApellidos());
            Glide.with(context)
                    .load(Uri.parse("http://"+URL+curent.getFoto()))
                    .override(300, 300)// prueba de escalado
                    .into(holder.ivFoto);

            holder.ivDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    removeItem(position);
                    viewModel.deleteJugador(curent);
                }
            });

            holder.ivEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //Intent edit jugador parcelable
                    context.startActivity(new Intent(context, EditJugador.class).putExtra("jugador",curent));
                }
            });
        }
    }
    public void setData(List<Jugador>typeList){
        this.jugadorList =typeList;
        notifyDataSetChanged();
    }
    public void removeItem(int position) {
        jugadorList.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public int getItemCount() {
        int elements=0;
        if(jugadorList !=null){
            elements= jugadorList.size();
        }
        return elements;
    }

    public class ItemHolder extends RecyclerView.ViewHolder {
        private final TextView tvNomJ,tvApe;
        private final ImageView ivFoto,ivDelete,ivEdit;

        public ItemHolder(@NonNull View itemView) {
            super(itemView);
            tvNomJ=itemView.findViewById(R.id.tvNomJ);
            tvApe=itemView.findViewById(R.id.tvApe);
            ivFoto=itemView.findViewById(R.id.ivFoto);
            ivDelete=itemView.findViewById(R.id.ivDeleJ);
            ivEdit=itemView.findViewById(R.id.ivEditJ);
        }
    }
}
