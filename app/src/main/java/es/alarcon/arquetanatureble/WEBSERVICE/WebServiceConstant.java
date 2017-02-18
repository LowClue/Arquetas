package es.alarcon.arquetanatureble.WEBSERVICE;

/**
 * Created by alarcon on 30/11/15.
 */
public final class WebServiceConstant
{
    public static final int CODIGO_DETALLE       = 100;
    public static final int CODIGO_ACTUALIZACION = 101;

    private static final String PUERTO_HOST      = "63343";
    private static final String IP               = "http://185.2.130.82/~alarcon";

    /*
     WEB SERVICE URLs
     */
    //public static final String ADREESS            = IP + PUERTO_HOST + "/webService";
    public static final String ADREESS            = IP + "/webService";
    public static final String ARQUETA_DIR        = "/arqueta/";
    public static final String INFORME_DIR        = "/informes/";
    public static final String SECTOR_TRABAJO_DIR = "/sector_trabajo/";

    public static final String GET_ARQUETA_BYID          = ADREESS + ARQUETA_DIR +"Obtener_arquetaById.php";
    public static final String GET_SECTOR_TRABAJO_BYID   = ADREESS + SECTOR_TRABAJO_DIR +"Obtener_sector_trabajoById.php";

    public static final String INSERT             = ADREESS + INFORME_DIR +"Insertar_informe.php";
}
