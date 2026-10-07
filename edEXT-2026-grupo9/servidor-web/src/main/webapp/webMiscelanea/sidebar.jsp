<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!-- Barra lateral fija (sidebar) -->
        <aside class="sidebar">
            <h3>Institutos</h3>
            <ol class="nav-list">
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
            </ol>

            <h3 style="margin-top: 1.5rem;">Explorar</h3>
            <ul class="nav-list">
                <li><a href="${pageContext.request.contextPath}/cursos">Todos los Cursos</a></li>
                <li><a href="${pageContext.request.contextPath}/programas">Programas de Formación</a></li>
            </ul>

            <div class="status-box">
                <small>Estado:</small>
                <span class="badge badge-success"><c:out value="${estadoDb}"/></span>
            </div>
        </aside>

