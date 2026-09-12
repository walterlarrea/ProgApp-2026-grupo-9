package com.grupo9.edext.grupo9.servidor_central.controller.edicion_de_curso;

import com.grupo9.edext.grupo9.mensajes.ErrorNoExiste;
import com.grupo9.edext.grupo9.mensajes.ErrorRepetidos;
import com.grupo9.edext.grupo9.servidor_central.controller.curso.Curso;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Docente;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.ManejadorEstudiantes;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.ManejadorDocente;
import com.grupo9.edext.grupo9.servidor_central.controller.usuario.Estudiante;
import com.grupo9.edext.grupo9.servidor_central.controller.instituto.Instituto;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEdicionCurso;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataInscEdicion;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataDocente;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataEstudiante;
import com.grupo9.edext.grupo9.servidor_central.controller.DtoMapper;
import com.grupo9.edext.grupo9.servidor_central.dominio.DataCurso;
import java.time.LocalDate;
import java.util.Set;
import java.util.HashSet;

public class EdicionCursoController implements IEdicionCurso {
    
    public EdicionCursoController(){}
    
    @Override
    public DataEdicionCurso guardarNuevaEdicionCurso(DataEdicionCurso nuevaEdicion){
        try{
            Set<InscEdicion> inscripciones = new HashSet<>();
            altaEdicionCurso(
                nuevaEdicion.getNombreEdi(),
                DtoMapper.toEntity(nuevaEdicion.getCursoAsoc()),
                nuevaEdicion.getFechaInicio(),
                nuevaEdicion.getFechaFin(),
                nuevaEdicion.getCupo(),
                inscripciones,
                DtoMapper.toEntity(nuevaEdicion.getDocentes()));
            return nuevaEdicion;
        }catch(ErrorRepetidos e) {
            System.out.println("[SERVIDOR] " + e.getMessage());
            return null;
        }
    }
    
    @Override
    public Docente[] traerDocentes(Instituto instituto) {
        return ManejadorDocente.getInstance().getDocentesPorInstituto(instituto);
    }
    
    @Override
    public void altaEdicionCurso(String nEdi, Curso cur, LocalDate fInicio, LocalDate fFin, Integer c, Set<InscEdicion> insc, Set<Docente> d) throws ErrorRepetidos{
        ManejadorEdiciones me = ManejadorEdiciones.getInstance();
        EdicionCurso ed = me.obtenerEdicion(nEdi);
        if(ed == null){
            ed = new EdicionCurso(nEdi, cur, fInicio,fFin, c, d, insc, LocalDate.now());
            me.addEdicion(ed);
        }else{
            throw new ErrorRepetidos("La Edición " + nEdi + " ya ha sido registrada. \n ¿Desea modificar los datos?");
        }
    }
    
    @Override
    public DataEdicionCurso consultarEdicionCurso(String nEdi) throws ErrorNoExiste{
       ManejadorEdiciones me = ManejadorEdiciones.getInstance();
    EdicionCurso ed = me.obtenerEdicion(nEdi);
    
    if (ed != null) {
        // FORZAMOS A HIBERNATE A CARGAR LAS PREVIAS DEL CURSO ASOCIADO
        if (ed.getCursoAsoc() != null) {
            org.hibernate.Hibernate.initialize(ed.getCursoAsoc().getPrevias());
        }

        //para obtener los inscriptos
        Set<DataInscEdicion> datosInscriptos = new HashSet<>();
        for(InscEdicion inscriptos : ed.getInscripciones()){
            Estudiante estudiante = inscriptos.getEstudiante();
            DataEstudiante datosEst = new DataEstudiante(estudiante.getNickname(),estudiante.getNombre(),estudiante.getApellido(),estudiante.getEmail(), estudiante.getFechaNac(), null);
            DataInscEdicion datosInsc = new DataInscEdicion(inscriptos.getFechaInscE(), datosEst, inscriptos.getEdicion().getNombreEdi());
            datosInscriptos.add(datosInsc);
        }
            
            //docentes
            Set<DataDocente> datosDocentes = DtoMapper.toData(ed.getDocentes());
            return new DataEdicionCurso(ed.getNombreEdi(), DtoMapper.toData(ed.getCursoAsoc()), ed.getFechaInicio(), ed.getFechaFin(), ed.getCupo(), datosDocentes, datosInscriptos, ed.getFechaPub()); 
        }else{
            throw new ErrorNoExiste("La Edición " + nEdi + " no está registrada.");
        }
    }
    
    @Override
    public EdicionCurso[] traerEdiciones(Curso curso){
        return ManejadorEdiciones.getInstance().getEdiciones(curso);
    } 
    
    @Override
    public HashSet<DataEdicionCurso> traerEdiciones(DataCurso dataCurso){
        HashSet<DataEdicionCurso> ediciones = new HashSet<>();
        EdicionCurso[] edicionesList = ManejadorEdiciones.getInstance().getEdiciones(DtoMapper.toEntity(dataCurso));
        
        for (EdicionCurso edicion: edicionesList){
            ediciones.add(DtoMapper.toData(edicion));
        }
        
        return ediciones;
    } 
    
    @Override
    public void inscripcionEdicionCurso(LocalDate fInsc, String nickEstudiante, String nEdi) throws ErrorRepetidos, ErrorNoExiste {
        ManejadorEdiciones me = ManejadorEdiciones.getInstance();
        ManejadorEstudiantes mest = ManejadorEstudiantes.getInstance();
        Estudiante est = mest.obtenerEstudiante(nickEstudiante);
        EdicionCurso ed = me.obtenerEdicion(nEdi);
        
        if (est == null) {
            throw new ErrorNoExiste("El estudiante " + nickEstudiante + " no existe.");
        }
        if (ed == null) {
            throw new ErrorNoExiste("La Edición " + nEdi + " no existe.");
        }

        // 1. Chequeo en Base de Datos
        jakarta.persistence.EntityManager em = com.grupo9.edext.grupo9.miscelanea.UtensiliosJPA.getEntityManagerFactory().createEntityManager();
        try {
            Long count = em.createQuery("SELECT COUNT(i) FROM InscEdicion i WHERE i.estudiante.nickname = :nick AND i.edicion.nombreEdi = :nEdi", Long.class)
                .setParameter("nick", nickEstudiante)
                .setParameter("nEdi", nEdi)
                .getSingleResult();
            if (count > 0) {
                throw new ErrorRepetidos("El estudiante " + nickEstudiante + " ya está inscripto.");
            }
        } finally {
            em.close();
        }

        // 2. Chequeo en Memoria (en el Estudiante y en la EdicionCurso)
        if (est.getInscripciones() != null) {
            for (InscEdicion insc : est.getInscripciones()) {
                if (insc.getEdicion() != null && nEdi.equalsIgnoreCase(insc.getEdicion().getNombreEdi())) {
                    throw new ErrorRepetidos("El estudiante " + nickEstudiante + " ya está inscripto.");
                }
            }
        }
        if (ed.getInscripciones() != null) {
            for (InscEdicion insc : ed.getInscripciones()) {
                if (insc.getEstudiante() != null && nickEstudiante.equalsIgnoreCase(insc.getEstudiante().getNickname())) {
                    throw new ErrorRepetidos("El estudiante " + nickEstudiante + " ya está inscripto.");
                }
            }
        }

        InscEdicion inscripcion = new InscEdicion(fInsc, est, ed);
        if (est.getInscripciones() != null) {
            est.getInscripciones().add(inscripcion);
        }
        if (ed.getInscripciones() != null) {
            ed.getInscripciones().add(inscripcion);
        }
        me.addInscripcion(inscripcion);
    }
    
    @Override
    public Estudiante[] traerEstudiantes(){
        return ManejadorEstudiantes.getInstance().getEstudiante();
    }
    
    @Override
    public void inscribirNuevoEstudiante(LocalDate fechaInsc, String nickname, String nombreEdi) throws ErrorRepetidos, ErrorNoExiste {
        inscripcionEdicionCurso(fechaInsc,nickname,nombreEdi);
    }
}
