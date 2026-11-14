<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!-- Barra lateral fija (sidebar) -->
        <aside class="sidebar">
            <c:if test="${not empty sessionScope.usuarioLogueado}">
            <div class="seccion">
                <div class="nav-list">
                    <a href="${pageContext.request.contextPath}/perfil"><h3>Mi perfil</h3></a>
                </div>
            </div>
            </c:if> 
            
            <c:if test="${sessionScope.usuarioLogueado.tipo == 'Estudiante'}">
            <div class="seccion">
                <h3>Cursos</h3>
                <ul class="nav-list">
                    <li><a href="${pageContext.request.contextPath}/misCursos">Mis cursos</a></li>
                    <li><a href="${pageContext.request.contextPath}/evaluacionEstudiante">Ver evaluaciones</a></li>
                    <li><a href="${pageContext.request.contextPath}/actaEstudiante">Acta</a></li>
                </ul>
            </div>
            <div class="seccion">
                <h3>Inscripciones</h3>
                <ul class="nav-list">
                    <li><a href="${pageContext.request.contextPath}/inscripcion?accion=insc">Inscribirme</a></li>
                    <li><a href="${pageContext.request.contextPath}/inscripcion">Ver resultados</a></li>
                </ul>
            </div>
            </c:if>    
            
            <c:if test="${sessionScope.usuarioLogueado.tipo == 'Docente'}">   
            <div class="seccion"> 
                <h3>Cursos</h3>
                <ul class="nav-list">
                    <li><a href="${pageContext.request.contextPath}/curso?accion=alta">Alta curso</a></li>
                    <li><a href="${pageContext.request.contextPath}/edicionCurso?accion=alta">Alta edición</a></li>
                    <!-- La opción de abajo queda deshabilitada ya que no es un requerimiento actualmente -->
                    <!-- <li><a href="${pageContext.request.contextPath}/programas">Alta programa</a></li> -->
                    <li><a href="${pageContext.request.contextPath}/inscriptosDocente">Ver inscriptos</a></li>
                    <li><a href="${pageContext.request.contextPath}/inscripcionesDocente">Ver inscripciones pendientes</a></li>
                    <li><a href="${pageContext.request.contextPath}/evaluacionDocente">Ingresar evaluaciones</a></li>
                    <li><a href="${pageContext.request.contextPath}/verEvaluacionesDocente">Ver evaluaciones</a></li>
                </ul>
            </div>
            </c:if>
                
            <div class="seccion">
            <h3>Institutos</h3>
            <ul class="nav-list" id="sidebar-institutos" data-context-path="${pageContext.request.contextPath}">
                <li class="empty-hint">Cargando institutos...</li>
            </ul>
            <script src="${pageContext.request.contextPath}/js/sidebarInstitutos.js"></script>
            </div>
            
            <div class="seccion">
            <h3>Todos</h3>
                <div class="nav-list">
                    <a href="<%= request.getContextPath() %>/home">Ver todos los cursos</a>
                </div>
            </div>
        </aside>
