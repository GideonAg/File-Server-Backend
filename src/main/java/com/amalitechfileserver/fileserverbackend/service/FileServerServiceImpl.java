package com.amalitechfileserver.fileserverbackend.service;

import com.amalitechfileserver.fileserverbackend.auth.SendMails;
import com.amalitechfileserver.fileserverbackend.dto.FileDto;
import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.exception.FileNotFound;
import com.amalitechfileserver.fileserverbackend.exception.InputBlank;
import com.amalitechfileserver.fileserverbackend.repository.FileRepository;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class FileServerServiceImpl implements FileServerService{

    private final FileRepository fileRepository;
    private final SendMails sendMails;

    @Override
    public String uploadFile(MultipartFile file, String title, String description) throws IOException {
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
    public String shareFile(FileShareDto fileShareDto) throws MessagingException, FileNotFound, InputBlank {
        FileEntity fetchedFile = fileRepository.findById(fileShareDto.getFileId())
                .orElseThrow(() -> new FileNotFound("File not found"));

        sendMails.sendFileShareEmail(fileShareDto, fetchedFile);
        return "File sent successfully";
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
    public List<FileDto> getAllFiles() {
        return fileRepository.findAll().stream().map( file ->
                FileDto.builder()
                        .id(file.getId())
                        .title(file.getTitle())
                        .description(file.getDescription())
                        .numberOfDownloads(file.getNumberOfDownloads())
                        .numberOfShares(file.getNumberOfShares())
                        .build()
        ).toList();
    }

    @Override
    public String deleteFileById(String fileId) throws FileNotFound {
        FileEntity file = fileRepository.findById(fileId)
                .orElseThrow(() -> new FileNotFound("Failed to delete file"));
        fileRepository.delete(file);
        return "File deleted successfully";
    }

    @Override
    public List<FileDto> searchForFile(String fileName) {
        return getAllFiles().stream()
                .filter(fileDto -> fileDto.getTitle().toLowerCase().contains(fileName.toLowerCase()))
                .toList();
    }

}
