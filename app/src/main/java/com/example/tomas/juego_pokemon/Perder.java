package com.example.tomas.juego_pokemon;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.WindowManager;

/**
 * Created by Tomas on 12/08/2014.
 */
public class Perder extends AppCompatActivity {

    private final int TIEMPO_ESPERA = 4000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_perder);
        Cuenta_Regresiva();
    }

    public void Cuenta_Regresiva() {
        new CountDownTimer(TIEMPO_ESPERA, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {
                finish();
            }
        }.start();
    }
}

