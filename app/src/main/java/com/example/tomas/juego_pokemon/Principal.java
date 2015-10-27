package com.example.tomas.juego_pokemon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class Principal extends AppCompatActivity {
   private Button jugar,creditos,listapokemon,salir;
   private MediaPlayer reproductor;
   private int adivinados=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_principal);
        jugar=(Button)findViewById(R.id.btnjugar);
        creditos=(Button)findViewById(R.id.btnacerca);
        listapokemon= (Button) findViewById(R.id.btnlista);
        salir= (Button) findViewById(R.id.btnsalir);
        CargarPreferencias();
        reproductor= MediaPlayer.create(this,R.raw.musicafondo);
        reproductor.setLooping(true);
        reproductor.start();

        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (adivinados==Jugar.nombre_pokemon.length)
                {
                    Toast.makeText(getApplication(),"Has adivinado todos los Pokemons",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    Intent nuevoform=new Intent(Principal.this,Jugar.class);
                    startActivity(nuevoform);
                }

            }
        });

        listapokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform=new Intent(Principal.this,Pokedex.class);
                startActivity(nuevoform);
            }
        });

        creditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform=new Intent(Principal.this,Creditos.class);
                startActivity(nuevoform);

            }
        });

        salir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (reproductor.isPlaying())
        {
            reproductor.stop();
            reproductor.release();
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        reproductor.start();
        CargarPreferencias();
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        reproductor.pause();
    }

    public void CargarPreferencias()
    {
        SharedPreferences mispreferencias = getSharedPreferences("PreferenciaPokemon", Context.MODE_PRIVATE);
        adivinados=mispreferencias.getInt("adivinados",0);
    }

}
