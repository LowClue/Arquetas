package es.alarcon.arquetanatureble.WEBSERVICE;

/**
 * Created by alarcon on 26/10/15.
 */
public class BeanSector_trabajo
{
    private static String id;
    private static String fecha_control;
    private static String zona;
    private static String localizacion;
    private static String ramal;
    private static String responsable_control;
    private static String responsable_zona;
    private static String tm;
    private static String th;
    private static String direccion_aqueta;

    public BeanSector_trabajo()
    {
        super();
    }

    public static String getId() {
        return id;
    }

    public static void setId(String id) {
        BeanSector_trabajo.id = id;
    }

    public static String getFecha_control() {
        return fecha_control;
    }

    public static void setFecha_control(String fecha_control) {
        BeanSector_trabajo.fecha_control = fecha_control;
    }

    public static String getZona() {
        return zona;
    }

    public static void setZona(String zona) {
        BeanSector_trabajo.zona = zona;
    }

    public static String getLocalizacion() {
        return localizacion;
    }

    public static void setLocalizacion(String localizacion) {
        BeanSector_trabajo.localizacion = localizacion;
    }

    public static String getRamal() {
        return ramal;
    }

    public static void setRamal(String ramal) {
        BeanSector_trabajo.ramal = ramal;
    }

    public static String getResponsable_control() {
        return responsable_control;
    }

    public static void setResponsable_control(String responsable_control) {
        BeanSector_trabajo.responsable_control = responsable_control;
    }

    public static String getResponsable_zona() {
        return responsable_zona;
    }

    public static void setResponsable_zona(String responsable_zona) {
        BeanSector_trabajo.responsable_zona = responsable_zona;
    }

    public static String getTm() {
        return tm;
    }

    public static void setTm(String tm) {
        BeanSector_trabajo.tm = tm;
    }

    public static String getTh() {
        return th;
    }

    public static void setTh(String th) {
        BeanSector_trabajo.th = th;
    }

    public static String getDireccion_aqueta() {
        return direccion_aqueta;
    }

    public static void setDireccion_aqueta(String direccion_aqueta) {
        BeanSector_trabajo.direccion_aqueta = direccion_aqueta;
    }
}
