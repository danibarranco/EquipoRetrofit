package com.example.equiporetrofit;

import android.content.Intent;
import android.os.Bundle;

import com.example.equiporetrofit.Model.Data.Equipo;
import com.example.equiporetrofit.Model.Data.Jugador;
import com.example.equiporetrofit.View.EquipoViewAdapter;
import com.example.equiporetrofit.View.MainViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static final String URL="54.162.70.0";
    private EquipoViewAdapter adapter;
    private MainViewModel viewModel;
    private Boolean borrarEquipo=true;
    private ArrayList<Jugador>jugadoresEquipo;
    private ArrayList <Long> equiposLlenos=new ArrayList<>();

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("ONRESUME");
        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getLiveEquipoList().observe(this,new Observer<List<Equipo>>() {
            @Override
            public void onChanged(List<Equipo> equipos) {
                adapter.setData(equipos);
            }
        });
        viewModel.getLiveJugadorList().observe(this,new Observer<List<Jugador>>() {
            @Override
            public void onChanged(List<Jugador> jugadores) {
                jugadoresEquipo= (ArrayList<Jugador>) jugadores;
                for (Jugador j:jugadores
                     ) {
                    if(!equiposLlenos.contains(j.getIdequipo())){
                        equiposLlenos.add(j.getIdequipo());
                    }
                }
                System.out.println(equiposLlenos.toArray().toString());
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        viewModel =  ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.getLiveEquipoList().observe(this,new Observer<List<Equipo>>() {
            @Override
            public void onChanged(List<Equipo> equipos) {
                adapter.setData(equipos);

            }
        });


        init();
    }

    private void init() {
        adapter= new EquipoViewAdapter(new EquipoViewAdapter.OnItemClickListenner() {
            @Override
            public void onItemClick(final Equipo equipo, final View v, EquipoViewAdapter.ItemHolder holder) {
                //Lanzar intent con equipo
                final int pos=holder.getAdapterPosition();

                switch (v.getId()){
                    case R.id.ivDeleJ:
                        //Borrar equipo de servidor y recycler
                        System.out.println("IdEquipo "+equipo.getId());
                        if(equiposLlenos.contains(equipo.getId())){
                            borrarEquipo=false;
                        }else{
                            borrarEquipo=true;
                        }
                        borrarEquipo(pos,equipo);
                        break;
                    case R.id.ivEditJ:
                        //lanzar intent editar con parcelable
                        startActivity(new Intent(MainActivity.this,EditEquipo.class).putExtra("equipo",equipo));
                        break;
                    case R.id.cl:
                        //lanzar intent para ver jugadores del equipo
                        startActivity(new Intent(MainActivity.this,JugadorActivity.class).putExtra("idEquipo",equipo.getId()));
                        break;
                }
            }
        },this);
        RecyclerView rvList = findViewById(R.id.rvList);
        rvList.setLayoutManager(new LinearLayoutManager(this));
        rvList.setAdapter(adapter);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,AddEquipo.class));
            }
        });
    }

    private void borrarEquipo(int pos, Equipo equipo) {
        if(borrarEquipo){
            viewModel.deleteEquipo(equipo);
            adapter.removeItem(pos);
        }else {
            Snackbar.make(findViewById(R.id.rvList), "No se puede eliminar equipo con jugadores", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
