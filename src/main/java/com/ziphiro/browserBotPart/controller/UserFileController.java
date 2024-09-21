

package com.ziphiro.browserBotPart.controller;

import com.ziphiro.browserBotPart.entityes.User;
import com.ziphiro.browserBotPart.entityes.UserFile;
import com.ziphiro.browserBotPart.service.UserFileService;
import com.ziphiro.browserBotPart.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@RequiredArgsConstructor
@RequestMapping("/files")
public class UserFileController {

    private final UserFileService userFileService;

    @GetMapping("/userName/{creator}")
    public List<UserFile> getAllUserFilesByUserName(@PathVariable String creator) {
        return userFileService.getAllUserFilesPaths(creator);
    }

    @GetMapping(value = "/download/{userName}/{fileName}", consumes = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    public CompletableFuture<ResponseEntity<Resource>> downloadFile(@PathVariable String fileName,
                                                                    @PathVariable String userName) throws IOException {
        return userFileService.downloadFile(fileName, userName);
    }

    @PostMapping("/upload/{userName}")
    public CompletableFuture<String> uploadFile(@RequestParam MultipartFile file,
                                                @PathVariable String userName) throws IOException {
        return userFileService.uploadFile(file, userName);
    }

    @GetMapping("/del/{fileName}/{fileId}/{userName}")
    public void deleteFileFromStorage(@PathVariable String fileName, @PathVariable Long fileId,
                                      @PathVariable String userName) throws IOException {
        userFileService.deleteFileFromStorage(fileName, userName);
        userFileService.deleteFileFromDataBase(fileId);
    }


    @Autowired
    private UserService userService;

    @GetMapping
    public boolean checkUser(User user) {
        return userService.checkUser(user);

    }

}
