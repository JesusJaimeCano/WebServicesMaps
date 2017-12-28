package e.jesus.webservicesmaps;

import android.Manifest;
import android.app.ProgressDialog;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Jesus on 26/12/2017.
 */

public class AgregarPunto extends AppCompatActivity {

    Spinner hashTagspinner;
    String[] hashtag = {"#bache", "#semaforomal", "#fugadeagua", "#inseguridad"};
    ArrayAdapter adapter;
    String elegido = "";
    EditText comentario;
    Button nuevoComentario;
    int reporte = 0;
    double latitud = 0.0, longitud = 0.0;

    TextView latitudAhora , longitudAhora;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.agregar_punto);

        //////////////////////////////////MAPAS///////////////////////

        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION}, 10);
        latitudAhora = findViewById(R.id.latitudeAhora);
        longitudAhora = findViewById(R.id.longitudAhora);

        LocationManager mLocManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Localizacion localizacion = new Localizacion();

        mLocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,localizacion);


        //////////////////////////////////MAPAS///////////////////////



        hashTagspinner = findViewById(R.id.hashTagNuevoPunto);
        comentario = findViewById(R.id.comentarioNuevoPunto);
        nuevoComentario = findViewById(R.id.nuevoPuntoButton);

        adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, hashtag);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        hashTagspinner.setAdapter(adapter);
        hashTagspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                elegido = hashtag[position];
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                elegido = hashtag[0];
            }
        });

        nuevoComentario.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SubirComentario();
            }
        });

    }

    public class Localizacion implements LocationListener{

        @Override
        public void onLocationChanged(Location location) {
            String text = "Mi ubicacion actual es: \n" + "Latitud " + location.getLatitude() + "\nLongitud" + location.getLongitude();
            latitudAhora.setText(text);
            latitud = location.getLatitude();
            longitud = location.getLongitude();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    }

    public void SubirComentario(){

        final ProgressDialog loading = ProgressDialog.show(this, "Subiendo Punto", "Espere, no este chingando", false, false);
        String url = "http://alexchaps.com/bde/nuevoReporte.php";

        StringRequest stringRequest = new StringRequest(
                Request.Method.POST,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    loading.dismiss();
                    }
                }
        ){
            protected Map<String,String> getParams(){

                Map<String, String> params = new HashMap<>();
                params.put("reportes", String.valueOf(reporte));
                params.put("hashtag", elegido);
                params.put("comentario", comentario.getText().toString());
                params.put("latitud", latitud+"");
                params.put("latitud", longitud+"");



                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(stringRequest);
    }




}
