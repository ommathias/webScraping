package com.mathias.webscraping;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipCompressor {

    public static void compressFiles(List<String> fileNames, String zipFileName) {
        try (FileOutputStream fos = new FileOutputStream(zipFileName);
             ZipOutputStream zipOut = new ZipOutputStream(fos)) //creates zipFile
         {

            for (String fileName : fileNames) {
                File fileToZip = new File(fileName);

                if (!fileToZip.exists()) {
                    System.err.println("File not found: " + fileName);
                    continue; // Skip if the file doesn't exist
                }

                try (FileInputStream fis = new FileInputStream(fileToZip)) //open file
                {
                    ZipEntry zipEntry = new ZipEntry(fileToZip.getName());
                    zipOut.putNextEntry(zipEntry);

                    byte[] buffer = new byte[1024];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                        zipOut.write(buffer, 0, bytesRead);
                    }

                    zipOut.closeEntry();
                    System.out.println("Added to ZIP: " + fileName);
                }
            }

            System.out.println("ZIP file created successfully: " + zipFileName);
        } catch (IOException e) {
            System.err.println("Error creating ZIP file: " + e.getMessage());
            e.printStackTrace();
        }
    }
}