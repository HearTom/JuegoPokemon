package com.example.tomas.juego_pokemon;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Tomas on 08/08/2014.
 */
public class PokemonAdapter extends BaseAdapter {
    protected Activity activity;
    protected ArrayList<Pokemon> items;

    public PokemonAdapter(Activity activity, ArrayList<Pokemon> items) {
        this.activity = activity;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int position) {
        return items.get(position);
    }

    @Override
    public long getItemId(int position) {
        return items.get(position).getId();
    }

    @Override
    public View getView(int position, View contentView, ViewGroup parent) {
        View vi=contentView;

        if(contentView == null) {
            LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            vi = inflater.inflate(R.layout.listview_layout, null);
        }

        Pokemon item = items.get(position);

        ImageView image = (ImageView) vi.findViewById(R.id.imgpokemon);
        int resId = activity.getResources().getIdentifier(item.getNombre(), "drawable", activity.getPackageName());
        image.setImageResource(resId);
        TextView nombre = (TextView) vi.findViewById(R.id.lblpokemon);
        nombre.setText(item.getNombre().equals("pokeball") ? "??????" : item.getNombre());
        return vi;
    }
}
