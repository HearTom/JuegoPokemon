package com.example.tomas.juego_pokemon;

/**
 * Created by Tomas on 08/08/2014.
 */
public class Pokemons {
    private String imagen;
    private String nombre_pokemon;
    protected long id;
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }


    public Pokemons(long id,String imagen, String nombre_pokemon) {
        this.id=id;
        this.imagen = imagen;
        this.nombre_pokemon = nombre_pokemon;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getNombre_pokemon() {
        return nombre_pokemon;
    }

    public void setNombre_pokemon(String nombre_pokemon) {
        this.nombre_pokemon = nombre_pokemon;
    }
}
