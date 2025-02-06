package com.lseg.digital.framework.qa.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExcelObjectRepository {
    private static final String OBJECT_REPO_PATH = "src/test/resources/object-repository/PageObjects.xlsx";
    private static Map<String, Map<String, String>> pageObjects = new HashMap<>();

    static {
        loadObjectRepository();
    }

    private static void loadObjectRepository() {
        try (FileInputStream fis = new FileInputStream(OBJECT_REPO_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheet("LoginPage");
            if (sheet != null) {
                Map<String, String> elements = new HashMap<>();
                
                // Skip header row
                for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                    Row row = sheet.getRow(i);
                    if (row != null) {
                        String elementName = getCellValue(row.getCell(0));
                        String locatorType = getCellValue(row.getCell(1));
                        String locatorValue = getCellValue(row.getCell(2));
                        
                        elements.put(elementName, locatorType + "=" + locatorValue);
                    }
                }
                pageObjects.put("LoginPage", elements);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load object repository", e);
        }
    }

    private static String getCellValue(Cell cell) {
        if (cell == null) return "";
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            default:
                return "";
        }
    }

    public static String getLocator(String pageName, String elementName) {
        Map<String, String> pageElements = pageObjects.get(pageName);
        if (pageElements == null) {
            throw new RuntimeException("Page " + pageName + " not found in object repository");
        }
        String locator = pageElements.get(elementName);
        if (locator == null) {
            throw new RuntimeException("Element " + elementName + " not found in " + pageName);
        }
        return locator;
    }
} 