package com.alexis.miprimeraplicacion;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONObject;

public class Login extends AppCompatActivity {

    Button btn;
    ImageButton btnIcon, btnIcon2;
    EditText editText;
    Cursor cInicio;
    TextView temval;
    JSONArray jsonArray;
    JSONObject jsonObject;

    DB db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);
        btn = findViewById(R.id.btnCrearCuenta);
        btn.setOnClickListener(view -> {
            abrirCrearCuenta();
        });
        btn = findViewById(R.id.btnIniciarSesion);
        btn.setOnClickListener(view -> {
            try {
                temval = findViewById(R.id.txtNombreUsuario);
                String user = temval.getText().toString();
                temval = findViewById(R.id.txtPassword);
                String pass = temval.getText().toString();
                inicioDeSesion(user, pass);
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
            }

        });
        db = new DB(this);
        botonOjos();

    }

    private void botonOjos() {
        //Para ver o no el password
        btnIcon = findViewById(R.id.btnOcultar);
        btnIcon.setTag("oculto");
        btnIcon.setOnClickListener(view -> {
            //Lo pongo alreves por si btnIcon.getTag() = null
            if ("oculto".equals(btnIcon.getTag())) {
                btnIcon.setImageResource(R.drawable.ojo_ver);
                btnIcon.setTag("visible");
                //Toast.makeText(this, "Ya puedes ver", Toast.LENGTH_SHORT).show();
                ver();
            } else {
                btnIcon.setImageResource(R.drawable.ojo_no_ver);
                btnIcon.setTag("oculto");
                ocultar();
                //Toast.makeText(this, "No puedes ver", Toast.LENGTH_SHORT).show();
            }

        });
    }

    private void ver() {
        editText = findViewById(R.id.txtPassword);
        editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD | android.text.InputType.TYPE_CLASS_TEXT);
    }

    private void ocultar() {
        editText = findViewById(R.id.txtPassword);
        editText.setInputType(android.text.InputType.TYPE_TEXT_VARIATION_PASSWORD | android.text.InputType.TYPE_CLASS_TEXT);
    }

    private void abrirCrearCuenta() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        Intent intent = new Intent(this, crear_cuenta.class);
        startActivity(intent);
    }

    private void abrirPantallaPrincipal() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        String id;
        String nombre;
        String usuario;
        String contrasena;
        String correo;
        String llave;

        id = cInicio.getString(0);
        nombre = cInicio.getString(1);
        usuario = cInicio.getString(2);
        contrasena = cInicio.getString(3);
        correo = cInicio.getString(4);
        llave = cInicio.getString(5);

        Intent intent = new Intent(this, MainActivity.class);
        datosCuentaEnUso.setIdCuenta(id);
        datosCuentaEnUso.setNombreCuenta(nombre);
        datosCuentaEnUso.setUsuarioCuenta(usuario);
        datosCuentaEnUso.setContraseñaCuenta(contrasena);
        datosCuentaEnUso.setCorreoCuenta(correo);
        datosCuentaEnUso.setLlaveCuenta(llave);


        startActivity(intent);
    }

    private void inicioDeSesion(String user, String password) {
        try {
            cInicio = db.iniciarSesion(user, password);
            if (cInicio.moveToFirst()) {
                abrirPantallaPrincipal();
            } else {
                mostrarMsg("Usuario o contraseña incorrectos.");
            }

        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }

    private void mostrarMsg(String msg) {
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

}