package es.alarcon.arquetanatureble.UTIL;

/**
 * Created by alarcon on 4/7/15.
 */
public final class Constant
{
    public static boolean DEBUG = true;
    public static String TAG    ="IoT-APP";

    //Data to move between activities.
    public static final String EXTRA_ADDRESS                = "address";
    public static final String EXTRA_NAME                   = "name";
    public static final String EXTRA_BEAN_BLUETOOTHDEVICE   = "beanbluetoothdevice";
    public static final String EXTRA_INFORMESDB             = "extra_informesDB";

    //to caugth data from sensors.
    public static final String MSG_FLOW     = "MSG_FLOW";
    public static final String MSG_PRESSU   = "MSG_PRESSU";
    public static final String MSG_VALVE    = "MSG_VALVE";
    public static final String MSG_UNKNOWN  = "MSG_UNKNOWN";
}