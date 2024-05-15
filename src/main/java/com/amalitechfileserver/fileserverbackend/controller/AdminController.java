package com.amalitechfileserver.fileserverbackend.controller;

import com.amalitechfileserver.fileserverbackend.exception.FileNotFound;
import com.amalitechfileserver.fileserverbackend.service.FileServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
//@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
@RequestMapping("/file")
@CrossOrigin(value = "http://localhost:5173")
public class AdminController {

    private final FileServerService fileServerService;

    @PostMapping("/upload")
//    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<String> uploadFile(
            @RequestParam(name = "file") MultipartFile file,
            @RequestParam(name = "title") String title,
            @RequestParam(name = "description") String description
    ) throws IOException {
        return ResponseEntity.ok(fileServerService.uploadFile(file, title, description));
    }

    @DeleteMapping("/delete/{fileId}")
//    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<String> deleteFileById(@PathVariable(name = "fileId") String fileId) throws FileNotFound {
        return ResponseEntity.ok(fileServerService.deleteFileById(fileId));
    }

}
