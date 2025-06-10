package com.alexis.miprimeraplicacion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class AdaptadorCitas extends BaseAdapter {
    Context context;
    ArrayList<citas> alCitas;
    citas misCitas;
    LayoutInflater inflater;

    public AdaptadorCitas(Context context, ArrayList<citas> alCitas) {
        this.context = context;
        this.alCitas = alCitas;
    }

    //Cuenta el array de amigos y da su cantidad de valores
    @Override
    public int getCount() {
        return alCitas.size();
    }

    //Obtiene el objeto de la posicion del array y su posicion
    @Override
    public Object getItem(int position) {
        return alCitas.get(position);
    }

    //Obtiene el id del objeto de la posicion del array
    @Override
    public long getItemId(int position) {
        return 0;
    }

    //Obtiene la vista de la posicion del array
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.vista_citas, parent, false);
        try {
            misCitas = alCitas.get(position);

            TextView tempVal = itemView.findViewById(R.id.lblNombreCitaAdaptador);
            tempVal.setText("Control de " + misCitas.getnombreMascota());

            tempVal = itemView.findViewById(R.id.lblFechaAdaptador);
            tempVal.setText(misCitas.getfecha());

            tempVal = itemView.findViewById(R.id.lblClinicaAdaptador);
            tempVal.setText(misCitas.getclinica());

            tempVal = itemView.findViewById(R.id.lblNotaAdaptador);
            tempVal.setText(misCitas.getnota());

            ImageView img = itemView.findViewById(R.id.imgFotoAdaptador);
            Bitmap bitmap = BitmapFactory.decodeFile(misCitas.getFoto());
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}