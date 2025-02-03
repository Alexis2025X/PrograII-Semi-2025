package com.alexis.miprimeraplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btn;
    TextView tempVal;
    Spinner spn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.btnCalcular);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tempVal = findViewById(R.id.txtNum1);
                double num1 = 0;
                if(tempVal.getText().toString().isEmpty()){
                    num1 = 0;
                }
                else{
                    num1 = Double.parseDouble(tempVal.getText().toString());
                }
                //Double.parseDouble(tempVal.getText().toString());

                tempVal = findViewById(R.id.txtNum2);
                double num2 = 0;
                if(tempVal.getText().toString().isEmpty()){
                    num2 = 0;
                }
                else{
                    num2 = Double.parseDouble(tempVal.getText().toString());
                }
                //Double.parseDouble(tempVal.getText().toString());

                double respuesta = 0.0;

                spn = findViewById(R.id.spnOpciones);
                switch (spn.getSelectedItemPosition()){
                    case 0:
                        respuesta = num1 + num2;
                        break;
                    case 1:
                        respuesta = num1 - num2;
                        break;
                    case 2:
                        respuesta = num1 * num2;
                        break;
                    case 3:
                        respuesta = num1 / num2;
                        break;
                    case 4:
                        respuesta = Math.pow(num1, num2);
                        break;
                    case 5:
                        respuesta = (num1 / num2) * 100;
                        break;
                    case 6:
                        respuesta = Math.pow(num2, 1.0 / num1);
                        break;
                    case 7:
                        double fact = 1;
                        for (double i = num1; i > 0; i--) {
                            fact = fact * i;
                        }
                        respuesta = fact;
                        break;

                }
                tempVal = findViewById(R.id.lblRespuesta);
                tempVal.setText("Respuesta: "+ respuesta);
            }
        });
    }
}