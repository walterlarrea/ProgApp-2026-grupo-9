package com.grupo9.edext.grupo9.servidor_central.controller.curso;

//import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import java.io.Serializable;
import java.time.LocalDate;
//import jakarta.persistence.GeneratedValue;
//import jakarta.persistence.GenerationType;
import jakarta.persistence.Entity;       
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import java.util.Set;

@Entity
public class Curso implements Serializable{
    private String nombreInst;
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

    public Curso(String nombreInst, String nombreCurso, String descCurso, int duracion, int cantHoras, int cantCred, LocalDate fechaReg, String url) {
        this.nombreInst = nombreInst;
        this.nombreCurso = nombreCurso;
        this.descCurso = descCurso;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCred = cantCred;
        this.fechaReg = fechaReg;
        this.url = url;
    }

    public String getNombreInst() {
        return nombreInst;
    }

    public void setNombreInst(String nombreInst) {
        this.nombreInst = nombreInst;
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
