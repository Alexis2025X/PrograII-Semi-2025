package com.alexis.miprimeraplicacion;

import com.google.gson.internal.bind.JsonTreeReader;

public class chats {
    String idChat;
    String nombre;
    String direccion;
    String telefono;
    String email;
    String dui;
    String foto;
    //String urlCompletaFotoFirestore;
    String token;
    String llave;

    public chats() {}
/*    public String getUrlCompletaFotoFirestore() {
        return urlCompletaFotoFirestore;
    }
    public void setUrlCompletaFotoFirestore(String urlCompletaFotoFirestore) {
        this.urlCompletaFotoFirestore = urlCompletaFotoFirestore;
    }*/
public chats(String idChat, String nombre, String direccion, String telefono, String email, String dui, String foto, String token, String llave) {
    this.idChat = idChat;
    this.nombre = nombre;
    this.direccion = direccion;
    this.telefono = telefono;
    this.email = email;
    this.dui = dui;
    this.foto = foto;
    this.token = token;
    this.token = llave;
    //this.urlCompletaFotoFirestore = urlCompletaFotoFirestore;
}

    public String getLlave() {
        return llave;
    }

    public void setLlave(String llave) {
        this.llave = llave;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdChat() {
        return idChat;
    }

    public void setIdChat(String IdChat) {
        this.idChat = IdChat;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}