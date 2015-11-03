package com.example.tomas.juego_pokemon;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;


public class Principal extends AppCompatActivity {
    private Button jugar, creditos, listapokemon, salir;
    private MediaPlayer reproductor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_principal);
        jugar = (Button) findViewById(R.id.btnjugar);
        creditos = (Button) findViewById(R.id.btnacerca);
        listapokemon = (Button) findViewById(R.id.btnlista);
        salir = (Button) findViewById(R.id.btnsalir);
        PokemonDB.cargarDatos(getApplicationContext());
        reproductor = MediaPlayer.create(this, R.raw.musicafondo);
        reproductor.setLooping(true);
        reproductor.start();

        jugar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PokemonDB.isWin()) {
                    Toast.makeText(getApplication(), getResources().getString(R.string.msg_ganaste), Toast.LENGTH_SHORT).show();
                } else {
                    Intent intent = new Intent(Principal.this, Jugar.class);
                    startActivity(intent);
                }

            }
        });

        listapokemon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(Principal.this, Pokedex.class);
                startActivity(nuevoform);
            }
        });

        creditos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent nuevoform = new Intent(Principal.this, Creditos.class);
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
        if (reproductor.isPlaying()) {
            reproductor.stop();
            reproductor.release();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        reproductor.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        reproductor.pause();
    }
}
