package com.app.EcommerceInformatico.excel;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.app.EcommerceInformatico.model.Producto;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

public class ProductosExcel {
	public static ByteArrayInputStream productosToExcel(List<Producto> productos) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet("Productos");

            // Header row
            Row headerRow = sheet.createRow(0);
            String[] headers = {"ID", "Nombre", "Descripcion", "Categoria", "Precio", "Stock", "Imagen", "Descuento", "Precio con Descuento", "Estado"};

            // Style for header
            CellStyle headerStyle = workbook.createCellStyle();
            Font font = workbook.createFont();
            font.setBold(true);
            headerStyle.setFont(font);

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            // Data rows
            int rowIdx = 1;
            for (Producto producto : productos) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(producto.getId());
                row.createCell(1).setCellValue(producto.getNombre());
                row.createCell(2).setCellValue(producto.getDescripcion());
                row.createCell(3).setCellValue(producto.getCategoria());
                row.createCell(4).setCellValue(producto.getPrecio());
                row.createCell(5).setCellValue(producto.getStock());
                row.createCell(6).setCellValue(producto.getImagen());
                row.createCell(7).setCellValue(producto.getDescuento());
                row.createCell(8).setCellValue(producto.getPrecioDescuento());
                row.createCell(9).setCellValue(producto.isEstado() ? "Activo" : "Inactivo");
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
