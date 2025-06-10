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

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class agregar_chat extends AppCompatActivity {
    FloatingActionButton fab;
    Button btn;
    DB db;
    Bundle parametros = new Bundle();
    TextView tempVal;
    String cuentaID;
    String accion = "nuevo", idChat = "", id="", rev="",miKey = "";
    ImageView img;
    String urlCompletaFoto = "";
    //String urlCompletaFoto = "", getUrlCompletaFotoFirestore = "";
    Intent tomarFotoIntent;
    detectarInternet di;
    DatabaseReference databaseReference;
    String miToken = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_chat);
        obtenerToken();
        img = findViewById(R.id.imgFotoMascotaChat);
       // db = new DB(this);
        btn = findViewById(R.id.btnGuardarChat);
        // btn.setOnClickListener(view -> subirFotoFirestore());
        btn.setOnClickListener(view->guardarChat());

        fab = findViewById(R.id.fabListaChat);
        fab.setOnClickListener(view->abrirVentana());
        cuentaID = datosCuentaEnUso.getIdCuenta();
        mostrarDatos();
        tomarFoto();
    }
    /*    private void subirFotoFirestore(){
        mostrarMsg("Subiendo foto a firestore");
        StorageReference reference = FirebaseStorage.getInstance().getReference();
        Uri file = Uri.fromFile(new File(urlCompletaFoto));
        final StorageReference fileRef = reference.child("fotosAmigos/"+file.getLastPathSegment());

        final UploadTask uploadTask = fileRef.putFile(file);
        uploadTask.addOnSuccessListener(taskSnapshot -> {
            fileRef.getDownloadUrl().addOnSuccessListener(uri -> {
                getUrlCompletaFotoFirestore = uri.toString();
                guardarChat();
            }).addOnFailureListener(e -> {
                mostrarMsg("Error al obtener la url de la foto: "+e.getMessage());
            });
        }).addOnFailureListener(e -> {
            mostrarMsg("Error al subir la foto: "+e.getMessage());
        });
    }llave*/
    private void abrirVentana() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cargar_layout","chat");
        intent.putExtras(parametros);
        startActivity(intent);

    }//Error al mostrar datos
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    //
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if( requestCode==1 && resultCode==RESULT_OK ){
                img.setImageURI(Uri.parse(urlCompletaFoto));
            }else{
                mostrarMsg("No se tomo la foto.");
            }
        }catch (Exception e){
            mostrarMsg("Error al tomar la foto: "+e.getMessage());
        }
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
    private void mostrarDatos(){

        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");

            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("Persona_chats"));

                //id = datos.getString("_id");
                //rev = datos.getString("_rev");
                idChat = datos.getString("idChat");
                miKey = datos.getString("token");


                tempVal = findViewById(R.id.txtNombreChatMascota);
                tempVal.setText(datos.getString("nombre"));
                tempVal = findViewById(R.id.txtDireccion);
                tempVal.setText(datos.getString("direccion"));
                tempVal = findViewById(R.id.txtTelefono);
                tempVal.setText(datos.getString("telefono"));
                tempVal = findViewById(R.id.txtEmail);
                tempVal.setText(datos.getString("email"));
                tempVal = findViewById(R.id.txtDui);
                tempVal.setText(datos.getString("dui"));
                urlCompletaFoto = datos.getString("urlFoto");
                img.setImageURI(Uri.parse(urlCompletaFoto));
            }
        }catch (Exception e){

            mostrarAlert("Error al mostrar datos: "+e.getMessage());
        }
    }
    private void guardarChat() {
        try {
            tempVal = findViewById(R.id.txtNombreChatMascota);
            String nombre = tempVal.getText().toString();
            tempVal = findViewById(R.id.txtDireccion);
            String direccion = tempVal.getText().toString();
            tempVal = findViewById(R.id.txtTelefono);
            String telefono = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtEmail);
            String email = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtDui);
            String dui = tempVal.getText().toString();

            if (nombre.isEmpty() || direccion.isEmpty() || telefono.isEmpty( ) || email.isEmpty() || dui.isEmpty()) {
                mostrarMsg("Error: Todos los campos son obligatorios.");
                return;
            }
            String[] datos = {idChat, nombre, direccion, telefono, email, dui,  urlCompletaFoto,miToken,miKey, cuentaID};

            if (accion == "modificar") {
                try {
                    db = new DB(this);
                    //String[] datos = {idChat, nombre, direccion, telefono, email, dui,  urlCompletaFoto, cuentaID,miToken};
                    String mensaje = db.administrar_Chat(accion, datos);
                    mostrarMsg("Estado del chat: " + mensaje);
                    //comienzo de actualizacion en fireBase

                    try {
                        Map<String, Object> updates = new HashMap<>();
                        updates.put("idChat",idChat );
                        updates.put("nombre", nombre);
                        updates.put("direccion", direccion);
                        updates.put("telefono", telefono);
                        updates.put("email", email);
                        updates.put("dui", dui);
                        updates.put("urlFoto", urlCompletaFoto);
                        updates.put("token", miToken);
                        updates.put("llave", miKey);
                        updates.put("usuario", cuentaID);

                        if( miToken!= null || miToken == ""){
                            databaseReference = FirebaseDatabase.getInstance().getReference("Persona_chats");
                            databaseReference.child(miKey).updateChildren(updates).addOnSuccessListener(success->{
                                mostrarMsg("Registro actualizado con exito.");
                            }).addOnFailureListener(failure->{
                                mostrarMsg("Error al actualizar datos: "+failure.getMessage());
                            });
                        } else {
                            mostrarMsg("Error al guardar en firebase.");
                        }
                    } catch (Exception e) {
                        mostrarMsg("ERROR al actualizar en Firebase: " + e.getMessage());
                    }

                } catch (Exception e) {
                    mostrarMsg("Error al actualizar: " + e.getMessage());
                }
            }else{
                try{
                db = new DB(this);
                try{
                String mensaje = db.administrar_Chat("nuevo", datos);
                mostrarMsg("Estado del chat local: " + mensaje);
                }catch (Exception e){
                    mostrarMsg("Error al guardar chat localmente: "+e.getMessage());
                    return;
                }

                if( miToken.equals("") || miToken==null ){
                    obtenerToken();
                }
                databaseReference = FirebaseDatabase.getInstance().getReference("Persona_chats");
                String key = databaseReference.push().getKey();

                chats chat = new chats(idChat, nombre, direccion, telefono, email, dui, urlCompletaFoto, miToken,key);

                if( key!= null ){
                    databaseReference.child(key).setValue(chat).addOnSuccessListener(success->{
                        //mostrarMsg("Registro guardado con exito." + mensaje);
                        abrirVentana();
                    }).addOnFailureListener(failure->{
                        mostrarMsg("Error al registrar datos: "+failure.getMessage());
                    });
                } else {
                    mostrarMsg("Error al guardar en firebase.");
                }
                }catch (Exception e){
                    mostrarAlert("Error al guardar en firebase: "+e.getMessage());
                }

            }

        }catch (Exception e){
            mostrarMsg("Error guardar: "+e.getMessage());
        }

    }

    private void mostrarAlert(String mensaje){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(mensaje);
        builder.setPositiveButton("Aceptar", (dialog, which) -> dialog.dismiss());
        builder.create().show();
    }
    private void tomarFoto(){
        img.setOnClickListener(view->{
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoChat = null;
            try{
                fotoChat = crearImagenChat();
                if( fotoChat!=null ){
                    Uri uriFotoAimgo = FileProvider.getUriForFile(agregar_chat.this,
                            "com.alexis.petcare20.fileprovider", fotoChat);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoAimgo);
                    startActivityForResult(tomarFotoIntent, 1);
                }else{
                    mostrarMsg("No se pudo crear la imagen.");
                }
            }catch (Exception e){
                mostrarMsg("Error al tomar foto: "+e.getMessage());
            }
        });
    }
    private File crearImagenChat() throws Exception{
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
}