package com.alexis.miprimeraplicacion;

import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btn;
    //tamVal es un valor temporal
    TextView tempVal;
    RadioGroup rgo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         //Buscamos el boton por su ID
         btn = findViewById(R.id.btnCalcular);

         //El bloque de codigo se ejecutara al dar click en le boton
         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                    // temVal = findViewById(R.id.txtNum1);
                    // double num1 = Double.parseDouble(temVal.getText().toString());
                 //tempVal txt = findViewById()

                 tempVal = findViewById(R.id.txtNum1);
                 double num1 = 0;


                 if (tempVal.getText().toString().isEmpty()){
                     num1 = 0;
                 }else {
                     num1 = Double.parseDouble(tempVal.getText().toString());
                 }
                 //Descomentarlo sino funciona
                 tempVal = findViewById(R.id.txtNum2);
                 double num2 = 0;
                 double respuesta = 0;

                 if (tempVal.getText().toString().isEmpty()){
                     num2 = 0;
                 }else {
                     num2 = Double.parseDouble(tempVal.getText().toString());
                 }

                     tempVal = findViewById(R.id.lblRespuesta);

                     rgo = findViewById(R.id.rgoOpciones);
                     if (rgo.getCheckedRadioButtonId() == R.id.rgoSuma) {
                         //tempVal = findViewById(R.id.txtNum2);
                         //tempVal.setEnabled(true);
                         respuesta = num1 + num2;

                     } else if (rgo.getCheckedRadioButtonId() == R.id.rgoResta) {
                         respuesta = num1 - num2;
                     } else if (rgo.getCheckedRadioButtonId() == R.id.rgoMultiplicacion) {
                         respuesta = num1 * num2;
                     } else if (rgo.getCheckedRadioButtonId() == R.id.rgoDivision) {
                         respuesta = num1 / num2;
                     }
                     //Parte 2
                     else if (rgo.getCheckedRadioButtonId() == R.id.rgoExponencial) {
                         respuesta = Math.pow(num1, num2);
                     } else if (rgo.getCheckedRadioButtonId() == R.id.rgoPorcentaje) {
                         respuesta = (num1 / num2) * 100;
                     } else if (rgo.getCheckedRadioButtonId() == R.id.rgoRaiz) {
                         //tempVal = findViewById(R.id.txtNum1);
                        // tempVal.setHint("Indice" + R.id.txtNum1);
                         respuesta = Math.pow(num2, 1.0 / num1);
                       //  tempVal.setText("Respuesta: " + num1 +"√"+ num2 + "="+ respuesta);

                     } else if (rgo.getCheckedRadioButtonId() == R.id.rgoFactorial) {
                         //temVal = num2.SetEnabled(false);
                         double fact = 1;
                         for (double i = num1; i > 0; i--) {
                             fact = fact * i;
                         }
                         respuesta = fact;
                     }
                     tempVal.setText("Respuesta: " + respuesta);
                 }

         });
    }

}