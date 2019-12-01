package com.example.equiporetrofit;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import com.example.equiporetrofit.Model.Data.Equipo;
import com.example.equiporetrofit.Model.Data.Jugador;
import com.example.equiporetrofit.View.EquipoViewAdapter;
import com.example.equiporetrofit.View.JugadorViewAdapter;
import com.example.equiporetrofit.View.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.PersistableBundle;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class JugadorActivity extends AppCompatActivity {
    private JugadorViewAdapter adapter;
    private MainViewModel viewModel;
    private long idEquipo;

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume "+idEquipo);
        getIntent().getLongExtra("idEquipo",0);
        viewModel.getLiveJugadorList().observe(this,new Observer<List<Jugador>>() {
            @Override
            public void onChanged(List<Jugador> jugadores) {
                ArrayList<Jugador>jugadoresEquipo=new ArrayList<>();
                for (Jugador j:jugadores
                ) {
                    if(j.getIdequipo()==idEquipo){
                        jugadoresEquipo.add(j);
                    }
                }
                adapter.setData(jugadoresEquipo);
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jugador);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(savedInstanceState!=null){
            idEquipo=savedInstanceState.getLong("idEquipo");
        }else{
            idEquipo=getIntent().getLongExtra("idEquipo",0);
        }
        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getLiveJugadorList().observe(this,new Observer<List<Jugador>>() {
            @Override
            public void onChanged(List<Jugador> jugadores) {
                ArrayList<Jugador>jugadoresEquipo=new ArrayList<>();
                for (Jugador j:jugadores
                     ) {
                    if(j.getIdequipo()==idEquipo){
                        jugadoresEquipo.add(j);
                    }
                }
                adapter.setData(jugadoresEquipo);
            }
        });
        init();
    }





    private void init() {
        adapter= new JugadorViewAdapter(this ,viewModel);
        RecyclerView rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(JugadorActivity.this,AddJugador.class).putExtra("idEquipo",idEquipo));
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState, @NonNull PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outPersistentState.putLong("idEquipo",idEquipo);
    }
}
