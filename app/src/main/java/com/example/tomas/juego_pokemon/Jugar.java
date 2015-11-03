package com.example.tomas.juego_pokemon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Tomas on 30/07/2014.
 */
public class Jugar extends AppCompatActivity implements View.OnClickListener {
    ImageView imagen;
    TextView lblintentos;
    TextView lblcuenta;
    private MediaPlayer reproductor;
    private final int TIEMPO_ESPERA = 5000;
    private Button btn1, btn2, btn3, btn4;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_jugar);
        iniciarComponentes();
        PokemonDB.cargarDatos(getApplicationContext());
        reproductor = MediaPlayer.create(this, R.raw.atrapalosya);
        reproductor.setLooping(true);
        reproductor.start();
        new MiTarea().execute();
        PokemonDB.cargarConfiguracion(getApplicationContext());
        lblintentos.setText(String.valueOf(PokemonDB.INTENTOS));
    }


    public void setSombra(int id) {
        int resId = getResources().getIdentifier(PokemonDB.getSombra(id), "drawable", getPackageName());
        imagen.setImageResource(resId);
    }

    public void setPokemon(int id) {
        int resId = getResources().getIdentifier(PokemonDB.getNombre(id), "drawable", getPackageName());
        imagen.setImageResource(resId);
    }

    private void iniciarComponentes() {
        imagen = (ImageView) findViewById(R.id.miimagen);
        lblintentos = (TextView) findViewById(R.id.lblintentos);
        lblcuenta = (TextView) findViewById(R.id.lblcuenta);
        btn1 = (Button) findViewById(R.id.btn1);
        btn2 = (Button) findViewById(R.id.btn2);
        btn3 = (Button) findViewById(R.id.btn3);
        btn4 = (Button) findViewById(R.id.btn4);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Button boton = (Button) v;
        String nombrePokemon = boton.getText().toString().toLowerCase();
        if (PokemonDB.isPokemon(nombrePokemon)) {
            setPokemon(PokemonDB.NUMEROGENERADO);
            PokemonDB.setAdivinado(PokemonDB.NUMEROGENERADO, true);
            PokemonDB.ADIVINADOS++;
            habilitarBotones(false);
            boton.setVisibility(View.VISIBLE);
            boton.setClickable(false);
            esperar();
        } else {
            PokemonDB.DisminuirIntentos();
            lblintentos.setText(String.valueOf(PokemonDB.INTENTOS));
            v.setVisibility(View.INVISIBLE);
        }

        if (PokemonDB.isGameOver()) {
            Intent i = new Intent(Jugar.this, Perder.class);
            startActivity(i);
            finish();
        }
    }

    public void esperar() {
        new CountDownTimer(TIEMPO_ESPERA, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                lblcuenta.setText("Generando en " + (millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                lblcuenta.setText("");
                if (!PokemonDB.isWin()) {
                    new MiTarea().execute();
                } else {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.msg_ganaste), Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }.start();
    }

    public void habilitarBotones(boolean valor) {
        if (valor) {
            btn1.setVisibility(View.VISIBLE);
            btn2.setVisibility(View.VISIBLE);
            btn3.setVisibility(View.VISIBLE);
            btn4.setVisibility(View.VISIBLE);
            btn1.setClickable(true);
            btn2.setClickable(true);
            btn3.setClickable(true);
            btn4.setClickable(true);
        } else {
            btn1.setVisibility(View.INVISIBLE);
            btn2.setVisibility(View.INVISIBLE);
            btn3.setVisibility(View.INVISIBLE);
            btn4.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (PokemonDB.isGameOver()) {
            PokemonDB.removerDatos(getApplicationContext());
        } else {
            PokemonDB.guardarDatos(getApplicationContext());
        }
        reproductor.pause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        reproductor.start();
    }

    @Override
    protected void onDestroy() {
        if (reproductor.isPlaying()) {
            reproductor.stop();
            reproductor.release();
        }
        super.onDestroy();
    }

    private class MiTarea extends AsyncTask<Void, Void, Void> {
        private ProgressDialog dialog;
        private int numero = 0;
        private int totalgenerados = 4;
        private int numerosrestantes = totalgenerados - 1;
        private int contador = 0;
        private int permitidos = 0;
        private int valorgenerado = -1;
        ArrayList<Integer> numeros = new ArrayList<>();

        @Override
        protected void onPreExecute() {
            dialog = new ProgressDialog(Jugar.this);
            dialog.setMessage("Generando ...");
            dialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {
            do {
                numero = ((int) (Math.random() * PokemonDB.getTamaño()));
                if (!PokemonDB.isAdivinado(numero) && valorgenerado <= 0) {
                    valorgenerado = numero;
                    contador++;
                    numeros.add(numero);
                } else if (!numeros.contains(numero) && permitidos < numerosrestantes) {
                    numeros.add(numero);
                    contador++;
                    permitidos++;
                }
            } while (contador < totalgenerados);
            Collections.shuffle(numeros);

            try {
                Thread.sleep(250);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            PokemonDB.NUMEROGENERADO = valorgenerado;
            setSombra(valorgenerado);
            habilitarBotones(true);
            btn1.setText(PokemonDB.getNombre(numeros.get(0)));
            btn2.setText(PokemonDB.getNombre(numeros.get(1)));
            btn3.setText(PokemonDB.getNombre(numeros.get(2)));
            btn4.setText(PokemonDB.getNombre(numeros.get(3)));
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
            super.onPostExecute(aVoid);
        }
    }
}
