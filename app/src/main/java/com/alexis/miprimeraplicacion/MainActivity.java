package com.alexis.miprimeraplicacion;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

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
    String urlCompletaFoto = "";
    String urlCompletaFoto1 = "";
    String urlCompletaFoto2 = "";
    Intent tomarFotoIntent;
    utilidades utls;
    detectarInternet di;
    int posicionImg = 0;
    Button btnSiguiente;
    Button btnAnterior;
    TextView txtPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        utls = new utilidades();
        img = findViewById(R.id.imgFotoProducto);
        db = new DB(this);
        btn = findViewById(R.id.btnGuardarProducto);
        btn.setOnClickListener(view->guardarAmigo());

        fab = findViewById(R.id.fabListaProductos);
        fab.setOnClickListener(view->abrirVentana());

        btnAnterior = findViewById(R.id.btnAtras);
        btnAnterior.setOnClickListener(view -> anteriorClick());

        btnSiguiente = findViewById(R.id.btnAdelante);
        btnSiguiente.setOnClickListener(view -> siguienteClick());

        txtPosition = findViewById(R.id.lblPosicion);
        posicionImg = 0;
        mostrarDatos();
        tomarFoto();

    }

    private void siguienteClick(){
        if (posicionImg < 2) {

            posicionImg++;
            txtPosition.setText(posicionImg+1 + " de 3");

            switch (posicionImg) {
                case 0:
                    img.setImageURI(Uri.parse(urlCompletaFoto));
                    break;
                case 1:
                    img.setImageURI(Uri.parse(urlCompletaFoto1));
                    break;
                case 2:
                    img.setImageURI(Uri.parse(urlCompletaFoto2));
                    break;
            }
        }else {
            mostrarMsg("No hay mas fotos");
        }
    }
    private void anteriorClick(){
        if (posicionImg > 0) {
            posicionImg--;
            txtPosition.setText(posicionImg+1 + " de 3");
            switch (posicionImg) {
                case 0:
                    img.setImageURI(Uri.parse(urlCompletaFoto));
                    break;
                case 1:
                    img.setImageURI(Uri.parse(urlCompletaFoto1));
                    break;
                case 2:
                    img.setImageURI(Uri.parse(urlCompletaFoto2));
                    break;
            }
        }else {
            mostrarMsg("No hay mas fotos");
        }
    }

    private void mostrarDatos(){
        try {
            Bundle parametros = getIntent().getExtras();
            accion = parametros.getString("accion");

            if (accion.equals("modificar")) {
                JSONObject datos = new JSONObject(parametros.getString("productos"));
                di = new detectarInternet(this);
                if (di.hayConexionInternet()){
                    id = datos.getString("_id");
                    rev = datos.getString("_rev");
                }



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

                urlCompletaFoto = datos.getString("foto");
                urlCompletaFoto1 = datos.getString("foto1");
                urlCompletaFoto2 = datos.getString("foto2");
                img.setImageURI(Uri.parse(urlCompletaFoto));
            }else {
                idProducto = utls.generarUnicoId();
            }
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }
    private void tomarFoto(){
        img.setOnClickListener(view->{
            tomarFotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File fotoProducto = null;
            try{
                fotoProducto = crearImagenAmigo();
                if( fotoProducto!=null ){
                    Uri uriFotoAimgo = FileProvider.getUriForFile(MainActivity.this,
                            "com.alexis.miprimeraplicacion.fileprovider", fotoProducto);
                    tomarFotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriFotoAimgo);
                    startActivityForResult(tomarFotoIntent, 1);
                }else{
                    mostrarMsg("Nose pudo crear la imagen.");
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
                //Bitmap imagenBitmap = BitmapFactory.decodeFile(urlCompletaFoto);
                switch (posicionImg) {
                    case 0:
                        img.setImageURI(Uri.parse(urlCompletaFoto));
                        break;
                    case 1:
                        img.setImageURI(Uri.parse(urlCompletaFoto1));
                        break;
                    case 2:
                        img.setImageURI(Uri.parse(urlCompletaFoto2));
                        break;
                }
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
        switch (posicionImg) {
            case 0:
                urlCompletaFoto = image.getAbsolutePath();
                break;
            case 1:
                urlCompletaFoto1 = image.getAbsolutePath();
                break;
            case 2:
                urlCompletaFoto2 = image.getAbsolutePath();
                break;
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

            tempVal = findViewById(R.id.txtCosto);
            String costo = tempVal.getText().toString();

            tempVal = findViewById(R.id.txtStock);
            String stock = tempVal.getText().toString();

            JSONObject datosProductos = new JSONObject();

            di = new detectarInternet(this);

            if (di.hayConexionInternet()){
                if (accion.equals("modificar")) {
                    datosProductos.put("_id", id);
                    datosProductos.put("_rev", rev);
                }
            }



            datosProductos.put("idProducto", idProducto);
            datosProductos.put("codigo", codigo);
            datosProductos.put("descripcion", descripcion);
            datosProductos.put("marca", marca);
            datosProductos.put("presentacion", presentacion);
            datosProductos.put("precio", precio);
            datosProductos.put("foto", urlCompletaFoto);
            datosProductos.put("foto1", urlCompletaFoto1);
            datosProductos.put("foto2", urlCompletaFoto2);
            datosProductos.put("costo", costo);
            datosProductos.put("stock", stock);

            di = new detectarInternet(this);
            if(di.hayConexionInternet()) {//online
                //enviar los datos al servidor
                enviarDatosServidor objEnviarDatos = new enviarDatosServidor(this);
                String respuesta = objEnviarDatos.execute(datosProductos.toString(), "POST", utilidades.url_mto).get();

                JSONObject respuestaJSON = new JSONObject(respuesta);
                if(respuestaJSON.getBoolean("ok")){
                    id = respuestaJSON.getString("id");
                    rev = respuestaJSON.getString("rev");
                }else{
                    mostrarMsg("Error: "+respuestaJSON.getString("msg"));
                }
            }else{
                String res =   db.administrarActualizados("modificar", "verdadero",idProducto);
                String res1 =   db.administrarActualizados("nuevo", "verdadero",idProducto);

            }
            String[] datos = {idProducto, codigo, descripcion, marca, presentacion, precio, urlCompletaFoto,costo,stock, urlCompletaFoto1, urlCompletaFoto2};
            String respuesta = db.administrar_productos(accion, datos);

            Toast.makeText(getApplicationContext(), "estado de registro ." + respuesta, Toast.LENGTH_LONG).show();
            abrirVentana();
        }catch (Exception e){
            mostrarMsg("Error: "+e.getMessage());
        }
    }
}