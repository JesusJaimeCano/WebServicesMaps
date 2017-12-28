package e.jesus.webservicesmaps;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Jesus on 26/12/2017.
 */

public class MainActivity extends AppCompatActivity {

    String url = "http://alexchaps.com/bde/verReportes.php?longitud=-99.1732938&latitud=19.4137175";
    ArrayList<Punto> puntos;
    ListView listaPuntos;
    Button irAgregar, vertodo;
    Spinner hashTagspinner;
    String[] hashtag = {"","#bache", "#semaforomal", "#fugadeagua", "#inseguridad"};
    ArrayAdapter adapter;
    String elegido = "";
    ArrayList<Punto> busqueda;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        puntos = new ArrayList<>();
        listaPuntos = findViewById(R.id.listaPuntosLV);
        busqueda = new ArrayList<>();

        irAgregar = findViewById(R.id.nuevaVentanaAgregarPuntoButton);
        irAgregar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AgregarPunto.class);
                startActivity(intent);
            }
        });
        vertodo= findViewById(R.id.verTodoButton);

        vertodo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MapsActivity2.class);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        ConsultarWebServices();
    }



    public void ConsultarWebServices(){
        puntos.clear();

        JsonObjectRequest request = new JsonObjectRequest(
                com.android.volley.Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Respuesta sin limpiar", response.toString());
                        try {
                            JSONArray jsonArray = response.getJSONArray("reportes");

                            for (int i = 0; i<jsonArray.length(); i++){
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String hashtag = jsonObject.getString("hashtag");
                                String comanetario = jsonObject.getString("comentario");
                                String latitud = jsonObject.getString("latitud");
                                String longitud = jsonObject.getString("longitud");
                                String distancia = jsonObject.getString("distancia");
                                puntos.add(new Punto(hashtag,comanetario,latitud,longitud, distancia));
                                listaPuntos.setAdapter(new MyAdapter());
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        hashTagspinner = findViewById(R.id.busquedaHashTagSpinner);
                        adapter = new ArrayAdapter(MainActivity.this, android.R.layout.simple_spinner_item, hashtag);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        hashTagspinner.setAdapter(adapter);

                        hashTagspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                elegido = hashtag[position];


                                if(elegido.isEmpty()){
                                    Toast.makeText(MainActivity.this, "Esta vashio carnal", Toast.LENGTH_SHORT).show();
                                }else{
                                    for(Punto punto: puntos){
                                        if((punto.getHashTag()).equals(elegido)){
                                            busqueda.add(punto);
                                        }
                                    }
                                    listaPuntos.setAdapter(new MyAdapter());
                                }

                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {

                            }
                        });



                    }

                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("Respuesta", "Error" + error.getMessage());
                    }
                }

        );
        AppController.getInstance().addToRequestQueue(request);
    }

    class MyAdapter extends ArrayAdapter<Punto>{
        MyAdapter() {super(MainActivity.this, R.layout.elemento_row, puntos);}

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View row = getLayoutInflater().inflate(R.layout.elemento_row,parent,false);

            TextView hashtag = row.findViewById(R.id.hashTagWSTV);
            TextView comentario = row.findViewById(R.id.comentarioWSTV);
            TextView latitud = row.findViewById(R.id.latitudWETV);
            TextView longitud = row.findViewById(R.id.longitudWSTV);
            TextView distancia = row.findViewById(R.id.distanciaWSTV);
            Button alMapa = row.findViewById(R.id.verEnMapaUnSoloPuntoButton);

            final Punto puntoActual = puntos.get(position);

            hashtag.setText(puntoActual.getHashTag());
            comentario.setText(puntoActual.getComentario());
            latitud.setText(puntoActual.getLatitud());
            longitud.setText(puntoActual.getLongitud());
            distancia.setText(puntoActual.getDistancia());

            alMapa.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                    intent.putExtra("punto", puntoActual);
                    startActivity(intent);
                }
            });

            return row;
        }
    }

}
