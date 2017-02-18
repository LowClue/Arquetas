package es.alarcon.arquetanatureble.BLE;

import android.app.Activity;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import es.alarcon.arquetanatureble.BEAN.BeanBluetoothDevice;
import es.alarcon.arquetanatureble.BLE.HandlerBLE;
import es.alarcon.arquetanatureble.CORE.ServiceDetectionTag;
import es.alarcon.arquetanatureble.GUI.ScanArrayAdapter;
import es.alarcon.arquetanatureble.UTIL.Constant;

/**
 * Created by alarcon on 26/7/15.
 */
public class BLEBroadcastReceiver extends BroadcastReceiver
{
    private static Activity mActivity;
    private static ScanArrayAdapter mAdapter;
    private static Context mContext;

    public BLEBroadcastReceiver(Activity activity, ScanArrayAdapter adapter)
    {
        super();
        mAdapter  = adapter;
        mActivity = activity;
        mContext  = activity.getApplicationContext();
    }

    public BLEBroadcastReceiver(Context context)
    {
        super();
        mContext = context;
    }

    public BLEBroadcastReceiver()
    {
        super();
    }

    @Override
    public void onReceive(Context context, Intent intent)
    {
        if(Constant.DEBUG)
            Log.i(Constant.TAG, "BLEBroadcastReceiver(Listener) -- OnReceive() -> BroadcastReceiver new device found.");

        String action = intent.getAction();
        final BeanBluetoothDevice beanDeviceFound = intent.getExtras().getParcelable(Constant.EXTRA_BEAN_BLUETOOTHDEVICE);
        //byte[] scanRecord = beanDeviceFound.getmScanRecord();
        //get signal and add new device into MyarrayAdapter

        //if(action.equals(HandlerBLE.ACTION_BLE_FOUND))
        try
        {

            //mActivity = ScanActivity.GetScanActivity();
            //((ScanActivity)mActivity).addNewDevice2MySimpleArrayAdapter(beanDeviceFound);

            final BluetoothDevice deviceFound = beanDeviceFound.getBluetoothDevice();


            /*if(Constant.DEBUG)
                Log.i(Constant.TAG, "BLEBroadcastReceiver -- onReceive -> ScanRecord value: "
                        +byteArray2Hexadecimal(scanRecord));*/
            //((ScanActivity)mActivity).addNewDevice2MySimpleArrayAdapter(beanDeviceFound);

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {

                    BeanBluetoothDevice oldDevice;
                    oldDevice = mAdapter.findElement(beanDeviceFound.getBluetoothDevice().getName()
                            , beanDeviceFound.getBluetoothDevice().getAddress());

                    if (oldDevice != null) {
                        //delete old one and inset new one.
                        mAdapter.deleteElement(oldDevice);
                    }

                    mAdapter.addElement(beanDeviceFound);

                    if (Constant.DEBUG)
                        Log.i(Constant.TAG, "mActivity.runOnUiThreadr -- runnable() -> Added new device "
                                + beanDeviceFound.getBluetoothDevice().getAddress()
                                + " --- Name: " + beanDeviceFound.getBluetoothDevice().getName());
                }
            });
        } catch (Exception e) {
            Log.i(Constant.TAG, "[Error(BLEBroadcastReceiver)]: \n Message: " + e.getMessage() + "\n Cause:" + e.getCause() + "\n StackTrace" + e.getStackTrace() + "\n" + e.getLocalizedMessage());
        }


    }
}