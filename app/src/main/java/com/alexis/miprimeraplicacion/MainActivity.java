package com.alexis.miprimeraplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MainActivity extends AppCompatActivity {
    TabHost tbh;
    Button btn;
    TextView tempVal;
    Spinner spn;
    conversores objConversores = new conversores();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversor);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("Monedas").setContent(R.id.tabMonedas).setIndicator("MONEDAS", null));
        tbh.addTab(tbh.newTabSpec("Masa").setContent(R.id.tabMasa).setIndicator("MASA", null));
        tbh.addTab(tbh.newTabSpec("Volumen").setContent(R.id.tabVolumen).setIndicator("VOLUMEN", null));
        tbh.addTab(tbh.newTabSpec("Longitud").setContent(R.id.tabLongitud).setIndicator("LONGITUD", null));
        tbh.addTab(tbh.newTabSpec("Almacenamiento").setContent(R.id.tabAlmacenamiento).setIndicator("ALMACENAMIENTO", null));
        tbh.addTab(tbh.newTabSpec("Tiempo").setContent(R.id.tabTiempo).setIndicator("TIEMPO", null));
        tbh.addTab(tbh.newTabSpec("TransferenciaDatos").setContent(R.id.tabTransferenciaDatos).setIndicator("TRANFERENCIA DE DATOS", null));


        btn = findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int opcion = tbh.getCurrentTab();
                int de = 0;
                int a = 0;
                String msg = "";
                String TextDe = "";
                String TextA ="";

                switch (opcion){
                    case 0:
                        spn = findViewById(R.id.spnDeMonedas);
                        de = spn.getSelectedItemPosition();
                        TextDe = spn.getItemAtPosition(de).toString();

                        spn = findViewById(R.id.spnAMonedas);
                        a = spn.getSelectedItemPosition();
                        TextA = spn.getItemAtPosition(a).toString();
                        break;
                    case 1:
                        spn = findViewById(R.id.spnDeMasa);
                        de = spn.getSelectedItemPosition();
                        TextDe = spn.getItemAtPosition(de).toString();

                        spn = findViewById(R.id.spnAMasa);
                        a = spn.getSelectedItemPosition();
                        TextA = spn.getItemAtPosition(a).toString();
                        break;
                    case 2:
                        spn = findViewById(R.id.spnDeVolumen);
                        de = spn.getSelectedItemPosition();
                        TextDe = spn.getItemAtPosition(de).toString();
                        spn = findViewById(R.id.spnAVolumen);
                        a = spn.getSelectedItemPosition();
                        TextA = spn.getItemAtPosition(a).toString();
                        break;
                    case 3:
                        spn = findViewById(R.id.spnDeLongitud);
                        de = spn.getSelectedItemPosition();
                        TextDe = spn.getItemAtPosition(de).toString();
                        spn = findViewById(R.id.spnALongitud);
                        a = spn.getSelectedItemPosition();
                        TextA = spn.getItemAtPosition(a).toString();
                        break;
                    case 4:
                        spn = findViewById(R.id.spnDeAlmacenamiento);
                        de = spn.getSelectedItemPosition();
                        TextDe = spn.getItemAtPosition(de).toString();
                        spn = findViewById(R.id.spnAAlmacenamiento);
                        a = spn.getSelectedItemPosition();
                        TextA = spn.getItemAtPosition(a).toString();
                        break;
                    case 5:
                        spn = findViewById(R.id.spnDeTiempo);
                        de = spn.getSelectedItemPosition();
                        TextDe = spn.getItemAtPosition(de).toString();
                        spn = findViewById(R.id.spnATiempo);
                        a = spn.getSelectedItemPosition();
                        TextA = spn.getItemAtPosition(a).toString();
                        break;
                    case 6:
                        spn = findViewById(R.id.spnDeTransferenciasDatos);
                        de = spn.getSelectedItemPosition();
                        TextDe = spn.getItemAtPosition(de).toString();
                        spn = findViewById(R.id.spnATransferenciasDatos);
                        a = spn.getSelectedItemPosition();
                        TextA = spn.getItemAtPosition(a).toString();
                        break;
                }

                tempVal = findViewById(R.id.txtCantidad);
                if(tempVal.getText().toString().isEmpty()){
                    double cantidad = 0;
                    msg = "La cantidad que ingresaste no es valida";
                }
                else{
                    double cantidad = Double.parseDouble(tempVal.getText().toString());
                    tempVal = findViewById(R.id.lblRespuesta);

                    BigDecimal respuesta = objConversores.convertir(opcion, de, a, cantidad);

                    // respuesta = respuesta.setScale(2, RoundingMode.HALF_UP);
                    tempVal.setText("Respuesta: "+ respuesta);
                    msg = "El resultado de combertir " + cantidad + TextDe + " a " + TextA + " : ";

                    Toast.makeText(MainActivity.this, msg + respuesta, Toast.LENGTH_LONG).show();
                }

            }
        });
    }
}
class conversores{
    BigDecimal[][] valores = {
            {new BigDecimal("1"), new BigDecimal("0.92"), new BigDecimal("0.78"), new BigDecimal("110.50"), new BigDecimal("1.27"), new BigDecimal("0.93"), new BigDecimal("1.35"), new BigDecimal("6.36"), new BigDecimal("20.50"), new BigDecimal("5.25")}, // monedas
            {new BigDecimal("1"), new BigDecimal("1000"), new BigDecimal("1000000"), new BigDecimal("0.001"), new BigDecimal("35.274"), new BigDecimal("2.20462"), new BigDecimal("0.01"), new BigDecimal("10"), new BigDecimal("100"), new BigDecimal("10000")}, // masa
            {new BigDecimal("1"), new BigDecimal("0.000000001"), new BigDecimal("0.000001"), new BigDecimal("0.001"), new BigDecimal("1000"), new BigDecimal("1057"), new BigDecimal("264.172"), new BigDecimal("6.29"), new BigDecimal("35.3148"), new BigDecimal("61023.7")}, // volumen

            {new BigDecimal("1"), new BigDecimal("0.001"), new BigDecimal("100"), new BigDecimal("1000"), new BigDecimal("39.3701"), new BigDecimal("3.28084"), new BigDecimal("1.09361"), new BigDecimal("0.000621371"), new BigDecimal("1000000"), new BigDecimal("1000000000")}, // longitud

            {new BigDecimal("1"), new BigDecimal("8"), new BigDecimal("8192"), new BigDecimal("8388608"), new BigDecimal("8589934592"), new BigDecimal("8796093022208"), new BigDecimal("9007199254740992"), new BigDecimal("9223372036854775808"), new BigDecimal("9444732965739290427392"), new BigDecimal("9671406556917033397649408")}, // Almacenamiento

            {new BigDecimal("1"), new BigDecimal("0.0167"), new BigDecimal("0.000278"), new BigDecimal("0.0000116"),  new BigDecimal("0.00000165"), new BigDecimal("0.0000000317"), new BigDecimal("0.000000000317"), new BigDecimal("0.00000000317"), new BigDecimal("0.00000000634"), new BigDecimal("0.0000000106")}, // tiempo

            {new BigDecimal("1"), new BigDecimal("0.125"), new BigDecimal("0.001"), new BigDecimal("1e-6"), new BigDecimal("1e-9"), new BigDecimal("1e-12"), new BigDecimal("1e-15"), new BigDecimal("1e-18"), new BigDecimal("1e-21"), new BigDecimal("1e-24")}, // transferencia

    };

    public BigDecimal convertir(int opcion, int de, int a, double cantidad){
        BigDecimal cantidadBigDecimal = new BigDecimal(String.valueOf(cantidad));
        BigDecimal resultado = valores[opcion][a].divide(valores[opcion][de], 10, RoundingMode.HALF_UP).multiply(cantidadBigDecimal);

        return resultado;
    }
}
    //Acelerometro: Desplazamiento vertical, horizontal y
