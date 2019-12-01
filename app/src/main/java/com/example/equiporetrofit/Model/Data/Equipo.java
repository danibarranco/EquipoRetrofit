package com.example.equiporetrofit.Model.Data;

import android.os.Parcel;
import android.os.Parcelable;

public class Equipo implements Parcelable {
    private long id;
    private String nombre;
    private String ciudad;
    private String estadio;
    private long aforo;
    private String escudo;

    public Equipo(){
        this(0,"","","",0,"");
    }

    public Equipo(long id, String nombre, String ciudad, String estadio, long aforo, String escudo) {
        this.id = id;
        this.nombre = nombre;
        this.ciudad = ciudad;
        this.estadio = estadio;
        this.aforo = aforo;
        this.escudo = escudo;
    }

    protected Equipo(Parcel in) {
        id = in.readLong();
        nombre = in.readString();
        ciudad = in.readString();
        estadio = in.readString();
        aforo = in.readLong();
        escudo = in.readString();
    }

    public static final Creator<Equipo> CREATOR = new Creator<Equipo>() {
        @Override
        public Equipo createFromParcel(Parcel in) {
            return new Equipo(in);
        }

        @Override
        public Equipo[] newArray(int size) {
            return new Equipo[size];
        }
    };

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCiudad() {
        return ciudad;
    }

    public void setCiudad(String ciudad) {
        this.ciudad = ciudad;
    }

    public String getEstadio() {
        return estadio;
    }

    public void setEstadio(String estadio) {
        this.estadio = estadio;
    }

    public long getAforo() {
        return aforo;
    }

    public void setAforo(long aforo) {
        this.aforo = aforo;
    }

    public String getEscudo() {
        return escudo;
    }

    public void setEscudo(String escudo) {
        this.escudo = escudo;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(nombre);
        parcel.writeString(ciudad);
        parcel.writeString(estadio);
        parcel.writeLong(aforo);
        parcel.writeString(escudo);
    }
}
