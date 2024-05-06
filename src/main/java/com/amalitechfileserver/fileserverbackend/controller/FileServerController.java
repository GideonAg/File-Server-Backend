package com.amalitechfileserver.fileserverbackend.controller;

import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.service.FileServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileServerController {

    private final FileServerService fileServerService;

    @PostMapping("/share")
    public ResponseEntity<String> shareFile(@RequestBody FileShareDto fileShareDto) {
        return ResponseEntity.ok(fileServerService.shareFile(fileShareDto));
    }

    @PostMapping("/download")
    public void downloadFile(@RequestBody FileShareDto fileDownloadDto) {
        fileServerService.downloadFile(fileDownloadDto);
    }

}
