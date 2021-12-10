package com.bolsadeideas.springboot.app.view.pdf;

import com.bolsadeideas.springboot.app.models.entity.Cotizacion;
import com.bolsadeideas.springboot.app.models.entity.ItemCotizacion;
import com.lowagie.text.Document;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.view.document.AbstractPdfView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.util.Locale;
import java.util.Map;

@Component("cotizacion/ver")
public class CotizacionPdfView extends AbstractPdfView {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private LocaleResolver localeResolver;

    @Override
    protected void buildPdfDocument(Map<String,
            Object> model,
                                    Document document,
                                    PdfWriter writer,
                                    HttpServletRequest request,
                                    HttpServletResponse response) throws Exception {

        Cotizacion cotizacion = (Cotizacion) model.get("cotizacion");

        Locale locale = localeResolver.resolveLocale(request);

        MessageSourceAccessor mensajes =  getMessageSourceAccessor();

        PdfPTable tabla = new PdfPTable(1);
        tabla.setSpacingAfter(20);

        PdfPCell cell = null;

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.cotizacion.ver.datos.gestor", null, locale)));
        cell.setBackgroundColor(new Color(184, 218, 255));
        cell.setPadding(8f);
        tabla.addCell(cell);

        tabla.addCell("Nombres y Apellidos: " + cotizacion.getGestor().getNombres() + " " + cotizacion.getGestor().getApellidos());
        tabla.addCell("Telefono de contacto: " + cotizacion.getGestor().getTelefono());
        tabla.addCell("Correo de contacto: " + cotizacion.getGestor().getEmail());
        tabla.addCell("Distrito: " + cotizacion.getGestor().getDistrito());

        PdfPTable tabla2 = new PdfPTable(1);
        tabla2.setSpacingAfter(20);

        cell = new PdfPCell(new Phrase(messageSource.getMessage("text.cotizacion.ver.datos.cotizacion", null, locale)));
        cell.setBackgroundColor(new Color(195, 230, 203));
        cell.setPadding(8f);

        tabla2.addCell(cell);
        tabla2.addCell(mensajes.getMessage("text.gestor.cotizacion.numero") + ": " + cotizacion.getId());
        tabla2.addCell(mensajes.getMessage("text.gestor.cotizacion.fecha") + ": " + cotizacion.getCreateAt());
        tabla2.addCell(mensajes.getMessage("text.gestor.cotizacion.observacion") + ": " + cotizacion.getObservacion());

        document.add(tabla);
        document.add(tabla2);

        PdfPTable tabla3 = new PdfPTable(4);
        tabla3.setWidths(new float[] {3.5f, 1, 1, 1});
        tabla3.addCell(mensajes.getMessage("text.cotizacion.form.item.descripcion"));
        tabla3.addCell(mensajes.getMessage("text.cotizacion.form.item.precio_unit"));
        tabla3.addCell(mensajes.getMessage("text.cotizacion.form.item.cantidad"));
        tabla3.addCell(mensajes.getMessage("text.cotizacion.form.item.subtotal"));

        for(ItemCotizacion item: cotizacion.getItems()){
            tabla3.addCell(item.getProducto().getNombre());
            tabla3.addCell(item.getProducto().getPrecio().toString());

            cell =  new PdfPCell(new Phrase(item.getCantidad().toString()));
            cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
            tabla3.addCell(cell);
            tabla3.addCell(item.calcularImporte().toString());
        }

        cell = new PdfPCell(new Phrase(mensajes.getMessage("text.cotizacion.form.subtoal") ));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(cotizacion.getSubtotal().toString());

        cell = new PdfPCell(new Phrase(mensajes.getMessage("text.cotizacion.form.igv") ));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(cotizacion.getIgv().toString());

        cell = new PdfPCell(new Phrase(mensajes.getMessage("text.cotizacion.form.total") ));
        cell.setColspan(3);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_RIGHT);
        tabla3.addCell(cell);
        tabla3.addCell(cotizacion.getTotal().toString());

        document.add(tabla3);

    }
}
