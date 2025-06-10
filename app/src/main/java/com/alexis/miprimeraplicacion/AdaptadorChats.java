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

public class AdaptadorChats extends BaseAdapter {
    Context context;
    ArrayList<chats> alChats;
    chats misChats;
    LayoutInflater inflater;
    
        public AdaptadorChats(Context context, ArrayList<chats> alChats) {
            this.context = context;
            this.alChats = alChats;
        }

        @Override
        public int getCount() {
            return alChats.size();
        }

        @Override
        public Object getItem(int position) {
            return alChats.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View itemView = inflater.inflate(R.layout.vista_chats, parent, false);
            try {
                misChats = alChats.get(position);

                TextView tempVal = itemView.findViewById(R.id.lblNombreChatAdaptador);
                tempVal.setText(misChats.getNombre());

                tempVal = itemView.findViewById(R.id.lblTelefonoAdaptador);
                tempVal.setText(misChats.getTelefono());

                tempVal = itemView.findViewById(R.id.lblEmailAdaptador);
                tempVal.setText(misChats.getEmail());

                ImageView img = itemView.findViewById(R.id.imgFotoAdaptador);
                Bitmap bitmap = BitmapFactory.decodeFile(misChats.getFoto());
                img.setImageBitmap(bitmap);
            } catch (Exception e) {
                Toast.makeText(context, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
            return itemView;
        }
    }
