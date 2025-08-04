package org.veeva.utilities;

import io.qameta.allure.Allure;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class AllureReportUtils {

    public static void attachFileToAllure(String name, String filePath, String mimeType) {
        try {
            File file = new File(filePath);
            if (file.exists()) {
                byte[] fileContent = Files.readAllBytes(file.toPath());
                Allure.addAttachment(name, mimeType, new ByteArrayInputStream(fileContent), getFileExtension(file));
            } else {
                System.err.println("File not found for Allure attachment: " + filePath);
            }
        } catch (IOException e) {
            System.err.println("Failed to attach file to Allure report: " + e.getMessage());
        }
    }

    public static void attachTextToAllure(String name, String content) {
        Allure.addAttachment(name, "text/plain", content);
    }

    public static void attachScreenshotBytes(String name, byte[] screenshotBytes) {
        Allure.addAttachment(name, "image/png", new ByteArrayInputStream(screenshotBytes), "png");
    }

    private static String getFileExtension(File file) {
        String name = file.getName();
        int lastDot = name.lastIndexOf(".");
        return (lastDot == -1) ? "" : name.substring(lastDot + 1);
    }
}
