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
            .file-chooser-container {
                width: 100%;
            }
            .file-chooser-box {
                display: flex;
                align-items: center;
                gap: 0.75rem;
                padding: 0.5rem 0.75rem;
                border: 1px solid var(--border-color);
                border-radius: 6px;
                background-color: #ffffff;
                cursor: pointer;
                transition: border-color 0.2s, box-shadow 0.2s;
            }
            .file-chooser-box:hover {
                border-color: var(--primary);
                box-shadow: 0 0 0 2px rgba(37, 99, 235, 0.1);
            }
            .file-chooser-icon {
                font-size: 1.25rem;
                line-height: 1;
                display: flex;
                align-items: center;
                justify-content: center;
            }
            .file-chooser-info {
                display: flex;
                align-items: center;
                gap: 0.6rem;
                overflow: hidden;
                width: 100%;
            }
            .file-chooser-btn {
                background-color: #f1f5f9;
                color: var(--text-primary);
                font-size: 0.85rem;
                font-weight: 500;
                padding: 0.35rem 0.75rem;
                border-radius: 4px;
                border: 1px solid var(--border-color);
                white-space: nowrap;
                transition: background-color 0.2s;
            }
            .file-chooser-box:hover .file-chooser-btn {
                background-color: #e2e8f0;
            }
            .file-name-text {
                font-size: 0.88rem;
                color: var(--text-secondary);
                white-space: nowrap;
                overflow: hidden;
                text-overflow: ellipsis;
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

                    <form action="${pageContext.request.contextPath}/modificarUsuario" method="POST" enctype="multipart/form-data">
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
                                <label class="info-label" for="imagenArchivo">Imagen de Perfil</label>
                                <div class="file-chooser-container">
                                    <div class="file-chooser-box" onclick="document.getElementById('imagenArchivo').click()">
                                        <div class="file-chooser-icon">📁</div>
                                        <div class="file-chooser-info">
                                            <span class="file-chooser-btn">Seleccionar imagen</span>
                                            <span id="file-name-display" class="file-name-text">
                                                <c:choose>
                                                    <c:when test="${not empty usuario.imagen}">
                                                        Imagen actual asignada
                                                    </c:when>
                                                    <c:otherwise>
                                                        Ningún archivo seleccionado
                                                    </c:otherwise>
                                                </c:choose>
                                            </span>
                                        </div>
                                    </div>
                                    <input type="file" id="imagenArchivo" name="imagenArchivo" accept="image/*" style="display: none;" onchange="actualizarNombreArchivo(this)">
                                    <input type="hidden" id="imagen" name="imagen" value="${usuario.imagen}">
                                </div>
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

        <script>
            function actualizarNombreArchivo(input) {
                var display = document.getElementById('file-name-display');
                if (input.files && input.files.length > 0) {
                    display.textContent = input.files[0].name;
                    display.style.color = 'var(--text-primary)';
                    display.style.fontWeight = '500';
                }
            }
        </script>
    </body>
</html>
