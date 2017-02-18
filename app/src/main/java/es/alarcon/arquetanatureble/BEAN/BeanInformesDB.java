package es.alarcon.arquetanatureble.BEAN;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by alarcon on 22/9/15.
 */
public class BeanInformesDB implements Parcelable
    {
        public static final int BUENO               = 1;
        public static final int ACEPTABLE           = 2;
        public static final int NECESITA_REPARACION = 3;

        private static int idDataBase;
        private static String fecha;
        //parametros exterios
        private static String direccion_arqueta;
        private static int accesoUbicacion;
        private static int perimetroArqueta;
        private static int puertaAcceso;
        private static int cubierta;
        private static int parVertInt;
        private static int parVertExt;
        private static int ventilacionLateral;
        private static int ventilacionSuperior;
        private static int patesEscalera;
        private static int distanciaRegElementos;

        //parametros interior
        private static int ventosas;
        private static int valvulas;
        private static int juntasUnion;
        private static int manometros;
        private static int contadores;

        private static String comentario;

        public static byte[] foto;

        public BeanInformesDB(Parcel source)
        {
            super();
            this.idDataBase             = source.readInt();
            this.accesoUbicacion        = source.readInt();
            this.perimetroArqueta       = source.readInt();
            this.puertaAcceso           = source.readInt();
            this.cubierta               = source.readInt();
            this.parVertInt             = source.readInt();
            this.parVertExt             = source.readInt();
            this.ventilacionLateral     = source.readInt();
            this.ventilacionSuperior    = source.readInt();
            this.patesEscalera          = source.readInt();
            this.distanciaRegElementos  = source.readInt();
            this.fecha                  = source.readString();
            this.direccion_arqueta      = source.readString();

            this.ventosas               = source.readInt();
            this.valvulas               = source.readInt();
            this.juntasUnion            = source.readInt();
            this.manometros             = source.readInt();
            this.contadores             = source.readInt();
            this.comentario             = source.readString();
            source.readByteArray( this.foto );
    }
    public BeanInformesDB(){super();}

    public static void setIDDataBase(int id)
    {
        BeanInformesDB.idDataBase = id;
    }

    public static int getIDDataBase()
    {
        return idDataBase;
    }
    public static String getComentario() {
        return comentario;
    }

    public static void setComentario(String comentario) {
        BeanInformesDB.comentario = comentario;
    }

    public static byte[] getFoto() {
        return foto;
    }

    public static void setFoto(byte[] foto) {
        BeanInformesDB.foto = foto;
    }

    public static String getFecha() {
        return fecha;
    }

    public static void setFecha(String fecha) {
        BeanInformesDB.fecha = fecha;
    }

    public static String getDireccion_arqueta() {
        return direccion_arqueta;
    }

    public static void setDireccion_arqueta(String direccion_arqueta) {
        BeanInformesDB.direccion_arqueta = direccion_arqueta;
    }

    public static int getAccesoUbicacion() {
        return accesoUbicacion;
    }

    public static void setAccesoUbicacion(int ext_1) {
        BeanInformesDB.accesoUbicacion = ext_1;
    }

    public static int getPerimetroArqueta() {
        return perimetroArqueta;
    }

    public static void setPerimetroArqueta(int perimetroArqueta) {
        BeanInformesDB.perimetroArqueta = perimetroArqueta;
    }

    public static int getPuertaAcceso() {
        return puertaAcceso;
    }

    public static void setPuertaAcceso(int puertaAcceso) {
        BeanInformesDB.puertaAcceso = puertaAcceso;
    }

    public static int getCubierta() {
        return cubierta;
    }

    public static void setCubierta(int cubierta) {
        BeanInformesDB.cubierta = cubierta;
    }

    public static int getParVertInt() {
        return parVertInt;
    }

    public static void setParVertInt(int parVertInt) {
        BeanInformesDB.parVertInt = parVertInt;
    }

    public static int getParVertExt() {
        return parVertExt;
    }

    public static void setParVertExt(int parVertExt) {
        BeanInformesDB.parVertExt = parVertExt;
    }

    public static int getVentilacionLateral() {
        return ventilacionLateral;
    }

    public static void setVentilacionLateral(int ventilacionLateral) {
        BeanInformesDB.ventilacionLateral = ventilacionLateral;
    }

    public static int getVentilacionSuperior() {
        return ventilacionSuperior;
    }

    public static void setVentilacionSuperior(int ventilacionSuperior) {
        BeanInformesDB.ventilacionSuperior = ventilacionSuperior;
    }

    public static int getPatesEscalera() {
        return patesEscalera;
    }

    public static void setPatesEscalera(int patesEscalera) {
        BeanInformesDB.patesEscalera = patesEscalera;
    }

    public static int getDistanciaRegElementos() {
        return distanciaRegElementos;
    }

    public static void setDistanciaRegElementos(int distanciaRegElementos) {
        BeanInformesDB.distanciaRegElementos = distanciaRegElementos;
    }

    public static int getVentosas() {
        return ventosas;
    }

    public static void setVentosas(int ventosas) {
        BeanInformesDB.ventosas = ventosas;
    }

    public static int getValvulas() {
        return valvulas;
    }

    public static void setValvulas(int valvulas) {
        BeanInformesDB.valvulas = valvulas;
    }

    public static int getJuntasUnion() {
        return juntasUnion;
    }

    public static void setJuntasUnion(int juntasUnion) {
        BeanInformesDB.juntasUnion = juntasUnion;
    }

    public static int getManometros() {
        return manometros;
    }

    public static void setManometros(int manometros) {
        BeanInformesDB.manometros = manometros;
    }

    public static int getContadores() {
        return contadores;
    }

    public static void setContadores(int contadores) {
        BeanInformesDB.contadores = contadores;
    }

    public static void gnrlSetEi(int value, int offset)
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

    public static void gnrlSetExt(int value, int offset)
    {
        switch (offset)
        {
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
    //////////////////////////////////////////////////
    // Parceable implementation
    /////////////////////////////////////////////////
    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i)
    {
        parcel.writeInt(idDataBase);
        parcel.writeInt(accesoUbicacion);
        parcel.writeInt(perimetroArqueta);
        parcel.writeInt(puertaAcceso);
        parcel.writeInt(cubierta);
        parcel.writeInt(parVertInt);
        parcel.writeInt(ventilacionLateral);
        parcel.writeInt(ventilacionSuperior);
        parcel.writeInt(patesEscalera);
        parcel.writeInt(distanciaRegElementos);
        parcel.writeString(fecha);
        parcel.writeString(direccion_arqueta);
        parcel.writeInt(ventosas);
        parcel.writeInt(valvulas);
        parcel.writeInt(juntasUnion);
        parcel.writeInt(manometros);
        parcel.writeInt(contadores);
        parcel.writeString(comentario);
        parcel.writeByteArray(foto);
    }

    public static final Parcelable.Creator<BeanInformesDB> CREATOR = new Parcelable.Creator<BeanInformesDB>() {

        @Override
        public BeanInformesDB createFromParcel(Parcel source) {
            return new BeanInformesDB(source);
        }

        @Override
        public BeanInformesDB[] newArray(int size) {
            return new BeanInformesDB[size];
        }

    };
}
