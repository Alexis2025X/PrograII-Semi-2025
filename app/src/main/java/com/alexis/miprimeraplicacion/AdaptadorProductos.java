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

public class AdaptadorProductos extends BaseAdapter {
    Context context;
    ArrayList<productos> alAmigos;
    productos misProductos;
    LayoutInflater inflater;

    public AdaptadorProductos(Context context, ArrayList<productos> alAmigos) {
        this.context = context;
        this.alAmigos = alAmigos;
    }

    @Override
    public int getCount() {
        return alAmigos.size();
    }

    @Override
    public Object getItem(int position) {
        return alAmigos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View itemView = inflater.inflate(R.layout.fotos, parent, false);
        try {
            misProductos = alAmigos.get(position);

            TextView tempVal = itemView.findViewById(R.id.lblcodigoAdaptador);
            tempVal.setText(misProductos.getcodigo());

            tempVal = itemView.findViewById(R.id.lblDescripcionAdaptador);
            tempVal.setText(misProductos.getdescripcion());

            tempVal = itemView.findViewById(R.id.lblMarcaAdaptador);
            tempVal.setText(misProductos.getmarca());

            tempVal = itemView.findViewById(R.id.lblPrecioAdaptador);
            tempVal.setText(misProductos.getprecio());

            tempVal = itemView.findViewById(R.id.lblPresentacionAdaptador);
            tempVal.setText(misProductos.getpresentacion());

            tempVal = itemView.findViewById(R.id.lblCostoAdaptador);
            tempVal.setText(misProductos.getCosto());

            tempVal = itemView.findViewById(R.id.lblStockAdaptador);
            tempVal.setText(misProductos.getStock());

            tempVal = itemView.findViewById(R.id.lblGananciaAdaptador);
            String ganancia = String.valueOf(Double.parseDouble(misProductos.getprecio()) - Double.parseDouble(misProductos.getCosto()));
            tempVal.setText(ganancia);

            ImageView img = itemView.findViewById(R.id.imgFotoAdaptador);
            Bitmap bitmap = BitmapFactory.decodeFile(misProductos.getFoto());
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}