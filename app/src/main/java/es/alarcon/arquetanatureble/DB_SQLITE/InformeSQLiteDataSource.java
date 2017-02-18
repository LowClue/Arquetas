package es.alarcon.arquetanatureble.DB_SQLITE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import es.alarcon.arquetanatureble.BEAN.BeanInformesDB;

/**
 * Created by alarcon on 1/11/15.
 */
public class InformeSQLiteDataSource
{
    //Metainformación de la base de datos
    public static final String INFORMES_TABLE_NAME = "INFORMES";
    public static final String VARCHAR_TYPE        = "varchar";
    public static final String INT_TYPE            = "integer";
    public static final String SMALLINT_TYPE       = "smallint";
    public static final String BLOB_TYPE           = "blob";
    public static final String DATETIME_TYPE       = "datetime";


    public static final class ColumnInformes
    {
        public static final String ID_INFORME           = "acceso_ubicacion";
        public static final String ACCESO_UBICACION     = "acceso_ubicacion";
        public static final String PERIMETRO_ARQUETA    = "perimetro_arqueta";
        public static final String PUERTA_ACCESO        = "puerta_acceso";
        public static final String CUBIERTA             = "cubierta";
        public static final String PARAM_VERTICALES_INT = "param_verticales_int";
        public static final String PARAM_VERTICALES_EXT = "param_verticales_ext";
        public static final String VENTILACION_LATERAL  = "ventilacion_lateral";
        public static final String VENTILACION_SUPERIOR = "ventilacion_superior";
        public static final String PATES_ESCALERA       = "pates_escalera";
        public static final String DISTANCIA_REGLA_ELEMENTOS = "distancia_reglamentaria_elementos";
        public static final String VENTOSAS             = "ventosas";
        public static final String VALVULAS             = "valvulas";
        public static final String JUNTAS_UNION         = "juntas_carretes";
        public static final String MANOMETROS           = "manometros";
        public static final String CONTADORES           = "contadores";
        public static final String FECHA                = "fecha";
        public static final String DIRECCION_ARQUETA    = "direccion_arqueta";
        public static final String COMENTARIO           = "comentario";
        public static final String FOTO                 = "foto";
    }

    //Script de Creación de la tabla Quotes
    public static final String CREATE_INFORME_SCRIPT =" ( " +
                    ColumnInformes.ID_INFORME+" "+INT_TYPE+"(11) unsigned NOT NULL AUTO_INCREMENT," +
                    ColumnInformes.ACCESO_UBICACION+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.PERIMETRO_ARQUETA+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.PUERTA_ACCESO+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.CUBIERTA+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.PARAM_VERTICALES_INT+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.PARAM_VERTICALES_EXT+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.VENTILACION_LATERAL+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.VENTILACION_SUPERIOR+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.PATES_ESCALERA+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.DISTANCIA_REGLA_ELEMENTOS+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.VENTOSAS+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.VALVULAS+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.JUNTAS_UNION +" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.MANOMETROS+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.CONTADORES+" "+SMALLINT_TYPE+"(6) DEFAULT NULL,"+
                    ColumnInformes.FECHA+" "+DATETIME_TYPE+" NOT NULL,"+
                    ColumnInformes.DIRECCION_ARQUETA+" "+VARCHAR_TYPE+"(30) DEFAULT NULL,"+
                    ColumnInformes.COMENTARIO+" "+VARCHAR_TYPE+"(30) DEFAULT NULL,"+
                    ColumnInformes.FOTO+" "+BLOB_TYPE+" )";

    private MySQLiteOpenHelper openHelper;
    private SQLiteDatabase database;

    public InformeSQLiteDataSource(Context context)
    {
        //getting instance of database
        openHelper  = MySQLiteOpenHelper.getInstance(context);
        database    = openHelper.getWritableDatabase();
    }

    public void insertInforme(BeanInformesDB informeDB)
    {
        ContentValues cont = new ContentValues();

        cont.put(ColumnInformes.ACCESO_UBICACION,informeDB.getAccesoUbicacion());
        cont.put(ColumnInformes.PERIMETRO_ARQUETA,informeDB.getPerimetroArqueta());
        cont.put(ColumnInformes.PUERTA_ACCESO,informeDB.getPuertaAcceso());
        cont.put(ColumnInformes.CUBIERTA,informeDB.getCubierta());
        cont.put(ColumnInformes.PARAM_VERTICALES_INT,informeDB.getParVertInt());
        cont.put(ColumnInformes.PARAM_VERTICALES_EXT,informeDB.getParVertExt());
        cont.put(ColumnInformes.VENTILACION_LATERAL,informeDB.getVentilacionLateral());
        cont.put(ColumnInformes.VENTILACION_SUPERIOR,informeDB.getVentilacionSuperior());
        cont.put(ColumnInformes.DISTANCIA_REGLA_ELEMENTOS,informeDB.getDistanciaRegElementos());
        cont.put(ColumnInformes.VENTOSAS,informeDB.getVentosas());
        cont.put(ColumnInformes.VALVULAS,informeDB.getValvulas());
        cont.put(ColumnInformes.JUNTAS_UNION,informeDB.getJuntasUnion());
        cont.put(ColumnInformes.MANOMETROS,informeDB.getManometros());
        cont.put(ColumnInformes.CONTADORES,informeDB.getContadores());
        cont.put(ColumnInformes.FECHA,informeDB.getFecha());
        cont.put(ColumnInformes.DIRECCION_ARQUETA,informeDB.getDireccion_arqueta());
        cont.put(ColumnInformes.COMENTARIO,informeDB.getComentario());
        cont.put(ColumnInformes.FOTO,informeDB.getFoto());

        database.insert(INFORMES_TABLE_NAME, null, cont);
    }

    public boolean deleteInforme(int id)
    {
        boolean flag = true;
        String selection = ColumnInformes.ID_INFORME + " = ?";
        String[] selectionArgs = { String.valueOf(id) };

        int state = database.delete(INFORMES_TABLE_NAME, selection, selectionArgs);

        if(state < 0)
            flag = false;

        return flag;
    }


    public int numbersInformesInsideDB()
    {
        int id = -1;
        Cursor c = database.rawQuery("SELECT Count(*) FROM "+INFORMES_TABLE_NAME,null);

        if(c.moveToFirst())
            id = c.getInt(0);

        return id;
    }

    public List<BeanInformesDB> getAllInformes()
    {
        List<BeanInformesDB> informesList = new ArrayList<BeanInformesDB>();
        BeanInformesDB beanInformesDB;

        String query = "SELECT * FROM "+INFORMES_TABLE_NAME;

        Cursor c= database.rawQuery(query, null);

        if(c.moveToFirst())
        {
            do{
                beanInformesDB = new BeanInformesDB();

                beanInformesDB.setIDDataBase(c.getInt(c.getColumnIndex(ColumnInformes.ID_INFORME)));
                beanInformesDB.setDireccion_arqueta(c.getString(c.getColumnIndex(ColumnInformes.DIRECCION_ARQUETA)));
                beanInformesDB.setPerimetroArqueta(c.getInt(c.getColumnIndex(ColumnInformes.PERIMETRO_ARQUETA)));
                beanInformesDB.setPuertaAcceso(c.getInt(c.getColumnIndex(ColumnInformes.PUERTA_ACCESO)));
                beanInformesDB.setCubierta(c.getInt(c.getColumnIndex(ColumnInformes.CUBIERTA)));
                beanInformesDB.setParVertExt(c.getInt(c.getColumnIndex(ColumnInformes.PARAM_VERTICALES_EXT)));
                beanInformesDB.setParVertInt(c.getInt(c.getColumnIndex(ColumnInformes.PARAM_VERTICALES_INT)));
                beanInformesDB.setVentilacionLateral(c.getInt(c.getColumnIndex(ColumnInformes.VENTILACION_LATERAL)));
                beanInformesDB.setVentilacionSuperior(c.getInt(c.getColumnIndex(ColumnInformes.VENTILACION_SUPERIOR)));
                beanInformesDB.setPatesEscalera(c.getInt(c.getColumnIndex(ColumnInformes.PATES_ESCALERA)));
                beanInformesDB.setDistanciaRegElementos(c.getInt(c.getColumnIndex(ColumnInformes.DISTANCIA_REGLA_ELEMENTOS)));

                beanInformesDB.setVentosas(c.getInt(c.getColumnIndex(ColumnInformes.VENTOSAS)));
                beanInformesDB.setValvulas(c.getInt(c.getColumnIndex(ColumnInformes.JUNTAS_UNION)));
                beanInformesDB.setManometros(c.getInt(c.getColumnIndex(ColumnInformes.MANOMETROS)));
                beanInformesDB.setContadores(c.getInt(c.getColumnIndex(ColumnInformes.CONTADORES)));

                beanInformesDB.setComentario(c.getString(c.getColumnIndex(ColumnInformes.COMENTARIO)));
                beanInformesDB.setFoto(c.getBlob(c.getColumnIndex(ColumnInformes.FOTO)));

                informesList.add(beanInformesDB);

            }while(c.moveToNext());

            return informesList;
        }
        return informesList;
    }
}
