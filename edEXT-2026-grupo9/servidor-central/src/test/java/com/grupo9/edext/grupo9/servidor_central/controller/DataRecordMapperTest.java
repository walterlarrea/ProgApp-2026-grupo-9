package com.grupo9.edext.grupo9.servidor_central.controller;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso.EdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.InscripcionPrograma;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacion;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Usuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DataRecordMapperTest {

    @Test
    public void testInstitutoMapper() {
        DataInstituto dataInst = new DataInstituto("Instituto Test");
        Instituto inst = DataRecordMapper.toEntity(dataInst);

        assertNotNull(inst);
        assertEquals("Instituto Test", inst.getNombreI());

        DataInstituto dataInstConverted = DataRecordMapper.toData(inst);
        assertNotNull(dataInstConverted);
        assertEquals("Instituto Test", dataInstConverted.nombreI());

        assertNull(DataRecordMapper.toEntity((DataInstituto) null));
        assertNull(DataRecordMapper.toData((Instituto) null));
    }

    @Test
    public void testUsuarioMapper() {
        DataUsuario dataUsr = new DataUsuario("userTest", "Juan", "Pérez", "juan@test.com", LocalDate.of(1990, 1, 1),
                null);
        Usuario usr = DataRecordMapper.toEntity(dataUsr);

        assertNotNull(usr);
        assertEquals("userTest", usr.getNickname());

        DataUsuario dataUsrConverted = DataRecordMapper.toData(usr);
        assertNotNull(dataUsrConverted);
        assertEquals("userTest", dataUsrConverted.getNickname());

        assertNull(DataRecordMapper.toEntity((DataUsuario) null));
        assertNull(DataRecordMapper.toData((Usuario) null));
    }

    @Test
    public void testDocenteMapper() {
        DataDocente dataDoc = new DataDocente("docente1", "Carlos", "Gómez", "carlos@test.com",
                LocalDate.of(1985, 3, 10), "path/foto.png", "Instituto Test");

        Docente doc = DataRecordMapper.toEntity(dataDoc);

        assertNotNull(doc);
        assertEquals("docente1", doc.getNickname());
        assertEquals("Carlos", doc.getNombre());
        assertEquals("Instituto Test", doc.getNombreInst());

        DataDocente dataDocConverted = DataRecordMapper.toData(doc);
        assertNotNull(dataDocConverted);
        assertEquals("docente1", dataDocConverted.getNickname());
        assertEquals("Instituto Test", dataDocConverted.getNombreInst());
    }

    @Test
    public void testCursoMapper() {
        DataInstituto dataInst = new DataInstituto("Instituto FING");
        HashSet<String> categorias = new HashSet<>();
        categorias.add("Social");
        categorias.add("Educativo");

        DataCurso dataCurso = new DataCurso(
                dataInst, "Curso 101", "Descripción del curso", 10, 60, 5,
                LocalDate.of(2024, 1, 1), "http://curso101.com", new HashSet<>(), new HashSet<>(), categorias,
                "img/curso101.png");

        Curso curso = DataRecordMapper.toEntity(dataCurso);
        assertNotNull(curso);
        assertEquals("Curso 101", curso.getNombreCurso());
        assertEquals("Descripción del curso", curso.getDescCurso());
        assertEquals(10, curso.getDuracion());
        assertEquals(60, curso.getCantHoras());
        assertEquals(5, curso.getCantCred());
        assertEquals("img/curso101.png", curso.getImagen());
//        assertNotNull(curso.getCategorias());
//        assertEquals(2, curso.getCategorias().size());

        DataCurso dataCursoConverted = DataRecordMapper.toData(curso);
        assertNotNull(dataCursoConverted);
        assertEquals("Curso 101", dataCursoConverted.nombreCurso());
        assertEquals("http://curso101.com", dataCursoConverted.url());
        assertEquals("img/curso101.png", dataCursoConverted.imagen());
        assertEquals(categorias, dataCursoConverted.categorias());

        assertNull(DataRecordMapper.toEntity((DataCurso) null));
        assertNull(DataRecordMapper.toData((Curso) null));
    }

    @Test
    public void testEstudianteConInscripcionesMapper() {
        Estudiante est = new Estudiante("student1", "Nombre", "Apellido", "student@test.com", LocalDate.of(2000, 1, 1), "img/usr.png");
        ProgramaDeFormacion prog = new ProgramaDeFormacion("ProgX", "DescX", new HashSet<>(), LocalDate.now(), LocalDate.now().plusMonths(2), LocalDate.now());
        InscripcionPrograma insc = new InscripcionPrograma(est, prog, LocalDate.of(2026, 3, 1));
        Set<InscripcionPrograma> inscripciones = new HashSet<>();
        inscripciones.add(insc);
        est.setInscripcionesProgramas(inscripciones);

        DataEstudiante dataEst = DataRecordMapper.toData(est);
        assertNotNull(dataEst);
        assertEquals("student1", dataEst.getNickname());
        assertNotNull(dataEst.getInscripcionesProgramas());
        assertEquals(1, dataEst.getInscripcionesProgramas().size());
        DataInscPrograma dataInsc = dataEst.getInscripcionesProgramas().iterator().next();
        assertEquals("ProgX", dataInsc.getProgramaNombre());
        assertEquals(LocalDate.of(2026, 3, 1), dataInsc.getFechaInscP());
    }

    @Test
    public void testProgramaDeFormacionMapper() {
        DataProgramaFormacion dataProg = new DataProgramaFormacion(
                "Programa Bachiller", "Programa de prueba", new HashSet<>(),
                LocalDate.of(2024, 1, 1), LocalDate.of(2024, 12, 31), LocalDate.of(2024, 1, 1), null);

        ProgramaDeFormacion prog = DataRecordMapper.toEntity(dataProg);
        assertNotNull(prog);
        assertEquals("Programa Bachiller", prog.getNombre());
        assertEquals("Programa de prueba", prog.getDescripcion());

        DataProgramaFormacion dataProgConverted = DataRecordMapper.toData(prog);
        assertNotNull(dataProgConverted);
        assertEquals("Programa Bachiller", dataProgConverted.nombre());

        assertNull(DataRecordMapper.toEntity((DataProgramaFormacion) null));
        assertNull(DataRecordMapper.toData((ProgramaDeFormacion) null));
    }

    @Test
    public void testEdicionCursoMapper() {
        DataInstituto dataInst = new DataInstituto("Instituto FING");
        DataCurso dataCurso = new DataCurso(
                dataInst, "Curso Base", "Desc", 4, 30, 3,
                LocalDate.now(), "http://base.com", new HashSet<>(), new HashSet<>(), new HashSet<>(), null);

        DataEdicionCurso dataEd = new DataEdicionCurso(
                "Edición 2024", dataCurso, LocalDate.of(2024, 3, 1), LocalDate.of(2024, 7, 1),
                30, new HashSet<>(), new HashSet<>(), LocalDate.now(), null);

        EdicionCurso ed = DataRecordMapper.toEntity(dataEd);
        assertNotNull(ed);
        assertEquals("Edición 2024", ed.getNombreEdi());
        assertEquals(30, ed.getCupo());

        DataEdicionCurso dataEdConverted = DataRecordMapper.toData(ed);
        assertNotNull(dataEdConverted);
        assertEquals("Edición 2024", dataEdConverted.getNombreEdi());
    }

    @Test
    public void testSetDocentesMapper() {
        Set<DataDocente> dataDocentes = new HashSet<>();
        DataDocente d = new DataDocente("doc1", "Ana", "Lopez", "ana@test.com", LocalDate.of(1980, 5, 5), null, "INCO");
        dataDocentes.add(d);

        Set<Docente> docentes = DataRecordMapper.toEntity(dataDocentes);
        assertNotNull(docentes);
        assertEquals(1, docentes.size());

        Set<DataDocente> dataDocentesConverted = DataRecordMapper.toData(docentes);
        assertNotNull(dataDocentesConverted);
        assertEquals(1, dataDocentesConverted.size());

        assertTrue(DataRecordMapper.toEntity((Set<DataDocente>) null).isEmpty());
        assertTrue(DataRecordMapper.toData((Set<Docente>) null).isEmpty());
    }
}
