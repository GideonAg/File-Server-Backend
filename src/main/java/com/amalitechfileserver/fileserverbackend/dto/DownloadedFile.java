package com.amalitechfileserver.fileserverbackend.dto;

import lombok.Builder;

@Builder
public record DownloadedFile(String filename, String fileType, byte[] fileByteArray) {
}
