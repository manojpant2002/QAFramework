package com.lseg.digital.framework.qa.utils;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ExcelObjectRepository {
    private static final String OBJECT_REPOSITORY_PATH = "src/test/resources/object-repository/ObjectRepository.xlsx";
    private static Map<String, Map<String, String>> pageObjectsCache = new HashMap<>();
    
    static {
        loadObjectRepository();
    }

    private static void loadObjectRepository() {
        log.info("Loading object repository from: {}", OBJECT_REPOSITORY_PATH);
        try (FileInputStream fis = new FileInputStream(OBJECT_REPOSITORY_PATH);
             Workbook workbook = new XSSFWorkbook(fis)) {
            
            for (int i = 0; i < workbook.getNumberOfSheets(); i++) {
                Sheet sheet = workbook.getSheetAt(i);
                String pageName = sheet.getSheetName();
                log.debug("Processing sheet: {}", pageName);
                Map<String, String> pageObjects = new HashMap<>();

                for (int rowNum = 1; rowNum <= sheet.getLastRowNum(); rowNum++) {
                    Row row = sheet.getRow(rowNum);
                    if (row != null) {
                        String elementName = getCellValueAsString(row.getCell(0));
                        String locatorType = getCellValueAsString(row.getCell(1));
                        String locatorValue = getCellValueAsString(row.getCell(2));
                        
                        if (elementName != null && locatorType != null && locatorValue != null) {
                            pageObjects.put(elementName, locatorType + "=" + locatorValue);
                            log.debug("Added locator for element: {} = {}={}", 
                                elementName, locatorType, locatorValue);
                        }
                    }
                }
                pageObjectsCache.put(pageName, pageObjects);
                log.info("Loaded {} elements for page: {}", pageObjects.size(), pageName);
            }
            log.info("Object repository loaded successfully with {} pages", pageObjectsCache.size());
        } catch (IOException e) {
            log.error("Failed to load object repository", e);
            throw new RuntimeException("Failed to load object repository: " + e.getMessage(), e);
        }
    }

    private static String getCellValueAsString(Cell cell) {
        if (cell == null) return null;
        
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf((int) cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return null;
        }
    }

    public static String getLocator(String pageName, String elementName) {
        Map<String, String> pageObjects = pageObjectsCache.get(pageName);
        if (pageObjects == null) {
            throw new IllegalArgumentException("Page '" + pageName + "' not found in object repository");
        }

        String locator = pageObjects.get(elementName);
        if (locator == null) {
            throw new IllegalArgumentException("Element '" + elementName + "' not found in page '" + pageName + "'");
        }

        return locator;
    }

    public static void reloadRepository() {
        pageObjectsCache.clear();
        loadObjectRepository();
    }
} 