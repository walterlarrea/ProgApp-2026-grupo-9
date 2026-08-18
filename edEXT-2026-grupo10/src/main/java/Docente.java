import java.time.LocalDate;

public class Docente extends Usuario {
    private String nombreInst;

    public Docente(String nickname, String nombreUs, String apellidoUs, String email, LocalDate fechaNac, String nombreInst) {
        super(nickname, nombreUs, apellidoUs, email, fechaNac);
        this.nombreInst = nombreInst;
    }

    public String getNombreInst() {
        return nombreInst;
    }

    public void setNombreInst(String nombreInst) {
        this.nombreInst = nombreInst;
    }
}
