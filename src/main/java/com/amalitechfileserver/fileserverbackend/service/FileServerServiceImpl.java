package com.amalitechfileserver.fileserverbackend.service;

import com.amalitechfileserver.fileserverbackend.auth.SendMails;
import com.amalitechfileserver.fileserverbackend.dto.FileDto;
import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.repository.FileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class FileServerServiceImpl implements FileServerService{

    private final FileRepository fileRepository;
    private final SendMails sendMails;

    @Override
    public String uploadFile(FileDto fileDto) {
        FileEntity file = FileEntity.builder()
                .title(fileDto.getTitle())
                .description(fileDto.getDescription())
                .file(fileDto.getFile())
                .build();
        fileRepository.save(file);
        return "File uploaded successfully";
    }

    @Override
    public String shareFile(FileShareDto fileShareDto) {
        Optional<FileEntity> fetchedFile = fileRepository.findById(fileShareDto.getFileId());
        if (fetchedFile.isPresent()) {
            sendMails.sendFileShareEmail(fileShareDto, fetchedFile.get());
            return "File sent successfully";
        }

        return "Failed to send file";
    }

    @Override
    public void downloadFile(FileShareDto fileDownloadDto) {
        Optional<FileEntity> fetchedFile = fileRepository.findById(fileDownloadDto.getFileId());
        fetchedFile.ifPresent(file -> {
            int numberOfDownloads = file.getNumberOfDownloads() + 1;
            file.setNumberOfDownloads(numberOfDownloads);
            fileRepository.save(file);
        });
    }

}
