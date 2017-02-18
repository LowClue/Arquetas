package es.alarcon.arquetanatureble.GUI;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;

import es.alarcon.arquetanatureble.BEAN.BeanBluetoothDevice;
import es.alarcon.arquetanatureble.BEAN.BeanInformesDB;
import es.alarcon.arquetanatureble.BLE.HandlerBLE;
import es.alarcon.arquetanatureble.BLE.ServiceData;
import es.alarcon.arquetanatureble.CORE.BLE_Application;


import es.alarcon.arquetanatureble.R;
import es.alarcon.arquetanatureble.UTIL.Constant;
import es.alarcon.arquetanatureble.WEBSERVICE.BeanArqueta;
import es.alarcon.arquetanatureble.WEBSERVICE.VolleySingleton;
import es.alarcon.arquetanatureble.WEBSERVICE.WebServiceConstant;


public class DeviceActivity extends Activity implements View.OnClickListener
{
    private static Button bt_resumen;
    private static Button bt_conect2serverBLE;
    private static HandlerBLE mHandlerBLE;
    private static BeanInformesDB beanInformesDB;
    private static BeanBluetoothDevice beanBluetoothDevice;
    private static BroadcasterDataSensor broadcasterDataSensor;
    private static Handler mHandler;

    private static TextView flowmeter;
    private static TextView valve;
    private static TextView pressure;

    private static Gson gson = new Gson();
    private static JsonObjectRequest jsonObjectRequest;

    private static final int DELAY_ADQUISITION = 1;

    //###################################################################
    /****************** metodos del flujo Android. **********************/
    //###################################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        //get information about informes
        beanInformesDB      = getIntent().getParcelableExtra(Constant.EXTRA_INFORMESDB);
        beanBluetoothDevice = getIntent().getParcelableExtra(Constant.EXTRA_BEAN_BLUETOOTHDEVICE);

        //handler BLE
        mHandlerBLE = ((BLE_Application) getApplication()).getmHandlerBLEInstance(this);
        ((BLE_Application) getApplication()).resetHandlerBLE();

        bt_conect2serverBLE = (Button) findViewById(R.id.bt_enviarInforme);
        bt_resumen          = (Button) findViewById(R.id.bt_connectServer);

        bt_conect2serverBLE.setOnClickListener(this);
        bt_resumen.setOnClickListener(this);

        pressure  = (TextView) findViewById(R.id.tV_sensor1);
        flowmeter = (TextView) findViewById(R.id.tV_sensor2);
        valve     = (TextView)findViewById(R.id.tV_sensor3);

        //get data from WebService
        loadFromWebService(1);

        broadcasterDataSensor = new BroadcasterDataSensor();
        mHandler = new Handler();
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        IntentFilter i = new IntentFilter(BroadcasterDataSensor.DATA_FLOW_ACT);
        registerReceiver(broadcasterDataSensor, i);

        i = new IntentFilter(BroadcasterDataSensor.DATA_PRESSU_ACT);
        registerReceiver(broadcasterDataSensor, i);

        i = new IntentFilter(BroadcasterDataSensor.DATA_VALVE_ACT);
        registerReceiver(broadcasterDataSensor,i);

        i = new IntentFilter(BroadcasterDataSensor.DATA_UNKNOWN_ACT);
        registerReceiver(broadcasterDataSensor,i);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcasterDataSensor);
    }

    @Override
    protected void onStop()
    {
        super.onStop();
        mHandler.removeCallbacks(adquisitionData);
    }
    //###################################################################
    /****************** metodos implements Android. *********************/
    //###################################################################
    //here events or action from usar are taken.
    @Override
    public void onClick(View v)
    {
        Intent intentActivity;
        switch (v.getId())
        {
            case R.id.bt_enviarInforme:
                //falta enviar datos.
                intentActivity = new Intent(this, ResumenActivity.class);
                startActivityForResult(intentActivity, 0);
                break;
            case R.id.bt_connectServer:
                Button bt_conexion = (Button)v;
                if(bt_conexion.getText().equals("Conectar"))
                {
                    String deviceAddress = beanBluetoothDevice.getBluetoothDevice().getAddress();
                    mHandlerBLE.connect(this, mHandlerBLE.getMyCallBack(), deviceAddress);

                    Toast.makeText(this,"Conectando con el dispositivo.",Toast.LENGTH_LONG).show();

                    bt_conexion.setText("Desconectar");

                    //automatically adquisition data after DELAY_ADQUISITION seconds
                    mHandler.postDelayed(adquisitionData,DELAY_ADQUISITION * 1000);
                }
                else
                {
                    mHandlerBLE.disconnect();
                    mHandler.removeCallbacks(adquisitionData);
                    Toast.makeText(this,"Desconectando con el dispositivo.",Toast.LENGTH_LONG).show();
                    bt_conexion.setText("Conectar");
                }

                break;
            default:
                Log.i(Constant.TAG,"Error not ID button found it.");
                break;
        }
    }
    //###################################################################
    /** method to activity **/
    //###################################################################
    private void updateFlowmeter(byte [] value)
    {
        int [] data = ServiceData.workFlometerData(value);
        if(Constant.DEBUG)
            Log.i(Constant.TAG,"DeviceActivity -- updateFlowmeter -- updating value = "+ new String(String.valueOf(data)));
        //read data from sensor.
        //adapter
        flowmeter.setText(new String(String.valueOf(data)));
    }

    private void updatePressure(byte [] value)
    {
        int [] data = ServiceData.workPressureData(value);
        if(Constant.DEBUG)
            Log.i(Constant.TAG,"DeviceActivity -- updatePressure -- updating value = "+ new String(String.valueOf(data)));
        //read data from sensor.
        //adapter
        pressure.setText(new String(String.valueOf(data)));
    }

    private void updateValve(byte [] value)
    {
        boolean flag = ServiceData.workValveData(value);
        if(Constant.DEBUG)
            Log.i(Constant.TAG,"DeviceActivity -- updateValve -- updating value = "+ flag);

        valve.setText(flag ? "On" : "Off");

    }

    //Handle automatic
    private Runnable adquisitionData = new Runnable() {
        @Override
        public void run() {

            if (Constant.DEBUG)
                Log.i(Constant.TAG, "Stop scanning");

            mHandler.postDelayed(adquisitionData,DELAY_ADQUISITION * 1000);
        }
    };

    /**
     * Carga el adaptador con las metas obtenidas
     * en la respuesta
     */
    public void loadFromWebService(int id)
    {
        final String newURL = WebServiceConstant.GET_ARQUETA_BYID + "?idArqueta = "+id ;
        jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                newURL,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject jsonObject) {
                        // Procesar la respuesta Json
                        procesarRespuesta(jsonObject);
                        Log.d(Constant.TAG, "Procesando respuesta");
                        Log.d(Constant.TAG, "URL : "+newURL);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Log.d(Constant.TAG, "Error Volley: " + volleyError.getMessage());
                    }
                });

        // Petición GET
        VolleySingleton.getInstace(this).addToRequestQueue(jsonObjectRequest);
    }

    /**
     * Interpreta los resultados de la respuesta y así
     * realizar las operaciones correspondientes
     *
     * @param response Objeto Json con la respuesta
     */
    private void procesarRespuesta(JSONObject response) {
        try {
            // Obtener atributo "estado"
            String estado = response.getString("estado");

            switch (estado) {
                case "1": // EXITO
                    Log.d(Constant.TAG, "respuesta exitosa.");
                    // Obtener array "metas" Json
                    JSONObject mensaje = response.getJSONObject("arqueta");
                    // Parsear con Gson
                    Log.d(Constant.TAG, "ParsearGson mensaje: "+mensaje.toString());
                    gson = new Gson();
                    BeanArqueta arqueta = gson.fromJson(mensaje.toString(), BeanArqueta.class);
                    Log.d(Constant.TAG, "arqueta: "+arqueta.toString());
                    //presentar en activity
                    updateDatosActivity(arqueta);
                    break;
                case "2": // FALLIDO
                    Log.d(Constant.TAG, "respuesta fallida.");
                    String mensaje2 = response.getString("mensaje");
                    Toast.makeText(
                            this,
                            mensaje2,
                            Toast.LENGTH_LONG).show();
                    break;
                default:
                    Log.d(Constant.TAG, "respuesta error estado:"+estado);
                    break;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void updateDatosActivity(BeanArqueta arquetas)
    {

        /*
        tvId.setText(arquetas.getId());
        tvFechaAlta.setText(arquetas.getInsert_time());
        tvNombre.setText(arquetas.getNombre_arqueta());
        tvDireccion.setText(arquetas.getDireccion_arqueta());
        tvUuid1.setText(arquetas.getUuid_sensor1());
        tvUuid2.setText(arquetas.getUuid_sensor2());
        tvUuid3.setText(arquetas.getUuid_sensor3());*/

    }

    //###################################################################
    /** Class to update value of several sensor in UI **/
    //###################################################################
    public class BroadcasterDataSensor extends BroadcastReceiver
    {
        public static final String DATA_FLOW_ACT     = "DATA_FLOW_ACT";
        public static final String DATA_PRESSU_ACT   = "DATA_PRESSU_ACT";
        public static final String DATA_VALVE_ACT    = "DATA_VALVE_ACT";
        public static final String DATA_UNKNOWN_ACT  = "DATA_UNKNOWN_ACT";

        @Override
        public void onReceive(Context context, Intent intent)
        {
            String action = intent.getAction();
            byte[] value;

            if(Constant.DEBUG)
                Log.i(Constant.TAG,"BroadcasterDataSensor -- onReceive -- action :" +action);

            if(action.equals(DATA_FLOW_ACT))
            {
                value  = intent.getByteArrayExtra(Constant.MSG_FLOW);
                updateFlowmeter(value);
            }
            else if(action.equals(DATA_PRESSU_ACT))
            {
                value  = intent.getByteArrayExtra(Constant.MSG_PRESSU);
                updatePressure(value);
            }
            else if(action.equals(DATA_VALVE_ACT))
            {
                value  = intent.getByteArrayExtra(Constant.MSG_VALVE);
                updateValve(value);
            }
            else //message unknown
            {
                if(Constant.DEBUG)
                    Log.i(Constant.TAG,"BroadcasterDataSensor -- Error msg unknown.");
            }
        }
    }
}
