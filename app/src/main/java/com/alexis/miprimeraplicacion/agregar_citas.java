package com.alexis.miprimeraplicacion;
import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class agregar_citas extends AppCompatActivity {
/*    private MainActivity mainActivity; // Referencia a MainActivity

    public void setMainActivity(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }*/
    FloatingActionButton fab;
    Button btn;
//    TextView tempVal;
    DB db;
    Bundle parametros = new Bundle();
    String accion = "nuevo";
    String idCitas = "";
    int idMascota = 0;
    String cuentaID, user;
    ImageView img;
    String urlCompletaFoto = "";
    Intent tomarFotoIntent;
    String miToken = "";

    String miKey = "";
    DatabaseReference databaseReference;
    Spinner spn;
    String nombre_spinner;
    mascotas_spinner mascotaSeleccionada;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_agregar_citas);
        img = findViewById(R.id.imgFotoMascotaCita);
        spn = findViewById(R.id.spnNombresCitaMascota);

        db = new DB(this);
        //usuario = getIntent().getStringExtra("usuarioCuenta");

        btn = findViewById(R.id.btnGuardarCita);
        btn.setOnClickListener(view->guardarCita());

        fab = findViewById(R.id.fabListaCitasMascotas);
        fab.setOnClickListener(view->abrirVentana());
        cuentaID = datosCuentaEnUso.getIdCuenta();
       // mostrarMsg("Este es el usuario: " + cuentaID);
        mostrarDatos();
        try {
            tomarFoto();
        }catch (Exception e){
            mostrarMsg("Error al tomar foto: "+e.getMessage());
        }
        List<mascotas_spinner> listaNombres = nombresMascotas();
        ArrayAdapter<mascotas_spinner> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, listaNombres);
        spn.setAdapter(adapter);
        spn.setOnItemSelectedListener(new android.widget.AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(android.widget.AdapterView<?> parent, View view, int position, long id) {
                try {
                    mascotas_spinner mascotaSeleccionada = (mascotas_spinner) parent.getItemAtPosition(position);
                    mascotas_spinner mascotaSeleccionadaFoto = (mascotas_spinner) parent.getItemAtPosition(position);
                    nombre_spinner = mascotaSeleccionada.getNombre().toString();
                    idMascota = mascotaSeleccionada.getIdMascota();

                    //urlCompletaFoto = mascotaSeleccionada.getFoto();
                   // urlCompletaFoto = mascotaSeleccionadaFoto.getFoto().toString();
                    img.setImageURI(Uri.parse(urlCompletaFoto));
                    //mostrarAlertDialog(img.getContext().toString());
                }catch (Exception e){
                    mostrarMsg("Error al seleccionar mascota: " + e.getMessage());
                }
            }

            @Override
            public void onNothingSelected(android.widget.AdapterView<?> parent) {
                // No hacer nada si no se selecciona nada
            }
        });

    }

    private List<mascotas_spinner> nombresMascotas(){
        List<mascotas_spinner> listaNombres = new ArrayList<>();
        Cursor cursor = db.lista_nombre_mascota_citas(cuentaID);
        if (cursor != null){
        if( cursor.moveToFirst() ){
            do{
                // Verificar si la columna existe antes de acceder a ella
                if (cursor.getColumnIndex("idMascota") == -1 || cursor.getColumnIndex("nombre") == -1) {
                    mostrarMsg("Error: Columnas no encontradas en la consulta.");
                }else{
                    mascotas_spinner mascota = new mascotas_spinner();
                    mascota.setIdMascota(cursor.getInt(cursor.getColumnIndex("idMascota")));
                    mascota.setNombre(cursor.getString(cursor.getColumnIndex("nombre")));
                   // urlCompletaFoto = cursor.getString(cursor.getColumnIndex("foto")); Error 1
                    listaNombres.add(mascota);
                }
               // mascota.setFoto(cursor.getString(2));
            }while(cursor.moveToNext());
        }
        db.close();
        }else{
            mostrarMsg("No se encontraron mascotas.");
        }
        return listaNombres;
    }

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
    public boolean FechaValida(String fecha, String formato) {
        SimpleDateFormat FormatoFechaValido = new SimpleDateFormat(formato);
        FormatoFechaValido.setLenient(false); // Esto evita que fechas como "31/02/2023" se ajusten automáticamente (Esta en modo estricto)
        Date fechaActual = new Date();
        try {
            Date fechaIngresada = FormatoFechaValido.parse(fecha);
            if (fechaIngresada.before(fechaActual)) {
                mostrarMsg("La fecha no puede ser anterior a la fecha actual.");
                return false; // La fecha no es válida
            }

            return true; // Si no lanza excepción, la fecha es válida
        } catch (Exception e) {
            return false; // La fecha no es válida
        }
    }
/*    public boolean FechaValida(String fecha, String formato) {
        SimpleDateFormat FormatoFechaValido = new SimpleDateFormat(formato);
        FormatoFechaValido.setLenient(false); // Esto evita que fechas como "31/02/2023" se ajusten automáticamente (Esta en modo estricto)
        Date fechaActual = new Date();
        try {
            Date fechaIngresada = FormatoFechaValido.parse(fecha);
            if(fecha != "dd/MM/yyyy") {
                mostrarMsg("Formato de fecha inválido. Use el formato dd/MM/yyyy.");
                return false; // La fecha no es válida
            }else if(fechaIngresada.before(fechaActual)) {
                    mostrarMsg("La fecha no puede ser anterior a la fecha actual.");
                    return false; // La fecha no es válida
            }

            return true; // Si no lanza excepción, la fecha es válida
        } catch (Exception e) {
            return false; // La fecha no es válida
        }
    }*/
    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");


            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("citas"));

                idCitas = datos.getString("idCitas");
                miKey = datos.getString("llave");
                //nombre_spinner = datos.getString("nombreMascota");
                TextView tempVal;

               // mostrarMsg(datos.getString("nombre") + "sfsdf");


                tempVal = findViewById(R.id.txtFecha);
                tempVal.setText(datos.getString("fecha"));

                tempVal = findViewById(R.id.txtNombreClinica);
                tempVal.setText(datos.getString("clinica"));

                tempVal = findViewById(R.id.txtNota);
                tempVal.setText(datos.getString("nota"));

                urlCompletaFoto = datos.getString("foto");
                img.setImageURI(Uri.parse(urlCompletaFoto));
            }

        }catch (Exception e){
/*
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("ERROR");
            confirmacion.setMessage("Este es el error: "+ e.getMessage());
            confirmacion.create().show();
*/

            mostrarMsg("Error 1: "+e.getMessage());
        }
    }
    private void abrirVentana() {

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("cargar_layout","citas");
        intent.putExtras(parametros);
        startActivity(intent);

    }
    private void guardarCita(){

/*        TextView  tempVal = findViewById(R.id.txtNombreCitaMascota);
        String nombreMascota = tempVal.getText().toString();*/

        TextView  tempVal;

        tempVal = findViewById(R.id.txtFecha);
        String fecha = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtNombreClinica);
        String clinica = tempVal.getText().toString();

        tempVal = findViewById(R.id.txtNota);
        String nota = tempVal.getText().toString();

        //String usuario = getIntent().getStringExtra("usuarioCuenta");
        if(!FechaValida(fecha, "dd/MM/yyyy")) {
            mostrarMsg("Error: Fecha no válida. Formato esperado: dd/MM/yyyy");
            return;
        }
        //FechaValida(fecha, "dd/MM/yyyy");
        if (fecha.isEmpty() || clinica.isEmpty() || nota.isEmpty()) {
        //if (nombreMascota.isEmpty() || fecha.isEmpty() || clinica.isEmpty() || nota.isEmpty()) {
            mostrarMsg("Error: Todos los campos son obligatorios.");
            return;
        }
        cuentaID = datosCuentaEnUso.getIdCuenta();
        if (cuentaID == null || cuentaID.isEmpty()) {
            mostrarMsg("Error: Usuario no encontrado.");
            return;
        }

        //Toast.makeText(getApplicationContext(), "Datos: " + datos[5], Toast.LENGTH_LONG).show();


        if (accion == "modificar") {
            try {
            String[] datos = {idCitas, nombre_spinner, fecha, clinica, nota, urlCompletaFoto, cuentaID,miKey};
            String mensaje = db.administrar_Citas(accion, datos);
            mostrarMsg("Estado de la cita: " + mensaje);
            //comienzo de actualizacion en fireBase

            try {
                Map<String, Object> updates = new HashMap<>();
                updates.put("idCitas",idCitas );
                updates.put("nombreMascota", nombre_spinner);
                updates.put("fecha", fecha);
                updates.put("clinica", clinica);
                updates.put("nota", nota);
                updates.put("urlFoto", urlCompletaFoto);
                updates.put("usuario", cuentaID);
                updates.put("llave", miKey);

                if( miKey!= null || miKey == ""){
                    databaseReference = FirebaseDatabase.getInstance().getReference("citas");
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
            if( miToken.equals("") || miToken==null ){
                obtenerToken();
            }
            try{

                databaseReference = FirebaseDatabase.getInstance().getReference("citas");
                String key = databaseReference.push().getKey();

                if (key == null) {
                    key = "";
                }

                String[] datos = {idCitas, nombre_spinner, fecha, clinica, nota, urlCompletaFoto, cuentaID,key};
                String mensaje = db.administrar_Citas(accion, datos);

                citas cita = new citas(idCitas, nombre_spinner, fecha, clinica, nota, urlCompletaFoto, cuentaID,key);
                if( key!= null ){
                    databaseReference.child(key).setValue(cita).addOnSuccessListener(success->{
                        mostrarMsg("Registro guardado con exito.");
                    }).addOnFailureListener(failure->{
                        mostrarMsg("Error al registrar datos: "+failure.getMessage());
                    });
                } else {
                    mostrarMsg("Error al guardar en firebase.");
                }
                mostrarMsg("Estado de la cita: " + mensaje);
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
            }
        }

       // db.administrar_Citas(accion, datos);

/*        AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
        confirmacion.setTitle("ERROR");
        confirmacion.setMessage("Este es el error: "+ mensaje.toString());
        confirmacion.create().show();*/

        abrirVentana();

    }
    private void mostrarAlertDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setPositiveButton("Aceptar", null);
        builder.create().show();
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
    private void tomarFoto(){
        img.setOnClickListener(view->{
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoChat = null;
            try{
                fotoChat = crearImagenCita();
                if( fotoChat!=null ){
                    Uri uriFotoAimgo = FileProvider.getUriForFile(agregar_citas.this,
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
    private File crearImagenCita() throws Exception{
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