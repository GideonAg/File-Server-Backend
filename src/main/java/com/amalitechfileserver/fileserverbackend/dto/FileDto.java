package com.amalitechfileserver.fileserverbackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileDto {
    private String id;
    private String title;
    private String description;
    private String file;
}
