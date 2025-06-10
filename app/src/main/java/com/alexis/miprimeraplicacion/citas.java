package com.alexis.miprimeraplicacion;

import java.util.Date;

public class citas {
    String idCitas;
    String nombreMascota;
    String fecha;
    String clinica;
    String nota;
    String foto;
    String usuario;
    String llave;

/*    String foto;
    String miToken;*/

    public citas(String idCitas, String nombreMascota, String fecha, String clinica, String nota, String foto, String usuario,String llave) {
    //public citas(String idCitas, String nombreMascota, String fecha, String clinica, String nota, String foto, String miToken) {
        this.idCitas = idCitas;
        this.nombreMascota = nombreMascota;
        this.fecha = fecha;
        this.clinica = clinica;
        this.nota = nota;
        this.foto = foto;
        this.usuario = usuario;
        this.llave = llave;

        //this.miToken = miToken;
    }

/*    public String getMiToken() {
        return miToken;
    }

    public void setMiToken(String miToken) {
        this.miToken = miToken;
    }*/

    public String getidCitas() {
        return idCitas;
    }

    public void setidCitas(String idCitas) {
        this.idCitas = idCitas;
    }

    public String getnombreMascota() {
        return nombreMascota;
    }

    public void setnombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getfecha() {
        return fecha;
    }

    public void setfecha(String fecha) {
        this.fecha = fecha;
    }

    public String getclinica() {
        return clinica;
    }

    public void setclinica(String clinica) {
        this.clinica = clinica;
    }

    public String getnota() {
        return nota;
    }

    public void setnota(String nota) {
        this.nota = nota;
    }

    public String getusuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }
    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}