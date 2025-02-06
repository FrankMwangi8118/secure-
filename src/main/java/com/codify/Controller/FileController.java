package com.codify.Controller;

import com.codify.Commons.ApiResponse;
import com.codify.Service.FileUploadAndDownload.FileServicesImpl;
import org.apache.coyote.http2.Http2AsyncUpgradeHandler;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;

@RestController
public class FileController {

    private final FileServicesImpl fileServicesImpl;

    public FileController(FileServicesImpl fileServicesImpl) {
        this.fileServicesImpl = fileServicesImpl;
    }
    @PostMapping("/upload")

    public ResponseEntity<ApiResponse> uploadFile(@RequestParam MultipartFile file) throws IOException {
        try {


            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status("200")
                            .message(fileServicesImpl.uploadFileToDir(file))
                            .build()
            );
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .status("500")
                            .message(e.getMessage())
                            .build()
            );
        }

    }
    @GetMapping("/download")
     public ResponseEntity<ApiResponse> downloadHashedFile(@RequestParam ("file") String fileName) throws IOException {
        var fileTodownload=fileServicesImpl.downloadFile(fileName);
        try {
            return ResponseEntity.ok(
                    ApiResponse.builder()
                            .status("200")
                            .message("download complete")
                            .data(ResponseEntity.ok()
                                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                                    .contentLength(fileTodownload.length())
                                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                                    .body(new FileSystemResource(fileTodownload))).build()
            );
        }catch (Exception e){
            return  ResponseEntity.internalServerError().body(
                    ApiResponse.builder()
                            .status("500")
                            .message(e.getMessage())
                            .build()
            );
        }

}}
