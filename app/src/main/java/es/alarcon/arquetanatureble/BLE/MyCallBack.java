package es.alarcon.arquetanatureble.BLE;

import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCallback;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattDescriptor;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothProfile;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.util.List;
import java.util.UUID;

import es.alarcon.arquetanatureble.GUI.DeviceActivity;
import es.alarcon.arquetanatureble.UTIL.Constant;

/**
 * Created by alarcon on 16/9/15.
 */
public class MyCallBack extends BluetoothGattCallback {


    private HandlerBLE mHandlerBLE;
    private Context mContext;
    // State machine
    private int mState;
    private boolean nextSensor;

    public MyCallBack(Context mContext, HandlerBLE mHandlerBLE)
    {
        this.mContext    = mContext;
        this.mHandlerBLE = mHandlerBLE;
        mState           = 0;
        nextSensor       = true;
    }

    protected void resetStateMachine()
    {
        mState           = 0;
        nextSensor       = true;
    }

    protected void sendBroadCast(Context context,String action,String parameter,byte [] value)
    {
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(action);
        broadcastIntent.putExtra(parameter, value);
        context.sendBroadcast(broadcastIntent);
    }

    protected void advanceStateMachine()
    {
        mState++;
    }

    protected void readDataFromSensor(BluetoothGatt gatt)
    {

        BluetoothGattCharacteristic characteristic;
        //BluetoothGattService service = new BluetoothGattService(ServiceData.CHAMBER_SERVICE
        //        ,BluetoothGattService.SERVICE_TYPE_PRIMARY);

        BluetoothGattService service = mHandlerBLE.getService(ServiceData.CHAMBER_SERVICE).getService();

        switch (mState)
        {
            case 0:
                characteristic = service.getCharacteristic(ServiceData.FLOWMETER_CHARACTERISTIC);
                break;
            case 1:
                characteristic = service.getCharacteristic(ServiceData.PRESSURE_CHARACTERISTIC);
                break;
            case 2:
                characteristic = service.getCharacteristic(ServiceData.VALVE_CHARACTERISTIC);
                nextSensor     = false;
                break;
            default:
                return;
        }
        gatt.readCharacteristic(characteristic);
    }
    @Override
    public void onConnectionStateChange(BluetoothGatt gatt, int status, int newState) {
        super.onConnectionStateChange(gatt, status, newState);

        if(Constant.DEBUG){
            if (status!=0) {
                Log.i(Constant.TAG, "MyCallBack -- Error status received onConnectionStateChange: " + status + " - Newstate: " + newState);
            } else {
                Log.i(Constant.TAG, "MyCallBack -- onConnectionStateChange received. status = " + status +
                        " - NewState: " + newState);
            }
        }
        if(status != BluetoothGatt.GATT_SUCCESS)
        {
            gatt.disconnect();
            return;
        }

        //Connection established
        if (newState == BluetoothProfile.STATE_CONNECTED) {

            if(Constant.DEBUG)
                Log.i(Constant.TAG, "MyCallBack -- onConnectionStateChange -- discovering services.");

            mHandlerBLE.setGatt(gatt);
            //Discover services
            //gatt.discoverServices();
            mHandlerBLE.discoverServices();

        } else if (newState == BluetoothProfile.STATE_DISCONNECTED)
        {
            //Handle a disconnect event
            Log.d(Constant.TAG,"MyCallBack -- onConnectionStateChange -- device disconnecting.");
        }

    }


    @Override
    public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {
        super.onReadRemoteRssi(gatt, rssi, status);
    }


    @Override
    public void onCharacteristicWrite(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicWrite(gatt, characteristic, status);
    }


    @Override
    public void onCharacteristicRead(BluetoothGatt gatt, BluetoothGattCharacteristic characteristic, int status) {
        super.onCharacteristicRead(gatt, characteristic, status);

        String uuid_charac = characteristic.getUuid().toString();
        byte[] value = characteristic.getValue();

        if(Constant.DEBUG)
            Log.i(Constant.TAG, "MyCallBack -- onCharacteristicRead: " + uuid_charac);

        if(uuid_charac.equals(ServiceData.FLOWMETER_CHARACTERISTIC.toString()))
        {
            //to send the information UI.
            sendBroadCast(mContext, DeviceActivity.BroadcasterDataSensor.DATA_FLOW_ACT
                    ,Constant.MSG_FLOW,value);
        }
        else if(uuid_charac.equals(ServiceData.PRESSURE_CHARACTERISTIC.toString()))
        {
            sendBroadCast(mContext,DeviceActivity.BroadcasterDataSensor.DATA_PRESSU_ACT
                    ,Constant.MSG_PRESSU,value);
        }
        else if (uuid_charac.equals(ServiceData.VALVE_CHARACTERISTIC.toString()))
        {
            sendBroadCast(mContext,DeviceActivity.BroadcasterDataSensor.DATA_VALVE_ACT
                    ,Constant.MSG_VALVE,value);
        }
        else
        {
            sendBroadCast(mContext,DeviceActivity.BroadcasterDataSensor.DATA_UNKNOWN_ACT
                    ,Constant.MSG_UNKNOWN,value);
        }

        if(nextSensor)
        {
            advanceStateMachine();
            readDataFromSensor(mHandlerBLE.getGatt());
        }
    }


    @Override
    public void onCharacteristicChanged(BluetoothGatt gatt,BluetoothGattCharacteristic characteristic) {
        super.onCharacteristicChanged(gatt, characteristic);

        String uuid_charac = characteristic.getUuid().toString();
        byte[] value = characteristic.getValue();

        if(Constant.DEBUG)
            Log.i(Constant.TAG, "MyCallBack -- onCharacteristicChanged: " + uuid_charac);

        if(uuid_charac.equals(ServiceData.FLOWMETER_CHARACTERISTIC.toString()))
        {
            //to send the information UI.
            sendBroadCast(mContext, DeviceActivity.BroadcasterDataSensor.DATA_FLOW_ACT
                    ,Constant.MSG_FLOW,value);
        }
        else if(uuid_charac.equals(ServiceData.PRESSURE_CHARACTERISTIC.toString()))
        {
            sendBroadCast(mContext,DeviceActivity.BroadcasterDataSensor.DATA_PRESSU_ACT
                    ,Constant.MSG_PRESSU,value);
        }
        else if (uuid_charac.equals(ServiceData.VALVE_CHARACTERISTIC.toString()))
        {
            sendBroadCast(mContext,DeviceActivity.BroadcasterDataSensor.DATA_VALVE_ACT
                    ,Constant.MSG_VALVE,value);
        }
    }


    @Override
    public void  onServicesDiscovered(BluetoothGatt gatt, int status) {
        super.onServicesDiscovered(gatt, status);

        //adding new services
        if(Constant.DEBUG)
            Log.i(Constant.TAG, "MyCallBack -- onServicesDiscovered status: " + status);

        String serviceUUID;
        ServiceType serviceType;
        List<BluetoothGattCharacteristic> characteristics;
        for(BluetoothGattService serviceInList: gatt.getServices())
        {
            serviceUUID = serviceInList.getUuid().toString();
            serviceType = new ServiceType(serviceInList);
            characteristics = serviceType.getCharacteristics();

            if(Constant.DEBUG)
                Log.i(Constant.TAG, "MyCallBack -- onServicesDiscovered -- New service: " + serviceUUID);

            for(BluetoothGattCharacteristic characteristicInList : serviceInList.getCharacteristics())
            {
                if(Constant.DEBUG)
                    Log.i(Constant.TAG, "MyCallBack -- onServicesDiscovered -- New characteristic: " + characteristicInList.getUuid().toString());
                characteristics.add(characteristicInList);
            }

            serviceType.setCharacteristics(characteristics);
            mHandlerBLE.addService(serviceType);

            characteristics.clear();
        }

        ///////////////////////////////////
        //ServiceType serviceType1 =  mHandlerBLE.getService(ServiceData.CHAMBER_SERVICE);
        //for(BluetoothGattCharacteristic characteristicInList : serviceType1.getService().getCharacteristics())
        //{
        //    if(Constant.DEBUG)
        //        Log.i(Constant.TAG, "MyCallBack -- test -- service characteristic: " + characteristicInList.getUuid().toString());
        //
        //}
        ///////////////////////////////////

        resetStateMachine();
        readDataFromSensor(gatt);
    }


    @Override
    public void onDescriptorWrite(BluetoothGatt gatt, BluetoothGattDescriptor descriptor, int status) {
        super.onDescriptorWrite(gatt, descriptor, status);
    }

}
