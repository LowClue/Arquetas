package es.alarcon.arquetanatureble.BLE;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothAdapter.LeScanCallback;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import es.alarcon.arquetanatureble.BEAN.BeanBluetoothDevice;
import es.alarcon.arquetanatureble.CORE.ServiceDetectionTag;
import es.alarcon.arquetanatureble.UTIL.Constant;


/**
 * Created by alarcon on 4/7/15.
 */

public class HandlerBLE implements LeScanCallback
{
    public static final String ACTION_BLE_FOUND            = "es.alarcon.arquetanatureble.BLE_FOUND";

    private static HandlerBLE mHandlerBLE;
    private static List<ServiceType> mServices;
    private static Context mContext;
    private static MyCallBack mCallBack;
    private static BluetoothDevice mDevice;
    private static String mDeviceAddress;
    private static BluetoothGatt mGatt;
    private static BluetoothAdapter mBlueAdapter = null;

    public static boolean isScanning = false;

    //###################################################################
    /****************** Constructor                **********************/
    //###################################################################
    public HandlerBLE(Context context)
    {
        mContext        = context;
        mDeviceAddress  = null;
        mServices       = new ArrayList<ServiceType>();
        mCallBack       = new MyCallBack(context, this);
    }

    //###################################################################
    /****************** Getters & setters          **********************/
    //###################################################################

    public MyCallBack getMyCallBack() {return mCallBack;}
    public List<ServiceType> getServices()
    {
        return mServices;
    }

    public ServiceType getService(UUID uuid)
    {
        for (ServiceType service:mServices)
        {
            if(service.getService().getUuid().toString().equals(uuid.toString()))
                return service;
        }
        return null;
    }
    public void addService(ServiceType serviceType)
    {
        mServices.add(serviceType);
    }
    public void setGatt(BluetoothGatt gatt){mGatt = gatt;}
    public BluetoothGatt getGatt(){return mGatt;}
    //###################################################################
    /*********************  statics methods   **************************/
    //###################################################################
    public static HandlerBLE getInstance(Context context) {
        if(mHandlerBLE==null){
            mHandlerBLE = new HandlerBLE(context);
            setup();
        }
        return mHandlerBLE;
    }

    public static void  resetHandlerBLE()
    {
        mDeviceAddress     = null;
        mGatt              = null;
        if (mServices!=null) mServices.clear();
        disconnect();
    }

    public static void setup()
    {
        BluetoothManager manager = (BluetoothManager) mContext.getSystemService(Context.BLUETOOTH_SERVICE);
        mBlueAdapter = manager.getAdapter();
    }

    //###################################################################
    /********************* methods bluetooth                *************/
    //###################################################################

    public boolean IsEnabledBlue()
    {
        if(mBlueAdapter != null)
            return mBlueAdapter.isEnabled();

        return false;
    }

    public BluetoothAdapter getBlueAdapter()
    {
        return mBlueAdapter;
    }

    public void startLeScan()
    {
        try
        {
            mBlueAdapter.startLeScan(this);//deprecated
            isScanning = true;
            //mBlueAdapter.startDiscovery();
        }
        catch (Exception e)
        {
            Log.i(Constant.TAG,"(HandlerBLE)[Error]:"+e.getStackTrace()+" "+e.getCause()+" "+e.getMessage()+
                    " "+e.getLocalizedMessage());
        }

    }

    public void stopLeScan()
    {
        try
        {
            mBlueAdapter.stopLeScan(this); //deprecated
            //mBlueAdapter.startDiscovery();
            isScanning = false;
        }
        catch(Exception e)
        {
            Log.i(Constant.TAG,"(HandlerBLE)[Error]:"+e.getStackTrace()+" "+e.getCause()+" "+e.getMessage()+
                    " "+e.getLocalizedMessage());
        }
    }

    //###################################################################
    /********************* methods GATT bluetooth          *************/
    //###################################################################
    public void discoverServices()
    {
        if (Constant.DEBUG)
            Log.i(Constant.TAG, "(HandlerBLE)Scanning services and caracteristics");
        mGatt.discoverServices();
    }

    public void connect()
    {
        mDevice   = mBlueAdapter.getRemoteDevice(mDeviceAddress);
        mServices.clear();
        if(mGatt!=null)
        {
            mGatt.connect();
        }else
        {
            mDevice.connectGatt(mContext, false, mCallBack);
        }
    }

    public void connect(Context context, MyCallBack callBack,String deviceAddress)
    {
        mDevice = mBlueAdapter.getRemoteDevice(deviceAddress);
        mDevice.connectGatt(context, false, callBack);
    }

    public static void disconnect()
    {
        if (mGatt!=null)
        {
            try
            {
                mGatt.disconnect();
                mGatt.close();
                if (Constant.DEBUG)
                    Log.i(Constant.TAG, "(HandlerBLE)Disconnecting GATT");
            } catch(Exception ex)
            {
                if (Constant.DEBUG)
                    Log.i(Constant.TAG, "(HandlerBLE) Error disconnecting :"+ex.getMessage());
            }
        }
        mGatt = null;
    }

    public boolean isConnected(){
        return (mGatt!=null);
    }

    public void getDataFromSensors()
    {
        if(mCallBack != null && mGatt != null)
        {
            mCallBack.resetStateMachine();
            mCallBack.readDataFromSensor(mGatt);
        }
    }

    //###################################################################
    /********************* methods Scan bluetooth          *************/
    //###################################################################
    /*
    * this method is used to receive devices which were found durind a scan*/
    @Override
    public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord)
    {

        if(Constant.DEBUG)
            Log.i(Constant.TAG,"(HandlerBLE) -- onLeScan -> throwing information to the listener.");

        BeanBluetoothDevice beanBlue = new BeanBluetoothDevice();
        beanBlue.setBluetoothDevice(device);
        beanBlue.setmRssi(rssi);
        beanBlue.setmScanRecord(scanRecord);

        //create the packet wich will be sent to listener.
        Intent intent = new Intent();
        intent.setAction(HandlerBLE.ACTION_BLE_FOUND);
        intent.putExtra(Constant.EXTRA_BEAN_BLUETOOTHDEVICE,beanBlue);
        mContext.sendBroadcast(intent);

        //listener in background
        Intent newIntent = new Intent();
        newIntent.setAction(ServiceDetectionTag.ServiceBroadcastReceiver.ACTION_NOTIFY_NEW_DEVICE_FOUND);
        newIntent.putExtra(Constant.EXTRA_BEAN_BLUETOOTHDEVICE,beanBlue);
        mContext.sendBroadcast(newIntent);
    }
}
