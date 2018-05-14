package io.vscale.uniservice.services.implementations.storage;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.*;
import io.vscale.uniservice.domain.Event;
import io.vscale.uniservice.domain.FileOfService;
import io.vscale.uniservice.domain.Profile;
import io.vscale.uniservice.domain.User;
import io.vscale.uniservice.repositories.data.EventRepository;
import io.vscale.uniservice.repositories.data.FileOfServiceRepository;
import io.vscale.uniservice.repositories.data.ProfileRepository;
import io.vscale.uniservice.services.interfaces.auth.AuthenticationService;
import io.vscale.uniservice.services.interfaces.storage.StorageService;
import io.vscale.uniservice.utils.FileStorageUtil;
import lombok.SneakyThrows;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.HashSet;

/**
 * 16.04.2018
 *
 * @author Aynur Aymurzin
 * @version 1.0
 */
@Service
public class StorageServiceImpl implements StorageService{

    private final AuthenticationService authenticationService;

    private final ProfileRepository profileRepository;

    private final FileOfServiceRepository filesRepository;

    private final EventRepository eventRepository;

    private final FileStorageUtil fileStorageUtil;

    private AmazonS3 s3Client;

    @Value("${amazonProperties.endpointUrl}")
    private String endpointUrl;

    @Value("${amazonProperties.bucketName}")
    private String bucketName;

    @Value("${amazonProperties.accessKey}")
    private String accessKey;

    @Value("${amazonProperties.secretKey}")
    private String secretKey;


    @Autowired
    public StorageServiceImpl(@Qualifier("generalAuthenticationService") AuthenticationService authenticationService,
                              ProfileRepository profileRepository, FileOfServiceRepository filesRepository,
                              EventRepository eventRepository, FileStorageUtil fileStorageUtil) {
        this.authenticationService = authenticationService;
        this.profileRepository = profileRepository;
        this.filesRepository = filesRepository;
        this.eventRepository = eventRepository;
        this.fileStorageUtil = fileStorageUtil;
    }

    @PostConstruct
    private void initializeAmazon() {
        AWSCredentials credentials = new BasicAWSCredentials(this.accessKey, this.secretKey);
        this.s3Client = AmazonS3ClientBuilder.standard()
                                             .withRegion("us-west-2")
                                             .withCredentials(new AWSStaticCredentialsProvider(credentials))
                                             .build();
    }

    @Override
    public String savePhoto(MultipartFile multipartFile, Authentication authentication) {
        FileOfService fileOfService = fileStorageUtil.convertFromMultipart(multipartFile);

        if (!fileOfService.getType().startsWith("image")) {
            throw new IllegalArgumentException("FileFormatException");
        }

        try {
            User user = authenticationService.getUserByAuthentication(authentication);
            Profile profile = user.getProfile();
            if (profile.getAvatar() != null){
                deleteFileFromS3Bucket(profile.getAvatar().getUrl());
                filesRepository.delete(profile.getAvatar());
            }
            profile.setAvatar(fileOfService);
            profileRepository.save(profile);

            File file = fileStorageUtil.convertMultiPartToFile(multipartFile);
            String fileName = fileOfService.getEncodedName();

            uploadFileTos3bucket(fileName, file);
            file.delete();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileOfService.getUrl();
    }

    @Override
    @SneakyThrows
    public void writeFileToResponse(String fileName, HttpServletResponse response) {
        FileOfService file = filesRepository.findOneByEncodedName(fileName);
        response.setContentType(file.getType());

        S3Object object = s3Client.getObject(new GetObjectRequest(bucketName, fileName));
        InputStream inputStream = object.getObjectContent();

        IOUtils.copy(inputStream, response.getOutputStream());

        response.flushBuffer();
    }

    @Override
    public void uploadFileTos3bucket(String fileName, File file) {
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, file)
                .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    @Override
    public void deleteFileFromS3Bucket(String fileUrl) {
        String fileName = fileUrl.substring(fileUrl.lastIndexOf("/") + 1);
        s3Client.deleteObject(new DeleteObjectRequest(bucketName, fileName));
    }

    @Override
    public void saveFile(Event event, MultipartFile multipartFile) {
        FileOfService fileOfService = fileStorageUtil.convertFromMultipart(multipartFile);
        this.filesRepository.save(fileOfService);

        try {
            File file = fileStorageUtil.convertMultiPartToFile(multipartFile);
            String fileName = fileOfService.getEncodedName();
            uploadFileTos3bucket(fileName, file);
            file.delete();

            if (event.getFiles()==null){
                event.setFiles(new HashSet<>());
            }

            event.getFiles().add(fileOfService);
            eventRepository.save(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
