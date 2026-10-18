<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title><c:out value="${usuario.nickname}"/> - edEXT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
</head>
<body>
    <jsp:include page="/webMiscelanea/header.jsp" />
    <div class="layout-container">
        <jsp:include page="/webMiscelanea/sidebar.jsp" />
        <main class="main-content">
            <c:if test="${not empty param.mensaje}"><p class="success"><c:out value="${param.mensaje}"/></p></c:if>
            <c:if test="${not empty param.error}"><p class="error"><c:out value="${param.error}"/></p></c:if>
            <section class="profile-container">
                <div class="profile-header">
                    <div class="profile-user-details">
                        <c:choose>
                            <c:when test="${not empty usuario.imagen}">
                                <c:choose>
                                    <c:when test="${usuario.imagen.startsWith('http://') || usuario.imagen.startsWith('https://')}"><c:set var="imgSrc" value="${usuario.imagen}"/></c:when>
                                    <c:otherwise><c:set var="imgSrc" value="${pageContext.request.contextPath}/imagen?path=${usuario.imagen}"/></c:otherwise>
                                </c:choose>
                                <img class="profile-avatar" src="${imgSrc}" alt="Foto de perfil" onerror="this.style.display='none'">
                            </c:when>
                            <c:otherwise><div class="profile-avatar-placeholder"><i class="fas fa-user-slash"></i></div></c:otherwise>
                        </c:choose>
                        <div>
                            <h1><c:out value="${usuario.nombre}"/> <c:out value="${usuario.apellido}"/></h1>
                            <p>@<c:out value="${usuario.nickname}"/></p>
                            <span class="badge badge-success"><c:out value="${usuario.tipo}"/></span>
                        </div>
                    </div>
                    <c:choose>
                        <c:when test="${propio}"><a class="btn btn-primary" href="${pageContext.request.contextPath}/perfil">Volver a mi perfil</a></c:when>
                        <c:when test="${not empty sessionScope.usuarioLogueado}">
                            <form action="${pageContext.request.contextPath}/usuario" method="post">
                                <input type="hidden" name="nickname" value="${usuario.nickname}">
                                <input type="hidden" name="accion" value="${siguiendo ? 'dejar' : 'seguir'}">
                                <button class="btn btn-primary" type="submit"><c:choose><c:when test="${siguiendo}">Dejar de seguir</c:when><c:otherwise>Seguir</c:otherwise></c:choose></button>
                            </form>
                        </c:when>
                        <c:otherwise><a class="btn btn-primary" href="${pageContext.request.contextPath}/login">Inicia sesión para seguir</a></c:otherwise>
                    </c:choose>
                </div>
                <div class="profile-info-grid">
                    <div class="info-item"><div class="info-label">Nickname</div><div class="info-value"><c:out value="${usuario.nickname}"/></div></div>
                    <div class="info-item"><div class="info-label">Correo</div><div class="info-value"><c:out value="${usuario.email}"/></div></div>
                    <div class="info-item"><div class="info-label">Fecha de nacimiento</div><div class="info-value"><c:out value="${usuario.fechaNac}"/></div></div>
                </div>
            </section>
        </main>
    </div>
</body>
</html>
