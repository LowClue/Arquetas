package es.alarcon.arquetanatureble.CORE;

import android.app.Application;
import android.content.Context;

import es.alarcon.arquetanatureble.BLE.HandlerBLE;


/**
 * Created by alarcon on 4/7/15.
 */
public class BLE_Application extends Application
{
    static HandlerBLE mHandlerBLE;

    public void resetHandlerBLE()
    {
        HandlerBLE.resetHandlerBLE();
    }

    public HandlerBLE getmHandlerBLEInstance(Context context)
    {
        return mHandlerBLE = HandlerBLE.getInstance(context);
    }

    public static HandlerBLE getmHandlerBLE()
    {
        return mHandlerBLE;
    }

}
