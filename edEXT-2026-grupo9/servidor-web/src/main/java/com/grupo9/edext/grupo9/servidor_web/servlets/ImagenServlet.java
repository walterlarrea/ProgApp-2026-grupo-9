package com.grupo9.edext.grupo9.servidor_web.servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

@WebServlet("/imagen")
public class ImagenServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String path = request.getParameter("path");

        if (path == null || path.trim().isEmpty()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        path = path.trim();

        if (path.startsWith("http://") || path.startsWith("https://")) {
            response.sendRedirect(path);
            return;
        }

        // Probar si es un archivo local en disco
        File file = new File(path);
        InputStream in = null;

        if (file.exists() && file.isFile()) {
            in = new FileInputStream(file);
        } else {
            // Probar recurso dentro del contexto web (webapp)
            String resourcePath = path.startsWith("/") ? path : "/" + path;
            in = getServletContext().getResourceAsStream(resourcePath);
        }

        if (in == null) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        String mimeType = getServletContext().getMimeType(path);
        if (mimeType == null) {
            if (path.toLowerCase().endsWith(".png")) {
                mimeType = "image/png";
            } else if (path.toLowerCase().endsWith(".gif")) {
                mimeType = "image/gif";
            } else if (path.toLowerCase().endsWith(".webp")) {
                mimeType = "image/webp";
            } else {
                mimeType = "image/jpeg";
            }
        }

        response.setContentType(mimeType);

        try (OutputStream out = response.getOutputStream()) {
            byte[] buffer = new byte[8192];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        } finally {
            in.close();
        }
    }
}
