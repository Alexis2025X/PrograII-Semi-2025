package com.alexis.miprimeraplicacion;
import java.util.Base64;
public class utilidades {
    static String url_consulta = "http://192.168.1.3:5984/tienda/_design/tienda/_view/tienda";
    static String url_mto = "http://192.168.1.3:5984/tienda";
    static String user = "admin";//Agregar usuario
    static String passwd = "HolaMundo2007!";//Agregar contraseña
    static String credencialesCodificadas = Base64.getEncoder().encodeToString((user + ":" + passwd).getBytes());
    public String generarUnicoId(){
        return java.util.UUID.randomUUID().toString();
    }
}
