package com.amalitechfileserver.fileserverbackend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class FileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    @Lob
    private byte[] file;

    @Column(nullable = false)
    private int numberOfDownloads = 0;

    @Column(nullable = false)
    private int numberOfShares = 0;

    @Column(nullable = false)
    private String fileType;
}
