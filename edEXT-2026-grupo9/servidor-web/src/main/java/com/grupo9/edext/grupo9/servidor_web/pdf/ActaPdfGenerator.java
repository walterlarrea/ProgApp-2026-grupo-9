package com.grupo9.edext.grupo9.servidor_web.pdf;

import com.grupo9.edext.grupo9.servidor_web.web_service_client.edicion_de_curso.DataEdicionCursoDTO;
import com.grupo9.edext.grupo9.servidor_web.web_service_client.usuario.DataEstudianteDTO;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.OutputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Map;

//para generar el PDF del acta estudiantil usando iText 5
public final class ActaPdfGenerator {

    //mismos colores que en la parte web (styles.css)
    private static final BaseColor COLOR_PRIMARY = new BaseColor(0x25, 0x63, 0xEB);
    private static final BaseColor COLOR_TEXTO = new BaseColor(0x1E, 0x29, 0x3B);
    private static final BaseColor COLOR_BORDE = new BaseColor(0xCB, 0xD5, 0xE1);
    private static final BaseColor COLOR_FONDO_ENCABEZADO = new BaseColor(0xF1, 0xF5, 0xF9);

    private static final DateTimeFormatter FORMATO_FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private ActaPdfGenerator() {}

    public static void generar(DataEstudianteDTO estudiante,
                               Map<DataEdicionCursoDTO, Integer> acta,
                               OutputStream salida) throws DocumentException {
        Document documento = new Document(PageSize.A4, 40, 40, 40, 40);
        PdfWriter.getInstance(documento, salida);
        documento.open();

        documento.add(crearCabecera());
        documento.add(crearDatosEstudiante(estudiante));
        documento.add(crearTabla(acta));

        documento.close();
    }

    //título a la izquierda y el edEXT a la derecha, igual que .brand de la web.
    private static PdfPTable crearCabecera() throws DocumentException {
        PdfPTable cabecera = new PdfPTable(2);
        cabecera.setWidthPercentage(100);
        cabecera.setWidths(new float[]{2f, 1f});
        cabecera.setSpacingAfter(16f);

        Font fuenteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 22, COLOR_TEXTO);
        PdfPCell titulo = new PdfPCell(new Phrase("Acta estudiantil", fuenteTitulo));
        titulo.setBorder(Rectangle.NO_BORDER);
        titulo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cabecera.addCell(titulo);

        // 1.5rem = 24px = 18pt; letter-spacing -0.5px = -0.375pt
        Font fuenteLogo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, COLOR_TEXTO);
        Font fuenteLogoAcento = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, COLOR_PRIMARY);
        Chunk ed = new Chunk("ed", fuenteLogo);
        Chunk ext = new Chunk("EXT", fuenteLogoAcento);
        ed.setCharacterSpacing(-0.375f);
        ext.setCharacterSpacing(-0.375f);
        Paragraph logo = new Paragraph();
        logo.add(ed);
        logo.add(ext);
        logo.setAlignment(Element.ALIGN_RIGHT);

        PdfPCell celdaLogo = new PdfPCell();
        celdaLogo.addElement(logo);
        celdaLogo.setBorder(Rectangle.NO_BORDER);
        celdaLogo.setVerticalAlignment(Element.ALIGN_MIDDLE);
        cabecera.addCell(celdaLogo);
        return cabecera;
    }

    private static Paragraph crearDatosEstudiante(DataEstudianteDTO estudiante) {
        Font etiqueta = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_TEXTO);
        Font valor = FontFactory.getFont(FontFactory.HELVETICA, 10, COLOR_TEXTO);

        Paragraph datos = new Paragraph();
        datos.setSpacingAfter(16f);
        datos.setLeading(15f);
        agregarLinea(datos, "Estudiante: ", estudiante.getNombre() + " " + estudiante.getApellido(), etiqueta, valor);
        agregarLinea(datos, "Fecha de Nacimiento: ", formatearFecha(estudiante.getFechaNac()), etiqueta, valor);
        agregarLinea(datos, "Fecha de Expedición: ", LocalDate.now().format(FORMATO_FECHA), etiqueta, valor);
        return datos;
    }

    private static void agregarLinea(Paragraph destino, String titulo, String texto, Font etiqueta, Font valor) {
        destino.add(new Chunk(titulo, etiqueta));
        destino.add(new Chunk(texto + "\n", valor));
    }

    private static PdfPTable crearTabla(Map<DataEdicionCursoDTO, Integer> acta) throws DocumentException {
        PdfPTable tabla = new PdfPTable(6);
        tabla.setWidthPercentage(100);
        tabla.setWidths(new float[]{3f, 1.4f, 1.4f, 1.4f, 1.1f, 1.4f});
        tabla.setHeaderRows(1);

        String[] columnas = {"Edición", "Instituto", "Finalización", "Duración", "Créditos", "Evaluación"};
        Font fuenteEncabezado = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_TEXTO);
        for (String columna : columnas) {
            PdfPCell celda = new PdfPCell(new Phrase(columna, fuenteEncabezado));
            celda.setBackgroundColor(COLOR_FONDO_ENCABEZADO);
            celda.setBorderColor(COLOR_BORDE);
            celda.setPadding(7f);
            tabla.addCell(celda);
        }

        Font fuenteCelda = FontFactory.getFont(FontFactory.HELVETICA, 10, COLOR_TEXTO);
        Font fuenteNota = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, COLOR_TEXTO);

        for (Map.Entry<DataEdicionCursoDTO, Integer> fila : acta.entrySet()) {
            DataEdicionCursoDTO edicion = fila.getKey();
            boolean hayCurso = edicion.getCursoAsoc() != null;
            String instituto = hayCurso && edicion.getCursoAsoc().getInstituto() != null
                    ? edicion.getCursoAsoc().getInstituto().getNombreI() : "-";
            String duracion = hayCurso ? edicion.getCursoAsoc().getDuracion() + " semanas" : "-";
            String creditos = hayCurso ? String.valueOf(edicion.getCursoAsoc().getCantCred()) : "-";

            agregarCelda(tabla, edicion.getNombreEdi(), fuenteCelda);
            agregarCelda(tabla, instituto, fuenteCelda);
            agregarCelda(tabla, formatearFecha(edicion.getFechaFin()), fuenteCelda);
            agregarCelda(tabla, duracion, fuenteCelda);
            agregarCelda(tabla, creditos, fuenteCelda);
            agregarCelda(tabla, String.valueOf(fila.getValue()), fuenteNota);
        }

        if (acta.isEmpty()) {
            PdfPCell vacio = new PdfPCell(new Phrase("Aún no tienes ediciones evaluadas.", fuenteCelda));
            vacio.setColspan(6);
            vacio.setPadding(12f);
            vacio.setHorizontalAlignment(Element.ALIGN_CENTER);
            vacio.setBorderColor(COLOR_BORDE);
            tabla.addCell(vacio);
        }
        return tabla;
    }

    private static void agregarCelda(PdfPTable tabla, String texto, Font fuente) {
        PdfPCell celda = new PdfPCell(new Phrase(texto, fuente));
        celda.setPadding(7f);
        celda.setBorderColor(COLOR_BORDE);
        tabla.addCell(celda);
    }

    // convierte yyyy-MM-dd a dd/MM/yyyy; si no se puede parsear devuelve el texto original 
    private static String formatearFecha(String fecha) {
        if (fecha == null || fecha.isBlank()) {
            return "-";
        }
        try {
            return LocalDate.parse(fecha).format(FORMATO_FECHA);
        } catch (DateTimeParseException e) {
            return fecha;
        }
    }
}
