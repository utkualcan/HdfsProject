package com.example.hdfsproject.service;

import jakarta.annotation.PostConstruct;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.FSDataInputStream;
import org.apache.hadoop.fs.Path;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.logging.Logger;

@Service
public class HDFSService {

    @Value("${hdfs.baseDir}")
    private final String hdfsBaseDir = "/testdata/images";

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
        Path path = new Path(hdfsBaseDir + "/" + fileName);
        logger.info("Attempting to read file: " + fileName);
        logger.info("Reading image from HDFS path: " + path.toString());

        if (!fileSystem.exists(path)) {
            logger.warning("File does not exist in HDFS: " + path.toString());
            return null;
        }


        try (FSDataInputStream inputStream = fileSystem.open(path)) {
            return inputStream.readAllBytes();
        } catch (IOException e) {
            logger.severe("Error reading file from HDFS: " + e.getMessage());
            throw e;
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
