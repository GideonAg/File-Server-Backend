package com.amalitechfileserver.fileserverbackend.controller;

import com.amalitechfileserver.fileserverbackend.dto.DownloadedFile;
import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.exception.FileNotFound;
import com.amalitechfileserver.fileserverbackend.service.FileServerService;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/file")
@RequiredArgsConstructor
public class FileServerController {

    private final FileServerService fileServerService;

    @PostMapping("/share")
    public ResponseEntity<String> shareFile(@RequestBody @Valid FileShareDto fileShareDto)
            throws MessagingException, FileNotFound {
        return ResponseEntity.ok(fileServerService.shareFile(fileShareDto));
    }

    @GetMapping("/download/{id}")
    public ResponseEntity<?> downloadFile(@PathVariable(name = "id") String id) throws FileNotFound {
        DownloadedFile file = fileServerService.downloadFile(id);
        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=" + file.filename())
                .contentType(MediaType.valueOf(file.fileType())).body(file.fileByteArray());
    }

    @GetMapping("/user/all-files")
    public ResponseEntity<List<FileEntity>> getAllFiles() {
        return ResponseEntity.ok(fileServerService.userGetAllFiles());
    }

    @GetMapping("/user/search-for-file/{fileName}")
    public ResponseEntity<List<FileEntity>> searchForFile(@PathVariable String fileName) {
        return ResponseEntity.ok(fileServerService.userSearchForFile(fileName));
    }

}
