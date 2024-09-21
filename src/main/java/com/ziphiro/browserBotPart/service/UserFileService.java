package com.ziphiro.browserBotPart.service;

import com.ziphiro.browserBotPart.entityes.UserFile;
import com.ziphiro.browserBotPart.repositories.UserFileRepository;
import com.ziphiro.browserBotPart.values.StrV;
import lombok.RequiredArgsConstructor;
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

@RequiredArgsConstructor
@Service
public class UserFileService {

    private final UserFileRepository userFileRepository;

    public List<UserFile> getAllUserFilesPaths(String name){
        return userFileRepository.findFilesByCreator(name);
    }

    @Async("asyncDownload")
    public CompletableFuture<ResponseEntity<Resource>> downloadFile(String fileName, String userName) throws IOException {
        Path filePath  = Path.of(StrV.STORAGE_DIR, userName, fileName);
        InputStream fileStream = new FileInputStream(filePath.toFile());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" +
                fileName + StrV.SLASH);
        return CompletableFuture.completedFuture(ResponseEntity.ok().headers(headers).contentLength(filePath.toFile().length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(new InputStreamResource(fileStream)));
    }
    @Async("asyncUpload")
    public CompletableFuture<String> uploadFile(MultipartFile file, String userName) throws IOException {
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
