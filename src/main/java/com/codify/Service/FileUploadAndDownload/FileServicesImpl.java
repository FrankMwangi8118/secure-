package com.codify.Service.FileUploadAndDownload;

import com.codify.Service.ImageHashing.ImageHashingImpl;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;

@Service

public class FileServicesImpl implements FileService {
    public final ImageHashingImpl imageHashingImpl;
    public static final String DIR = "./files";
    @Value("${download.path}")
  public final String downloadPath;

    public FileServicesImpl(ImageHashingImpl imageHashingImpl, @Value("${download.path}") String downloadPath) {
        this.imageHashingImpl = imageHashingImpl;
        this.downloadPath = downloadPath;
    }


    @Override
    public String uploadFileToDir(MultipartFile file) throws IOException {

        if (file == null) {
            throw new NullPointerException("null files aint allowed");
        }
        var targetFile = new File(DIR + File.separator + file.getOriginalFilename());

//        if(!(Objects.equals(targetFile.getParentFile(),DIR))){
//            throw new SecurityException("unsupported file name");
//        }

        File fileDir = new File(DIR);

        if (!fileDir.exists()) {
            fileDir.mkdir();
        }
        Files.copy(file.getInputStream(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);

        final String filePath = DIR + File.separator + file.getOriginalFilename();

        byte[] imageBytes = Files.readAllBytes(Path.of(DIR + File.separator + file.getOriginalFilename()));
        imageHashingImpl.hashEngine(imageBytes);

        return downloadPath + File.separator + file.getOriginalFilename();
    }

    @Override
    public File downloadFile(String fileName) throws FileNotFoundException {
        if (fileName == null || fileName.isEmpty()) {
            throw new NullPointerException();
        }
        var fileToDownload = new File(DIR + File.separator + fileName);
        if (!fileToDownload.exists()) {
            throw new FileNotFoundException("no file named ...." + fileName);
        }
        return fileToDownload;
    }


}
