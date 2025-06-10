package com.alexis.miprimeraplicacion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class agregar_mascotas extends AppCompatActivity {
    FloatingActionButton fab;
    DB db;
    ImageView img;
    String idMascota = "";
    String urlCompletaFoto = "";
    String accion = "nuevo";
    Button btnGuardarMascota;
    Intent tomarFotoMascotaIntent;
    String cuentaID;
    String miToken = "";
    String miKey = "";
    detectarInternet di;
    DatabaseReference databaseReference;
    TextView temval;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_agregar_mascotas);
        db = new DB(this);
        img = findViewById(R.id.imgFotoMascotaCita);

        fab = findViewById(R.id.fabListaMascotas);
        fab.setOnClickListener(view -> {
            abrirListaMascotas();
        });

        btnGuardarMascota = findViewById(R.id.btnGuardarMascota);
        btnGuardarMascota.setOnClickListener(view -> {
            guardarMascota();
        });
        cuentaID = datosCuentaEnUso.getIdCuenta();
        //mostrarMsg("Este es el usuario: " + cuentaID);
        mostarDatosMascotaModificar();
        tomarFoto();
        obtenerToken();
    }

    private void mostarDatosMascotaModificar() {

        try {

            Bundle parameters = getIntent().getExtras();
            accion = parameters.getString("accion");


            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parameters.getString("mascotas"));
                TextView temval = findViewById(R.id.txtNombreMascota);
                temval.setText(datos.getString("nombre"));
                temval = findViewById(R.id.txtEdad);
                temval.setText(datos.getString("edad"));
                temval = findViewById(R.id.txtRaza);
                temval.setText(datos.getString("raza"));
                temval = findViewById(R.id.txtProblemasMedicos);
                temval.setText(datos.getString("problemas_medicos"));
                temval = findViewById(R.id.txtDueño);
                temval.setText(datos.getString("dueño"));

                miKey = datos.getString("llave");
                idMascota = datos.getString("idMascota");
                urlCompletaFoto = datos.getString("foto");
                if (!urlCompletaFoto.isEmpty()) {
                    img.setImageURI(Uri.parse(urlCompletaFoto));
                }

            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }

    }

    private void guardarMascota() {
    try {
        db = new DB(this);
        temval = findViewById(R.id.txtNombreMascota);
        String nombre = temval.getText().toString();
        temval = findViewById(R.id.txtEdad);
        String edad = temval.getText().toString();
        temval = findViewById(R.id.txtRaza);
        String raza = temval.getText().toString();
        temval = findViewById(R.id.txtProblemasMedicos);
        String problemasMedicos = temval.getText().toString();
        temval = findViewById(R.id.txtDueño);
        String dueño = temval.getText().toString();

        if (nombre.isEmpty() || edad.isEmpty() || raza.isEmpty() || problemasMedicos.isEmpty() || dueño.isEmpty()) {
            mostrarMsg("Por favor, completa todos los campos.");
            return;
        }
        cuentaID = datosCuentaEnUso.getIdCuenta();
        String datosMascota[] = {idMascota, dueño, nombre, edad, raza, problemasMedicos, urlCompletaFoto, cuentaID,miKey};
        //Toast.makeText(getApplicationContext(), "Datos: " + datosMascota[7], Toast.LENGTH_LONG).show();
        if (accion.equals("modificar")) {
           try {
               db = new DB(this);
               String respuesta =  db.administrar_Mascota("modificar", datosMascota);
               mostrarMsg("Estado del registro: " + respuesta );


               di = new detectarInternet(this);
               if(di.hayConexionInternet()) {
                   try {  //comienzo de modificacion en firebase
                       Map<String, Object> updates = new HashMap<>();
                       updates.put("idMascota", idMascota);
                       updates.put("dueño", dueño);
                       updates.put("nombre", nombre);
                       updates.put("raza", raza);
                       updates.put("problemas_medicos", problemasMedicos);
                       updates.put("foto", urlCompletaFoto);
                       updates.put("usuario", cuentaID);
                       updates.put("llave", miKey);
                       databaseReference = FirebaseDatabase.getInstance().getReference("mascotas");
                       if (miKey != null || miKey == "") {
                           databaseReference.child(miKey).updateChildren(updates).addOnSuccessListener(success -> {
                               mostrarMsg("Registro actualizado con exito.");
                           }).addOnFailureListener(failure -> {
                               mostrarMsg("Error al registrar datos: " + failure.getMessage());
                           });
                       } else {
                           mostrarMsg("Error al guardar modificaciones en firebase.");
                       }

                   } catch (Exception e) {
                       mostrarMsg("Error al modificar: " + e.getMessage());
                   }
               }
               comprovacion(respuesta);
           }catch (Exception e){
               mostrarMsg("Error al modificar: " + e.getMessage());
           }


        } else {

            String respuesta =   db.administrar_Mascota("nuevo", datosMascota);
            mostrarMsg("Estado del registro: " + respuesta );


            if( miToken.equals("") || miToken==null ){
                obtenerToken();
            }
            di = new detectarInternet(this);
            if(di.hayConexionInternet()) {
                try {
                    databaseReference = FirebaseDatabase.getInstance().getReference("mascotas");
                    String key = databaseReference.push().getKey();

                    mascotas mascotas = new mascotas(idMascota, dueño, nombre, edad, raza, problemasMedicos, urlCompletaFoto, cuentaID, key);
                    if (key != null) {
                        databaseReference.child(key).setValue(mascotas).addOnSuccessListener(success -> {
                            mostrarMsg("Registro guardado con exito.");
                        }).addOnFailureListener(failure -> {
                            mostrarMsg("Error al registrar datos: " + failure.getMessage());
                        });
                    } else {
                        mostrarMsg("Error al guardar en firebase.");
                    }
                } catch (Exception e) {
                    mostrarMsg("Error: " + e.getMessage());
                }
            }
            comprovacion(respuesta);

        }

    } catch (Exception e) {
        mostrarMsg("Error: " + e.getMessage());
    }
    }

    private void comprovacion(String respuesta){
        if (respuesta.equals("ok")){
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("cargar_layout","mascotas");
                startActivity(intent);
        }
    }


    private void tomarFoto(){
        img.setOnClickListener(view->{
            tomarFotoMascotaIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoMascota = null;
            try{
                fotoMascota = crearImagenMascota();
                if( fotoMascota!=null ){
                    Uri uriFotoMascota = FileProvider.getUriForFile(agregar_mascotas.this,
                            "com.alexis.petcare20.fileprovider", fotoMascota);
                    tomarFotoMascotaIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoMascota);
                    startActivityForResult(tomarFotoMascotaIntent, 1);
                }else{
                    mostrarMsg("No fue posible tomar la fotografia..");
                }
            }catch (Exception e){
                mostrarMsg("Error: "+e.getMessage());
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                        img.setImageURI(Uri.parse(urlCompletaFoto));
            }else{
                mostrarMsg("No fue posible tomar la fotografia.");
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }

    private File crearImagenMascota() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_"+ fechaHoraMs+"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);

                urlCompletaFoto = image.getAbsolutePath();

        return image;
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }

    private void abrirListaMascotas() {
        // Aquí puedes abrir la ventana de agregar citas
        // Por ejemplo, puedes iniciar una nueva actividad
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
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