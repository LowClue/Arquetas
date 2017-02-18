package es.alarcon.arquetanatureble.GUI;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import es.alarcon.arquetanatureble.BEAN.BeanBluetoothDevice;
import es.alarcon.arquetanatureble.R;


/**
 * Created by alarcon on 27/7/15.
 */
/*
    Clase para crear el adaptador de dispositos Bluetooh
     */
public class ScanArrayAdapter extends ArrayAdapter {
    private final Context mContext;
    private static ScanActivity mScanActivity;
    private List<BeanBluetoothDevice> listBeanBluetoothDevices;

    public ScanArrayAdapter(Context context, List<BeanBluetoothDevice> deviceList)
    {
        super(context, R.layout.activity_scan_item, deviceList);
        this.mContext = context;
        listBeanBluetoothDevices = deviceList;
    }

    public ScanArrayAdapter(Activity activity, List<BeanBluetoothDevice> deviceList)
    {
        super(activity.getApplicationContext(), R.layout.activity_scan_item, deviceList);
        mScanActivity = (ScanActivity)activity;
        mContext = mScanActivity.getApplicationContext();
        listBeanBluetoothDevices = deviceList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = mScanActivity.getLayoutInflater();
        View item = inflater.inflate(R.layout.activity_scan_item, null);

        TextView nameDevice         = (TextView) item.findViewById(R.id.deviceName);
        TextView addressDevice      = (TextView) item.findViewById(R.id.deviceAddress);
        TextView rssilevelTV        = (TextView) item.findViewById(R.id.rssiLevel);
        ImageView imageView         = (ImageView) item.findViewById(R.id.chamberPicture);

        final BluetoothDevice bluetoothDevice = listBeanBluetoothDevices.get(position).getBluetoothDevice();

        nameDevice.setText(bluetoothDevice.getName());
        addressDevice.setText(bluetoothDevice.getAddress());
        int rssi = listBeanBluetoothDevices.get(position).getmRssi();
        rssilevelTV.setText(String.valueOf(rssi));

        imageView.setImageResource(R.drawable.chamber_device);

        return item;
    }

    public void addElement(BeanBluetoothDevice beanBluetoothDevice)
    {
        this.add(beanBluetoothDevice);
        this.notifyDataSetChanged();
    }

    public BeanBluetoothDevice findElement(String name, String address)
    {
        for (BeanBluetoothDevice beanBluetoothDevice : listBeanBluetoothDevices)
        {
                if(beanBluetoothDevice.getBluetoothDevice().getName().equals(name)
                        && beanBluetoothDevice.getBluetoothDevice().getAddress().equals(address))
                { return beanBluetoothDevice;}
        }

        return null;
    }

    public void deleteElement(BeanBluetoothDevice beanBluetoothDevice)
    {
        listBeanBluetoothDevices.remove(beanBluetoothDevice);
    }
}
