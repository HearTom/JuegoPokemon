package com.example.tomas.juego_pokemon;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;
import android.widget.ListView;
import java.util.ArrayList;


/**
 * Created by Tomas on 09/08/2014.
 */
public class Pokedex extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_lista_pokemon);
        ListView lv = (ListView)findViewById(R.id.lvlista);
        ArrayList<Pokemons> itemsCompra = obtenerItems();
        PokemonAdapter adapter = new PokemonAdapter(this, itemsCompra);
        lv.setAdapter(adapter);
    }

    private ArrayList<Pokemons> obtenerItems() {
        ArrayList<Pokemons> items = new ArrayList<Pokemons>();
        SharedPreferences mispreferencias = getSharedPreferences("PreferenciaPokemon", Context.MODE_PRIVATE);
        for (int i=0;i<Jugar.nombre_pokemon.length;i++)
        {
            boolean bol=mispreferencias.getBoolean(Jugar.nombre_pokemon[i],false);
            if (bol)
            {
              items.add(new Pokemons(i,Jugar.nombre_pokemon[i],Jugar.nombre_pokemon[i]));
            }
            else
            {
                items.add(new Pokemons(i,"pokeball"," ??????????"));
            }
        }
        return items;
    }
}
