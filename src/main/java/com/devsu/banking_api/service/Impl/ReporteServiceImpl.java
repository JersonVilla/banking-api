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
    	log.info("Generando PDF para {} movimientos", movimientos.size());
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
            
            String[] headers = {"Cliente", "Número Cuenta", "Tipo Cuenta", "Saldo Inicial", 
                    "Tipo movimiento", "Movimiento", "Saldo Disponible", "Fecha"};
            
            PdfPTable table = new PdfPTable(headers.length);
            table.setWidthPercentage(100);
            table.setWidths(new float[]{3, 2, 2, 2, 2, 2, 2, 3});

            log.info("Encabezados reporte");
            for (String h: headers) {
            	table.addCell(createHeaderCell(h));
            }
            
            for (MovimientoResponseDTO m : movimientos) {
                table.addCell(createDataCell(m.getCliente(), Element.ALIGN_LEFT));
                table.addCell(createDataCell(m.getNumeroCuenta(), Element.ALIGN_CENTER));
                table.addCell(createDataCell(m.getTipoCuenta(), Element.ALIGN_CENTER));
                table.addCell(createDataCell(MONEDA_FORMAT.format(m.getSaldoInicial()), Element.ALIGN_RIGHT));
                table.addCell(createDataCell(m.getTipoMovimiento(), Element.ALIGN_CENTER));
                table.addCell(createDataCell(MONEDA_FORMAT.format(m.getMovimiento()), Element.ALIGN_RIGHT));
                table.addCell(createDataCell(MONEDA_FORMAT.format(m.getSaldoDisponible()), Element.ALIGN_RIGHT));
                table.addCell(createDataCell(m.getFecha().format(FECHA_FORMAT), Element.ALIGN_CENTER));
            }

            document.add(table);
            document.close();

            log.info("PDF generado correctamente");
            return baos.toByteArray();
        } catch (Exception e) {
        	log.error("Error generando PDF", e);
            throw new GeneralException("Error generando PDF");
        }
    }

    private PdfPCell createHeaderCell(String title) {
        PdfPCell cell = new PdfPCell(new Phrase(title, FontFactory.getFont(FontFactory.HELVETICA_BOLD)));
        cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        return cell;
    }
    
    private PdfPCell createDataCell(String value, int align) {
        PdfPCell cell = new PdfPCell(new Phrase(value, FontFactory.getFont(FontFactory.HELVETICA, 10)));
        cell.setHorizontalAlignment(align);
        return cell;
    }

}
