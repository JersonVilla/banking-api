package com.devsu.banking_api.service.Impl;

import java.io.ByteArrayOutputStream;
import java.text.DecimalFormat;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.devsu.banking_api.dto.MovimientoResponseDTO;
import com.devsu.banking_api.exception.GeneralException;
import com.devsu.banking_api.service.IReporteService;
import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class ReporteServiceImpl implements IReporteService {

	private static final DateTimeFormatter FECHA_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private static final DecimalFormat MONEDA_FORMAT = new DecimalFormat("#,##0.00");

    @Override
    public byte[] generarReporteMovimientos(List<MovimientoResponseDTO> movimientos) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            Document document = new Document(PageSize.A4, 36, 36, 36, 36);
            PdfWriter.getInstance(document, baos);
            document.open();

            log.info("Titulo");
            Paragraph titulo = new Paragraph("Reporte de Movimientos",
                    FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16));
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);
            document.add(Chunk.NEWLINE);
            
            PdfPTable table = new PdfPTable(8);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 2, 2, 2, 2, 2, 2, 3});

            log.info("Encabezados reporte");
            addTableHeader(table, "Cliente");
            addTableHeader(table, "Número Cuenta");
            addTableHeader(table, "Tipo Cuenta");
            addTableHeader(table, "Saldo Inicial");
            addTableHeader(table, "Tipo movimiento");
            addTableHeader(table, "Movimiento");
            addTableHeader(table, "Saldo Disponible");
            addTableHeader(table, "Fecha");
            
            for (MovimientoResponseDTO m : movimientos) {
                table.addCell(m.getCliente());
                table.addCell(m.getNumeroCuenta());
                table.addCell(m.getTipoCuenta());
                table.addCell(MONEDA_FORMAT.format(m.getSaldoInicial()));
                table.addCell(m.getTipoMovimiento());
                table.addCell(MONEDA_FORMAT.format(m.getMovimiento()));
                table.addCell(MONEDA_FORMAT.format(m.getSaldoDisponible()));
                table.addCell(m.getFecha().format(FECHA_FORMAT));
            }

            document.add(table);
            document.close();

            return baos.toByteArray();
        } catch (Exception e) {
            throw new GeneralException("Error generando PDF");
        }
    }

    private void addTableHeader(PdfPTable table, String headerTitle) {
        PdfPCell header = new PdfPCell();
        header.setBackgroundColor(BaseColor.LIGHT_GRAY);
        header.setPhrase(new Phrase(headerTitle, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        header.setHorizontalAlignment(Element.ALIGN_CENTER);
        table.addCell(header);
    }

}
