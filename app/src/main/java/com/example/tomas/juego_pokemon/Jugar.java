package com.example.tomas.juego_pokemon;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Tomas on 30/07/2014.
 */
public class Jugar extends AppCompatActivity {
    public static String[] nombre_pokemon={"bulbasaur","ivysaur","venasaur","charmander","charmeleon","charizard",
            "squirtle","wartortle","blastoise","caterpie","metapod","butterfree",
            "weedle","kakuna","beedrill","pidgey","pidgeotto","pidgeot","rattata",
            "raticate","spearow","fearow","ekans","arbok","pikachu","raichu","sandshrew",
            "sandslash","nidoran","nidorina","nidoqueen","cloyster","gloom","golem",
            "krabby","magmar","marowak","snorlax","starmie","vulpix"};

    public static String[] sombra_pokemon={"s_bulbasaur","s_ivysaur","s_venasaur","s_charmander","s_charmeleon","s_charizard",
            "s_squirtle","s_wartortle","s_blastoise","s_caterpie","s_metapod","s_butterfree",
            "s_weedle","s_kakuna","s_beedrill","s_pidgey","s_pidgeotto","s_pidgeot","s_rattata",
            "s_raticate","s_spearow","s_fearow","s_ekans","s_arbok","s_pikachu","s_raichu","s_sandshrew",
            "s_sandslash","s_nidoran","s_nidorina","s_nidoqueen","s_cloyster","s_gloom","s_golem",
            "s_krabby","s_magmar","s_marowak","s_snorlax","s_starmie","s_vulpix"};

    public static boolean[] estado={false,false,false,false,false,false,
            false,false,false,false,false,false,
            false,false,false,false,false,false,false,
            false,false,false,false,false,false,false,false,
            false,false,false,false,false,false,false,
            false,false,false,false,false,false};

    public static int pokemons_adivinados=0;
    public static int intentos=3;
    public static int numero_generado=0;
    private Button aceptar;
    private TextView mensaje_intentos,mensaje_cuenta;
    private EditText usuario_pokemon;
    private ImageView miimagen;
    private MediaPlayer reproductor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_jugar);
        aceptar=(Button) findViewById(R.id.btnaceptar);
        mensaje_intentos=(TextView)findViewById(R.id.lblintentos);
        mensaje_cuenta=(TextView)findViewById(R.id.lblcuenta);
        usuario_pokemon=(EditText) findViewById(R.id.txtpokemon);
        miimagen=(ImageView)findViewById(R.id.imgpokemon);
        CargarPreferencias();
        new MiTarea().execute();
        reproductor= MediaPlayer.create(this,R.raw.atrapalosya);
        reproductor.setLooping(true);
        reproductor.start();
        mensaje_intentos.setText("Tiene " + intentos + " intentos");
        aceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              String nombre=usuario_pokemon.getText().toString().toLowerCase();
                if(nombre.equals(nombre_pokemon[numero_generado]))
                {
                      establecer_pokemon(numero_generado);
                      estado[numero_generado]=true;
                      pokemons_adivinados++;
                      esperar();
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"No es el pokemon",Toast.LENGTH_SHORT).show();
                    intentos=intentos-1;
                    mensaje_intentos.setText("Tiene " + intentos + " intentos");
                }

                if (intentos==0)
                {
                    removerPreferencias();
                    Intent i = new Intent(Jugar.this,Perder.class);
                    startActivity(i);
                    finish();
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        reproductor.start();
    }

    public void esperar()
    {
        new CountDownTimer(5000,1000)
        {

            @Override
            public void onTick(long millisUntilFinished) {
               mensaje_cuenta.setText("Generando en " + (millisUntilFinished/1000));
            }

            @Override
            public void onFinish() {

                if (pokemons_adivinados==nombre_pokemon.length)
                {
                    finish();
                }
                else
                {
                    new MiTarea().execute();
                    mensaje_cuenta.setText("");
                    usuario_pokemon.setText("");
                }
            }
       }.start();
    }

    public void CargarPreferencias()
    {
        SharedPreferences mispreferencias = getSharedPreferences("PreferenciaPokemon", Context.MODE_PRIVATE);
        intentos=mispreferencias.getInt("intentos",3);
        pokemons_adivinados=mispreferencias.getInt("adivinados",0);
        for (int i=0;i<nombre_pokemon.length;i++)
        {
           estado[i]=mispreferencias.getBoolean(nombre_pokemon[i],false);
        }
    }

    public void GuardarPreferencias()
    {
        SharedPreferences mispreferencias = getSharedPreferences("PreferenciaPokemon", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = mispreferencias.edit();
        editor.putInt("intentos",intentos);
        editor.putInt("adivinados",pokemons_adivinados);
        for (int i=0;i<nombre_pokemon.length;i++)
        {
            editor.putBoolean(nombre_pokemon[i], estado[i]);
        }
        editor.commit();
    }

    private void establecer_pokemon(int numero)
    {
        int resId = getResources().getIdentifier(nombre_pokemon[numero], "drawable", getPackageName());
        miimagen.setImageResource(resId);
    }

    private void establecer_sombra(int numero)
    {
        int resId = getResources().getIdentifier(sombra_pokemon[numero], "drawable", getPackageName());
        miimagen.setImageResource(resId);
    }


    private void removerPreferencias()
    {
        SharedPreferences settings = getSharedPreferences("PreferenciaPokemon", Context.MODE_PRIVATE);
        settings.edit().clear().commit();
    }
    
    @Override
    protected void onStop() {
        if (intentos==0)
        {
            removerPreferencias();
        }
        else
        {
            GuardarPreferencias();
        }
        reproductor.pause();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        if (reproductor.isPlaying())
        {
            reproductor.stop();
            reproductor.release();
        }
        super.onDestroy();
    }

    private class MiTarea extends AsyncTask<Void, Void, Void> {
         private int valor_generado;
        @Override
        protected Void doInBackground(Void... params) {
            do {
                valor_generado=((int)(Math.random()*nombre_pokemon.length));
            }while(estado[valor_generado]);
            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            numero_generado=valor_generado;
            establecer_sombra(valor_generado);
            super.onPostExecute(aVoid);
        }
    }
}
