package com.example.hdfsproject.controller;

import com.example.hdfsproject.service.HDFSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/images") // Tüm resim istekleri için temel yol
public class ImageController {

    private final HDFSService hdfsService;

    @Autowired
    public ImageController(HDFSService hdfsService) {
        this.hdfsService = hdfsService;
    }

    @GetMapping("/{fileName}") // Dosya adını dinamik olarak alan yol
    public ResponseEntity<byte[]> getImage(@PathVariable String fileName) {
        try {
            byte[] imageBytes = hdfsService.readImage(fileName);
            if (imageBytes == null) {
                return ResponseEntity.notFound().build(); // Dosya bulunamazsa 404 döndür
            }
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG) // Resim türünü ayarlayın
                    .body(imageBytes);
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // Hata durumunda 500 döndür
        }
    }
}
