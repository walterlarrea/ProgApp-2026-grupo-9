<%@ page contentType="text/html;charset=UTF-8" language="java" %> <%@ taglib
prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
  <head>
    <meta charset="UTF-8" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0" />
    <title>Programas de formación - edEXT</title>
    <link
      rel="stylesheet"
      href="${pageContext.request.contextPath}/css/styles.css" />
  </head>
  <body>
    <jsp:include page="/webMiscelanea/header.jsp" />
    <div class="layout-container">
      <jsp:include page="/webMiscelanea/sidebar.jsp" />
      <main class="main-content">
        <section class="hero-banner">
          <h1>Programas de formación</h1>
          <p>Explora los programas y sus cursos asociados.</p>
          <!-- La opción de abajo queda deshabilitada ya que no es un requerimiento actualmente -->
          <!-- <a
            class="btn btn-primary"
            href="${pageContext.request.contextPath}/programas?accion=alta"
            >Crear programa</a
          > -->
        </section>
        <c:choose>
          <c:when test="${not empty programas}">
            <div class="ediciones-list">
              <c:forEach var="programa" items="${programas}">
                <article class="edicion-card">
                  <h2>
                    <a
                      href="${pageContext.request.contextPath}/programa?nombre=${programa.nombre()}"
                      ><c:out value="${programa.nombre()}"
                    /></a>
                  </h2>
                  <p><c:out value="${programa.descripcion()}" /></p>
                  <p>
                    <c:out value="${programa.fechaInicio()}" /> -
                    <c:out value="${programa.fechaFin()}" />
                  </p>
                </article>
              </c:forEach>
            </div>
          </c:when>
          <c:otherwise><p>No hay programas disponibles.</p></c:otherwise>
        </c:choose>
      </main>
    </div>
  </body>
</html>
