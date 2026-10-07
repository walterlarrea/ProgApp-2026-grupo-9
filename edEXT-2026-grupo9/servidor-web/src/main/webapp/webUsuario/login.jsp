<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <title>Iniciar Sesión - edEXT</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
    <link href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/6.0.0/css/all.min.css" rel="stylesheet">
</head>
<body class="bg-light">

    <%-- Incluir Header --%>
    <jsp:include page="/webMiscelanea/header.jsp" />

    <div class="container my-5">
        <div class="row justify-content-center">
            <div class="col-md-5">
                <div class="card shadow-sm border-0 rounded-3">
                    <div class="card-body p-4">
                        <h3 class="text-center mb-4 text-primary">
                            <i class="fas fa-sign-in-alt me-2"></i>Iniciar Sesión
                        </h3>

                        <% 
                            String error = (String) request.getAttribute("error");
                            if (error != null) { 
                        %>
                            <div class="alert alert-danger alert-dismissible fade show" role="alert">
                                <i class="fas fa-exclamation-circle me-2"></i><%= error %>
                                <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
                            </div>
                        <% } %>

                        <form action="<%= request.getContextPath() %>/login" method="POST">
                            <div class="mb-3">
                                <label for="nicknameOEmail" class="form-label font-weight-bold">Nickname o Correo Electrónico</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-user"></i></span>
                                    <input type="text" class="form-control" id="nicknameOEmail" name="nicknameOEmail" placeholder="Ingresa tu nickname o email" required autofocus>
                                </div>
                            </div>

                            <div class="mb-4">
                                <label for="password" class="form-label font-weight-bold">Contraseña</label>
                                <div class="input-group">
                                    <span class="input-group-text"><i class="fas fa-lock"></i></span>
                                    <input type="password" class="form-control" id="password" name="password" placeholder="Ingresa tu contraseña" required>
                                </div>
                            </div>

                            <div class="d-grid gap-2">
                                <button type="submit" class="btn btn-primary btn-lg">Ingresar</button>
                                <a href="<%= request.getContextPath() %>/home" class="btn btn-outline-secondary mt-2">
                                    <i class="fas fa-arrow-left me-2"></i>Volver a Inicio
                                </a>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </div>
    </div>

    <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>
</body>
</html>
