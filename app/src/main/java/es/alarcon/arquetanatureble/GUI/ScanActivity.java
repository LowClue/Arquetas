package es.alarcon.arquetanatureble.GUI;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import android.os.Handler;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import es.alarcon.arquetanatureble.BEAN.BeanBluetoothDevice;
import es.alarcon.arquetanatureble.BEAN.BeanInformesDB;
import es.alarcon.arquetanatureble.BLE.BLEBroadcastReceiver;
import es.alarcon.arquetanatureble.BLE.HandlerBLE;

import es.alarcon.arquetanatureble.CORE.BLE_Application;
import es.alarcon.arquetanatureble.CORE.MyNotificationHandler;
import es.alarcon.arquetanatureble.CORE.ServiceDetectionTag;
import es.alarcon.arquetanatureble.DB_SQLITE.InformeSQLiteDataSource;
import es.alarcon.arquetanatureble.R;
import es.alarcon.arquetanatureble.UTIL.Constant;
import es.alarcon.arquetanatureble.WEBSERVICE.BeanInforme;
import es.alarcon.arquetanatureble.WEBSERVICE.VolleySingleton;
import es.alarcon.arquetanatureble.WEBSERVICE.WebServiceConstant;


public class ScanActivity extends Activity implements OnItemClickListener { //}, LeScanCallback {

    private static final int SCAN_ITEM = 1;
    private static final int UPDATE_INFO_INTO_CLOUD = 2;
    private static ListView mListView;
    private static Context mContext;
    private static HandlerBLE mHandlerBLE;
    private Handler mHandler;
    private static ScanArrayAdapter mAdapter;
    private static List<BeanBluetoothDevice> mDeviceList;
    private Menu mMenu;
    private static Activity mActivity;
    private static InformeSQLiteDataSource informesSQLite;
    private BLEBroadcastReceiver mScanBroadcastReceiver;
    public static boolean isOncreate = false;

    //###################################################################
    /****************** metodos del flujo Android. **********************/
    //###################################################################
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan);

        isOncreate = true;

        mActivity   = this;
        mContext    = this;
        mHandler    = new Handler() ;
        mDeviceList = new ArrayList<BeanBluetoothDevice>();
        mAdapter    = new ScanArrayAdapter(this, mDeviceList);

        //manejador BLE
        mHandlerBLE = ((BLE_Application) getApplication()).getmHandlerBLEInstance(this);
        ((BLE_Application) getApplication()).resetHandlerBLE();

        mScanBroadcastReceiver = new BLEBroadcastReceiver(this, mAdapter);

        /*IntentFilter i = new IntentFilter(HandlerBLE.ACTION_BLE_FOUND);
        registerReceiver(mScanBroadcastReceiver, i);*/

        //mListView = getListView();
        mListView = (ListView) findViewById(R.id.listView);
        mListView.setVisibility(View.VISIBLE);

        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(this);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        mMenu = menu;

        String menuTitle = getResources().getString(R.string.scan);
        menu.add(0,SCAN_ITEM,0,menuTitle);

        menuTitle = getResources().getString(R.string.sendInfo2Cloud);
        menu.add(0,UPDATE_INFO_INTO_CLOUD,0,menuTitle);

       /*
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scan, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()){
            case SCAN_ITEM:
                scan();
                Toast.makeText(this, "Escaneando arquetas.", Toast.LENGTH_LONG).show();
                break;
            case UPDATE_INFO_INTO_CLOUD:
                //updateInfoCloud();
                break;
        }
        return true;
        /*
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);*/
    }


    @Override
    protected void onResume()
    {
        super.onResume();

        //restart broadcaster
        mScanBroadcastReceiver = new BLEBroadcastReceiver(this, mAdapter);
        IntentFilter i = new IntentFilter(HandlerBLE.ACTION_BLE_FOUND);
        registerReceiver(mScanBroadcastReceiver,i);

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE))
        {
            Toast.makeText(this, "Tecnología BLE no está soportado por este dispositivo.", Toast.LENGTH_SHORT).show();
            finish();
        }

        //checking if bluetooth is enable.
        if(!mHandlerBLE.IsEnabledBlue())
        {
            Intent enableBlue = new Intent(mHandlerBLE.getBlueAdapter().ACTION_REQUEST_ENABLE);
            startActivity(enableBlue);
            finish();
            return;
        }

        Log.d(Constant.TAG,"#>> onResume : Stopping service.");
        //stop service
        Intent service = new Intent(this, ServiceDetectionTag.class);
        stopService(service);

        mAdapter.clear();
        HandlerBLE.setup();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onPause() {
        super.onPause();

        //Make sure that there is no pending Callback
        mHandler.removeCallbacks(mStopRunnable);

        if(mScanBroadcastReceiver != null)
            unregisterReceiver(mScanBroadcastReceiver);

        mAdapter.clear();
        mAdapter.notifyDataSetChanged();

        isOncreate = false;

        if (mHandlerBLE.isScanning) {
            mHandlerBLE.stopLeScan();
        }
    }

    @Override
    protected void onDestroy() {

        //stop service
        Intent service = new Intent(this, ServiceDetectionTag.class);
        stopService(service);

        super.onDestroy();
    }
    @Override
    protected void onStop()
    {
        super.onStop();

        Log.d(Constant.TAG, "#>> onStop : Running service.");

        //Run service
        Intent service = new Intent(this, ServiceDetectionTag.class);
        startService(service);

    }

    //###################################################################
    /****************** metodos manejo Tag        **********************/
    //###################################################################
    @Override
    //recogemos los metodos del tag seleccionado y recogemos los datos.
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        //stop service
        Intent service = new Intent(this, ServiceDetectionTag.class);
        stopService(service);

        if (Constant.DEBUG)
            Log.i(Constant.TAG, "Selected device " + mDeviceList.get(position).getBluetoothDevice().getAddress());

        if (mHandlerBLE.isScanning)
        { //stop scanning
            configureScan(false);
            mHandlerBLE.stopLeScan();
            if (Constant.DEBUG)
                Log.i(Constant.TAG, "Stop scanning");
        }

        String address  = mDeviceList.get(position).getBluetoothDevice().getAddress();
        String name     = mDeviceList.get(position).getBluetoothDevice().getName();

        if (name==null)
            name="unknown";

        BeanBluetoothDevice beanBlue = (BeanBluetoothDevice) mAdapter.getItem(position);

        Intent intentActivity= new Intent(this, OutsideChamberActivity.class);
        intentActivity.putExtra(Constant.EXTRA_BEAN_BLUETOOTHDEVICE,beanBlue);
        //intentActivity.putExtra(Constant.EXTRA_ADDRESS, address);
        //intentActivity.putExtra(Constant.EXTRA_NAME, name);
        this.startActivity(intentActivity);

    }

    //Handle automatic stop of LEScan
    private Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            configureScan(false);
            if (Constant.DEBUG)
                Log.i(Constant.TAG, "Stop scanning");

        }
    };

    public void configureScan(boolean flag)
    {
        String itemText = null;

        if (!flag)
        {
            itemText = getResources().getString(R.string.stopScan);
            mHandlerBLE.stopLeScan();

            if (Constant.DEBUG)
                Log.i(Constant.TAG, "ScanActivity -- StopScan");

        }
        else
        {
            itemText= getResources().getString(R.string.scan);
            mHandlerBLE.startLeScan();

            if (Constant.DEBUG)
                Log.i(Constant.TAG, "ScanActivity -- StartScan");
        }

        mMenu.findItem(SCAN_ITEM).setTitle(itemText);

        if (Constant.DEBUG)
            Log.i(Constant.TAG, "Updated Menu Item. New value: " + itemText);
    }

    // Metodo para iniciar el scaneo cuando te llaman manualmente.
    private void scan() {
        Boolean flag = mMenu.findItem(SCAN_ITEM).getTitle().equals(R.string.stopScan)?true:false;
        if (flag) { //stop scanning

            configureScan(false);

            if (Constant.DEBUG)
                Log.i(Constant.TAG, "Stop scanning");

        } else {
            mAdapter.clear();
            mAdapter.notifyDataSetChanged();
            configureScan(true);

            if (Constant.DEBUG)
                Log.i(Constant.TAG, "Start scanning for BLE devices...");

            //automatically stop LE scan after 5 seconds
            mHandler.postDelayed(mStopRunnable, 30000);
        }
    }

    private void updateInfoCloud()
    {

        try
        {
            informesSQLite = new InformeSQLiteDataSource(getApplicationContext());
        }
        catch(Exception e )
        {
            Toast.makeText(this, "Error enviando informes."+ e.getMessage()+e.getStackTrace(), Toast.LENGTH_LONG).show();
        }

        List<BeanInformesDB> informes2Upload = informesSQLite.getAllInformes();
        if(informes2Upload.isEmpty())
        {
            Toast.makeText(this, "No hay ningún informe para enviar.", Toast.LENGTH_LONG).show();
            return;
        }

        String newURL;

        for (final BeanInformesDB bean:informes2Upload )
        {
            newURL = "";
            newURL = WebServiceConstant.INSERT
                    +"?acceso_ubicacion="+ bean.getAccesoUbicacion()
                    +"&perimetro_arqueta="+ bean.getPerimetroArqueta()
                    +"&puerta_acceso="+ bean.getPuertaAcceso()
                    +"&cubierta="+ bean.getCubierta()
                    +"&param_verticales_int="+ bean.getParVertInt()
                    +"&param_verticales_ext="+ bean.getParVertExt()
                    +"&ventilacion_lateral="+  bean.getVentilacionLateral()
                    +"&ventilacion_superior="+ bean.getVentilacionSuperior()
                    +"&pates_escalera="+ bean.getPatesEscalera()
                    +"&distancia_reglamentaria_elementos="+bean.getDistanciaRegElementos()
                    +"&ventosas="+ bean.getVentosas()
                    +"&juntas_carretes="+ bean.getJuntasUnion()
                    +"&manometros="+ bean.getManometros()
                    +"&contadores="+ bean.getContadores()
                    +"&fecha="+ bean.getFecha()
                    +"&direccion_arqueta="+bean.getDireccion_arqueta()
                    +"&comentario="+ bean.getComentario()
                    +"&foto="+ bean.getFoto();

            // Actualizar datos en el servidor
            VolleySingleton.getInstace(this).addToRequestQueue(
                    new JsonObjectRequest(
                            Request.Method.GET,
                            newURL,
                            new Response.Listener<JSONObject>() {
                                @Override
                                public void onResponse(JSONObject response)
                                {
                                    // Procesar la respuesta del servidor
                                    try {
                                        // Obtener atributo "estado"
                                        String estado = response.getString("estado");

                                        switch (estado)
                                        {
                                            case "1": // EXITO
                                                Log.d(Constant.TAG, "respuesta exitosa.");
                                                informesSQLite.deleteInforme(bean.getIDDataBase());
                                                Toast.makeText(mContext, "Insercion correcta", Toast.LENGTH_LONG).show();
                                                break;
                                            case "2": // FALLIDO
                                                Log.d(Constant.TAG, "Error al enviar los datos.");
                                                String mensaje2 = response.getString("mensaje");
                                                Toast.makeText(mContext, mensaje2, Toast.LENGTH_LONG).show();
                                                break;
                                            default:
                                                Log.d(Constant.TAG, "respuesta error estado:"+estado);
                                                break;
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    Log.d(Constant.TAG, "Error Volley (response insert): " + error.getMessage());
                                    Toast.makeText(mContext, "No se comunicó con el Cloud intentelo más tarde.", Toast.LENGTH_LONG).show();
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
    public void addNewDevice2MySimpleArrayAdapter(final BeanBluetoothDevice beanDeviceFound) {

        final BluetoothDevice deviceFound = beanDeviceFound.getBluetoothDevice();
        this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run()
                    {
                        mAdapter.addElement(beanDeviceFound);

                        if (Constant.DEBUG)
                            Log.i(Constant.TAG, "ScanActivity -- addNewDevice2MySimpleArrayAdapter() -> Added new device "
                                    + deviceFound.getAddress() + " --- Name: " + deviceFound.getName());
                    }
                });
            }
        });
    }
}
