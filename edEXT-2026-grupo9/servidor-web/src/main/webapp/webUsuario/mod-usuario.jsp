<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Modificar Perfil - edEXT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body>

    <!-- Cabezal Fijo -->
    <jsp:include page="/webMiscelanea/header.jsp" />

    <div class="layout-container">
        <!-- Sidebar -->
        <jsp:include page="/webMiscelanea/sidebar.jsp" />

        <!-- Contenido Principal -->
        <main class="main-content">
            <div class="card shadow-sm border-0 rounded-3 p-4">
                <h3 class="mb-4 text-primary">
                    <i class="fas fa-user-edit me-2"></i>Modificar Mis Datos
                </h3>

                <c:if test="${not empty error}">
                    <div class="alert alert-danger alert-dismissible fade show" role="alert">
                        <i class="fas fa-exclamation-circle me-2"></i><c:out value="${error}"/>
                        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                    </div>
                </c:if>

                <form action="${pageContext.request.contextPath}/modificarUsuario" method="POST">
                    <div class="row g-3">
                        <!-- Campos No Editables -->
                        <div class="col-md-6">
                            <label class="form-label font-weight-bold text-muted">Nickname (No editable)</label>
                            <input type="text" class="form-control bg-light" value="${usuario.nickname}" readonly disabled>
                        </div>

                        <div class="col-md-6">
                            <label class="form-label font-weight-bold text-muted">Correo Electrónico (No editable)</label>
                            <input type="email" class="form-control bg-light" value="${usuario.email}" readonly disabled>
                        </div>

                        <!-- Campos Editables -->
                        <div class="col-md-6">
                            <label for="nombre" class="form-label font-weight-bold">Nombre</label>
                            <input type="text" class="form-control" id="nombre" name="nombre" value="${usuario.nombre}" required>
                        </div>

                        <div class="col-md-6">
                            <label for="apellido" class="form-label font-weight-bold">Apellido</label>
                            <input type="text" class="form-control" id="apellido" name="apellido" value="${usuario.apellido}" required>
                        </div>

                        <div class="col-md-6">
                            <label for="fechaNac" class="form-label font-weight-bold">Fecha de Nacimiento</label>
                            <input type="date" class="form-control" id="fechaNac" name="fechaNac" value="${usuario.fechaNac}">
                        </div>

                        <div class="col-md-6">
                            <label for="imagen" class="form-label font-weight-bold">Ruta o URL de Imagen de Perfil</label>
                            <input type="text" class="form-control" id="imagen" name="imagen" value="${usuario.imagen}" placeholder="Ej: https://sitio.com/foto.jpg o C:\Ruta\Foto.jpg">
                        </div>

                        <!-- Sección de Cambio de Contraseña -->
                        <div class="col-12 mt-4">
                            <hr>
                            <h5 class="text-secondary mb-3"><i class="fas fa-key me-2"></i>Cambiar Contraseña (Opcional)</h5>
                        </div>

                        <div class="col-md-4">
                            <label for="passActual" class="form-label font-weight-bold">Contraseña Actual</label>
                            <input type="password" class="form-control" id="passActual" name="passActual" placeholder="Ingresa tu contraseña actual">
                            <div class="form-text">Requerida sólo si vas a cambiar tu contraseña.</div>
                        </div>

                        <div class="col-md-4">
                            <label for="passNueva" class="form-label font-weight-bold">Nueva Contraseña</label>
                            <input type="password" class="form-control" id="passNueva" name="passNueva" placeholder="Nueva contraseña">
                        </div>

                        <div class="col-md-4">
                            <label for="passNuevaConfirm" class="form-label font-weight-bold">Confirmar Nueva Contraseña</label>
                            <input type="password" class="form-control" id="passNuevaConfirm" name="passNuevaConfirm" placeholder="Repite la nueva contraseña">
                        </div>

                        <!-- Botones de Acción -->
                        <div class="col-12 d-flex gap-2 mt-4">
                            <button type="submit" class="btn btn-primary btn-lg">
                                <i class="fas fa-save me-2"></i>Guardar Cambios
                            </button>
                            <a href="${pageContext.request.contextPath}/perfil" class="btn btn-outline-secondary btn-lg">
                                Cancelar
                            </a>
                        </div>
                    </div>
                </form>
            </div>
        </main>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
