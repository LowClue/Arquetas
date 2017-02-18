package es.alarcon.arquetanatureble.WEBSERVICE;

import android.content.Context;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by alarcon on 26/10/15.
 */
public final class VolleySingleton
{
    private static VolleySingleton singleton;
    private RequestQueue requestQueu;
    private static Context mContext;

    private VolleySingleton(Context context)
    {
        this.mContext = context;
        requestQueu   = getRequestQueu();
    }

    public static synchronized VolleySingleton getInstace(Context context)
    {
        if(singleton == null)
            singleton = new VolleySingleton(context.getApplicationContext());

        return singleton;
    }

    public RequestQueue getRequestQueu()
    {
        if(requestQueu == null)
            requestQueu = Volley.newRequestQueue(mContext.getApplicationContext());

        return requestQueu;
    }

    public <T> void addToRequestQueue(Request<T> req)
    {
        getRequestQueu().add(req);
    }
}


