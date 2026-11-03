<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<%--
  Popup parametrizable para mostrar avisos y mensajes en pantalla.
  Parámetros aceptados (vía <jsp:param>, atributos de request, sesión o llamada JS):
    - titulo: Título del popup (default: '¡Aviso!')
    - mensaje: Contenido del mensaje
    - icono: Ícono o texto dentro del círculo superior (default: '✓')
    - visible: 'true' o 'false' para controlar la visibilidad inicial en la carga
--%>
<c:set var="popupTitulo" value="${not empty param.titulo ? param.titulo : (not empty requestScope.tituloPopup ? requestScope.tituloPopup : (not empty sessionScope.tituloPopup ? sessionScope.tituloPopup : '¡Aviso!'))}" />
<c:set var="popupMensaje" value="${not empty param.mensaje ? param.mensaje : (not empty requestScope.mensajePopup ? requestScope.mensajePopup : (not empty sessionScope.mensajeExito ? sessionScope.mensajeExito : ''))}" />
<c:set var="popupIcono" value="${not empty param.icono ? param.icono : '✓'}" />
<c:set var="popupVisible" value="${not empty param.visible ? param.visible : (not empty popupMensaje ? 'true' : 'false')}" />

<div id="popupExito"
     class="popup-overlay"
     role="dialog"
     aria-modal="true"
     aria-labelledby="tituloPopup"
     aria-describedby="mensajePopup"
     style="${popupVisible eq 'true' ? 'display: flex;' : 'display: none;'}">

    <div class="popup-contenido">
        <div class="popup-icono" id="popupIcono"><c:out value="${popupIcono}"/></div>

        <h2 id="tituloPopup"><c:out value="${popupTitulo}"/></h2>

        <p id="mensajePopup">
            <c:out value="${popupMensaje}"/>
        </p>

        <button type="button"
                id="btnCerrarPopup"
                class="btn btn-primary"
                onclick="cerrarPopup()">
            Aceptar
        </button>
    </div>
</div>

<c:remove var="mensajeExito" scope="session"/>
<c:remove var="tituloPopup" scope="session"/>

<script src="${pageContext.request.contextPath}/js/cerrarPopup.js"></script>
