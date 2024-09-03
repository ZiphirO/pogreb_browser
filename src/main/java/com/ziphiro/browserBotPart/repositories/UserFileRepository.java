package com.ziphiro.browserBotPart.repositories;

import com.ziphiro.browserBotPart.entityes.UserFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserFileRepository extends JpaRepository<UserFile, Long> {

    List<UserFile> findFilesByCreator(String creator);

}
