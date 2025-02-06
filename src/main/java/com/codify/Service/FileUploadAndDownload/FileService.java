package com.codify.Service.FileUploadAndDownload;

import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface FileService {
    File downloadFile(String fileName) throws FileNotFoundException;
    String uploadFileToDir(MultipartFile file) throws IOException;
}
