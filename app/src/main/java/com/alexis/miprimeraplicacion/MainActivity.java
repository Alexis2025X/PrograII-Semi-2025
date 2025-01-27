package com.alexis.miprimeraplicacion;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button btn;
    //tamVal es un valor temporal
    TextView temVal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         //Buscamos el boton por su ID
         btn = findViewById(R.id.btnCalcular);
         btn.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 temVal = findViewById(R.id.txtNum1);
                 double num1 = Double.parseDouble(temVal.getText().toString());

                 temVal = findViewById(R.id.txtNum2);
                 double num2 = Double.parseDouble(temVal.getText().toString());

                 double respuesta = num1 + num2;

                 temVal = findViewById(R.id.lblRespuesta);
                 temVal.setText("Respuesta: " + respuesta);

             }
         });
    }
}