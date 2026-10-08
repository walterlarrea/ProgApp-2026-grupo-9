package com.grupo9.edext.grupo9.servidor_central.controller.busqueda;

import com.grupo9.edext.grupo9.servidor_central.controller.curso.CursoController;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.InstitutoController;
import com.grupo9.edext.grupo9.servidor_central.controller.programa_de_formacion.ProgramaDeFormacionController;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.UsuarioController;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInstituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataProgramaFormacion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario;
import com.grupo9.edext.grupo9.servidor_central.dominio.ResultadoBusqueda;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BusquedaControllerTest {
    private BusquedaController busquedaController;
    private CursoController cursoController;
    private InstitutoController institutoController;
    private ProgramaDeFormacionController programaController;
    private UsuarioController usuarioController;

    @BeforeEach
    void setUp() {
        busquedaController = new BusquedaController();
        cursoController = new CursoController();
        institutoController = new InstitutoController();
        programaController = new ProgramaDeFormacionController();
        usuarioController = new UsuarioController();
    }

    @Test
    void buscarCursosPorNombreIgnoraMayusculasYBuscaParcialmente() {
        String sufijo = UUID.randomUUID().toString();
        DataInstituto instituto = institutoController.guardarNuevoInstituto(
                new DataInstituto("InstitutoBusqueda" + sufijo));
        String nombreCurso = "CursoBusqueda" + sufijo;
        cursoController.guardarNuevoCurso(new DataCurso(
                instituto, nombreCurso, "Descripcion", 4, 20, 2, LocalDate.now(), "", new HashSet<>(), new HashSet<>(), null));

        HashSet<DataCurso> resultados = busquedaController.buscarCursos("cursobusqueda" + sufijo);

        assertEquals(1, resultados.size());
        assertTrue(resultados.stream().anyMatch(curso -> curso.nombreCurso().equals(nombreCurso)));
        assertTrue(busquedaController.buscarCursos("  ").isEmpty());
    }

    @Test
    void buscarProgramasPorNombreIgnoraMayusculasYBuscaParcialmente() {
        String nombrePrograma = "ProgramaBusqueda" + UUID.randomUUID();
        programaController.guardarNuevoProgramaDeFormacion(new DataProgramaFormacion(
                nombrePrograma, "Descripcion", new HashSet<>(), LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now()));

        HashSet<DataProgramaFormacion> resultados = busquedaController.buscarProgramas("programabusqueda");

        assertTrue(resultados.stream().anyMatch(programa -> programa.nombre().equals(nombrePrograma)));
    }

    @Test
    void buscarUsuariosPorNombreYApellidoAplicaAmbosFiltros() throws Exception {
        String sufijo = UUID.randomUUID().toString();
        String nickname = "usuarioBusqueda" + sufijo;
        usuarioController.registrarEstudiante(nickname, "AnaBusqueda", "LopezBusqueda",
                nickname + "@test.com", LocalDate.of(2000, 1, 1), null);

        HashSet<DataUsuario> resultados = busquedaController.buscarUsuarios(
                "anabusqueda", "lopezbusqueda");

        assertEquals(1, resultados.size());
        assertEquals(nickname, resultados.iterator().next().getNickname());
        assertTrue(busquedaController.buscarUsuarios("AnaBusqueda", "apellidoIncorrecto").isEmpty());
        assertTrue(busquedaController.buscarUsuarios("", "").isEmpty());
    }

        @Test
        void busquedaPrincipalDevuelveResultadosDeLosTresTipos() throws Exception {
        String termino = "Busqueda" + UUID.randomUUID();
        DataInstituto instituto = institutoController.guardarNuevoInstituto(
            new DataInstituto("Instituto" + termino));
        cursoController.guardarNuevoCurso(new DataCurso(
            instituto, "Curso" + termino, "Descripcion", 4, 20, 2, LocalDate.now(), "", new HashSet<>(), new HashSet<>(), null));
        programaController.guardarNuevoProgramaDeFormacion(new DataProgramaFormacion(
            "Programa" + termino, "Descripcion", new HashSet<>(), LocalDate.now(), LocalDate.now().plusMonths(1), LocalDate.now()));
        usuarioController.registrarEstudiante("usuario" + termino, "Nombre" + termino, "Apellido" + termino,
            "usuario" + termino + "@test.com", LocalDate.of(2000, 1, 1), null);

        List<ResultadoBusqueda> resultados = busquedaController.busquedaPrincipal(termino);

        assertEquals(3, resultados.size());
        assertTrue(resultados.stream().anyMatch(resultado -> resultado.tipo() == TipoBusqueda.CURSO
            && resultado.data() instanceof DataCurso));
        assertTrue(resultados.stream().anyMatch(resultado -> resultado.tipo() == TipoBusqueda.USUARIO
            && resultado.data() instanceof DataUsuario));
        assertTrue(resultados.stream().anyMatch(resultado -> resultado.tipo() == TipoBusqueda.PROGRAMA_FORMACION
            && resultado.data() instanceof DataProgramaFormacion));
        assertTrue(busquedaController.busquedaPrincipal(" ").isEmpty());
        }
}