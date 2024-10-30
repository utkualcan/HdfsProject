package com.example.hdfsproject.config;

import lombok.Getter;
import org.apache.hadoop.fs.FileSystem; // Hadoop FileSystem
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration; // Spring Configuration

import java.io.IOException;

@Configuration
public class HDFSConfiguration {
    @Value("${hdfs.uri}")
    private String hdfsUri;

    @Getter
    @Value("${hdfs.baseDir}") // baseDir'i de ekleyelim
    private final String hdfsBaseDir = "/testdata/images";

    @Bean
    public org.apache.hadoop.fs.FileSystem fileSystem() throws IOException {
        org.apache.hadoop.conf.Configuration config = new org.apache.hadoop.conf.Configuration(); // Hadoop Configuration
        config.set("fs.defaultFS", hdfsUri);
        return FileSystem.get(config);
    }

}
