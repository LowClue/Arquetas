package es.alarcon.arquetanatureble.GUI;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import es.alarcon.arquetanatureble.BEAN.BeanBluetoothDevice;
import es.alarcon.arquetanatureble.BEAN.BeanInformesDB;
import es.alarcon.arquetanatureble.DB_SQLITE.InformeSQLiteDataSource;
import es.alarcon.arquetanatureble.R;
import es.alarcon.arquetanatureble.UTIL.Constant;
import es.alarcon.arquetanatureble.WEBSERVICE.WebServiceConstant;
import es.alarcon.arquetanatureble.WEBSERVICE.VolleySingleton;


public class ResumenActivity extends Activity {

    private static BeanInformesDB beanInformesDB;
    private static BeanBluetoothDevice beanBluetoothDevice;

    private static Context mContex;
    private static InformeSQLiteDataSource mInformeBD;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resumen);

        mContex = this;

        //get information about informes
        beanInformesDB      = getIntent().getParcelableExtra(Constant.EXTRA_INFORMESDB);
        beanBluetoothDevice = getIntent().getParcelableExtra(Constant.EXTRA_BEAN_BLUETOOTHDEVICE);

        //Setting the data in text to check everything is ok.


        //Sending the data to the webservice.
        saveInfoOnCloud();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_resumen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void proccessReplyFromWebService(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    Log.d(Constant.TAG, "respuesta exitosa.");
                    Toast.makeText(this, "Insercion correcta", Toast.LENGTH_LONG).show();
                    break;
                case "2": // FALLIDO
                    Log.d(Constant.TAG, "respuesta fallida.");
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(this, mensaje2, Toast.LENGTH_LONG).show();
                    break;
                default:
                    Log.d(Constant.TAG, "respuesta error estado:"+estado);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    public void saveInfoOnCloud() {
        // Obtener valores actuales de los controles

        Log.d(Constant.TAG, WebServiceConstant.INSERT);

        String newURL = WebServiceConstant.INSERT
                +"?acceso_ubicacion="+ beanInformesDB.getAccesoUbicacion()
                +"&perimetro_arqueta="+ beanInformesDB.getPerimetroArqueta()
                +"&puerta_acceso="+ beanInformesDB.getPuertaAcceso()
                +"&cubierta="+ beanInformesDB.getCubierta()
                +"&param_verticales_int="+ beanInformesDB.getParVertInt()
                +"&param_verticales_ext="+ beanInformesDB.getParVertExt()
                +"&ventilacion_lateral="+  beanInformesDB.getVentilacionLateral()
                +"&ventilacion_superior="+ beanInformesDB.getVentilacionSuperior()
                +"&pates_escalera="+ beanInformesDB.getPatesEscalera()
                +"&distancia_reglamentaria_elementos="+beanInformesDB.getDistanciaRegElementos()
                +"&ventosas="+ beanInformesDB.getVentosas()
                +"&juntas_carretes="+ beanInformesDB.getJuntasUnion()
                +"&manometros="+ beanInformesDB.getManometros()
                +"&contadores="+ beanInformesDB.getContadores()
                +"&fecha="+ beanInformesDB.getFecha()
                +"&direccion_arqueta="+beanInformesDB.getDireccion_arqueta()
                +"&comentario="+ beanInformesDB.getComentario()
                +"&foto="+ beanInformesDB.getFoto();

        Log.d(Constant.TAG, newURL);

        // Actualizar datos en el servidor
        VolleySingleton.getInstace(this).addToRequestQueue(
                new JsonObjectRequest(
                        Request.Method.GET,
                        newURL,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                // Procesar la respuesta del servidor
                                proccessReplyFromWebService(response);
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Log.d(Constant.TAG, "Error Volley (response insert): " + error.getMessage());
                                Toast.makeText(mContex, "No se comunicó con el Cloud.", Toast.LENGTH_LONG).show();
                                //aqui guardamos en la SQLite
                                mInformeBD = new InformeSQLiteDataSource(getApplicationContext());
                                mInformeBD.insertInforme(beanInformesDB);
                                Toast.makeText(mContex, "Guardado informe para posterior envio.", Toast.LENGTH_LONG).show();
                            }
                        }

                ) {
                    @Override
                    public Map<String, String> getHeaders() {
                        Map<String, String> headers = new HashMap<String, String>();
                        headers.put("Content-Type", "application/json; charset=utf-8");
                        headers.put("Accept", "application/json");
                        return headers;
                    }

                    @Override
                    public String getBodyContentType() {
                        return "application/json; charset=utf-8" + getParamsEncoding();
                    }
                }
        );

    }
}
