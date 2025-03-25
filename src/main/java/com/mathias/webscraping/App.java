package com.mathias.webscraping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class App {


    public static void main(String[] args) {


        String url = "https://www.gov.br/ans/pt-br/acesso-a-informacao/participacao-da-sociedade/atualizacao-do-rol-de-procedimentos";
        List<String> downloadedFiles = new ArrayList<>();



        try {
            Document document = Jsoup.connect(url).get();
            //Selecting anchor elements which ends with .pdf
            Elements attachments = document.select("li a[href$=.pdf]");

            for (Element attachment : attachments) {
                String link = attachment.attr("href"); //link
                String text = attachment.text();                  //text

                // Check if the text contains "Anexo"
                if (text.contains("Anexo I")) {
                    String fileName = text + "pdf";
                    System.out.println(text + ": " + link);
                    downloadFile(link, fileName );
                    downloadedFiles.add(fileName);
                }
            }

            if (!downloadedFiles.isEmpty()) {
                ZipCompressor.compressFiles(downloadedFiles, "documents.zip");
            }

        } catch (IOException e) {
            System.err.println("Error connecting to the URL. Please check if the website is accessible: " + url);
            e.printStackTrace();
        }

    }


    public static void downloadFile(String fileUrl, String fileName) {
        try {
            URL url = new URL(fileUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setConnectTimeout(5000);


            InputStream inputStream = connection.getInputStream();
            FileOutputStream fileOutputStream = new FileOutputStream(fileName);

            byte[] buffer = new byte[1024];

            int bytesRead;
            while ((bytesRead = inputStream.read(buffer)) != -1) {
                fileOutputStream.write(buffer, 0, bytesRead);
            }


            inputStream.close();
            fileOutputStream.close();

            System.out.println("File downloaded: " + fileName);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}