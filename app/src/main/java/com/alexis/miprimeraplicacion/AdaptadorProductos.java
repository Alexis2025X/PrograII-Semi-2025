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
    ArrayList<productos> alProductos;
    productos misProductos;
    LayoutInflater inflater;

    public AdaptadorProductos(Context context, ArrayList<productos> alProductos) {
        this.context = context;
        this.alProductos = alProductos;
    }

    //Cuenta el array de amigos y da su cantidad de valores
    @Override
    public int getCount() {
        return alProductos.size();
    }

    //Obtiene el objeto de la posicion del array y su posicion
    @Override
    public Object getItem(int position) {
        return alProductos.get(position);
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
        View itemView = inflater.inflate(R.layout.fotos, parent, false);
        try {
            misProductos = alProductos.get(position);

            TextView tempVal = itemView.findViewById(R.id.lblCodigoAdaptador);
            tempVal.setText(misProductos.getCodigo());

            tempVal = itemView.findViewById(R.id.lblDescripcionAdaptador);
            tempVal.setText(misProductos.getDescripcion());

            tempVal = itemView.findViewById(R.id.lblMarcaAdaptador);
            tempVal.setText(misProductos.getMarca());

            tempVal = itemView.findViewById(R.id.lblPresentacionAdaptador);
            tempVal.setText(misProductos.getPresentacion());

            tempVal = itemView.findViewById(R.id.lblPrecioAdaptador);
            tempVal.setText(misProductos.getPrecio());

            ImageView img = itemView.findViewById(R.id.imgFotoAdaptador);
            Bitmap bitmap = BitmapFactory.decodeFile(misProductos.getFoto());
            img.setImageBitmap(bitmap);
        } catch (Exception e) {
            Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
        return itemView;
    }
}