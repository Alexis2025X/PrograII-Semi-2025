package com.alexis.miprimeraplicacion;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    FloatingActionButton fab;
    Button btn;
    TextView tempVal;
    DB db;
    String accion = "nuevo", idProducto = "", id="", rev="";
    ImageView img;
    ImageView img2;
    ImageView img3;
    String urlCompletaFoto = "";
    String urlCompletaFoto2 = "";
    String urlCompletaFoto3 = "";
    Uri uriFotoSeleccionada;
    Intent tomarFotoIntent;
    utilidades utls;
    detectarInternet di;
    private int contadorClicks = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utls = new utilidades();
        img = findViewById(R.id.imgFotoProducto);
        //Agregue
        img2 = findViewById(R.id.imgFotoProducto2);
        img3 = findViewById(R.id.imgFotoProducto3);
        db = new DB(this);
        btn = findViewById(R.id.btnGuardarProducto);
        btn.setOnClickListener(view->guardarAmigo());

        fab = findViewById(R.id.fabListaProductos);
        fab.setOnClickListener(view->abrirVentana());

        mostrarDatos();
        opcionesFoto();
        //posicion();
        Button btnAdelante = findViewById(R.id.btnAdelante);
        btnAdelante.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contadorClicks++;
                if (contadorClicks == 3) {
                    img = findViewById(R.id.imgFotoProducto2);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto3);
                    img.setVisibility(View.VISIBLE);
                    //mostrarMsg("Ha llegado al final de las fotos");
                    tempVal = findViewById(R.id.lblPosicion);
                    tempVal.setText("3 de 3");
                    mostrarMsg("Esta en la ultima foto");
                } else if (contadorClicks == 2) {
                    img = findViewById(R.id.imgFotoProducto);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto3);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto2);
                    img.setVisibility(View.VISIBLE);
                    tempVal = findViewById(R.id.lblPosicion);
                    tempVal.setText("2 de 3");
                } else if (contadorClicks == 1) {
                    img = findViewById(R.id.imgFotoProducto);
                    img.setVisibility(View.VISIBLE);
                    img = findViewById(R.id.imgFotoProducto2);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto3);
                    img.setVisibility(View.GONE);
                    tempVal = findViewById(R.id.lblPosicion);
                    tempVal.setText("1 de 3");
                }
            }
        });
        Button btnAtras = findViewById(R.id.btnAtras);
        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contadorClicks--;
                if (contadorClicks == 1) {
                    img = findViewById(R.id.imgFotoProducto);
                    img.setVisibility(View.VISIBLE);
                    img = findViewById(R.id.imgFotoProducto2);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto3);
                    img.setVisibility(View.GONE);
                    //mostrarMsg("Ha llegado al final de las fotos");
                    mostrarMsg("Esta en la primera foto");
                    tempVal = findViewById(R.id.lblPosicion);
                    tempVal.setText("1 de 3");
                } else if (contadorClicks == 2) {
                    img = findViewById(R.id.imgFotoProducto);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto3);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto2);
                    img.setVisibility(View.VISIBLE);
                    tempVal = findViewById(R.id.lblPosicion);
                    tempVal.setText("2 de 3");
                } else if (contadorClicks == 3) {
                    img = findViewById(R.id.imgFotoProducto2);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto);
                    img.setVisibility(View.GONE);
                    img = findViewById(R.id.imgFotoProducto3);
                    img.setVisibility(View.VISIBLE);
                    tempVal = findViewById(R.id.lblPosicion);
                    tempVal.setText("3 de 3");
                }
                }

        });
        /*tomarFoto();
        abrirrGaleria();*/
    }



    private void opcionesFoto(){
        img.setOnClickListener(view -> {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Seleccionar foto");
        builder.setMessage("¿Desea abrir la camara o la galeria?");
        builder.setPositiveButton("Abrir camar", (dialogInterface, i) -> tomarFoto());
        builder.setNegativeButton("Abrir galeria", (dialogInterface, i) -> abrirGaleria());
        builder.show();
        });
        img2.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleccionar foto");
            builder.setMessage("¿Desea abrir la camara o la galeria?");
            builder.setPositiveButton("Abrir camar", (dialogInterface, i) -> tomarFoto());
            builder.setNegativeButton("Abrir galeria", (dialogInterface, i) -> abrirGaleria());
            builder.show();
        });
        img3.setOnClickListener(view -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Seleccionar foto");
            builder.setMessage("¿Desea abrir la camara o la galeria?");
            builder.setPositiveButton("Abrir camar", (dialogInterface, i) -> tomarFoto());
            builder.setNegativeButton("Abrir galeria", (dialogInterface, i) -> abrirGaleria());
            builder.show();
        });

    }

    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");
            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("amigos"));
                id = datos.getString("_id");
                rev = datos.getString("_rev");
                idProducto = datos.getString("idProducto");

                tempVal = findViewById(R.id.txtCodigo);
                tempVal.setText(datos.getString("codigo"));

                tempVal = findViewById(R.id.txtDescripcion);
                tempVal.setText(datos.getString("descripcion"));

                tempVal = findViewById(R.id.txtMarca);
                tempVal.setText(datos.getString("marca"));

                tempVal = findViewById(R.id.txtPresentacion);
                tempVal.setText(datos.getString("presentacion"));

                tempVal = findViewById(R.id.txtPrecio);
                tempVal.setText(datos.getString("precio"));

                urlCompletaFoto = datos.getString("urlFoto");
                img.setImageURI(Uri.parse(urlCompletaFoto));

                urlCompletaFoto2 = datos.getString("urlFoto2");
                img2.setImageURI(Uri.parse(urlCompletaFoto2));

                urlCompletaFoto3 = datos.getString("urlFoto3");
                img3.setImageURI(Uri.parse(urlCompletaFoto3));
            }else {
                idProducto = utls.generarUnicoId();
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }
    //private static final int REQUEST_IMAGE_GALLERY = 1;

    private void abrirGaleria() {
        //img.setOnClickListener(view -> {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*");
        //startActivityForResult(Intent.createChooser(galleryIntent, "Seleccionar imagen"), 1);
        Galeria.launch(galleryIntent);

        //
        //});
    }
    private ActivityResultLauncher<Intent> Galeria = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                    Intent data = result.getData();
                    uriFotoSeleccionada = data.getData(); // Guarda la URI
                    img.setImageURI(uriFotoSeleccionada);// Muestra la imagen
                    img2.setImageURI(uriFotoSeleccionada);// Muestra la imagen
                    img3.setImageURI(uriFotoSeleccionada);// Muestra la imagen
                    urlCompletaFoto = uriFotoSeleccionada.toString();
                    mostrarMsg(urlCompletaFoto);
                   // startActivityForResult(uriFotoSeleccionada, 1);
                } else {
                    mostrarMsg("No se seleccionó la imagen.");
                }
            });

    private void tomarFoto(){
        //img.setOnClickListener(view->{
        if (contadorClicks == 1) {
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoAmigo = null;
            try{
                fotoAmigo = crearImagenAmigo();
                if( fotoAmigo!=null ){
                    Uri uriFotoAimgo = FileProvider.getUriForFile(MainActivity.this,
                            "com.alexis.miprimeraplicacion.fileprovider", fotoAmigo);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoAimgo);
                    startActivityForResult(tomarFotoIntent, 1);
                }else{
                    mostrarMsg("No se pudo crear la imagen.");
                }
            }catch (Exception e){
                mostrarMsg("Error: "+e.getMessage());
            }
        }else if (contadorClicks == 2) {
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoAmigo = null;
            try{
                fotoAmigo = crearImagenAmigo();
                if( fotoAmigo!=null ){
                    Uri uriFotoAimgo = FileProvider.getUriForFile(MainActivity.this,
                            "com.alexis.miprimeraplicacion.fileprovider", fotoAmigo);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoAimgo);
                    startActivityForResult(tomarFotoIntent, 2);
                }else{
                    mostrarMsg("No se pudo crear la imagen.");
                }
            }catch (Exception e){
                mostrarMsg("Error: "+e.getMessage());
            }
        }else if (contadorClicks == 3) {
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoAmigo = null;
            try{
                fotoAmigo = crearImagenAmigo();
                if( fotoAmigo!=null ){
                    Uri uriFotoAimgo = FileProvider.getUriForFile(MainActivity.this,
                            "com.alexis.miprimeraplicacion.fileprovider", fotoAmigo);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoAimgo);
                    startActivityForResult(tomarFotoIntent, 3);
                }else{
                    mostrarMsg("No se pudo crear la imagen.");
                }
            }catch (Exception e){
                mostrarMsg("Error: "+e.getMessage());
            }
        }

        //});
    }//nombre

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try{
            if( requestCode==1  ||requestCode==2 || requestCode==3  && resultCode==RESULT_OK ){
                //Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                if(contadorClicks == 1){
                    img = findViewById(R.id.imgFotoProducto);
                    img.setImageURI(Uri.parse(urlCompletaFoto));
                }else if(contadorClicks == 2) {
                    img = findViewById(R.id.imgFotoProducto2);
                    img2.setImageURI(Uri.parse(urlCompletaFoto2));
                }else if(contadorClicks == 3) {
                    img = findViewById(R.id.imgFotoProducto3);
                    img3.setImageURI(Uri.parse(urlCompletaFoto3));
                }
                //img.setImageURI(uriFotoSeleccionada);
            }else{
                mostrarMsg("No se tomo la foto.");
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }

    private File crearImagenAmigo() throws Exception{
        String fechaHoraMs = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date()),
                fileName = "imagen_"+ fechaHoraMs+"_";
        File dirAlmacenamiento = getExternalFilesDir(Environment.DIRECTORY_DCIM);
        if( dirAlmacenamiento.exists()==false ){
            dirAlmacenamiento.mkdir();
        }
        File image = File.createTempFile(fileName, ".jpg", dirAlmacenamiento);
        if(contadorClicks == 1){
            img = findViewById(R.id.imgFotoProducto);
            urlCompletaFoto = image.getAbsolutePath();

        }else if(contadorClicks == 2) {
            img = findViewById(R.id.imgFotoProducto2);
            urlCompletaFoto2 = image.getAbsolutePath();

        }else if(contadorClicks == 3) {
            img = findViewById(R.id.imgFotoProducto3);
            urlCompletaFoto3 = image.getAbsolutePath();

        }

        return image;
    }
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void abrirVentana(){
        Intent intent = new Intent(this, lista_productos.class);
        startActivity(intent);
    }
    private void guardarAmigo() {
        try {
            tempVal = findViewById(R.id.txtCodigo);
            String codigo = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtDescripcion);
            String descripcion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtMarca);
            String marca = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtPresentacion);
            String presentacion = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtPrecio);
            String precio = tempVal.getText().toString();

            JSONObject datosAmigos = new JSONObject();
            if (accion.equals("modificar")) {
                datosAmigos.put("_id", id);
                datosAmigos.put("_rev", rev);
            }
            datosAmigos.put("idProducto", idProducto);
            datosAmigos.put("codigo", codigo);
            datosAmigos.put("descripcion", descripcion);
            datosAmigos.put("marca", marca);
            datosAmigos.put("presentacion", presentacion);
            datosAmigos.put("precio", precio);
            datosAmigos.put("urlFoto", urlCompletaFoto);
            datosAmigos.put("urlFoto2", urlCompletaFoto2);
            datosAmigos.put("urlFoto3", urlCompletaFoto3);

            di = new detectarInternet(this);
            if(di.hayConexionInternet()) {//online
                //enviar los datos al servidor
                enviarDatosServidor objEnviarDatos = new enviarDatosServidor(this);
                String respuesta = objEnviarDatos.execute(datosAmigos.toString(), "POST", utilidades.url_mto).get();

                JSONObject respuestaJSON = new JSONObject(respuesta);
                if(respuestaJSON.getBoolean("ok")){
                    id = respuestaJSON.getString("id");
                    rev = respuestaJSON.getString("rev");
                }else{
                    mostrarMsg("Error: "+respuestaJSON.getString("msg"));
                }
            }
            String[] datos = {idProducto, codigo, descripcion, marca, presentacion, precio, urlCompletaFoto, urlCompletaFoto2, urlCompletaFoto3};
            db.administrar_amigos(accion, datos);
            Toast.makeText(getApplicationContext(), "Registro guardado con exito.", Toast.LENGTH_LONG).show();
            abrirVentana();
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }

  // public void Posicion(){

        //}*/

}
