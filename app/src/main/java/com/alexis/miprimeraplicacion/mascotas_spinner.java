package com.alexis.miprimeraplicacion;


public class mascotas_spinner {

    int idMascota;

    String nombre;

    static String foto;

    public static String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getIdMascota() {
        return idMascota;
    }
    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }


    public String toString(){
        return idMascota +"-"+ nombre;

    }
}
