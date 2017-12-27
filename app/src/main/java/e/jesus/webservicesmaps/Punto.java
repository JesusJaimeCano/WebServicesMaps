package e.jesus.webservicesmaps;

import android.widget.Spinner;

import java.io.Serializable;

/**
 * Created by Jesus on 26/12/2017.
 */

public class Punto implements Serializable {

    private String hashTag;
    private String comentario;
    private String latitud;
    private String longitud;
    private String distancia;

    public Punto(String hashTag, String comentario, String latitud, String longitud, String distancia) {
        this.hashTag = hashTag;
        this.comentario = comentario;
        this.latitud = latitud;
        this.longitud = longitud;
        this.distancia = distancia;
    }

    public String getHashTag() {
        return hashTag;
    }

    public String getComentario() {
        return comentario;
    }

    public String getLatitud() {
        return latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public String getDistancia() {
        return distancia;
    }

}
