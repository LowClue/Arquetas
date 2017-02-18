package es.alarcon.arquetanatureble.WEBSERVICE;


/**
 * Created by alarcon on 26/10/15.
 */
public class BeanArqueta
{
    private  String id;
    private  String insert_time;
    private  String nombre_arqueta;
    private  String direccion_arqueta;
    private  String uuid_sensor1;
    private  String uuid_sensor2;
    private  String uuid_sensor3;

    public BeanArqueta()
    {
        super();
    }

    public  String getId() {
        return id;
    }

    public  void setId(String id) {
        this.id = id;
    }

    public  String getInsert_time() {
        return insert_time;
    }

    public  void setInsert_time(String insert_time) {
        this.insert_time = insert_time;
    }

    public  String getNombre_arqueta() {
        return nombre_arqueta;
    }

    public  void setNombre_arqueta(String nombre_arqueta) {
        this.nombre_arqueta = nombre_arqueta;
    }

    public  String getDireccion_arqueta() {
        return direccion_arqueta;
    }

    public  void setDireccion_arqueta(String direccion_arqueta) {
        this.direccion_arqueta = direccion_arqueta;
    }

    public  String getUuid_sensor1() {
        return uuid_sensor1;
    }

    public  void setUuid_sensor1(String uuid_sensor1) {
        this.uuid_sensor1 = uuid_sensor1;
    }

    public  String getUuid_sensor2() {
        return uuid_sensor2;
    }

    public  void setUuid_sensor2(String uuid_sensor2) {
        this.uuid_sensor2 = uuid_sensor2;
    }

    public  String getUuid_sensor3() {
        return uuid_sensor3;
    }

    public  void setUuid_sensor3(String uuid_sensor3) {
        this.uuid_sensor3 = uuid_sensor3;
    }

    public String toString()
    {
        return "id="+id+" insert_time = "+insert_time+" nombre_arqueta ="+nombre_arqueta+" direccion_arqueta= "
                +direccion_arqueta+" uuidSensor1= "+uuid_sensor1+" uuidSensor2= "+uuid_sensor2+
                " uuidSensor3= "+uuid_sensor3;
    }
}
