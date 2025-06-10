package com.alexis.miprimeraplicacion;

public class datosCuentaEnUso {
    public static String idCuenta;
    public static String nombreCuenta;
    public static String usuarioCuenta;
    public static String contraseñaCuenta;
    public static String correoCuenta;
    public static String llaveCuenta;


     String constructorIdCuenta;
     String constructorNombreCuenta;

    public String getConstructorContraseñaCuenta() {
        return constructorContraseñaCuenta;
    }

    public void setConstructorContraseñaCuenta(String constructorContraseñaCuenta) {
        this.constructorContraseñaCuenta = constructorContraseñaCuenta;
    }

    public String getConstructorCorreoCuenta() {
        return constructorCorreoCuenta;
    }

    public void setConstructorCorreoCuenta(String constructorCorreoCuenta) {
        this.constructorCorreoCuenta = constructorCorreoCuenta;
    }

    public String getConstructorIdCuenta() {
        return constructorIdCuenta;
    }

    public void setConstructorIdCuenta(String constructorIdCuenta) {
        this.constructorIdCuenta = constructorIdCuenta;
    }

    public String getConstructorKeyCuenta() {
        return constructorKeyCuenta;
    }

    public void setConstructorKeyCuenta(String constructorKeyCuenta) {
        this.constructorKeyCuenta = constructorKeyCuenta;
    }

    public String getConstructorNombreCuenta() {
        return constructorNombreCuenta;
    }

    public void setConstructorNombreCuenta(String constructorNombreCuenta) {
        this.constructorNombreCuenta = constructorNombreCuenta;
    }

    public String getConstructorUsuarioCuenta() {
        return constructorUsuarioCuenta;
    }

    public void setConstructorUsuarioCuenta(String constructorUsuarioCuenta) {
        this.constructorUsuarioCuenta = constructorUsuarioCuenta;
    }

    String constructorUsuarioCuenta;
     String constructorContraseñaCuenta;
     String constructorCorreoCuenta;
     String constructorKeyCuenta;

    public datosCuentaEnUso(String constructorIdCuenta, String constructorNombreCuenta, String constructorUsuarioCuenta, String constructorContraseñaCuenta, String constructorCorreoCuenta, String constructorKeyCuenta) {
        this.constructorContraseñaCuenta = constructorContraseñaCuenta;
        this.constructorCorreoCuenta = constructorCorreoCuenta;
        this.constructorIdCuenta = constructorIdCuenta;
        this.constructorNombreCuenta = constructorNombreCuenta;
        this.constructorUsuarioCuenta = constructorUsuarioCuenta;
        this.constructorKeyCuenta = constructorKeyCuenta;
    }

    public static String getContraseñaCuenta() {
        return contraseñaCuenta;
    }

    public static void setContraseñaCuenta(String contraseñaCuenta) {
        datosCuentaEnUso.contraseñaCuenta = contraseñaCuenta;
    }

    public static String getCorreoCuenta() {
        return correoCuenta;
    }

    public static void setCorreoCuenta(String correoCuenta) {
        datosCuentaEnUso.correoCuenta = correoCuenta;
    }

    public static String getIdCuenta() {
        return idCuenta;
    }

    public static void setIdCuenta(String idCuenta) {
        datosCuentaEnUso.idCuenta = idCuenta;
    }

    public static String getNombreCuenta() {
        return nombreCuenta;
    }

    public static void setNombreCuenta(String nombreCuenta) {
        datosCuentaEnUso.nombreCuenta = nombreCuenta;
    }

    public static String getUsuarioCuenta() {
        return usuarioCuenta;
    }

    public static void setUsuarioCuenta(String usuarioCuenta) {
        datosCuentaEnUso.usuarioCuenta = usuarioCuenta;
    }

    public static String getLlaveCuenta() {
        return llaveCuenta;
    }

    public static void setLlaveCuenta(String llaveCuenta) {
        datosCuentaEnUso.llaveCuenta = llaveCuenta;
    }
}
