package com.amalitechfileserver.fileserverbackend.service;

import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.exception.FileNotFound;
import com.amalitechfileserver.fileserverbackend.exception.InputBlank;
import jakarta.mail.MessagingException;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileServerService {
    String uploadFile(MultipartFile file, String title, String description) throws Exception;

    String shareFile(FileShareDto fileShareDto) throws MessagingException, FileNotFound, InputBlank;

    FileEntity downloadFile(String fileId) throws FileNotFound;

    List<FileEntity> adminGetAllFiles();

    String deleteFileById(String fileId) throws FileNotFound;

    List<FileEntity> adminSearchForFile(String fileName);

    List<FileEntity> userGetAllFiles();

    List<FileEntity> userSearchForFile(String fileName);
}
