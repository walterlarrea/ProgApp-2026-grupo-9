package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.*;       

@Entity
public class Curso implements Serializable{
    @Id
    private String nombreCurso;
    private String descCurso;
    private int duracion;
    private int cantHoras;
    private int cantCred;
    private LocalDate fechaReg;
    private String url;
    @OneToMany(mappedBy = "cursoAsoc")
    private Set<EdicionCurso> ediciones;
    @ManyToOne
    @JoinColumn(name = "nombreI")
    private Instituto instituto;
    
    public Curso(){}

    public Curso(Instituto instituto, String nombreCurso, String descCurso, int duracion, int cantHoras, int cantCred, LocalDate fechaReg, String url) {
        this.instituto = instituto;
        this.nombreCurso = nombreCurso;
        this.descCurso = descCurso;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCred = cantCred;
        this.fechaReg = fechaReg;
        this.url = url;
    }

    public Instituto getInstituto() {
        return instituto;
    }

    public void setInstituto(Instituto instituto) {
        this.instituto = instituto;
    }

    public String getNombreCurso() {
        return nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getDescCurso() {
        return descCurso;
    }

    public void setDescCurso(String descCurso) {
        this.descCurso = descCurso;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getCantHoras() {
        return cantHoras;
    }

    public void setCantHoras(int cantHoras) {
        this.cantHoras = cantHoras;
    }

    public int getCantCred() {
        return cantCred;
    }

    public void setCantCred(int cantCred) {
        this.cantCred = cantCred;
    }

    public LocalDate getFechaReg() {
        return fechaReg;
    }

    public void setFechaReg(LocalDate fechaReg) {
        this.fechaReg = fechaReg;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
