package com.alexis.miprimeraplicacion;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ImageView;
import java.util.ArrayList;


public class adaptadorMascotas extends BaseAdapter {
    Context context;
    ArrayList<mascotas> alMascotas;
    mascotas misMascotas;
    LayoutInflater inflater;

    public adaptadorMascotas(Context context, ArrayList<mascotas> alMascotas) {
        this.context = context;
        this.alMascotas = alMascotas;
    }

    @Override
    public int getCount() {
        return alMascotas.size();
    }

    @Override
    public Object getItem(int position) {
        return alMascotas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.vista_mascotas, parent, false);
        try {
            misMascotas= alMascotas.get(position);

            TextView tempVal = itemView.findViewById(R.id.lblNombreMascotaAdaptador);
            tempVal.setText(misMascotas.getNombre());

            ImageView img = itemView.findViewById(R.id.imgFotoMascotaAdaptado);
            Bitmap bitmap = BitmapFactory.decodeFile(misMascotas.getFoto());
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}