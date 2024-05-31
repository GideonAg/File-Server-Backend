package com.amalitechfileserver.fileserverbackend.service;

import com.amalitechfileserver.fileserverbackend.auth.SendMails;
import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.exception.FileNotFound;
import com.amalitechfileserver.fileserverbackend.exception.InputBlank;
import com.amalitechfileserver.fileserverbackend.repository.FileRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServerServiceImpl implements FileServerService{

    private final FileRepository fileRepository;
    private final SendMails sendMails;

    @Override
    public String uploadFile(MultipartFile file, String title, String description) throws Exception {
        if (file.getSize() > 20000000)
            throw new MaxUploadSizeExceededException(20000000);

        if (file.isEmpty() || title.isBlank() || description.isBlank())
            throw new InputBlank("Title, description and file are required");

        FileEntity fileEntity = FileEntity.builder()
                .title(title)
                .description(description)
                .file(file.getBytes())
                .fileType(file.getContentType())
                .build();
        fileRepository.save(fileEntity);
        return "File uploaded successfully";
    }

    @Override
    public String shareFile(FileShareDto fileShareDto) throws MessagingException, FileNotFound, InputBlank, IOException {
        if (fileShareDto.getReceiverEmail().isBlank() || fileShareDto.getFileId().isBlank())
            throw new InputBlank("Receiver email and file id are required");

        FileEntity fetchedFile = fileRepository.findById(fileShareDto.getFileId())
                .orElseThrow(() -> new FileNotFound("File not found"));

        sendMails.sendFileShareEmail(fileShareDto, fetchedFile);
        return String.format("File sent to %s successfully", fileShareDto.getReceiverEmail());
    }

    @Override
    public FileEntity downloadFile(String fileId) throws FileNotFound {
        FileEntity fetchedFile = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFound("File not found"));

        int numberOfDownloads = fetchedFile.getNumberOfDownloads() + 1;
        fetchedFile.setNumberOfDownloads(numberOfDownloads);
        fileRepository.save(fetchedFile);
        return fetchedFile;
    }

    @Override
    public List<FileEntity> adminGetAllFiles() {
        return fileRepository.adminGetAllFiles();
    }

    @Override
    public String deleteFileById(String fileId) throws FileNotFound {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFound("Failed to delete file"));
        fileRepository.delete(file);
        return "File deleted successfully";
    }

    @Override
    public List<FileEntity> adminSearchForFile(String fileName) {
        return fileRepository.adminSearchForFile(fileName);
    }

    @Override
    public List<FileEntity> userGetAllFiles() {
        return fileRepository.userGetAllFiles();
    }

    @Override
    public List<FileEntity> userSearchForFile(String fileName) {
        return fileRepository.userSearchForFile(fileName);
    }

}
