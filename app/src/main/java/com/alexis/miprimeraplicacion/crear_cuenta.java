package com.alexis.miprimeraplicacion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.stream.Stream;

public class crear_cuenta extends AppCompatActivity {

    FloatingActionButton fab;
    Button btn;
    DB db;
    Cursor cComprovacionUser;
    String miToken = "";
    DatabaseReference databaseReference;
    detectarInternet di;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_crear_cuenta);


        fab = findViewById(R.id.fabLogin);
        fab.setOnClickListener(view -> {
            abrirLogin();
        });
        btn = findViewById(R.id.btnCrearCuenta);
        btn.setOnClickListener(view -> {
            crearCuenta();
            abrirLogin();
        });
        obtenerToken();
    }

    private boolean validarCorreo(String correo) {
/*        if (correo.isEmpty()) {
            mostrarMsg("Error: El correo no puede estar vacío.");
            return false;
        }*/
        if (!correo.contains("@")) {
            mostrarMsg("Error: El correo debe contener '@'.");
            return false;
        }
        if (!correo.contains(".")) {
            mostrarMsg("Error: El correo debe contener un punto '.' después de '@'.");
            return false;
        }
        // Si el correo es válido, continuar con la lógica
        return true;
    }
    private void crearCuenta() {
        TextView temval = findViewById(R.id.txtUsuario);
        String usuario = temval.getText().toString();
        temval = findViewById(R.id.txtPassword);
        String contraseña = temval.getText().toString();
        temval = findViewById(R.id.txtCorreo);
        String correo = temval.getText().toString();
        temval = findViewById(R.id.txtNombreUser);
        String nombre = temval.getText().toString();

        if (!validarCorreo(correo)) {
            return;
        }

        if (nombre.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || correo.isEmpty()) {
            mostrarMsg("Por favor, completa todos los campos.");
            return;
        }
        if (comprovacionUser(usuario)) {
            return;
        }
        try {
            databaseReference = FirebaseDatabase.getInstance().getReference("cuentas");
            String key = databaseReference.push().getKey();

            if (key == null) {
               key = "";
            }
            db = new DB(this);
            String[] datos = {"", nombre, usuario, contraseña, correo,key};
            String respuesta = db.administrar_cuentas("nuevo", datos);
            mostrarMsg("Estado de la cuenta: " + respuesta);
            di = new detectarInternet(this);
            if(di.hayConexionInternet()) {
                try {

                    datosCuentaEnUso cuenta = new datosCuentaEnUso("", nombre, usuario, contraseña, correo, key);

                    if (key != null) {
                        databaseReference.child(key).setValue(cuenta).addOnSuccessListener(success -> {
                            mostrarMsg("Registro guardado con exito.");
                        }).addOnFailureListener(failure -> {
                            mostrarMsg("Error al registrar datos: " + failure.getMessage());
                        });
                    } else {
                        mostrarMsg("Error al guardar en firebase.");
                    }

                } catch (Exception e) {
                    mostrarMsg("Remote Error: " + e.getMessage());
                }
            }
        }catch (Exception e) {
            mostrarMsg("Local Error: " + e.getMessage());
        }
    }
    private boolean comprovacionUser(String user) {
        try {
            db = new DB(this);
            boolean result = false;
            cComprovacionUser = db.comprobarUser(user);

            if (cComprovacionUser.moveToFirst()) {
                mostrarMsg("El usuario que intenta crear ya existe.");
                result = true;
            }

            return result;
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
            return false;
        }
    }
    private void abrirLogin() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        Intent intent = new Intent(this, Login.class);
        startActivity(intent);
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void obtenerToken(){
        try{
            FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tarea->{
                if(!tarea.isSuccessful()){
                    mostrarMsg("Error al obtener token: "+tarea.getException().getMessage());
                }else{
                    miToken = tarea.getResult();
                }
            });
        }catch (Exception e){
            mostrarMsg("Error al obtener token: "+e.getMessage());
        }
    }
}