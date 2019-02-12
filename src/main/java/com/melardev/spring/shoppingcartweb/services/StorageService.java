package com.melardev.spring.shoppingcartweb.services;

import com.melardev.spring.shoppingcartweb.dtos.request.images.ImageDto;
import com.melardev.spring.shoppingcartweb.errors.exceptions.PermissionDeniedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.xml.bind.DatatypeConverter;
import java.io.*;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Service
public class StorageService {

    @Autowired
    private SettingsService settingsService;

    private static List<String> imageExtension = Arrays.asList("jpeg", "png", "svg");

    final char[] chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz01234567890".toCharArray();

    public List<File> upload(MultipartFile[] inputFiles) throws IOException {
        return upload(inputFiles, null);
    }

    public List<File> upload(MultipartFile[] inputFiles, String folder) throws IOException {
        List<File> uploadedFiles = new ArrayList<>();

        for (MultipartFile inputFile : inputFiles) {
            uploadedFiles.add(upload(inputFile, folder));
        }

        return uploadedFiles;
    }

    public File upload(MultipartFile fileInput, String folder) throws IOException {

        String outputDir = settingsService.getUploadsDirectory();
        if (folder != null)
            outputDir += folder;

        File f = new File(outputDir);
        File fileOutput = new File(outputDir + File.separator + generateRandomString(8) + getExtension(fileInput));
        if (!f.exists())
            f.mkdirs();
        upload(fileInput, fileOutput);
        return fileOutput;
    }

    private String getExtension(MultipartFile fileInput) {
        String fileName = fileInput.getOriginalFilename();
        String contentType = fileInput.getContentType();

        String[] parts = fileName.split("\\.");
        String extension = parts[parts.length - 1];
        if (extension.equalsIgnoreCase("png") ||
                extension.equalsIgnoreCase(".jpeg") || extension.equalsIgnoreCase("jpg"))
            return "." + extension;
        else
            throw new PermissionDeniedException("For security reasons you can not upload this, sorry");
    }

    public void upload(MultipartFile fileIn, File fileOut) throws IOException {
        fileIn.transferTo(fileOut);
    }

    public List<File> uploadBase64Images(Collection<ImageDto> imageDtos) {
        List<File> imagesList = new ArrayList<>(imageDtos.size());
        for (ImageDto imageDto : imageDtos) {
            imagesList.add(uploadBase64Image(imageDto));
        }
        return imagesList;
    }

    private File uploadBase64Image(ImageDto imageDto) {

        String[] strings = imageDto.getBase64Image().split(",");
        String extension;
        switch (strings[0]) {//check image's extension
            case "data:image/jpeg;base64":
                extension = ".jpeg";
                break;
            case "data:image/png;base64":
                extension = ".png";
                break;
            case "data:image/svg+xml;base64":
                extension = ".svg";
                break;
            default://should write cases for more images types
                throw new RuntimeException("Not supported");
        }
        //convert base64 string to binary data
        byte[] data = DatatypeConverter.parseBase64Binary(strings[1]);
        String fileName = imageDto.getName();
        String[] parts = fileName.split("\\.");
        if (parts.length == 1 || !imageExtension.contains(parts[parts.length - 1]))
            fileName = parts[0] + extension;
        File fileOutput = new File(settingsService.getUploadsDirectory() + fileName);
        try (OutputStream outputStream = new BufferedOutputStream(new FileOutputStream(fileOutput))) {
            outputStream.write(data);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return fileOutput;
    }

    public String generateRandomString(int length) {
        String randomString = "";

        final SecureRandom random = new SecureRandom();
        for (int i = 0; i < length; i++) {
            randomString = randomString + chars[random.nextInt(chars.length)];
        }

        return randomString;
    }
}
