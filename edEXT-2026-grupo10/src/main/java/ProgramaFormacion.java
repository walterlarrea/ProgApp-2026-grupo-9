import java.time.LocalDate;
import java.util.Set;

public class ProgramaFormacion {
    private String nombreForm;
    private String descForm;
    private Set<Curso> cursos;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    
    public ProgramaFormacion (String nombreForm, String descForm, Set<Curso> cursos, LocalDate fechaInicio, LocalDate fechaFin){
        this.nombreForm = nombreForm;
        this.descForm = descForm;
        this.cursos = cursos;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }
    
    public String getNombreFrom() {
        return nombreForm;
    }
    
    public void setNombreFrom(String nombreForm) {
        this.nombreForm = nombreForm;
    }
    
    public String getDescFrom() {
        return descForm;
    }
    
    public void setDescFrom(String descForm) {
        this.descForm = descForm;
    }
    
    public Set<Curso> getCursos() {
        return cursos;
    }
    
    public void setCursos(Set<Curso> cursos) {
        this.cursos = cursos;
    }
    
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }
    
    public void setFechaInicio(LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }
    
    public LocalDate getFechaFin() {
        return fechaFin;
    }
    
    public void setFechaFin(LocalDate fechaFin) {
        this.fechaFin = fechaFin;
    }
}
