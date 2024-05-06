package com.amalitechfileserver.fileserverbackend.service;

import com.amalitechfileserver.fileserverbackend.dto.FileDto;
import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;

public interface FileServerService {
    String uploadFile(FileDto fileDto);

    String shareFile(FileShareDto fileShareDto);

    void downloadFile(FileShareDto fileDownloadDto);
}
