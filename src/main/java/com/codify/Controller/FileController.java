package com.codify.Controller;

import com.codify.Commons.ApiResponse;
import com.codify.Service.FileUploadAndDownload.FileServicesImpl;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.http2.Http2AsyncUpgradeHandler;
import org.slf4j.Logger;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@Slf4j
@RestController
public class FileController {

    private final FileServicesImpl fileServicesImpl;

    public FileController(FileServicesImpl fileServicesImpl) {
        this.fileServicesImpl = fileServicesImpl;
    }

    @PostMapping("/upload")

    public ResponseEntity<ApiResponse> uploadFile(@RequestParam MultipartFile file) throws IOException {
        try {

            log.info("file");
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status("200")
                            .message(fileServicesImpl.uploadFileToDir(file))
                            .build()
            );
        } catch (Exception e) {
            log.error("error: ", e);
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .status("500")
                            .message(e.getMessage())
                            .build()
            );
        }

    }

    @GetMapping("/download/{file}")
    public ResponseEntity<Resource> downloadHashedFile(@PathVariable("file") String file) throws IOException {
        var fileTodownload = fileServicesImpl.downloadFile(file);

        try {
            return ResponseEntity.ok()
                                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file + "\"")
                                    .contentLength(fileTodownload.length())
                                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                    .body(new FileSystemResource(fileTodownload));
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }

    }

    }

