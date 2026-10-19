<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.grupo9.edext.grupo9.servidor_central.dominio.DataUsuario" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>edEXT - Iniciar Sesión</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/styles.css">
    <style>
        .login-wrapper {
            display: flex;
            justify-content: center;
            align-items: center;
            min-height: calc(100vh - 180px);
            padding: 2rem 1rem;
        }
        .login-card {
            background: #ffffff;
            border: 1px solid var(--border-color);
            border-radius: 10px;
            padding: 2.5rem;
            max-width: 440px;
            width: 100%;
            box-shadow: 0 4px 12px rgba(0,0,0,0.06);
        }
        .login-title {
            text-align: center;
            color: var(--primary);
            font-size: 1.75rem;
            font-weight: 700;
            margin-bottom: 1.5rem;
        }
        .login-form .dato-space2 {
            margin-bottom: 1.25rem;
        }
        .login-form label {
            display: block;
            font-size: 0.9rem;
            font-weight: 600;
            color: var(--text-primary);
            margin-bottom: 0.4rem;
        }
        .login-form input[type="text"],
        .login-form input[type="password"] {
            width: 100%;
            padding: 0.65rem 0.9rem;
            border: 1px solid var(--border-color);
            border-radius: 6px;
            font-size: 0.95rem;
            outline: none;
            box-sizing: border-box;
        }
        .login-form input:focus {
            border-color: var(--primary);
        }
        .login-actions {
            margin-top: 1.5rem;
            display: flex;
            flex-direction: column;
            gap: 0.75rem;
        }
        .login-actions .btn {
            text-align: center;
            padding: 0.75rem;
            font-size: 1rem;
        }
    </style>
</head>
<body data-context-path="${pageContext.request.contextPath}">

    <%-- Incluir Header --%>
    <jsp:include page="/webMiscelanea/header.jsp" />

    <div class="login-wrapper">
        <div class="login-card">
            <h2 class="login-title">Iniciar Sesión</h2>

            <% 
                String error = (String) request.getAttribute("error");
                if (error != null) { 
            %>
                <p class="error"><%= error %></p>
            <% } %>

            <% 
                String mensaje = request.getParameter("mensaje");
                if (mensaje != null) { 
            %>
                <div style="background-color: #dcfce7; color: #166534; padding: 0.75rem 1rem; border-radius: 6px; margin-bottom: 1.25rem; border: 1px solid #bbf7d0; font-size: 0.9rem;">
                    <%= mensaje %>
                </div>
            <% } %>

            <form action="<%= request.getContextPath() %>/login" method="POST" class="login-form">
                <div class="dato-space2">
                    <label for="nicknameOEmail">Nickname o Correo Electrónico</label>
                    <input type="text" id="nicknameOEmail" name="nicknameOEmail" placeholder="Ingresa tu nickname o email" required autofocus>
                </div>

                <div class="dato-space2">
                    <label for="password">Contraseña</label>
                    <input type="password" id="password" name="password" placeholder="Ingresa tu contraseña" required>
                </div>

                <div class="login-actions">
                    <button type="submit" class="btn btn-primary">Ingresar</button>
                    <a href="<%= request.getContextPath() %>/alta-usuario" class="btn btn-outline">¿No tienes cuenta? Regístrate</a>
                    <a href="<%= request.getContextPath() %>/home" class="btn btn-outline" style="border: none; color: var(--text-secondary);">
                        Volver al inicio
                    </a>
                </div>
            </form>
        </div>
    </div>

</body>
</html>
