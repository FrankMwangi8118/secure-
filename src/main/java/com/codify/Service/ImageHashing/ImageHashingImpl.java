package com.codify.Service.ImageHashing;

import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
@Service
public class ImageHashingImpl implements ImageHashingService{


    @Override
    public void hashEngine(byte[] imageBytes) {
        try {
            MessageDigest digest=MessageDigest.getInstance("SHA-256");
            byte[] hashedImageByte=digest.digest(imageBytes);
            System.out.println(Arrays.toString(hashedImageByte));


        } catch ( Exception e) {
            throw new RuntimeException(e.getMessage());
        }

    }
}
