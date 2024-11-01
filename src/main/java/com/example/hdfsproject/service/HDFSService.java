package com.example.hdfsproject.service;

import jakarta.annotation.PostConstruct;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;

@Service
public class HDFSService {

    @Value("${hdfs.baseDir}")
    private String hdfsBaseDir = "/testdata/images";

    private final FileSystem fileSystem;
    private static final Logger logger = Logger.getLogger(HDFSService.class.getName());

    public HDFSService(FileSystem fileSystem) {
        this.fileSystem = fileSystem;
    }

    @PostConstruct
    public void init() {
        testHDFSConnection();
    }

    public byte[] readImage(String fileName) throws IOException {

        String hdfsFileUri = "http://localhost:50070/webhdfs/v1" + hdfsBaseDir + "/" + fileName + "?op=OPEN";
        logger.info("Attempting to read file: " + hdfsFileUri);


        HttpURLConnection connection = null;
        try {
            URL url = new URL(hdfsFileUri);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.connect();

            int responseCode = connection.getResponseCode();
            if (responseCode != HttpURLConnection.HTTP_OK) {
                logger.severe("Failed to read image, HTTP response code: " + responseCode);
                throw new IOException("Failed to read image: " + responseCode);
            }

            try (InputStream inputStream = connection.getInputStream();
                 ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
                byte[] imageBytes = outputStream.toByteArray();
                logger.info("Image read successfully, size: " + imageBytes.length + " bytes");
                return imageBytes;
            }
        } catch (IOException e) {
            logger.severe("Error reading file from HDFS: " + e.getMessage());
            throw e;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    public void testHDFSConnection() {
        try {
            Path testPath = new Path(hdfsBaseDir);
            if (fileSystem.exists(testPath)) {
                logger.info("HDFS connection is successful, base directory exists: " + hdfsBaseDir);
            } else {
                logger.warning("HDFS base directory does not exist: " + hdfsBaseDir);
            }
        } catch (IOException e) {
            logger.severe("HDFS connection failed: " + e.getMessage());
        }
    }
}
