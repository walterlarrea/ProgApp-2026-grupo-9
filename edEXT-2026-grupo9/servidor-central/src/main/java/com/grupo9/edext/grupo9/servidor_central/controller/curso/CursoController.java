package com.grupo9.edext.grupo9.servidor_central.controller.curso;

import com.grupo9.edext.grupo9.servidor_central.controller.DataRecordMapper;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Categoria;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import com.grupo9.edext.grupo9.mensajes.*;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class CursoController implements ICurso {
    ManejadorCurso manejadorCurso = null;

    public CursoController() {
        this.manejadorCurso = ManejadorCurso.getInstance();
    }

    @Override
    public DataCurso guardarNuevoCurso(DataCurso curso) throws ErrorRepetidos {
        System.out.println("[SERVIDOR] Persistencia de un nuevo Curso: " + curso.nombreCurso());
        LocalDate fechaDeCreacion = LocalDate.now();

        Instituto instituto = DataRecordMapper.toEntity(curso.instituto());
        Set<Curso> previas = DataRecordMapper.toEntityList(curso.previas(), Curso.class);
        Set<Categoria> categorias = new HashSet<>();
        if (curso.categorias() != null) {
            for (String categoriaNombre : curso.categorias()) {
                if (categoriaNombre != null && !categoriaNombre.trim().isEmpty()) {
                    categorias.add(new Categoria(categoriaNombre.trim()));
                }
            }
        }

        Curso nuevoCurso = new Curso(instituto, curso.nombreCurso(), curso.descCurso(), curso.duracion(),
                curso.cantHoras(), curso.cantCred(), fechaDeCreacion, curso.url(), previas, curso.imagen());
        nuevoCurso.setCategorias(categorias);
        this.manejadorCurso.guardarNuevo(nuevoCurso);

        return curso;
    }

    @Override
    public HashSet<DataCurso> todosLosCursos() {
        System.out.println("[SERVIDOR] Consulta todos los Cursos a persistencia");
        try {
            HashSet<DataCurso> todosLosCursos = this.manejadorCurso.traerTodos();

            return todosLosCursos;
        } catch (Exception e) {
            System.out.println("[SERVIDOR] Persistencia FALLÓ al intentar traer todos los Cursos");
            System.out.println(e);
        }
        return null;
    }

    @Override
    public HashSet<DataCurso> cursosPorInstituto(String nombreInstituto) {
        System.out.println("[SERVIDOR] Consulta los Cursos del Instituto " + nombreInstituto + " a persistencia");
        try {
            return this.manejadorCurso.traerCursosPorInstituto(nombreInstituto);
        } catch (Exception e) {
            System.out.println(
                    "[SERVIDOR] Persistencia FALLÓ al intentar traer los Cursos del Instituto: " + nombreInstituto);
            System.out.println(e);
        }
        return null;
    }

    @Override
    public Boolean existeCurso(String nombre) {
        System.out.println("[SERVIDOR] Consulta si existe el Curso " + nombre + " en persistencia");
        try {
            return this.manejadorCurso.existeCurso(nombre);
        } catch (Exception e) {
            System.out.println("[SERVIDOR] Persistencia FALLÓ al consultar el Curso: " + nombre);
            System.out.println(e);
        }
        return false;
    }

    @Override
    public HashSet<DataCurso> cursosNoRelacionadosConUnProgDeFormacion(String idProgramaDeFormacion) {
        System.out.println(
                "[SERVIDOR] Consulta en persistencia los Cursos que no están relacionados a un Prog de Formación específico");
        try {
            HashSet<DataCurso> cursosNoRelacionados = this.manejadorCurso
                    .traerCursosNoRelacionadosConUnProgDeFormacion(idProgramaDeFormacion);

            return cursosNoRelacionados;
        } catch (Exception e) {
            System.out.println(
                    "[SERVIDOR] Persistencia FALLÓ al intentar traer los Cursos no relacionados a este Prog de Formación");
            System.out.println(e);
        }
        return null;
    }

    @Override
    public DataCurso traerCurso(String nombre) {
        System.out.println("[SERVIDOR] Busca un curso por su nombre " + nombre + " a persistencia");
        try {
            return this.manejadorCurso.traerCursoPorNombre(nombre);
        } catch (Exception e) {
            System.out.println("[SERVIDOR] Persistencia FALLÓ al intentar traer el curso: " + nombre);
            System.out.println(e);
        }
        return null;
    }
}
