package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;
import jakarta.persistence.*;       
import java.util.HashSet;

@Entity
@Table(name = "cursos")
public class Curso implements Serializable {
    @Id
    private String nombreCurso;
    @Column(length = 600)
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
    // Owning side: Defines the join table layout
    @ManyToMany (fetch = FetchType.EAGER)
    @JoinTable(
        name = "cursos_previas",
        joinColumns = @JoinColumn(name = "nombre_curso"),
        inverseJoinColumns = @JoinColumn(name = "nombre_curso_previa")
    )
    private Set<Curso> previas = new HashSet<>();
    // Inverse side: Uses mappedBy to reference the owning side's field
    @ManyToMany(mappedBy = "previas")
    private Set<Curso> dependientes = new HashSet<>();
    @ManyToMany(mappedBy = "cursos")
    private Set<ProgramaDeFormacion> programas;
       
    
    public Curso(){}

    public Curso(Instituto instituto, String nombreCurso, String descCurso, int duracion, int cantHoras, int cantCred, LocalDate fechaReg, String url, Set<Curso> previas) {
        this.instituto = instituto;
        this.nombreCurso = nombreCurso;
        this.descCurso = descCurso;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCred = cantCred;
        this.fechaReg = fechaReg;
        this.url = url;
        this.previas = previas;
    }

    public Curso(Instituto instituto, String nombreCurso, String descCurso, int duracion, int cantHoras, int cantCred, LocalDate fechaReg, String url, Set<Curso> previas, Set<ProgramaDeFormacion> programas) {
        this.instituto = instituto;
        this.nombreCurso = nombreCurso;
        this.descCurso = descCurso;
        this.duracion = duracion;
        this.cantHoras = cantHoras;
        this.cantCred = cantCred;
        this.fechaReg = fechaReg;
        this.url = url;
        this.previas = previas;
        this.programas = programas;
    }

    public Instituto getInstituto() {
        return this.instituto;
    }

    public void setInstituto(Instituto instituto) {
        this.instituto = instituto;
    }

    public String getNombreCurso() {
        return this.nombreCurso;
    }

    public void setNombreCurso(String nombreCurso) {
        this.nombreCurso = nombreCurso;
    }

    public String getDescCurso() {
        return this.descCurso;
    }

    public void setDescCurso(String descCurso) {
        this.descCurso = descCurso;
    }

    public int getDuracion() {
        return this.duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getCantHoras() {
        return this.cantHoras;
    }

    public void setCantHoras(int cantHoras) {
        this.cantHoras = cantHoras;
    }

    public int getCantCred() {
        return this.cantCred;
    }

    public void setCantCred(int cantCred) {
        this.cantCred = cantCred;
    }

    public LocalDate getFechaReg() {
        return this.fechaReg;
    }

    public void setFechaReg(LocalDate fechaReg) {
        this.fechaReg = fechaReg;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Set<Curso> getPrevias() {
        return this.previas;
    }

    public void setPrevias(Set<Curso> previas) {
        this.previas = previas;
    }

    public Set<Curso> getDependientes() {
        return this.dependientes;
    }

    public void setDependientes(Set<Curso> dependientes) {
        this.dependientes = dependientes;
    }
    
    public Set<ProgramaDeFormacion> getProgramas(){
        return this.programas;
    }
}
