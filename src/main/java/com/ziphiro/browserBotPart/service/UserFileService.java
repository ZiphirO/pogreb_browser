package com.ziphiro.browserBotPart.service;

import com.ziphiro.browserBotPart.entityes.UserFile;
import com.ziphiro.browserBotPart.repositories.UserFileRepository;
import com.ziphiro.browserBotPart.values.StV;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.nio.file.*;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
public class UserFileService {

    @Autowired
    private UserFileRepository userFileRepository;

    public void initUserFile(UserFile file){
        userFileRepository.save(file);
    }

    public List<UserFile> getAllUserFilesPaths(String name){
        return userFileRepository.findFilesByCreator(name);
    }

    @Async("downloadExecutor")
    public CompletableFuture<ResponseEntity<Resource>> downloadFile(String fileName, String userName) throws IOException {
        Path filePath  = Path.of(StV.STORAGE_DIR, userName, fileName);
        InputStream fileStream = new FileInputStream(filePath.toFile());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +
                fileName + StV.SLASH);
        return CompletableFuture.completedFuture(ResponseEntity.ok().headers(headers).contentLength(filePath.toFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream)));
    }
    @Async("uploadExecutor")
    public CompletableFuture<String> uploadFile(MultipartFile file, String userName) throws IOException {
        if (file.isEmpty()){
            return CompletableFuture.completedFuture(StV.UPLOAD_ERROR);
        } else {
                file.transferTo(Path.of(StV.STORAGE_DIR, userName, file.getOriginalFilename()));
            String uploadFileDir = StV.STORAGE_DIR + file.getOriginalFilename();
            UserFile uploadFile = UserFile.builder().fileName(file.getOriginalFilename())
                    .filePath(uploadFileDir)
                    .creator(userName).build();
            userFileRepository.save(uploadFile);
            initUserFile(uploadFile);
            return CompletableFuture.completedFuture(StV.UPLOAD_SUCCESS);
        }
    }

    public void deleteFileFromStorage(String fileName, String userName) throws IOException {
        Files.delete(Path.of(StV.STORAGE_DIR + userName + StV.SLASH + fileName));
    }
    public void deleteFileFromDataBase(Long fileId) {
        userFileRepository.deleteById(fileId);
    }
}
