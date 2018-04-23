package io.vscale.uniservice.utils;

import io.vscale.uniservice.domain.FileOfService;
import lombok.SneakyThrows;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

/**
 * 05.03.2018
 *
 * @author Aynur Aymurzin
 * @version 1.0
 */
@Component
public class FileStorageUtil {

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${url.path}")
    private String urlPath;

    public FileOfService convertFromMultipart(MultipartFile file) {
        String originalFileName = file.getOriginalFilename();
        String type = file.getContentType();

        String storageName = generateFileName(file);

        String fileUrl =  urlPath + "/" + storageName;
        return FileOfService.builder()
                            .originalName(originalFileName)
                            .encodedName(storageName)
                            .url(fileUrl)
                            .type(type)
                            .build();
    }

    public String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }

    public File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }
}
