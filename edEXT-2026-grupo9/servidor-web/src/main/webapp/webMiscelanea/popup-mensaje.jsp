<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<c:if test="${not empty sessionScope.mensajeExito}">
    <div id="popupExito"
         class="popup-overlay"
         role="dialog"
         aria-modal="true"
         aria-labelledby="tituloPopup"
         aria-describedby="mensajePopup">

        <div class="popup-contenido">
            <div class="popup-icono">Registrado</div>

            <h2 id="tituloPopup">¡Eres un exitoso!</h2>

            <p id="mensajePopup">
                <c:out value="${sessionScope.mensajeExito}"/>
            </p>

            <button type="button"
                    class="btn btn-primary"
                    onclick="cerrarPopup()">
                Aceptar
            </button>
        </div>
    </div>
</c:if>

<c:remove var="mensajeExito" scope="session"/>

<script src="${pageContext.request.contextPath}/js/cerrarPopup.js"></script>

