import java.time.LocalDate;

public class InscPrograma {
    private LocalDate fechaInscP;

    public InscPrograma(LocalDate fechaInscP) {
        this.fechaInscP = fechaInscP;
    }

    public LocalDate getFechaInscP() {
        return fechaInscP;
    }

    public void setFechaInscP(LocalDate fechaInscP) {
        this.fechaInscP = fechaInscP;
    }
}
