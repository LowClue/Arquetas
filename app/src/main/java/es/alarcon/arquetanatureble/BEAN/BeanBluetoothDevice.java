package es.alarcon.arquetanatureble.BEAN;

import android.bluetooth.BluetoothDevice;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import es.alarcon.arquetanatureble.UTIL.Constant;

/**
 * Created by alarcon on 23/7/15.
 */
//serializable class object to move between 2 Bluetooth activities ..
public class BeanBluetoothDevice implements Parcelable
{
    private BluetoothDevice mdevice;
    private int mRssi;
    private byte[] mScanRecord;

    public BeanBluetoothDevice() {
        super();
    }

    //###################################################################

    protected BeanBluetoothDevice(Parcel in) {
        mdevice     = in.readParcelable(BluetoothDevice.class.getClassLoader());
        mRssi       = in.readInt();
        mScanRecord = in.createByteArray();
    }

    public static final Creator<BeanBluetoothDevice> CREATOR = new Creator<BeanBluetoothDevice>() {
        @Override
        public BeanBluetoothDevice createFromParcel(Parcel in) {
            return new BeanBluetoothDevice(in);
        }

        @Override
        public BeanBluetoothDevice[] newArray(int size) {
            return new BeanBluetoothDevice[size];
        }
    };

    //###################################################################
    /****************** gets and sets methods     **********************/
    //###################################################################

    public void setBluetoothDevice(BluetoothDevice device)
    {mdevice = device;}

    public BluetoothDevice getBluetoothDevice()
    {return mdevice;}

    public int getmRssi() {
        return mRssi;
    }

    public void setmRssi(int mRssi) {
        this.mRssi = mRssi;
    }

    public byte[] getmScanRecord() {
        return mScanRecord;
    }

    public void setmScanRecord(byte[] mScanRecord) {
        this.mScanRecord = mScanRecord;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(mdevice, flags);
        dest.writeInt(mRssi);
        dest.writeByteArray(mScanRecord);
    }

}