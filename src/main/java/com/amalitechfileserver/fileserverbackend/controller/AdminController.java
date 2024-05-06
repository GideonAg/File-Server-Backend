package com.amalitechfileserver.fileserverbackend.controller;

import com.amalitechfileserver.fileserverbackend.dto.FileDto;
import com.amalitechfileserver.fileserverbackend.service.FileServerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class AdminController {

    private final FileServerService fileServerService;

    @PostMapping("/file/upload")
    @PreAuthorize("hasAuthority('admin:write')")
    public ResponseEntity<String> uploadFile(@RequestBody FileDto fileDto) {
        return ResponseEntity.ok(fileServerService.uploadFile(fileDto));
    }

}
