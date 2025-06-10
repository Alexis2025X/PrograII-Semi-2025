package com.alexis.miprimeraplicacion;

import java.util.Base64;

public class utilidades {
    //La Ip tendras que cambiarla ya que es la de tu computadora y es dinamica
    static String url_consulta = "http://192.168.1.3:5984/agenda/_design/agenda/_view/agenda";
    static String url_mto = "http://192.168.1.3:5984/agenda";
    //Usuario y contraseña de la base de datos en CouchDB (La cambie para subir cambios)
    static String user = "admin";
    static String password = "??????";
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user + ":"+ password).getBytes());
    public String generarUnicoId(){
        return java.util.UUID.randomUUID().toString();
    }

}
