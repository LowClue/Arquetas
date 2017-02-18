package es.alarcon.arquetanatureble.DB_SQLITE;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import es.alarcon.arquetanatureble.UTIL.Constant;

/**
 * Created by alarcon on 1/11/15.
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper
{
    private static String DATABASE_NAME    = "informeBackup.db";
    private static int    DATABASE_VERSION = 1;

    private static MySQLiteOpenHelper myInstance;
    private static Context mContext;

    public MySQLiteOpenHelper(Context context)
    {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        //Create table informes
        Log.d(Constant.TAG,"Query :"+InformeSQLiteDataSource.CREATE_INFORME_SCRIPT);
        db.execSQL(InformeSQLiteDataSource.CREATE_INFORME_SCRIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE " + InformeSQLiteDataSource.INFORMES_TABLE_NAME);
        db.execSQL(InformeSQLiteDataSource.CREATE_INFORME_SCRIPT);
    }

    //////////////////////////////////////////////
    //////////////singleton///////////////////////
    //////////////////////////////////////////////
    public static MySQLiteOpenHelper getInstance(Context context){
        if (myInstance == null)
            myInstance = new MySQLiteOpenHelper(context.getApplicationContext());

        return myInstance;
    }
    //////////////////////////////////////////////////
}
