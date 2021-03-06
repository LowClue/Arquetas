package es.alarcon.arquetanatureble.BLE;

import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by alarcon on 16/9/15.
 */
public class ServiceType {
    private BluetoothGattService mService;
    private List<BluetoothGattCharacteristic> mCharacteristics;

    ServiceType(BluetoothGattService service)
    {
        mService = service;
        mCharacteristics= new ArrayList<BluetoothGattCharacteristic>();
    }

    public BluetoothGattService getService() {return mService;}
    public List<BluetoothGattCharacteristic> getCharacteristics () {return mCharacteristics;}
    public void setCharacteristics(List<BluetoothGattCharacteristic> characteristicList)
    {
        mCharacteristics = characteristicList;
    }
}