package com.amalitechfileserver.fileserverbackend.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class FileShareDto {
    private String fileId;
    private String receiverEmail;
}
