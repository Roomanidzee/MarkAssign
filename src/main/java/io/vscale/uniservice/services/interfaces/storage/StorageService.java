package io.vscale.uniservice.services.interfaces.storage;

import io.vscale.uniservice.domain.Event;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;

/**
 * 16.04.2018
 *
 * @author Aynur Aymurzin
 * @version 1.0
 */
public interface StorageService {
    String savePhoto(MultipartFile multipartFile, Authentication authentication);
    void writeFileToResponse(String fileName, HttpServletResponse response);
    void uploadFileTos3bucket(String fileName, File file);
    void deleteFileFromS3Bucket(String fileUrl);
    void saveFile(Event event, MultipartFile multipartFile);
}
