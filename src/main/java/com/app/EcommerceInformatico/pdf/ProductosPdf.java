package com.app.EcommerceInformatico.pdf;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

import com.app.EcommerceInformatico.model.Producto;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

public class ProductosPdf {
	// Method to generate the products report in PDF format
	public static ByteArrayInputStream productsReport(List<Producto> productos) {
		Document document = new Document();
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		try {
			// Create a table with columns for the new fields
			PdfPTable table = new PdfPTable(7); // Adjust column count based on fields
			table.setWidthPercentage(100);
			table.setWidths(new int[] { 3, 5, 2, 2, 2, 2, 2 });

			// Define the font for the headers
			Font headFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

			// Add headers
			PdfPCell hcell;
			hcell = new PdfPCell(new Phrase("ID", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Nombre", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Categoria", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Precio", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Descuento (%)", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Precio con Descuento", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			hcell = new PdfPCell(new Phrase("Stock", headFont));
			hcell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(hcell);

			// Populate rows with product data
			for (Producto producto : productos) {
				PdfPCell cell;

				// ID column
				cell = new PdfPCell(new Phrase(String.valueOf(producto.getId())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				// Nombre column
				cell = new PdfPCell(new Phrase(producto.getNombre()));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				// Categoria column
				cell = new PdfPCell(new Phrase(producto.getCategoria()));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				// Precio column
				cell = new PdfPCell(new Phrase(String.valueOf(producto.getPrecio())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				// Descuento column
				cell = new PdfPCell(new Phrase(String.valueOf(producto.getDescuento())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				// Precio con Descuento column
				cell = new PdfPCell(new Phrase(String.valueOf(producto.getPrecioDescuento())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);

				// Stock column
				cell = new PdfPCell(new Phrase(String.valueOf(producto.getStock())));
				cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
				cell.setHorizontalAlignment(Element.ALIGN_CENTER);
				table.addCell(cell);
			}

			// Generate the PDF
			PdfWriter.getInstance(document, out);
			document.open();
			document.add(table);
			document.close();

		} catch (DocumentException ex) {
			ex.printStackTrace();
		}

		return new ByteArrayInputStream(out.toByteArray());
	}
}
