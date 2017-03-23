package es.alarcon.arquetanatureble.BEAN;



import android.os.Parcel;
import android.os.Parcelable;
import android.provider.Settings;

/**
 * Created by alarcon on 22/9/15.
 */
public class BeanInformesDB implements Parcelable
{
    public static final int BUENO               = 1;
    public static final int ACEPTABLE           = 2;
    public static final int NECESITA_REPARACION = 3;

    private  int idDataBase;
    private  String fecha;
    //parametros exterios
    private  String direccion_arqueta;
    private  int accesoUbicacion;
    private  int perimetroArqueta;
    private  int puertaAcceso;
    private  int cubierta;
    private  int parVertInt;
    private  int parVertExt;
    private  int ventilacionLateral;
    private  int ventilacionSuperior;
    private  int patesEscalera;
    private  int distanciaRegElementos;

    //parametros interior
    private  int ventosas;
    private  int valvulas;
    private  int juntasUnion;
    private  int manometros;
    private  int contadores;

    private String comentario;

    public  byte[] foto;

    public BeanInformesDB(){super();}
    public BeanInformesDB(Parcel inParcel)
    {
        super();
        idDataBase = inParcel.readInt();
        fecha      = inParcel.readString();
        direccion_arqueta = inParcel.readString();
        accesoUbicacion   = inParcel.readInt();
        perimetroArqueta  = inParcel.readInt();
        puertaAcceso= inParcel.readInt();
        cubierta= inParcel.readInt();
        parVertInt= inParcel.readInt();
        parVertExt= inParcel.readInt();
        ventilacionLateral= inParcel.readInt();
        ventilacionSuperior= inParcel.readInt();
        patesEscalera= inParcel.readInt();
        distanciaRegElementos= inParcel.readInt();
        ventosas= inParcel.readInt();
        valvulas= inParcel.readInt();
        juntasUnion= inParcel.readInt();
        manometros= inParcel.readInt();
        contadores= inParcel.readInt();

        foto = inParcel.createByteArray();

        comentario=inParcel.readString();
    }

    ///Setter and Getter methods

    public  void setIDDataBase(int id)
    {
        this.idDataBase = id;
    }

    public  int getIDDataBase()
    {
        return idDataBase;
    }
    public  String getComentario() {
        return comentario;
    }

    public  void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public  byte[] getFoto() {
        return foto;
    }

    public  void setFoto(byte[] foto) {
        this.foto= foto;
    }

    public  String getFecha() {
        return fecha;
    }

    public  void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public  String getDireccion_arqueta() {
        return direccion_arqueta;
    }

    public  void setDireccion_arqueta(String direccion_arqueta) {
        this.direccion_arqueta = direccion_arqueta;
    }

    public  int getAccesoUbicacion() {
        return accesoUbicacion;
    }

    public  void setAccesoUbicacion(int ext_1) {
        this.accesoUbicacion = ext_1;
    }

    public  int getPerimetroArqueta() {
        return perimetroArqueta;
    }

    public  void setPerimetroArqueta(int perimetroArqueta) {
        this.perimetroArqueta = perimetroArqueta;
    }

    public  int getPuertaAcceso() {
        return puertaAcceso;
    }

    public  void setPuertaAcceso(int puertaAcceso) {
        this.puertaAcceso = puertaAcceso;
    }

    public  int getCubierta() {
        return cubierta;
    }

    public  void setCubierta(int cubierta) {
        this.cubierta = cubierta;
    }

    public  int getParVertInt() {
        return parVertInt;
    }

    public  void setParVertInt(int parVertInt) {
        this.parVertInt = parVertInt;
    }

    public  int getParVertExt() {
        return parVertExt;
    }

    public  void setParVertExt(int parVertExt) {
        this.parVertExt = parVertExt;
    }

    public  int getVentilacionLateral() {
        return ventilacionLateral;
    }

    public  void setVentilacionLateral(int ventilacionLateral) {
        this.ventilacionLateral = ventilacionLateral;
    }

    public  int getVentilacionSuperior() {
        return this.ventilacionSuperior;
    }

    public void setVentilacionSuperior(int ventilacionSuperior) {
        this.ventilacionSuperior = ventilacionSuperior;
    }

    public  int getPatesEscalera() {
        return this.patesEscalera;
    }

    public  void setPatesEscalera(int patesEscalera) {
        this.patesEscalera = patesEscalera;
    }

    public  int getDistanciaRegElementos() {
        return this.distanciaRegElementos;
    }

    public  void setDistanciaRegElementos(int distanciaRegElementos) {
        this.distanciaRegElementos = distanciaRegElementos;
    }

    public int getVentosas() {
        return this.ventosas;
    }

    public void setVentosas(int ventosas) {
        this.ventosas = ventosas;
    }

    public int getValvulas() {
        return this.valvulas;
    }

    public  void setValvulas(int valvulas) {
        this.valvulas = valvulas;
    }

    public  int getJuntasUnion() {
        return juntasUnion;
    }

    public  void setJuntasUnion(int juntasUnion) {
        this.juntasUnion = juntasUnion;
    }

    public  int getManometros() {
        return manometros;
    }

    public  void setManometros(int manometros) {
        this.manometros = manometros;
    }

    public  int getContadores() {
        return this.contadores;
    }

    public  void setContadores(int contadores) {
        this.contadores = contadores;
    }
    public void gnrlSetEi(int value, int offset)
    {
        switch (offset)
        {
            case 1:
                setVentosas(value);
                break;
            case 2:
                setValvulas(value);
                break;
            case 3:
                setJuntasUnion(value);
                break;
            case 4:
                setManometros(value);
                break;
            default:
                setContadores(value);
                break;

        }

    }

    public void gnrlSetExt(int value, int offset) {
        switch (offset) {
            case 1:
                setAccesoUbicacion(value);
                break;
            case 2:
                setPerimetroArqueta(value);
                break;
            case 3:
                setPuertaAcceso(value);
                break;
            case 4:
                setCubierta(value);
                break;
            case 5:
                setParVertInt(value);
                break;
            case 6:
                setParVertExt(value);
                break;
            case 7:
                setVentilacionLateral(value);
                break;
            case 8:
                setVentilacionSuperior(value);
                break;
            case 9:
                setPatesEscalera(value);
                break;
            default:
                setDistanciaRegElementos(value);
                break;

        }

    }
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(idDataBase);
        parcel.writeString(fecha);
        parcel.writeString(direccion_arqueta);
        parcel.writeInt(accesoUbicacion);
        parcel.writeInt(perimetroArqueta);
        parcel.writeInt(puertaAcceso);
        parcel.writeInt(cubierta);
        parcel.writeInt(parVertInt);
        parcel.writeInt(parVertExt);
        parcel.writeInt(ventilacionLateral);
        parcel.writeInt(ventilacionSuperior);
        parcel.writeInt(patesEscalera);
        parcel.writeInt(distanciaRegElementos);
        parcel.writeInt(ventosas);
        parcel.writeInt(valvulas);
        parcel.writeInt(juntasUnion);
        parcel.writeInt(manometros);
        parcel.writeInt(contadores);
        parcel.writeByteArray(foto);
        parcel.writeString(comentario);

    }

    public static final Creator<BeanInformesDB> CREATOR = new Creator<BeanInformesDB>() {
        @Override
        public BeanInformesDB createFromParcel(Parcel parcel) {
            return new BeanInformesDB(parcel);
        }

        @Override
        public BeanInformesDB[] newArray(int size) {
            return new BeanInformesDB[size];
        }
    };
}
