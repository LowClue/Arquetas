package es.alarcon.arquetanatureble.CORE;

import android.app.IntentService;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;

import es.alarcon.arquetanatureble.BLE.HandlerBLE;
import es.alarcon.arquetanatureble.UTIL.Constant;


/**
 * Created by alarcon on 4/7/15.
 */
public class ServiceDetectionTag extends Service {
    private static final String NAMECLASS = "ServiceDetectionTag";
    private static final long STOP_SCAN_TIMER = 10 * 1000;
    private static final long START_SCAN_TIMER = 30 * 1000;

    private static Context mContext;
    private static HandlerBLE mHandlerBLE;
    private static ServiceBroadcastReceiver mServiceBroadcastReceiver;
    private static Thread workerThread = null;
    private static Boolean alive;

    public ServiceDetectionTag()
    {
        super();
    }

    /*private volatile Timer mTimerStart;
    private volatile Timer mTimerStop;*/

    public static Boolean isAlive()
    {
        return alive;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        alive = true;
        mHandlerBLE = ((BLE_Application) getApplication()).getmHandlerBLEInstance(this.getApplicationContext());
        ((BLE_Application) getApplication()).resetHandlerBLE();
        mContext = getApplicationContext();

        mServiceBroadcastReceiver = new ServiceBroadcastReceiver();
        IntentFilter i = new IntentFilter(ServiceBroadcastReceiver.ACTION_NOTIFY_NEW_DEVICE_FOUND);
        registerReceiver(mServiceBroadcastReceiver, i);

        if (Constant.DEBUG)
            Log.i(Constant.TAG, NAMECLASS + " ## -- Init Proccess");

            workerThread = new Thread(new Runnable() {
                public void run()
                {
                        while (isAlive())
                        {
                            try
                            {
                                if (Constant.DEBUG)
                                    Log.i(Constant.TAG, NAMECLASS + " ## -- onCreate -> Start scanning");

                                mHandlerBLE.startLeScan();
                                workerThread.sleep(START_SCAN_TIMER);

                                if (Constant.DEBUG)
                                    Log.i(Constant.TAG, NAMECLASS + " ## -- onCreate -> Stop scanning");
                                mHandlerBLE.stopLeScan();
                                workerThread.sleep(STOP_SCAN_TIMER);

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                        }
                }});
                workerThread.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy ()
    {
        alive = false;

        if (Constant.DEBUG)
            Log.i(Constant.TAG, NAMECLASS + " ## -- onDestroy -> Stop scanning");
        mHandlerBLE.stopLeScan();

        try {
            workerThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        unregisterReceiver(mServiceBroadcastReceiver);
        super.onDestroy();
    }
    //#############################################################################################
    //#############################################################################################
    public static class ServiceBroadcastReceiver extends BroadcastReceiver {
            public static final String ACTION_NOTIFY_NEW_DEVICE_FOUND = "es.alarcon.arquetanatureble.NOTIFY_NEW_DEVICE";
            public ServiceBroadcastReceiver(){}
            @Override
            public void onReceive(Context context, Intent intent) {
                if (Constant.DEBUG)
                    Log.i(Constant.TAG, " ## ServiceBroadcastReceiver(Listener) -- onReceive ");
                String action = intent.getAction();
                if (action.equals(ServiceBroadcastReceiver.ACTION_NOTIFY_NEW_DEVICE_FOUND)) {

                    if(mContext != null )
                    {
                        if (Constant.DEBUG)
                            Log.i(Constant.TAG, " ## ServiceBroadcastReceiver -- onReceive -> sending notication(statusBar)");

                        MyNotificationHandler myNotificationHandler;
                        myNotificationHandler = new MyNotificationHandler(mContext);
                        myNotificationHandler.SendNotify();
                    }
                }
            }

        }
}
