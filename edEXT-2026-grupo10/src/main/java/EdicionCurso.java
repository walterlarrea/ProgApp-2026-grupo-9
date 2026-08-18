import java.time.LocalDate;
import java.util.Set;

public class EdicionCurso {
    private String nombreEdi;
    private Curso cursoAsoc;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;
    private int cupo;
    private Set<Docente> docentes;
    private LocalDate fechaPub;

    public EdicionCurso(String nombreEdi, Curso cursoAsoc, LocalDate fechaInicio, LocalDate fechaFin, int cupo, Set<Docente> docentes, LocalDate fechaPub) {
        this.nombreEdi = nombreEdi;
        this.cursoAsoc = cursoAsoc;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.cupo = cupo;
        this.docentes = docentes;
        this.fechaPub = fechaPub;
    }

    public String getNombreEdi() {
        return nombreEdi;
    }

    public void setNombreEdi(String nombreEdi) {
        this.nombreEdi = nombreEdi;
    }

    public Curso getCursoAsoc() {
        return cursoAsoc;
    }

    public void setCursoAsoc(Curso cursoAsoc) {
        this.cursoAsoc = cursoAsoc;
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

    public int getCupo() {
        return cupo;
    }

    public void setCupo(int cupo) {
        this.cupo = cupo;
    }

    public Set<Docente> getDocentes() {
        return docentes;
    }

    public void setDocentes(Set<Docente> docentes) {
        this.docentes = docentes;
    }

    public LocalDate getFechaPub() {
        return fechaPub;
    }

    public void setFechaPub(LocalDate fechaPub) {
        this.fechaPub = fechaPub;
    }
}
