package com.ventulabs.admining.Models;

import com.google.firebase.database.Exclude;
import com.google.firebase.database.IgnoreExtraProperties;
import com.ventulabs.admining.Utils.DateUtils;

//@IgnoreExtraProperties
public class Usuario {

    /*@Exclude*/ private String idUsuario;
    private String nombre;
    private String email;
    private String fechaCreacion;
    public  long   puntuacion;

    public Usuario() {
        this("");
    }

    public Usuario(String idUsuario) {
        this(idUsuario, "");
    }

    public Usuario(String idUsuario, String nombre) {
        this(idUsuario, nombre, "");
    }

    public Usuario(String idUsuario, String nombre, String email) {
        this(idUsuario, nombre, email, DateUtils.getCurrentDate());
    }

    public Usuario(String idUsuario, String nombre, String email, String fechaCreacion) {
        this(idUsuario, nombre, email, fechaCreacion, 200);
    }

    public Usuario(String idUsuario, String nombre, String email, String fechaCreacion, int puntuacion) {
        this.idUsuario     = idUsuario;
        this.nombre        = nombre;
        this.email         = email;
        this.fechaCreacion = fechaCreacion;
        this.puntuacion    = puntuacion;
    }

    //@Exclude
    public String getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(String idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaCreacion() {
        return fechaCreacion;
    }

    public void setFechaCreacion(String fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "idUsuario='" + idUsuario + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", fechaCreacion='" + fechaCreacion + '\'' +
                '}';
    }
}
