package com.ziphiro.browserBotPart.service;

import com.ziphiro.browserBotPart.entityes.UserFile;
import com.ziphiro.browserBotPart.repositories.UserFileRepository;
import com.ziphiro.browserBotPart.values.StrV;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;
@Slf4j
@RequiredArgsConstructor
@Service
public class UserFileService {

    private final UserFileRepository userFileRepository;
    private final Logger LOGGER = LoggerFactory.getLogger(UserFileService.class);
    public List<UserFile> getAllUserFilesPaths(String name){
        return userFileRepository.findFilesByCreator(name);
    }

    @Async("asyncDownload")
    public CompletableFuture<ResponseEntity<Resource>> downloadFile(String fileName, String userName) throws IOException {
        LOGGER.info(userName + StrV.STARTING_DOWNLOAD + fileName);
        Path filePath  = Path.of(StrV.STORAGE_DIR, userName, fileName);
        InputStream fileStream = new FileInputStream(filePath.toFile());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, StrV.ATTACHMENT_FILENAME +
                fileName + StrV.SLASH);
        LOGGER.info(userName + StrV.COMPLETE_DOWNLOAD + fileName);
        return CompletableFuture.completedFuture(ResponseEntity.ok().headers(headers).contentLength(filePath.toFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream)));
    }
    @Async("asyncUpload")
    public CompletableFuture<String> uploadFile(MultipartFile file, String userName) throws IOException {
        LOGGER.info(userName + StrV.UPLOAD_START + file.getOriginalFilename());
        if (file.isEmpty()){
            throw new IOException(StrV.UPLOAD_ERROR);
        } else {
            file.transferTo(Path.of(StrV.STORAGE_DIR, userName, file.getOriginalFilename()));
            String uploadFileDir = StrV.STORAGE_DIR + file.getOriginalFilename();
            UserFile uploadFile = UserFile.builder().fileName(generateFileName(file.getOriginalFilename()))
                    .filePath(uploadFileDir)
                    .creator(userName).build();
            userFileRepository.save(uploadFile);
        }
        LOGGER.info(userName + StrV.UPLOAD_SUCCESS + file.getOriginalFilename());
        return CompletableFuture.completedFuture(StrV.UPLOAD_SUCCESS);
    }

    public void deleteFileFromStorage(String fileName, String userName) throws IOException {
        Files.delete(Path.of(StrV.STORAGE_DIR + userName + StrV.SLASH + fileName));
    }
    public void deleteFileFromDataBase(Long fileId){
        userFileRepository.deleteById(fileId);
    }
    public String generateFileName (String filename){
        if (userFileRepository.findUserFileByFileName(filename).isPresent()){
            return filename + StrV.PLUS;
        }
        else return filename;
    }
}
