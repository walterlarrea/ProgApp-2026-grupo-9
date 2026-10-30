<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Registro de Usuario</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .tipo-selector {
            display: flex;
            gap: 1.5rem;
            margin-top: 0.5rem;
            margin-bottom: 1rem;
        }
        .tipo-option {
            display: flex;
            align-items: center;
            gap: 0.5rem;
            cursor: pointer;
            font-weight: 500;
        }
    </style>
</head>
<body data-context-path="${pageContext.request.contextPath}">
    <!-- cabezal fijo -->
    <%@ include file="../webMiscelanea/header.jsp" %>
    
    <div class="layout-container">
        <!-- Sidebar / Menú Lateral -->
        <%@ include file="../webMiscelanea/sidebar.jsp" %>
        
        <main class="main-content">
            <section class="hero-banner">
                <div class="hero-contenido">
                    <h1>Registrarse en edEXT</h1>
                    <p>Crea tu cuenta de Estudiante o Docente para acceder a los cursos y ediciones</p>
                </div>
            </section>

            <c:if test="${not empty error}">
                <p class="error">
                    <c:out value="${error}"/>
                </p>
            </c:if>

            <div class="form-container">
                <form action="${pageContext.request.contextPath}/alta-usuario" method="post" enctype="multipart/form-data">
                    <h2>Datos de Registro</h2>

                    <div class="dato-space2">
                        <label>Tipo de Usuario</label>
                        <div class="tipo-selector">
                            <label class="tipo-option">
                                <input type="radio" name="tipo" value="Estudiante" ${empty param.tipo || param.tipo eq 'Estudiante' ? 'checked' : ''} onchange="toggleInstituto(this.value)">
                                Estudiante
                            </label>
                            <label class="tipo-option">
                                <input type="radio" name="tipo" value="Docente" ${param.tipo eq 'Docente' ? 'checked' : ''} onchange="toggleInstituto(this.value)">
                                Docente
                            </label>
                        </div>
                    </div>

                    <div class="dato-space2" id="div-instituto" style="${param.tipo eq 'Docente' ? 'display: block;' : 'display: none;'}">
                        <label for="instituto">Instituto</label>
                        <select id="instituto" name="instituto">
                            <option value="">Seleccione un instituto</option>
                            <c:forEach var="inst" items="${institutos}">
                                <option value="${inst.nombreI}" ${param.instituto eq inst.nombreI ? 'selected' : ''}>
                                    <c:out value="${inst.nombreI}"/>
                                </option>
                            </c:forEach>
                        </select>
                    </div>

                    <div class="dato-space2">
                        <label for="nickname">Nickname</label>
                        <input type="text" id="nickname" name="nickname" value="${param.nickname}" required placeholder="Ej: jdoe">
                    </div>

                    <div class="dato-space2">
                        <label for="nombre">Nombre</label>
                        <input type="text" id="nombre" name="nombre" value="${param.nombre}" required placeholder="Ej: Juan">
                    </div>

                    <div class="dato-space2">
                        <label for="apellido">Apellido</label>
                        <input type="text" id="apellido" name="apellido" value="${param.apellido}" required placeholder="Ej: Pérez">
                    </div>

                    <div class="dato-space2">
                        <label for="email">Correo Electrónico</label>
                        <input type="email" id="email" name="email" value="${param.email}" required placeholder="Ej: juan.perez@correo.com">
                    </div>

                    <div class="dato-space2">
                        <label for="fechaNac">Fecha de Nacimiento</label>
                        <input type="date" id="fechaNac" name="fechaNac" required>
                    </div>

                    <div class="dato-space2">
                        <label for="password">Contraseña</label>
                        <input type="password" id="password" name="password" required placeholder="Ingresa tu contraseña">
                    </div>

                    <div class="dato-space2">
                        <label for="confirmPassword">Confirmar Contraseña</label>
                        <input type="password" id="confirmPassword" name="confirmPassword" required placeholder="Repite tu contraseña">
                    </div>

                    <div class="dato-space2">
                        <label for="imagen">Foto de Perfil</label>
                        <input type="file" id="imagen" name="imagen" accept="image/*">
                    </div>

                    <div class="btn-der" style="margin-top: 1.5rem; display: flex; gap: 1rem; justify-content: flex-end;">
                        <a href="${pageContext.request.contextPath}/login" class="btn btn-outline">¿Ya tienes cuenta? Iniciar Sesión</a>
                        <button type="submit" class="btn btn-primary">Registrarse</button>
                    </div>
                </form>
            </div>
        </main>
    </div>

    <script>
        function toggleInstituto(tipo) {
            var divInst = document.getElementById('div-instituto');
            var selectInst = document.getElementById('instituto');
            if (tipo === 'Docente') {
                divInst.style.display = 'block';
                selectInst.required = true;
            } else {
                divInst.style.display = 'none';
                selectInst.required = false;
                selectInst.value = '';
            }
        }
    </script>
</body>
</html>
