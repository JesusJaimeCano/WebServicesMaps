package e.jesus.webservicesmaps;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MapsActivity2 extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private static ArrayList<Punto> puntos = new ArrayList<>();
    private String url = "http://alexchaps.com/bde/verReportes.php?longitud=-99.1732938&latitud=19.4137175";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps2);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;
        ConsultarWebServices(0);
    }

    public void ConsultarWebServices(int idReporte){
        JsonObjectRequest request = new JsonObjectRequest(
                com.android.volley.Request.Method.GET,
                url + String.valueOf(idReporte),
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Respuesta" + "", response.toString());
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
                                double latitudD = Double.parseDouble(latitud);
                                double longitudD = Double.parseDouble(longitud);
                                // Add a marker in Sydney and move the camera
                                LatLng sydney = new LatLng(latitudD,longitudD );
                                mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
                            }

                            Log.d("Lista", puntos.get(3).getLatitud());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
}
