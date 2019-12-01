package com.example.equiporetrofit.Model;

import android.util.Log;

import com.example.equiporetrofit.MainActivity;
import com.example.equiporetrofit.Model.Data.Jugador;
import com.example.equiporetrofit.Model.Data.Equipo;
import com.example.equiporetrofit.Model.Rest.EquipoClient;


import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static androidx.constraintlayout.widget.Constraints.TAG;
import static com.example.equiporetrofit.MainActivity.URL;


public class Repository {

    private EquipoClient apiClient;
    private  String url=URL;
    private MutableLiveData<List<Equipo>> equipoList =new MutableLiveData<>();
    private MutableLiveData<List<Jugador>> jugadorList =new MutableLiveData<>();

    public Repository() {
        retrieveApiClient(url);
        fetchEquipoList();
        fetchJugadorList();
    }

    private void retrieveApiClient(String url){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://" + url + "/web/equipo/public/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        apiClient=retrofit.create(EquipoClient.class);

    }

    public void fetchEquipoList() {
        Call<ArrayList<Equipo>> call = apiClient.getEquipos();
        call.enqueue(new Callback<ArrayList<Equipo>>() {
            @Override
            public void onResponse(Call<ArrayList<Equipo>> call, Response<ArrayList<Equipo>> response) {
                Log.v("xxx", response.body().toString());
                equipoList.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<Equipo>> call, Throwable t) {
                Log.v("xxx", t.getLocalizedMessage());
                equipoList=new MutableLiveData<>();
            }
        });
    }
    public void fetchJugadorList() {
        Call<ArrayList<Jugador>> call = apiClient.getJugadores();
        call.enqueue(new Callback<ArrayList<Jugador>>() {
            @Override
            public void onResponse(Call<ArrayList<Jugador>> call, Response<ArrayList<Jugador>> response) {
                //Log.v(TAG, response.body().toString());
                jugadorList.setValue(response.body());
            }
            @Override
            public void onFailure(Call<ArrayList<Jugador>> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
                jugadorList=new MutableLiveData<>();
            }
        });
    }


    public LiveData<List<Equipo>> getLiveEquipoList(){
        fetchEquipoList();
        return equipoList;
    }
    public LiveData<List<Jugador>> getLiveJugadorList(){
        fetchJugadorList();
        return jugadorList;
    }


    public void addEquipo(Equipo equipo) {
        Call<Integer> call = apiClient.postEquipo(equipo);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if(response.body()!=null){
                    Log.v(TAG, response.body().toString());
                    int resultado = response.body();
                    if(resultado>0){
                        fetchEquipoList();
                    }
                }

            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void addJugador(Jugador jugador) {
        Call<Integer> call = apiClient.postJugador(jugador);
        call.enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                Log.v(TAG, response.body().toString());
                long resultado = response.body();
                if(resultado>0){
                    fetchJugadorList();
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }
    public void deleteEquipo(Equipo equipo) {
        Call<Long> call = apiClient.deleteEquipo(equipo.getId());
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
               // Log.v(TAG, response.body().toString());
                long resultado = response.body();
                if(resultado>0){
                    fetchEquipoList();
                }
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void deleteJugador(Jugador jugador) {
        Call<Long> call = apiClient.deleteJugador(jugador.getId());
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                //Log.v(TAG, response.body().toString());
                long resultado = response.body();
                if(resultado>0){
                    fetchJugadorList();
                }
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }


    public void setUrl(String url) {
        retrieveApiClient(url);
    }

    public void updateEquipo(Equipo equipo) {
        Call<Long> call = apiClient.putEquipo(equipo.getId(),equipo);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                Log.v(TAG, response.body().toString());
                fetchEquipoList();
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }

    public void updateJugador(Jugador jugador) {
        Call<Long> call = apiClient.putJugador(jugador.getId(),jugador);
        call.enqueue(new Callback<Long>() {
            @Override
            public void onResponse(Call<Long> call, Response<Long> response) {
                fetchJugadorList();
            }
            @Override
            public void onFailure(Call<Long> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }
    public void upload(File file) {
        RequestBody requestBody = RequestBody.create(MediaType.parse("image/jpg"), file);
        MultipartBody.Part request = MultipartBody.Part.createFormData("file", file.getName(), requestBody);
        Call<String> call = apiClient.fileUpload(request);
        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.v(TAG, response.body());
            }
            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.v(TAG, t.getLocalizedMessage());
            }
        });
    }
}
