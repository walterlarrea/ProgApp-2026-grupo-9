<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Modificar Perfil</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .form-grid {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
            gap: 1.25rem;
        }
        .full-width {
            grid-column: 1 / -1;
        }
        .form-grid input[type="text"],
        .form-grid input[type="email"],
        .form-grid input[type="date"],
        .form-grid input[type="password"] {
            width: 100%;
            padding: 0.65rem 0.9rem;
            border: 1px solid var(--border-color);
            border-radius: 6px;
            font-size: 0.95rem;
            outline: none;
            box-sizing: border-box;
        }
        .form-grid input:focus {
            border-color: var(--primary);
        }
        .form-grid input:disabled {
            background-color: #f1f5f9;
            color: #64748b;
            cursor: not-allowed;
        }
    </style>
</head>
<body data-context-path="${pageContext.request.contextPath}">

    <!-- Cabezal Fijo -->
    <jsp:include page="/webMiscelanea/header.jsp" />

    <div class="layout-container">
        <!-- Sidebar -->
        <jsp:include page="/webMiscelanea/sidebar.jsp" />

        <!-- Contenido Principal -->
        <main class="main-content">
            <div class="profile-container">
                <h2 style="color: var(--primary); margin-bottom: 1.5rem;">Modificar Mis Datos</h2>

                <c:if test="${not empty error}">
                    <p class="error"><c:out value="${error}"/></p>
                </c:if>

                <form action="${pageContext.request.contextPath}/modificarUsuario" method="POST">
                    <div class="form-grid">
                        <!-- Campos No Editables -->
                        <div>
                            <label class="info-label">Nickname (No editable)</label>
                            <input type="text" value="${usuario.nickname}" readonly disabled>
                        </div>

                        <div>
                            <label class="info-label">Correo Electrónico (No editable)</label>
                            <input type="email" value="${usuario.email}" readonly disabled>
                        </div>

                        <!-- Campos Editables -->
                        <div>
                            <label class="info-label" for="nombre">Nombre</label>
                            <input type="text" id="nombre" name="nombre" value="${usuario.nombre}" required>
                        </div>

                        <div>
                            <label class="info-label" for="apellido">Apellido</label>
                            <input type="text" id="apellido" name="apellido" value="${usuario.apellido}" required>
                        </div>

                        <div>
                            <label class="info-label" for="fechaNac">Fecha de Nacimiento</label>
                            <input type="date" id="fechaNac" name="fechaNac" value="${usuario.fechaNac}">
                        </div>

                        <div>
                            <label class="info-label" for="imagen">Ruta o URL de Imagen de Perfil</label>
                            <input type="text" id="imagen" name="imagen" value="${usuario.imagen}" placeholder="Ej: https://sitio.com/foto.jpg o C:\Ruta\Foto.jpg">
                        </div>

                        <!-- Sección de Cambio de Contraseña -->
                        <div class="full-width" style="margin-top: 1rem; border-top: 1px solid var(--border-color); padding-top: 1rem;">
                            <h3 style="color: var(--text-primary); font-size: 1.1rem; margin-bottom: 0.5rem;">Cambiar Contraseña (Opcional)</h3>
                            <p style="font-size: 0.85rem; color: var(--text-secondary); margin-bottom: 1rem;">Si deseas conservar tu contraseña actual, deja estos campos en blanco.</p>
                        </div>

                        <div>
                            <label class="info-label" for="passActual">Contraseña Actual</label>
                            <input type="password" id="passActual" name="passActual" placeholder="Ingresa tu contraseña actual">
                        </div>

                        <div>
                            <label class="info-label" for="passNueva">Nueva Contraseña</label>
                            <input type="password" id="passNueva" name="passNueva" placeholder="Nueva contraseña">
                        </div>

                        <div>
                            <label class="info-label" for="passNuevaConfirm">Confirmar Nueva Contraseña</label>
                            <input type="password" id="passNuevaConfirm" name="passNuevaConfirm" placeholder="Repite la nueva contraseña">
                        </div>

                        <!-- Botones de Acción -->
                        <div class="full-width" style="display: flex; gap: 1rem; margin-top: 1.5rem;">
                            <button type="submit" class="btn btn-primary">
                                Guardar Cambios
                            </button>
                            <a href="${pageContext.request.contextPath}/perfil" class="btn btn-outline">
                                Cancelar
                            </a>
                        </div>
                    </div>
                </form>
            </div>
        </main>
    </div>

</body>
</html>
