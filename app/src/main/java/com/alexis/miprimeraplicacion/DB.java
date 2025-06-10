package com.alexis.miprimeraplicacion;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.internal.bind.JsonTreeReader;

import java.io.File;

public class DB extends SQLiteOpenHelper {

    public void deleteOldDatabases(Context context) {
        String[] oldDatabases = { "PetCare_1", "com.google.android.datatransport.events", "ue3.db"};

        for (String dbName : oldDatabases) {
            context.deleteDatabase(dbName);
            File dbFile = context.getDatabasePath(dbName);
            if (dbFile.exists()) {
                dbFile.delete();
            }
        }
    }
    //Nombre de la base de datos y version agp
    private static final String DATABASE_NAME = "PetCare";
    private static final int DATABASE_VERSION = 1;
    //Cración de la base de datos
    //tabla cuentas
    private static final String SQLdbCuentas = "CREATE TABLE Cuentas (idCuenta INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, usuario TEXT, contraseña TEXT, email TEXT, llave TEXT)";
    //tabla mascotas
    private static final String SQLdbMascotas = "CREATE TABLE Mascotas (idMascota INTEGER PRIMARY KEY AUTOINCREMENT, dueño TEXT, nombre TEXT, edad TEXT,raza TEXT,problemas_medicos TEXT,foto TEXT, usuario TEXT,llave TEXT)";
    //tabla de Citas
    private static final String SQLdbCitas = "CREATE TABLE Citas (idCitas INTEGER PRIMARY KEY AUTOINCREMENT, nombreMascota  TEXT, fecha DATETIME, clinica TEXT, nota TEXT, foto TEXT, usuario TEXT, llave TEXT)";
    //tabla de Chats esta ira a Firebase
    private static final String SQLdbChats = "CREATE TABLE Chat (idChat INTEGER PRIMARY KEY AUTOINCREMENT, nombre TEXT, direccion TEXT, telefono TEXT, email TEXT, dui TEXT, urlFoto TEXT, miToken TEXT,llave TEXT, usuario TEXT)";
    //private static final String SQLdbChats = "CREATE TABLE Chat (idChat TEXT, nombre TEXT, direccion TEXT, telefono TEXT, email TEXT, dui TEXT, urlFoto TEXT, miToken TEXT)";

    //Contexto de la base de datos
    public DB(Context context) {

        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Creación de la base de datos (Inicia la ejecuación para crearla)
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQLdbCuentas);
        db.execSQL(SQLdbMascotas);
        db.execSQL(SQLdbCitas);
        db.execSQL(SQLdbChats);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Actualizar la estrucutra de la base de datos si es necesario
    }
    //Métodos para administrar la base de datos
    public String administrar_cuentas(String accion, String[] datos) {
        try{
            //Escritura en la base de datos
            SQLiteDatabase db = getWritableDatabase();
            //Mensaje y consultas
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO Cuentas (nombre, usuario, contraseña, email,llave) VALUES ('"+ datos[1] +"', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4]+"', '" + datos[5]+"')";
                    break;
                case "modificar":
                    sql = "UPDATE Cuentas SET nombre = '" + datos[1] + "', usuario = '" + datos[2] + "', contraseña = '" + datos[3] + "', email = '" + datos[4] + "', llave = '" + datos[5] + "' WHERE idCuenta = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM Cuentas WHERE idCuenta = " + datos[0];
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
            //Excepción
        } catch (Exception e) {

            return e.getMessage();
        }

    }
    public String administrar_Mascota(String accion, String[] datos) {
        try{
            //Escritura en la base de datos
            SQLiteDatabase db = getWritableDatabase();
            //Mensaje y consultas
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO Mascotas (dueño, nombre, edad, raza, problemas_medicos, foto, usuario,llave) VALUES ('"+ datos[1] +"', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4]+ "', '" + datos[5] + "', '" + datos[6] + "', '" + datos[7] + "', '" + datos[8] + "')";
                    break;
                case "modificar":

                    sql = "UPDATE Mascotas SET dueño = '" + datos[1] + "', nombre = '" + datos[2] + "', edad = '" + datos[3] + "', raza = '" + datos[4] + "', problemas_medicos = '" + datos[5] + "', foto = '" + datos[6] + "', usuario = '" + datos[7] + "', llave = '" + datos[8] + "' WHERE idMascota = " + datos[0];

                    break;
                case "eliminar":
                    sql = "DELETE FROM Mascotas WHERE idMascota = " + datos[0];
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
            //Excepción
        } catch (Exception e) {

            return e.getMessage();
        }

    }
    public String administrar_Citas(String accion, String[] datos) {
        try{
            //Escritura en la base de datos
            SQLiteDatabase db = getWritableDatabase();
            //Mensaje y consultas
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO Citas (nombreMascota, fecha, clinica, nota, foto, usuario,llave) VALUES ('"+ datos[1] +"', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4]+ "', '" + datos[5]+ "', '" + datos[6]+ "', '" + datos[7]+ "')";
                    break;
                case "modificar":
                    sql = "UPDATE Citas SET nombreMascota = '" + datos[1] + "', fecha = '" + datos[2] + "', clinica = '" + datos[3] + "', nota = '" + datos[4] + "', foto = '" + datos[5] + "', usuario = '" + datos[6] + "', llave = '" + datos[7] + "' WHERE idCitas = " + datos[0];
                    break;
                case "eliminar":
                    sql = "DELETE FROM Citas WHERE idCitas = " + datos[0];
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
            //Excepción
        } catch (Exception e) {

            return e.getMessage();
        }

    }
    public String administrar_Chat(String accion, String[] datos) {
        try{
            //Escritura en la base de datos
            SQLiteDatabase db = getWritableDatabase();
            //Mensaje y consultas
            String mensaje = "ok", sql = "";
            switch (accion) {
                case "nuevo":
                    sql = "INSERT INTO Chat (nombre, direccion, telefono, email, dui, urlFoto, miToken,llave, usuario) VALUES ('"+ datos[1] +"', '" + datos[2] + "', '" + datos[3] + "', '" + datos[4] + "', '" + datos[5] + "', '" + datos[6] + "', '" + datos[7] + "', '" + datos[8] + "', '" + datos[9] + "')";
                    break;
                case "modificar":
                    sql = "UPDATE Chat SET nombre = '" + datos[1] + "', direccion = '" + datos[2] + "', telefono = '" + datos[3] + "', email = '" + datos[4] + "', dui = '" + datos[5] + "', urlFoto = '" + datos[6] + "', miToken = '" + datos[7] + "', llave = '" + datos[8] + "', usuario = '" + datos[9] + "' WHERE idChat = '" + datos[0] + "'";
                    break;
                case "eliminar":
                    sql = "DELETE FROM Chat WHERE idChat = '" + datos[0] + "'";
                    break;
            }
            db.execSQL(sql);
            db.close();
            return mensaje;
            //Excepción
        } catch (Exception e) {

            return e.getMessage();
        }

    }


    public Cursor iniciarSesion(String user, String pass){
        //bd es el ejecutador de consultas
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT idCuenta,nombre,usuario,contraseña,email,llave FROM Cuentas WHERE usuario   = '" + user + "' AND contraseña  = '" + pass + "'";

        return db.rawQuery(sql, null);
    }

    public Cursor comprobarUser(String user){
        //bd es el ejecutador de consultas
        SQLiteDatabase db = getReadableDatabase();
        String sql = "SELECT idCuenta,nombre,usuario,contraseña,email,llave FROM Cuentas WHERE usuario   = '" + user + "'";

        return db.rawQuery(sql, null);
    }

    public Cursor lista_Citas(String idCuenta){
        //bd es el ejecutador de consultas
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Citas WHERE usuario = '" + idCuenta + "'", null);
    }
    public Cursor lista_cuentas(){
        //bd es el ejecutador de consultas
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Cuentas", null);
    }
    public Cursor lista_nombre_mascota_citas(String idCuenta){
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT idMascota, nombre, foto FROM Mascotas WHERE usuario = '" + idCuenta + "'", null);
    }
    public Cursor lista_mascotas(String idCuenta){
        //bd es el ejecutador de consultas
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Mascotas WHERE usuario = '" + idCuenta + "'", null);
    }
    public Cursor lista_chat(String idCuenta){
        //bd es el ejecutador de consultas
        SQLiteDatabase db = getReadableDatabase();
        return db.rawQuery("SELECT * FROM Chat WHERE usuario = '" + idCuenta + "'", null);
    }

}