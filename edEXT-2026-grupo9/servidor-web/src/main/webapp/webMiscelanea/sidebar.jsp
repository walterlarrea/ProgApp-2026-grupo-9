<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!-- Barra lateral fija (sidebar) -->
        <aside class="sidebar">
            <div class="seccion">
                <div class="nav-list">
                    <a href="${pageContext.request.contextPath}/perfil"><h3>Mi perfil</h3></a>
                </div>
            </div>
            <div class="seccion">
                <h3>Inscripciones (si es est)</h3>
                <ul class="nav-list">
                    <li><a href="${pageContext.request.contextPath}/edicionCurso?accion=insc">Inscribirme</a></li>
                    <li><a href="${pageContext.request.contextPath}/edicionCurso?accion=resultado">Ver resultados</a></li>
                </ul>
            </div>
            <div class="seccion">
                <h3>Cursos (si es doc)</h3>
                <ul class="nav-list">
                    <li><a href="${pageContext.request.contextPath}/curso?accion=alta">Alta curso</a></li>
                    <li><a href="${pageContext.request.contextPath}/edicionCurso?accion=alta">Alta edición</a></li>
                    <li><a href="${pageContext.request.contextPath}/programas">Alta programa</a></li>
                </ul>
            </div>
            <div class="seccion">
            <h3>Institutos</h3>
            <ul class="nav-list">
                <c:choose>
                    <c:when test="${not empty institutos}">
                        <c:forEach var="inst" items="${institutos}">
                            <li>
                                <a class="corte-texto" href="${pageContext.request.contextPath}/instituto?nombre=${inst.nombreI()}">
                                    <c:out value="${inst.nombreI()}"/>
                                </a>
                            </li>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <li class="empty-hint">Sin institutos disponibles</li>
                    </c:otherwise>
                </c:choose>
            </ul>
            </div>
            <div class="seccion">
            <h3>Cursos</h3>
                <div class="nav-list">
                    <a href="<%= request.getContextPath() %>/home">Ver todos los cursos</a>
                </div>
            </div>
                <div class="salir">
                    <div class="nav-list">
                        <a href="${pageContext.request.contextPath}/logout">Salir</a>
                    </div>
                </div>
            <!-- <div class="status-box">
                <small>Estado:</small>
                <span class="badge badge-success"><c:out value="${estadoDb}"/></span>
            </div> -->
        </aside>

