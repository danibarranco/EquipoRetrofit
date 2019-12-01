package com.example.equiporetrofit.Model.Rest;

import com.example.equiporetrofit.Model.Data.Jugador;
import com.example.equiporetrofit.Model.Data.Equipo;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface EquipoClient {

    @DELETE("jugador/{id}")
    Call<Long> deleteJugador(@Path("id") long id);

    @GET("jugador/{id}")
    Call<Jugador> getJugador(@Path("id") long id);

    @GET("jugador")
    Call<ArrayList<Jugador>> getJugadores();

    @POST("jugador")
    Call<Integer> postJugador(@Body Jugador jugador);

    @PUT("jugador/{id}")
    Call<Long> putJugador(@Path("id") long id, @Body Jugador jugador);

    //////////////////////Equipo

    @DELETE("equipo/{id}")
    Call<Long> deleteEquipo(@Path("id") long id);

    @GET("equipo/{id}")
    Call<Equipo> getEquipo(@Path("id") long id);

    @GET("equipo")
    Call<ArrayList<Equipo>> getEquipos();

    @POST("equipo")
    Call<Integer> postEquipo(@Body Equipo equipo);

    @PUT("equipo/{id}")
    Call<Long> putEquipo(@Path("id") long id, @Body Equipo equipo);

    //Upload image
    @Multipart
    @POST("upload")
    Call<String> fileUpload(@Part MultipartBody.Part file);
}
