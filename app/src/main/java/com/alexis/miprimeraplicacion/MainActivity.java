package com.alexis.miprimeraplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;
import java.math.BigDecimal;
import java.math.RoundingMode;

import androidx.appcompat.app.AppCompatActivity;



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
                switch (opcion){
                    case 0:
                        spn = findViewById(R.id.spnDeMonedas);
                        de = spn.getSelectedItemPosition();

                        spn = findViewById(R.id.spnAMonedas);
                        a = spn.getSelectedItemPosition();
                        break;
                    case 1:
                        spn = findViewById(R.id.spnDeMasa);
                        de = spn.getSelectedItemPosition();

                        spn = findViewById(R.id.spnAMasa);
                        a = spn.getSelectedItemPosition();
                        break;
                    case 2:
                        spn = findViewById(R.id.spnDeVolumen);
                        de = spn.getSelectedItemPosition();

                        spn = findViewById(R.id.spnAVolumen);
                        a = spn.getSelectedItemPosition();
                        break;
                    case 3:
                        spn = findViewById(R.id.spnDeLongitud);
                        de = spn.getSelectedItemPosition();

                        spn = findViewById(R.id.spnALongitud);
                        a = spn.getSelectedItemPosition();
                        break;
                    case 4:
                        spn = findViewById(R.id.spnDeAlmacenamiento);
                        de = spn.getSelectedItemPosition();

                        spn = findViewById(R.id.spnAAlmacenamiento);
                        a = spn.getSelectedItemPosition();
                        break;
                    case 5:
                        spn = findViewById(R.id.spnDeTiempo);
                        de = spn.getSelectedItemPosition();

                        spn = findViewById(R.id.spnATiempo);
                        a = spn.getSelectedItemPosition();
                        break;
                    case 6:
                        spn = findViewById(R.id.spnDeTransferenciasDatos);
                        de = spn.getSelectedItemPosition();

                        spn = findViewById(R.id.spnATransferenciasDatos);
                        a = spn.getSelectedItemPosition();
                        break;
                }


                tempVal = findViewById(R.id.txtCantidad);
                double cantidad = Double.parseDouble(tempVal.getText().toString());

                tempVal = findViewById(R.id.lblRespuesta);
                BigDecimal respuesta = new BigDecimal(String.valueOf(objConversores.convertir(opcion, de, a, cantidad)));
               // double respuesta = objConversores.convertir(opcion, de, a, cantidad);
                tempVal.setText("Respuesta: "+ respuesta);
            }
        });
    }
}
class conversores {
    BigDecimal[][] valores = {
            {new BigDecimal(""), new BigDecimal(""), new BigDecimal(""), new BigDecimal(""), new BigDecimal(""), new BigDecimal(""), new BigDecimal("")}, // monedas
            {new BigDecimal("1"), new BigDecimal("1000"), new BigDecimal("1000000"), new BigDecimal("0.001"), new BigDecimal("35.274"), new BigDecimal("2.20462"), new BigDecimal("0.01"), new BigDecimal("10"), new BigDecimal("100"), new BigDecimal("10000")}, // masa
            {new BigDecimal("1"), new BigDecimal("0.000000001"), new BigDecimal("0.000001"), new BigDecimal("0.001"), new BigDecimal("1000"), new BigDecimal("1057"), new BigDecimal("264.172"), new BigDecimal("6.29"), new BigDecimal("35.3148"), new BigDecimal("61023.7")}, // volumen
            {new BigDecimal("1"), new BigDecimal("1000"), new BigDecimal("0.01"), new BigDecimal("0.001"), new BigDecimal("0.0254"), new BigDecimal("0.3048"), new BigDecimal("0.9144"), new BigDecimal("1609.34"), new BigDecimal("0.000001"), new BigDecimal("0.000000001")}, // longitud
            {new BigDecimal("1"), new BigDecimal("8"), new BigDecimal("8192"), new BigDecimal("8388608"), new BigDecimal("8589934592"), new BigDecimal("8796093022208"), new BigDecimal("9007199254740992"), new BigDecimal("9223372036854775808"), new BigDecimal("9444732965739290427392"), new BigDecimal("9671406556917033397649408")}, // Almacenamiento
            {}, // tiempo
            {new BigDecimal("1"), new BigDecimal("8"), new BigDecimal("1000"), new BigDecimal("1000000"), new BigDecimal("1000000000"), new BigDecimal("1000000000000"), new BigDecimal("1000000000000000"), new BigDecimal("1000000000000000000"), new BigDecimal("1000000000000000000000"), new BigDecimal("1000000000000000000000000")}, // transferencia
    };
//Acelerometro: Desplazamiento vertical, horizontal y
    public BigDecimal convertir(int opcion, int de, int a, double cantidad) {
        // Convierte la cantidad a BigDecimal para mantener la precisión
        BigDecimal cantidadBigDecimal = new BigDecimal(String.valueOf(cantidad));

        // Realiza la división con precisión y redondeo
        BigDecimal resultado = valores[opcion][a].divide(valores[opcion][de], 2, RoundingMode.HALF_UP).multiply(cantidadBigDecimal);

        return resultado;
    }
}