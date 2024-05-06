package com.amalitechfileserver.fileserverbackend.repository;

import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends JpaRepository<FileEntity, String> {
}
