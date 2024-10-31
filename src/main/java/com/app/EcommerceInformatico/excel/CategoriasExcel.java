package com.app.EcommerceInformatico.excel;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.app.EcommerceInformatico.model.Categoria;
public class CategoriasExcel {

	// Method to generate the categories report in Excel format
    public static ByteArrayInputStream categoriesReport(List<Categoria> categorias) {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            // Create a sheet
            Sheet sheet = workbook.createSheet("Categorias");

            // Define header font and style
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setColor(IndexedColors.WHITE.getIndex());

            CellStyle headerCellStyle = workbook.createCellStyle();
            headerCellStyle.setFillForegroundColor(IndexedColors.DARK_BLUE.getIndex());
            headerCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            headerCellStyle.setFont(headerFont);

            // Define body cell style
            CellStyle bodyCellStyle = workbook.createCellStyle();
            bodyCellStyle.setAlignment(HorizontalAlignment.LEFT);

            // Create header row
            Row headerRow = sheet.createRow(0);
            String[] headers = { "ID", "Descripción" };

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerCellStyle);
            }

            int rowIdx = 1;
            for (Categoria categoria : categorias) {
                Row row = sheet.createRow(rowIdx++);

                row.createCell(0).setCellValue(categoria.getId());
                Cell descripcionCell = row.createCell(1);
                descripcionCell.setCellValue(categoria.getNombre());
                descripcionCell.setCellStyle(bodyCellStyle);
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
