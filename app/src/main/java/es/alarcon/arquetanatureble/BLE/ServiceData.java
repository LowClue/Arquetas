package es.alarcon.arquetanatureble.BLE;

import java.util.UUID;

/**
 * Created by alarcon on 21/10/15.
 */
public final class ServiceData {
    /* mbed definition
    uint16_t customServiceUUID   = 0xA000;
    uint16_t ValveUUID           = 0xA001;
    uint16_t PressureUUID        = 0xA002;
    uint16_t FlowmeterUUID       = 0xA003;
     */
    public static final UUID CHAMBER_SERVICE           = UUID.fromString("0000a000-0000-1000-8000-00805f9b34fb");
    public static final UUID FLOWMETER_CHARACTERISTIC  = UUID.fromString("0000a001-0000-1000-8000-00805f9b34fb");
    public static final UUID PRESSURE_CHARACTERISTIC   = UUID.fromString("0000a002-0000-1000-8000-00805f9b34fb");
    public static final UUID VALVE_CHARACTERISTIC      = UUID.fromString("0000a003-0000-1000-8000-00805f9b34fb");

    private ServiceData()
    {super();}

    public static int []workFlometerData(byte[] value)
    {
        int[] data = new int[value.length];
        for(byte i=0;i < value.length;i++)
            data[i] = value[i] & 0xff;

        return data;
    }

    public static int[] workPressureData (byte[] value)
    {
        int[] data = new int[value.length];
        for(byte i=0;i < value.length;i++)
            data[i] = value[i] & 0xff;

        return data;
    }
    public static boolean workValveData (byte[] value)
    {
        int[] data = new int[value.length];
        for(byte i=0;i < value.length;i++)
            data[i] = value[i] & 0xff;

        return data[0] == 1? true:false;
    }

}
