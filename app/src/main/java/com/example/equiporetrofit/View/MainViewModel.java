package com.example.equiporetrofit.View;

import android.app.Application;

import com.example.equiporetrofit.Model.Data.Equipo;
import com.example.equiporetrofit.Model.Data.Jugador;
import com.example.equiporetrofit.Model.Repository;

import java.io.File;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class MainViewModel extends AndroidViewModel {
    private Repository repository;

    public MainViewModel(@NonNull Application application){
        super(application);
        repository= new Repository();

    }




    public LiveData<List<Equipo> >getLiveEquipoList(){
        return repository.getLiveEquipoList();
    }
    public LiveData<List<Jugador> >getLiveJugadorList(){
        return repository.getLiveJugadorList();
    }
    public void addEquipo(Equipo equipo) {
        repository.addEquipo(equipo);
    }
    public void addJugador(Jugador jugador) {
        repository.addJugador(jugador);
    }

    public void deleteEquipo(Equipo equipo) {
        repository.deleteEquipo(equipo);
    }
    public void deleteJugador(Jugador jugador) {
        repository.deleteJugador(jugador);
    }

    public void updateEquipo(Equipo equipo) {
        repository.updateEquipo(equipo);
    }
    public void updateJugador(Jugador jugador) {
        repository.updateJugador(jugador);
    }

    public void uploadPhoto(File file){
        repository.upload(file);
    }
    public void setUrl(String url) {
        repository.setUrl(url);
    }
}
