package com.mas_aplicaciones.appventon;

public class EntidadChofer
{
    private String Id;
    private String IdChofer;
    private String Nombre;
    private String Imagen;

    public String getIdChofer() {
        return IdChofer;
    }

    public void setIdChofer(String idChofer) {
        IdChofer = idChofer;
    }

    public String getImagen_Coche() {
        return Imagen_Coche;
    }

    public void setImagen_Coche(String imagen_Coche) {
        Imagen_Coche = imagen_Coche;
    }

    public String getComentario() {
        return Comentario;
    }

    public void setComentario(String comentario) {
        Comentario = comentario;
    }

    public String getEspacio() {
        return Espacio;
    }

    public void setEspacio(String espacio) {
        Espacio = espacio;
    }

    public String getTiempoEspera() {
        return TiempoEspera;
    }

    public void setTiempoEspera(String tiempoEspera) {
        TiempoEspera = tiempoEspera;
    }

    private String Imagen_Coche;
    private String Comentario;
    private String Espacio;
    private String TiempoEspera;
    private String Hora;
    private String Fecha;

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public String getFecha() {
        return Fecha;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }
}
