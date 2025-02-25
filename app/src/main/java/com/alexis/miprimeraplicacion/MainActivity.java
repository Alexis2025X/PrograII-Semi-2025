package com.alexis.miprimeraplicacion;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    TextView cantidadMetros;
    EditText cantidadM;
    TextView txtResultAgua;
    Button btn;
    Button btnCombersor;

    Spinner spn;
    EditText cantidadComber;
    TextView resultadoComber;

    TabHost tbh;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tbh = findViewById(R.id.tbhConversor);
        tbh.setup();

        tbh.addTab(tbh.newTabSpec("tabPagoAgua").setContent(R.id.tabPagoAgua).setIndicator("Pago agua", null));
        tbh.addTab(tbh.newTabSpec("tabConversorArea").setContent(R.id.tabConversorArea).setIndicator("Comvertir area", null));

        btn = findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantidadM = findViewById(R.id.txtCantidad);
                double metros = Double.parseDouble(cantidadM.getText().toString());
                double valor_a_pagar = 0;
                txtResultAgua = findViewById(R.id.lblRespuesta);
                if (metros >= 0 && metros <= 18) {
                    valor_a_pagar = 6;
                    txtResultAgua.setText("Respuesta: $" + valor_a_pagar);
                } else if (metros > 18 && metros <= 28) {

                    valor_a_pagar = ((metros - 18) * 0.45)+6;
                    txtResultAgua.setText("Respuesta: $" + valor_a_pagar);
                } else if (metros > 28) {
                    valor_a_pagar = (((metros - 28) * 0.65) + ((28 - 18) * 0.45)) + 6;
                    txtResultAgua.setText("Respuesta: $" + valor_a_pagar);
                }
            }
        });

        btnCombersor = findViewById(R.id.btnCalcularArea);
        btnCombersor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int de = 0;
                spn = findViewById(R.id.spnDeArea);
                de = spn.getSelectedItemPosition();
                int a = 0;
                spn = findViewById(R.id.spnAArea);
                a = spn.getSelectedItemPosition();

                cantidadComber = findViewById(R.id.txtCantidadArea);

                double cantidadAcombertir = Double.parseDouble(cantidadComber.getText().toString());
                resultadoComber = findViewById(R.id.lblRespuestaArea);
                resultadoComber.setText("Resultado: " + combertirArea(de, a, cantidadAcombertir));

            }
        });
    }

    class combersorArea {
        double[][] valores =
                {
                        {1,16,0.705012,7050.12,843187.33452,4970.419,7588686.011}
                };
    }
    public double combertirArea(int de, int a , double cant) {
        combersorArea obj = new combersorArea();
        return (obj.valores[0][a] / obj.valores[0][de]) * cant;
    }
}
