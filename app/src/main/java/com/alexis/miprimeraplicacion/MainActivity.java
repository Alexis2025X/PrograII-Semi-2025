package com.alexis.miprimeraplicacion;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, GoogleMap.OnMapLongClickListener {

   // private LinearLayout layout_mascotas, layout_chat, layout_citas, layout_veterinarios, layout_cuenta;
    //LinearLayout layout_mascotas, layout_chat, layout_citas, layout_veterinarios, layout_cuenta;
    RelativeLayout layout_mascotas, layout_chat, layout_citas, layout_veterinarios, layout_cuenta;
    BottomNavigationView bottomNav;
    Button btn;
    Bundle parametros = new Bundle();
    String cuentaID;
    JSONArray jsonArray, jsonArrayMascotas;
    JSONArray jsonArrayChats = new JSONArray();
    JSONObject jsonObject;
    int posicion = 0;
    DB db;
    String idCuentaActual = "";
    String miToken = "", llaveCuenta = "";
    detectarInternet di;
    FloatingActionButton fabAgregarCitas, fabAgregarMascotas, fabAgregarChat;
    ListView ltsCitas, ltsMascotas, ltsChat;
    Cursor cCitas, cMascotas, cChat;
    //String usuario;
    //Para Citas
    final ArrayList<citas> alCitas = new ArrayList<citas>();
    final ArrayList<citas> alCitasCopia = new ArrayList<citas>();
    citas misCitas;

//Para mascotas
    final ArrayList<mascotas> alMascotas = new ArrayList<mascotas>();
    final ArrayList<mascotas> alMascotasCopia = new ArrayList<mascotas>();
    mascotas misMascotas;
    //Para chats
    final ArrayList<chats> alChat = new ArrayList<chats>();
    final ArrayList<chats> alChatCopia = new ArrayList<chats>();
    chats misChat;
    DatabaseReference databaseReference;
//Para el mapa
    TextView tempVal, lblUbicacion, lblLatitud, lblLongitud;
    GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;
    //Location currentLocation;
    private Marker marcadorUbicacion;
    double latitud, longitud;
    //Chat

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Para Menu
        bottomNav = findViewById(R.id.bottom_nav);
        //Para las vistas
        layout_mascotas = findViewById(R.id.layout_mascotas);
        layout_chat = findViewById(R.id.layout_chat);
        layout_citas = findViewById(R.id.layout_citas);
        layout_veterinarios = findViewById(R.id.layout_veterinarios);
        layout_cuenta = findViewById(R.id.layout_cuenta);
        //Para las listas
        ltsChat = findViewById(R.id.ltsChat);
        ltsCitas = findViewById(R.id.ltsCitas);
        ltsMascotas = findViewById(R.id.ltsMascotas);

        fabAgregarCitas = findViewById(R.id.fabAgregarCitasMascotas);
        fabAgregarChat = findViewById(R.id.fabAgregarChat);

        //Listo por defecto
        fabAgregarMascotas = findViewById(R.id.fabAgregarMascotas);
        fabAgregarMascotas.setOnClickListener(view->abrirAgregarMascotas());

        //Para regresar al menu
        String CargarLayout = getIntent().getStringExtra("cargar_layout");
        if("mascotas".equals(CargarLayout)){
            ventanaMascota();
            bottomNav.setSelectedItemId(R.id.mascotasMenu);
        }else if("citas".equals(CargarLayout)){
            ventanaCita();
            bottomNav.setSelectedItemId(R.id.citasMenu);
        }else if("chat".equals(CargarLayout)){
            ventanaChat();
            bottomNav.setSelectedItemId(R.id.chatMenu);
        } else if ("veterinarios".equals(CargarLayout)) {
            ventanaVeterinario();
            bottomNav.setSelectedItemId(R.id.veterinariosMenu);
        }/*else if ("cuenta".equals(CargarLayout)) {
            ventanaCuenta();
            bottomNav.setSelectedItemId(R.id.cuentaMenu);
        }*/

        //usuario = getIntent().getStringExtra("usuarioCuenta");
        parametros.putString("accion", "nuevo");
        db = new DB(this);
        db.deleteOldDatabases(this);

        //Menu
        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.mascotasMenu) {
                ventanaMascota();
                return true;
            }else if (item.getItemId() == R.id.chatMenu) {
                ventanaChat();
                return true;
            }else if (item.getItemId() == R.id.citasMenu) {
                ventanaCita();
                return true;
            } else if (item.getItemId() == R.id.veterinariosMenu) {
                obtenerPosicion();
                ventanaVeterinario();
                return true;
            } else if (item.getItemId() == R.id.cuentaMenu){
                ventanaCuenta();
                return true;
            }
            return false;
        });

        //lblUbicacion = findViewById(R.id.lblUbicacion);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapa);
        mapFragment.getMapAsync(this);

        cuentaID = datosCuentaEnUso.getIdCuenta();
        //mostrarMsg("Bienvenido: " + cuentaID);
            //para mascotas
            obtenerDatosMascotas(cuentaID);
            buscarMascotas();
            //Para citas
            obtenerDatosCitas(cuentaID);
            buscarCitas();
            //Para chats
            listarDatos(cuentaID);
            buscarChats();
            try {
                mostrarChats();
            }catch (Exception e){
                mostrarAlertDialog("Error: "+ e.getMessage());// Error ID al mostrar
            }

            //Para cuentas
            mostrarDatosCuenta();

/*        if (layout_veterinarios.getVisibility() == View.VISIBLE) {*/

/*        }amigos*/

    }
    private void ventanaMascota(){
        layout_mascotas.setVisibility(View.VISIBLE);
        layout_chat.setVisibility(View.GONE);
        layout_citas.setVisibility(View.GONE);
        layout_veterinarios.setVisibility(View.GONE);
        layout_cuenta.setVisibility(View.GONE);
        //Los fabs desaparecen
        fabAgregarMascotas.setVisibility(View.VISIBLE);
        fabAgregarMascotas.setOnClickListener(view->abrirAgregarMascotas());
        fabAgregarCitas.setVisibility(View.GONE);
        fabAgregarChat.setVisibility(View.GONE);
    }//mostrarDatos
    private void ventanaChat(){
        layout_mascotas.setVisibility(View.GONE);
        layout_chat.setVisibility(View.VISIBLE);
        layout_citas.setVisibility(View.GONE);
        layout_veterinarios.setVisibility(View.GONE);
        layout_cuenta.setVisibility(View.GONE);

        //Los fabs desaparecen
        fabAgregarChat.setVisibility(View.VISIBLE);
        fabAgregarChat.setOnClickListener(view->abrirAgregarChat());
        fabAgregarMascotas.setVisibility(View.GONE);
        fabAgregarCitas.setVisibility(View.GONE);

    }
    private void ventanaCita(){
        layout_mascotas.setVisibility(View.GONE);
        layout_chat.setVisibility(View.GONE);
        layout_citas.setVisibility(View.VISIBLE);
        layout_veterinarios.setVisibility(View.GONE);
        layout_cuenta.setVisibility(View.GONE);

        fabAgregarChat.setVisibility(View.GONE);
        fabAgregarCitas.setVisibility(View.VISIBLE);
        fabAgregarCitas.setOnClickListener(view->abrirAgregarCitas());
        fabAgregarMascotas.setVisibility(View.GONE);

    }
    private void ventanaVeterinario(){
        layout_mascotas.setVisibility(View.GONE);
        layout_chat.setVisibility(View.GONE);
        layout_citas.setVisibility(View.GONE);
        layout_veterinarios.setVisibility(View.VISIBLE);
        layout_cuenta.setVisibility(View.GONE);
        //Los fabs desaparecen
        fabAgregarChat.setVisibility(View.GONE);
        fabAgregarMascotas.setVisibility(View.GONE);
        fabAgregarCitas.setVisibility(View.GONE);
        //Se muestra el mapa;
        mostrarMsg("Obteniendo ubicación...");
    }
    private void ventanaCuenta(){
        layout_mascotas.setVisibility(View.GONE);
        layout_chat.setVisibility(View.GONE);
        layout_citas.setVisibility(View.GONE);
        layout_veterinarios.setVisibility(View.GONE);
        layout_cuenta.setVisibility(View.VISIBLE);
        mostrarDatosCuenta();
        btn = findViewById(R.id.btnCerrarSesion);
        btn.setOnClickListener(view -> {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        });

                btn = findViewById(R.id.btnEditarCuenta);
                btn.setOnClickListener(view -> {
                   editarCuenta();
                });

                btn = findViewById(R.id.btnEliminarCuenta);
                btn.setOnClickListener(view -> {
                    eliminarCuenta();
                });
                //Los fabs desaparecen
                fabAgregarChat.setVisibility(View.GONE);
                fabAgregarMascotas.setVisibility(View.GONE);
                fabAgregarCitas.setVisibility(View.GONE);
    }
    private void abrirAgregarCitas(){
        Intent intent = new Intent(this, agregar_citas.class);
        cuentaID = datosCuentaEnUso.getIdCuenta();
        intent.putExtras(parametros);
        startActivity(intent);
    }
    private void abrirAgregarMascotas(){
        Intent intent = new Intent(this, agregar_mascotas.class);
        cuentaID = datosCuentaEnUso.getIdCuenta();
        intent.putExtras(parametros);
        startActivity(intent);
    }
    private void abrirAgregarChat(){
        Intent intent = new Intent(this, agregar_chat.class);
        intent.putExtras(parametros);
        startActivity(intent);
    }
    /////////////////////////////////////////
    //LA FUNCIÓN MSJ SIRVE PARA AMBOS
    private void mostrarMsg(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_LONG).show();
    }
    private void mostrarAlertDialog(String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage(msg);
        builder.setPositiveButton("Aceptar", null);
        builder.create().show();
    }
    //La función de menu sirve para mascotas y citas
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getMenuInflater();
        if(layout_chat.getVisibility() == View.VISIBLE) {
            inflater.inflate(R.menu.mi_menu, menu);
        } else if (layout_citas.getVisibility() == View.VISIBLE) {
            inflater.inflate(R.menu.mi_menu_cita, menu);
        }else if(layout_mascotas.getVisibility() == View.VISIBLE){
        inflater.inflate(R.menu.mi_menu_mascota, menu);
        }
        try {
            AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
            posicion = info.position;
            if(layout_chat.getVisibility() == View.VISIBLE) {
                menu.setHeaderTitle(jsonArrayChats.getJSONObject(posicion).getString("nombre"));
            } else if (layout_citas.getVisibility() == View.VISIBLE) {
                menu.setHeaderTitle(jsonArray.getJSONObject(posicion).getString("nombre"));
            }else if(layout_mascotas.getVisibility() == View.VISIBLE){
                menu.setHeaderTitle(jsonArrayMascotas.getJSONObject(posicion).getString("nombre"));
            }

        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    //La función de menu sirve para mascotas y citas SI LO PUEDES REFACTORIZAR HAZLO
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(layout_mascotas.getVisibility() == View.VISIBLE) {
            try {
                if (item.getItemId() == R.id.mnxNuevoMascota) {
                    parametros.putString("accion", "nuevo");
                        abrirAgregarMascotas();
                } else if (item.getItemId() == R.id.mnxModificarMascota) {
                    parametros.putString("accion", "modificar");
                    parametros.putString("mascotas", jsonArrayMascotas.getJSONObject(posicion).toString());
                    abrirAgregarMascotas();

                } else if (item.getItemId() == R.id.mnxEliminarMascota) {
                    eliminarMascota();
                    obtenerDatosMascotas(cuentaID);
                    buscarMascotas();
                }
                return true;
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
                return super.onContextItemSelected(item);
            }
        } else if (layout_citas.getVisibility() == View.VISIBLE) {
            try {
                if (item.getItemId() == R.id.mnxNuevoCita) {
                    parametros.putString("accion", "nuevo");
                    abrirAgregarCitas();
                } else if (item.getItemId() == R.id.mnxModificarCita) {
                    parametros.putString("accion", "modificar");
                    parametros.putString("citas", jsonArray.getJSONObject(posicion).toString());
                    abrirAgregarCitas();
                } else if (item.getItemId() == R.id.mnxEliminarCita) {
                    eliminarCita();
                    obtenerDatosCitas(cuentaID);
                    buscarCitas();
                }
                return true;
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
                return super.onContextItemSelected(item);
            }
        } else if (layout_chat.getVisibility() == View.VISIBLE) {
            try {
                if (item.getItemId() == R.id.mnxNuevo) {
                    parametros.putString("accion", "nuevo");
                    abrirAgregarChat();
                } else if (item.getItemId() == R.id.mnxModificar) {
                    parametros.putString("accion", "modificar");
                    parametros.putString("Persona_chats", jsonArrayChats.getJSONObject(posicion).toString());
                    abrirAgregarChat();
                } else if (item.getItemId() == R.id.mnxEliminar) {
                    eliminarChat();
                    listarDatos(cuentaID);
                    buscarChats();
                }
                return true;
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
                return super.onContextItemSelected(item);
            }
        } else {
            return super.onContextItemSelected(item);
        }
    }
    private void eliminarCita () {
            try {
                String nombre = jsonArray.getJSONObject(posicion).getString("nombre");
                AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
                confirmacion.setTitle("Esta seguro de eliminar a: ");
                confirmacion.setMessage(nombre);
                confirmacion.setPositiveButton("Si", (dialog, which) -> {
                    try {
                        String respuesta = db.administrar_Citas("eliminar", new String[]{jsonArray.getJSONObject(posicion).getString("idCitas")});
                        if (respuesta.equals("ok")) {
                            obtenerDatosCitas(cuentaID);
                            buscarCitas();
                            mostrarMsg("Registro eliminado con exito");
                        } else {
                            mostrarMsg("Error: " + respuesta);
                        }
                    } catch (Exception e) {
                        mostrarMsg("Error: " + e.getMessage());
                    }
                });
                confirmacion.setNegativeButton("No", (dialog, which) -> {
                    dialog.dismiss();
                });
                confirmacion.create().show();
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
            }
        }
    private void obtenerDatosCitas (String idCuenta) {
            try {
                db = new DB(this);
                cCitas = db.lista_Citas(idCuenta);
                //mostrarMsg(cCitas.toString() + "buyftyf");
                if (cCitas.moveToFirst()) {
                    jsonArray = new JSONArray();
                    do {
                        jsonObject = new JSONObject();
                        jsonObject.put("idCitas", cCitas.getString(0));
                        jsonObject.put("nombre", cCitas.getString(1));
                        jsonObject.put("fecha", cCitas.getString(2));
                        jsonObject.put("clinica", cCitas.getString(3));
                        jsonObject.put("nota", cCitas.getString(4));
                        jsonObject.put("foto", cCitas.getString(5));
                        jsonObject.put("cuentaID", cCitas.getString(6));
                        jsonObject.put("llave", cCitas.getString(7));
                        jsonArray.put(jsonObject);

                        //mostrarMsg(cCitas.getString(4) + "buyftyf");

                    } while (cCitas.moveToNext());
                    mostrarDatosCitas();
                } else {
                    if (layout_citas.getVisibility() == View.VISIBLE) {
                        mostrarMsg("No hay citas registrados.");
                        abrirAgregarCitas();
                    }

                }
            } catch (Exception e) {
                mostrarMsg("Error: " + e.getMessage());
            }
        }
    private void mostrarDatosCitas () {
            try {
                if (jsonArray.length() > 0) {
                    ltsCitas = findViewById(R.id.ltsCitas);
                    alCitas.clear();
                    //alCitasCopia.clear();

                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        misCitas = new citas(
                                jsonObject.getString("idCitas"),
                                jsonObject.getString("nombre"),
                                jsonObject.getString("fecha"),
                                jsonObject.getString("clinica"),
                                jsonObject.getString("nota"),
                                jsonObject.getString("foto"),
                                jsonObject.getString("cuentaID"),
                                jsonObject.getString("llave")

                                //jsonObject.getString("foto")
                        );
                        alCitas.add(misCitas);
                    }
                    alCitasCopia.addAll(alCitas);
                    ltsCitas.setAdapter(new AdaptadorCitas(this, alCitas));
                    registerForContextMenu(ltsCitas);
                } else {
                    mostrarMsg("No hay citas registrados.");
                    abrirAgregarCitas();
                }
            } catch (Exception e) {

                mostrarMsg("Error: " + e.getMessage());
            }
        }
    private void buscarCitas () {

            TextView tempVal = findViewById(R.id.txtBuscarCitas);
            tempVal.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    try{
                    alCitas.clear();
                    String buscar = tempVal.getText().toString().trim().toLowerCase();
                    if (buscar.length() <= 0) {
                        alCitas.addAll(alCitasCopia);
                    } else {
                        for (citas item : alCitasCopia) {
                            if (item.getnombreMascota().toLowerCase().contains(buscar) ||
                                    item.getfecha().toLowerCase().contains(buscar) ||
                                    item.getclinica().toLowerCase().contains(buscar)) {
                                alCitas.add(item);
                            }
                        }
                        ltsCitas.setAdapter(new AdaptadorCitas(getApplicationContext(), alCitas));
                    }
                    }catch (Exception e){
                        mostrarMsg("No hay citas registrados.");
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }

    //AQUI COMIENZA PARA MASCOTAS
    private void obtenerDatosMascotas(String idCuenta){
        try{
            db = new DB(this);

            cMascotas = db.lista_mascotas(idCuenta);
            if(cMascotas.moveToFirst()){
                jsonArrayMascotas = new JSONArray();
                do{
                    jsonObject = new JSONObject();
                    jsonObject.put("idMascota", cMascotas.getString(0));
                    jsonObject.put("dueño", cMascotas.getString(1));
                    jsonObject.put("nombre", cMascotas.getString(2));
                    jsonObject.put("edad", cMascotas.getString(3));
                    jsonObject.put("raza", cMascotas.getString(4));
                    jsonObject.put("problemas_medicos", cMascotas.getString(5));
                    jsonObject.put("foto", cMascotas.getString(6));
                    jsonObject.put("cuentaID", cMascotas.getString(7));
                    jsonObject.put("llave", cMascotas.getString(8));
                    jsonArrayMascotas.put(jsonObject);
                }while(cMascotas.moveToNext());

                mostrarMascotas();

            }else {
                if(layout_mascotas.getVisibility() == View.VISIBLE){
                    mostrarMsg("No hay mascotas registrados.");
                    abrirAgregarMascotas();
                }

            }
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
    }
    private void mostrarMascotas() {

        try{

            if(jsonArrayMascotas.length()>0){

                ltsMascotas = findViewById(R.id.ltsMascotas);
                alMascotas.clear();

                alMascotasCopia.clear();

                for (int i=0; i<jsonArrayMascotas.length(); i++){
                    jsonObject = jsonArrayMascotas.getJSONObject(i);
                    misMascotas = new mascotas(
                            jsonObject.getString("idMascota"),
                            jsonObject.getString("dueño"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("edad"),
                            jsonObject.getString("raza"),
                            jsonObject.getString("problemas_medicos"),
                            jsonObject.getString("foto"),
                            jsonObject.getString("cuentaID"),
                            jsonObject.getString("llave")
                    );


                    alMascotas.add(misMascotas);
                }
                alMascotasCopia.addAll(alMascotas);
                ltsMascotas.setAdapter(new adaptadorMascotas(this,alMascotas));
                registerForContextMenu(ltsMascotas);

            }else{
                mostrarMsg("No hay chat registrados.");

            }
        }catch (Exception e){
            mostrarMsg("1 Error: " + e.getMessage());
        }

    }
    private void buscarMascotas(){

        TextView tempVal = findViewById(R.id.txtBuscarMascotas);

        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{alMascotas.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if( buscar.length()<=0){
                    alMascotas.addAll(alMascotasCopia);
                }else{
                    for (mascotas item: alMascotasCopia){
                        if(item.getNombre().toLowerCase().contains(buscar) ||
                                item.getEdad().toLowerCase().contains(buscar) ||
                                item.getDueño().toLowerCase().contains(buscar)){
                            alMascotas.add(item);

                        }
                    }
                    ltsMascotas.setAdapter(new adaptadorMascotas(getApplicationContext(), alMascotas));
                }
                }catch (Exception e){
                    mostrarMsg("No hay mascotas registrados.");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private  void eliminarMascota(){
        try {
            String nombreMascota = jsonArrayMascotas.getJSONObject(posicion).getString("nombre");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("Esta seguro de eliminar a: ");
            confirmacion.setMessage(nombreMascota);
            confirmacion.setPositiveButton("Si", (dialog, which) -> {
                try {
                    String respuesta = db.administrar_Mascota("eliminar", new String[]{jsonArrayMascotas.getJSONObject(posicion).getString("idMascota")});
                    if (respuesta.equals("ok")) {
                        obtenerDatosMascotas(cuentaID);
                        buscarMascotas();
                        mostrarMsg("Registro eliminado con exito");
                    } else {
                        mostrarMsg("Error: " + respuesta);
                    }
                } catch (Exception e) {
                    mostrarMsg("Error: " + e.getMessage());
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            confirmacion.create().show();
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }
/*        try {
            db = new DB(this);
            String idMascota = jsonArrayMascotas.getJSONObject(posicion).getString("idMascota");
            String respuesta = db.administrar_Mascota("eliminar", new String[]{idMascota});
            if (respuesta.equals("ok")) {
                obtenerDatosMascotas(usuario);
                mostrarMsg("Registro eliminada.");
            } else {
                mostrarMsg("Error: " + respuesta);
            }
        } catch (Exception e) {
            mostrarMsg("Error: " + e.getMessage());
        }*/
    }

    //AQUI COMIENZA PARA CHATS
    private void listarDatos(String idCuenta){//Error al mostrar datos
        di = new detectarInternet(this);
        if(di.hayConexionInternet()){//online a
            try{
                databaseReference  = FirebaseDatabase.getInstance().getReference("Persona_chats");
                FirebaseMessaging.getInstance().getToken().addOnCompleteListener(tarea->{
                    if(!tarea.isSuccessful()){
                        mostrarMsg("Error al obtener token: "+tarea.getException().getMessage());
                        return;
                    }else{
                        miToken = tarea.getResult();
                        if( miToken!=null && miToken.length()>0 ){
                            databaseReference.orderByChild("token").equalTo(miToken).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    try{
                                        if( snapshot.getChildrenCount()<=0 ){
                                            parametros.putString("accion", "nuevo");
/*                                            if(layout_chat.getVisibility() == View.VISIBLE){
                                                mostrarMsg("No hay chats registrados.");
                                                //abrirAgregarChat();
                                            }
                                            //abrirAgregarChat();*/
                                        }
                                    }catch (Exception e){
                                        mostrarMsg("Error al llamar la ventana: " + e.getMessage());
                                    }
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    mostrarMsg("Error se cancelo: " + error.getMessage());
                                }
                            });
                        }
                    }
                });

                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {//databaseReference
                        try{
                            jsonArrayChats = new JSONArray();
                            for( DataSnapshot dataSnapshot : snapshot.getChildren() ){
                                chats chat = dataSnapshot.getValue(chats.class);
                                jsonObject = new JSONObject();
                                jsonObject.put("idChat", chat.getIdChat());//idAmigo
                                jsonObject.put("nombre", chat.getNombre());
                                jsonObject.put("direccion", chat.getDireccion());
                                jsonObject.put("telefono", chat.getTelefono());
                                jsonObject.put("email", chat.getEmail());
                                jsonObject.put("dui", chat.getDui());
                                jsonObject.put("urlFoto", chat.getFoto());
                                //jsonObject.put("urlCompletaFotoFirestore", chat.getUrlCompletaFotoFirestore());
                                jsonObject.put("to", chat.getToken());
                                jsonObject.put("from", miToken);
                                jsonObject.put("token", chat.getToken());
                                jsonArrayChats.put(jsonObject);
                            }
                            mostrarDatosChats();//setOnItemClickListener
                        }catch (Exception e){
                            mostrarMsg("Error al escuchar evento de firebase: " + e.getMessage());
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }catch (Exception e){
                mostrarMsg("Error al listar datos: " + e.getMessage());//Error ID
            }
        }else{
        try{
            db = new DB(this);
            //cChat = db.lista_chat(cuentaID); _id
            cChat = db.lista_chat(idCuenta);
            if(cChat.moveToFirst()){
                jsonArrayChats = new JSONArray();
                do{
                    jsonObject = new JSONObject();
                    jsonObject.put("idChat", cChat.getString(0));
                    jsonObject.put("nombre", cChat.getString(1));
                    jsonObject.put("direccion", cChat.getString(2));
                    jsonObject.put("telefono", cChat.getString(3));
                    jsonObject.put("email", cChat.getString(4));
                    jsonObject.put("dui", cChat.getString(5));
                    jsonObject.put("urlFoto", cChat.getString(6));
                    //jsonObject.put("cuentaID", cMascotas.getString(7));
                    jsonObject.put("to", cChat.getString(7));
                    jsonObject.put("token", cChat.getString(8));
                    jsonObject.put("cuentaID", cChat.getString(9));
                    jsonArrayChats.put(jsonObject);
                }while(cChat.moveToNext());

                mostrarDatosChats();

            }else {
/*                if(layout_chat.getVisibility() == View.VISIBLE){
                    mostrarMsg("No hay chats registrados.");
                    abrirAgregarChat();
                }*/

            }
        }catch (Exception e){
            mostrarMsg("Error: " + e.getMessage());
        }
        }


    }
    private void mostrarDatosChats(){
        try{
            if(jsonArrayChats.length()>0){
                //ltsChat = findViewById(R.id.ltsChat);
                alChat.clear();
                alChatCopia.clear();

                for (int i=0; i<jsonArrayChats.length(); i++){
                    jsonObject = jsonArrayChats.getJSONObject(i);
                    misChat = new chats(
                            jsonObject.getString("idChat"),
                            jsonObject.getString("nombre"),
                            jsonObject.getString("direccion"),
                            jsonObject.getString("telefono"),
                            jsonObject.getString("email"),
                            jsonObject.getString("dui"),
                            jsonObject.getString("urlFoto"),
                            //jsonObject.getString("urlCompletaFotoFirestore"),
                            jsonObject.getString("to"),
                            jsonObject.getString("token")//llave
                    );
                    alChat.add(misChat);
                }
                    alChatCopia.addAll(alChat);
                    ltsChat.setAdapter(new AdaptadorChats(this, alChat));
                    registerForContextMenu(ltsChat);
/*            }else{
                if(layout_chat.getVisibility() == View.VISIBLE){
                    mostrarMsg("No hay chats registrados.");
                    abrirAgregarChat();
                }*/
            }
        }catch (Exception e){
            mostrarAlertDialog("Error ID al mostrar: " + e.getMessage());
        }
    }
    private void buscarChats(){

        TextView tempVal = findViewById(R.id.txtBuscarChats);
        tempVal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try{
                alChat.clear();
                String buscar = tempVal.getText().toString().trim().toLowerCase();
                if( buscar.length()<=0){
                    alChat.addAll(alChatCopia);
                }else{
                    for (chats item: alChatCopia){
                        if(item.getNombre().toLowerCase().contains(buscar) ||
                                item.getDui().toLowerCase().contains(buscar) ||
                                item.getEmail().toLowerCase().contains(buscar)){
                            alChat.add(item);
                        }
                    }
                    ltsChat.setAdapter(new AdaptadorChats(getApplicationContext(), alChat));
                }
                }catch (Exception e){
                    mostrarMsg("No hay chats registrados.");
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
    private void eliminarChat(){
        try{

        String nombre = jsonArrayChats.getJSONObject(posicion).getString("nombre");
        AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
        confirmacion.setTitle("Esta seguro de eliminar a: ");
        confirmacion.setMessage(nombre);
        confirmacion.setPositiveButton("Si", (dialog, which) -> {//8
            try {
                di = new detectarInternet(this);
                if(di.hayConexionInternet()){//online

        databaseReference  = FirebaseDatabase.getInstance().getReference("Persona_chats").child(jsonArrayChats.getJSONObject(posicion).getString("token")); // si no funciona cambiar idChat por llave

        // Eliminar el registro
        databaseReference.removeValue()
                .addOnSuccessListener(aVoid -> {
                    // Eliminación exitosa
                    Log.d("Firebase", "Registro eliminado correctamente");
                    Toast.makeText(MainActivity.this, "Registro eliminado", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Error al eliminar
                    Log.e("Firebase", "Error al eliminar registro", e);
                    Toast.makeText(MainActivity.this, "Error al eliminar", Toast.LENGTH_SHORT).show();
                });
                }
                String respuesta = db.administrar_Chat("eliminar", new String[]{jsonArrayChats.getJSONObject(posicion).getString("idChat")});
                if(respuesta.equals("ok")) {
                    listarDatos(cuentaID);//idAmigo
                    mostrarMsg("Registro eliminado con exito");
                }else{
                    mostrarMsg("Error al eliminar: " + respuesta);
                }
            }catch (Exception e){
                mostrarMsg("Error al eliminar: " + e.getMessage());
            }
        });
        confirmacion.setNegativeButton("No", (dialog, which) -> {
            dialog.dismiss();
        });
        confirmacion.create().show();
        }catch (Exception e){
            mostrarMsg("Error al eliminar: " + e.getMessage());
        }
    //
/*        try{ abrirVentanaChat()
=======

            String nombre = jsonArrayChats.getJSONObject(posicion).getString("nombre");
            AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
            confirmacion.setTitle("Esta seguro de eliminar a: ");
            confirmacion.setMessage(nombre);
            confirmacion.setPositiveButton("Si", (dialog, which) -> {
                try {
                    di = new detectarInternet(this);
                    if(di.hayConexionInternet()){//online

                        try {
                            String keyChatEliminar = jsonArrayChats.getJSONObject(posicion).getString("from");

                            String[]datosChat = new String[]{jsonArrayChats.getJSONObject(posicion).getString("idChat")};
                            String respuesta = db.administrar_Chat("eliminar", datosChat);
                            if (respuesta.equals("ok")) {
                                mostrarMsg("Registro eliminado con exito localmente");
                            } else {
                                mostrarMsg("Error al eliminar localmente: " + respuesta);
                            }
                            if (!keyChatEliminar.isEmpty()) {
                                databaseReference = FirebaseDatabase.getInstance().getReference("Persona_chats");
                                databaseReference.child(keyChatEliminar).removeValue().addOnSuccessListener(success -> {
                                    mostrarMsg("Registro eliminado con exito.");
                                }).addOnFailureListener(failure -> {
                                    mostrarMsg("Error al eliminar datos: " + failure.getMessage());
                                });
                            } else {
                                mostrarMsg("Error al guardar modificaciones en firebase.");
                            }
                        } catch (Exception e) {
                            mostrarMsg("Error al eliminar mascota fireBase: " + e.getMessage());

                        }
                    }
                    String respuesta = db.administrar_Chat("eliminar", new String[]{jsonArrayChats.getJSONObject(posicion).getString("idChat")});
                    if(respuesta.equals("ok")) {
                        listarDatos();//idAmigo
                        mostrarMsg("Registro eliminado con exito");
                    }else{
                        mostrarMsg("Error al eliminar: " + respuesta);
                    }
                }catch (Exception e){
                    mostrarMsg("Error en eliminar: " + e.getMessage());
                }
            });
            confirmacion.setNegativeButton("No", (dialog, which) -> {
                dialog.dismiss();
            });
            confirmacion.create().show();
        }catch (Exception e){
            mostrarMsg("Error eliminar: " + e.getMessage());
        }*/

    }
    private void mostrarChats(){
        //ltsChat = findViewById(R.id.ltsChat);
        try{
            ltsChat.setOnItemClickListener( (parent, view, position, id)->{
                try{
                    Bundle parametros = new Bundle();
                    parametros.putString("nombre", jsonArrayChats.getJSONObject(position).getString("nombre"));
                    parametros.putString("to", jsonArrayChats.getJSONObject(position).getString("to"));
                    parametros.putString("from", jsonArrayChats.getJSONObject(position).getString("from"));
                    parametros.putString("urlFoto", jsonArrayChats.getJSONObject(position).getString("urlFoto"));
                    //parametros.putString("idChat", jsonArrayChats.getJSONObject(position).getJSONObject("value").getString("idChat"));
                    Intent intent = new Intent(getApplicationContext(), chat_funcionalidad.class);
                    intent.putExtras(parametros);
                    startActivity(intent);
                }catch (Exception e){
                    mostrarMsg("Error al abrir el chat: " + e.getMessage());
                }
            });}catch (Exception e){
            mostrarAlertDialog("Error al mostrar los chats: " + e.getMessage());
        }
    }

    //Para obtener ubicación

    void obtenerPosicion(){
        try{
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            if(ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != getPackageManager().PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != getPackageManager().PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                tempVal.setText("Solicitando permisos de ubicación...");
            }
            locationListener = new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    try{
                        LatLng miUbicacion = new LatLng(location.getLatitude(), location.getLongitude());//Obtengo la ubicación
                        if(miUbicacion == null){
                            mostrarMsg("Error: No se pudo obtener la ubicación.");
                            LatLng ElSalvador = new LatLng(13.3432943,-88.4530736);
                            marcadorUbicacion = mMap.addMarker(new MarkerOptions().position(ElSalvador).title("El Salvador").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                        }
                        //Creamos el marcados solo la primera vez
                        if(marcadorUbicacion == null){
                            marcadorUbicacion = mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));
                        } else {
                            // Actualizar posición del marcador existente
                            marcadorUbicacion.setPosition(miUbicacion);
                        }
                        mMap.moveCamera(CameraUpdateFactory.newLatLng(miUbicacion));
                        //Circulo alrededor de la ubicación
/*                    Circle circle = mMap.addCircle(new CircleOptions()
                            .center(miUbicacion)
                            .radius(1000*5) //mt->km
                            .strokeColor(Color.BLUE) // Color del borde
                            .strokeWidth(2f) // Ancho del borde
                            .fillColor(Color.argb(20, 0, 100, 255)) // Color de relleno (transparente)
                    );*/


                    }
                    catch (Exception e){
                        tempVal.setText("Error al obtener la ubicación: "+ e.getMessage());
                    }
                    //mMap.addMarker(new MarkerOptions().position(miUbicacion).title("Mi ubicación"));
                    mostrarUbicacion(location);

                }
                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    tempVal.setText("Estado del proveedor: "+ status);
                }
                @Override
                public void onProviderEnabled(String provider) {
                    tempVal.setText("Proveedor habilitado: "+ provider);
                }
                @Override
                public void onProviderDisabled(String provider) {
                    tempVal.setText("Proveedor deshabilitado: "+ provider);
                }
            };
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
        }catch (SecurityException e){
            tempVal.setText("Error al obtener la ubicación: "+ e.getMessage());
        }
    }
    void mostrarUbicacion(Location location){
        try{
        //lblUbicacion.setText("Mi ubicación: "+"\nLatitud: "+ location.getLatitude() + "\nLongitud: "+ location.getLongitude() + "\nAltitud: "+ location.getAltitude());
/*        lblUbicacion.setText("Mi ubicación: "+"\nLatitud: "+ latitud + "\nLongitud: "+ longitud);*/
        latitud = location.getLatitude();
        longitud = location.getLongitude();
        }
        catch (Exception e){
            mostrarMsg("Error al obtener la ubicación: "+ e.getMessage());
            //lblUbicacion.setText("Error al obtener la ubicación: "+ e.getMessage());
        }
    }

    //Parte del mapa
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        try {
            mMap = googleMap;
            this.mMap.setOnMapClickListener(this);
            this.mMap.setOnMapLongClickListener(this);
            //Para mostrar la ubicación actual
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(true);
            obtenerPosicion();
            //LatLng ElSalvador = new LatLng(13.3432943,-88.4530736);
            //mMap.addMarker(new MarkerOptions().position(ElSalvador).title("Ubicación de El Salvador"));
            //mMap.moveCamera(CameraUpdateFactory.newLatLng(ElSalvador));//actualiza el mapa a ubicación indicada
        }
        catch (Exception e){
            if(layout_veterinarios.getVisibility() == View.VISIBLE) {
                mostrarMsg("Error al obtener la ubicación: " + e.getMessage());
            }
        }
        }
    //Por si solo hay un click corto

    @Override
    public void onMapClick(@NonNull LatLng latLng) {
        try{
/*        lblLatitud.setText("Latitud: " +""+ latLng.latitude);
        lblLongitud.setText("Longitud: "+""+ latLng.longitude);*/

        mMap.clear();
        mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada").snippet("Latitud: " + latLng.latitude + " Longitud: " + latLng.longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        }
        catch (Exception e){
            mostrarMsg("Error al obtener la ubicación: "+ e.getMessage());
        }
    }
    //Por si hay un click largo
    @Override
    public void onMapLongClick(@NonNull LatLng latLng) {
/*        lblLatitud.setText(""+ latLng.latitude);
        lblLongitud.setText(""+ latLng.longitude);*/
        mMap.addMarker(new MarkerOptions().position(latLng).title("Ubicación seleccionada").snippet("Latitud: " + latLng.latitude + " Longitud: " + latLng.longitude));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));


    }

    //Para lugares cercanos


    //comienzo el CRUB de cuentas
    public void mostrarDatosCuenta(){
        try {
            Bundle parameters = getIntent().getExtras();
                TextView txtUsuario = findViewById(R.id.txtUsuarioCuenta);
                TextView txtNombre = findViewById(R.id.txtNombreCuenta);
                TextView txtContraseña = findViewById(R.id.txtContraseñaCuenta);
                TextView txtEmail = findViewById(R.id.txtEmailCuenta);

                idCuentaActual = datosCuentaEnUso.getIdCuenta();
                llaveCuenta = datosCuentaEnUso.getLlaveCuenta();
                txtUsuario.setText( datosCuentaEnUso.getUsuarioCuenta());
                txtNombre.setText(datosCuentaEnUso.getNombreCuenta());
                txtContraseña.setText(datosCuentaEnUso.getContraseñaCuenta());
                txtEmail.setText(datosCuentaEnUso.getCorreoCuenta());
        } catch (Exception e) {
            mostrarMsg("Error al obtener datos de la cuenta: " + e.getMessage());
        }
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
    public void editarCuenta(){
        try {

            Button btnEliminar = findViewById(R.id.btnEliminarCuenta);

            Button btnEditar = findViewById(R.id.btnEditarCuenta);
//            txtUsuarioCuenta
//            txtNombreCuenta
//            txtContraseñaCuenta
//             txtEmailCuenta

            if ( btnEditar.getText().toString() == "Actualizar") {
            TextView txtNombre = findViewById(R.id.txtNombreCuenta);
            TextView txtUsuario = findViewById(R.id.txtUsuarioCuenta);
            TextView txtContraseña = findViewById(R.id.txtContraseñaCuenta);
            TextView txtEmail = findViewById(R.id.txtEmailCuenta);
                try {

                    String nombre = txtNombre.getText().toString();
                    String usuario = txtUsuario.getText().toString();
                    String contraseña = txtContraseña.getText().toString();
                    String email = txtEmail.getText().toString();

                    if (!validarCorreo(email)) {
                        return;
                    }

                    if (nombre.isEmpty() || usuario.isEmpty() || contraseña.isEmpty() || email.isEmpty()) {
                        mostrarMsg("Por favor, complete todos los campos.");
                        return;
                    }


                    try {
                        String[] datos = {idCuentaActual, nombre, usuario, contraseña, email, llaveCuenta};
                        db = new DB(this);
                        String respuesta = db.administrar_cuentas("modificar", datos);

                        di = new detectarInternet(this);
                        try{
                        if (di.hayConexionInternet()) {
                            try {

                                Map<String, Object> updates = new HashMap<>();
                                updates.put("constructorIdCuenta", idCuentaActual);
                                updates.put("constructorNombreCuenta", nombre);
                                updates.put("constructorUsuarioCuenta", usuario);
                                updates.put("constructorContraseñaCuenta", contraseña);
                                updates.put("constructorCorreoCuenta", email);
                                updates.put("constructorKeyCuenta", llaveCuenta);

                                if (llaveCuenta != null || llaveCuenta == "") {
                                    databaseReference = FirebaseDatabase.getInstance().getReference("cuentas");
                                    databaseReference.child(llaveCuenta).updateChildren(updates).addOnSuccessListener(success -> {
                                        mostrarMsg("Registro actualizado con exito.");
                                    }).addOnFailureListener(failure -> {
                                        mostrarMsg("Error al actualizar datos: " + failure.getMessage());
                                    });
                                } else {
                                    mostrarMsg("Error al guardar en firebase.");
                                }
                                //String[] datos = {idCuentaActual,nombre, usuario, contraseña, email};
                            } catch (Exception e) {
                                mostrarMsg("Error al actualizar la cuenta en fireBase: " + e.getMessage());

                            }

                            //String[] datos = {idCuentaActual,nombre, usuario, contraseña, email};


                        }
                    }catch (Exception e) {
                            mostrarMsg("Error al actualizar la cuenta en fireBase: " + e.getMessage());
                        }


                        if (respuesta == "ok"){
                            mostrarMsg("Cuenta actualizada con éxito.");
                            datosCuentaEnUso.setNombreCuenta(nombre);
                            datosCuentaEnUso.setUsuarioCuenta(usuario);
                            datosCuentaEnUso.setCorreoCuenta(contraseña);
                            datosCuentaEnUso.setCorreoCuenta(email);
                            mostrarDatosCuenta();
                        }else {
                            mostrarMsg("Error al actualizar la cuenta: " + respuesta);
                        }
                    } catch (Exception e) {
                        mostrarMsg("Error al actualizar la cuenta: " + e.getMessage());
                    }




                } catch (Exception e) {
                    mostrarMsg("Error al actualizar la cuenta: " + e.getMessage());
                }

        }else {
                btnEliminar.setText("Cancelar");
                btnEditar.setText("Actualizar");
                enabledCuentas(true);
            }

        }catch (Exception e){
            mostrarMsg("Error al editar cuenta: " + e.getMessage());
        }
    }
    public void eliminarCuenta(){
        try {
            Button btnEliminar = findViewById(R.id.btnEliminarCuenta);
            Button btnEditar = findViewById(R.id.btnEditarCuenta);

            if(btnEliminar.getText().toString() == "Cancelar"){
                btnEliminar.setText("Eliminar Cuenta");
                btnEditar.setText("Editar Cuenta");
                enabledCuentas(false);
                mostrarDatosCuenta();
            }else {
                try{
                    AlertDialog.Builder confirmacion = new AlertDialog.Builder(this);
                    confirmacion.setTitle("Esta seguro de eliminar su cuenta \n esta acción es irreversible: ");
                    confirmacion.setMessage("Eliminar cuenta?");
                    confirmacion.setPositiveButton("Si", (dialog, which) -> {
                        try {
                            di = new detectarInternet(this);

                            String respuesta = db.administrar_cuentas("eliminar", new String[]{idCuentaActual});
                            if(respuesta.equals("ok")) {
                                Intent intent = new Intent(this, Login.class);
                                startActivity(intent);
                                mostrarMsg("Registro eliminado con exito");
                            }else{
                                mostrarMsg("Error al eliminar: " + respuesta);
                            }
                        }catch (Exception e){
                            mostrarMsg("Error en eliminar: " + e.getMessage());
                        }
                    });
                    confirmacion.setNegativeButton("No", (dialog, which) -> {
                        dialog.dismiss();
                    });
                    confirmacion.create().show();
                }catch (Exception e){
                    mostrarMsg("Error eliminar: " + e.getMessage());
                }
            }


        } catch (Exception e) {
            mostrarMsg("Error al eliminar cuenta: " + e.getMessage());
        }
    }
    public void enabledCuentas(boolean avilitado){
        TextView temv = findViewById(R.id.txtNombreCuenta);
        temv.setEnabled(avilitado);
        temv = findViewById(R.id.txtUsuarioCuenta);
        temv.setEnabled(avilitado);
        temv = findViewById(R.id.txtContraseñaCuenta);
        temv.setEnabled(avilitado);
        temv = findViewById(R.id.txtEmailCuenta);
        temv.setEnabled(avilitado);

    }


}

