package com.amalitechfileserver.fileserverbackend.repository;

import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
public class FileRepositoryTest {

    @Autowired
    private FileRepository fileRepository;

    @Test
    public void FileRepository_Save_ReturnSavedFile() {

        FileEntity file = FileEntity.builder()
                .title("Title of file")
                .description("Description of file")
                .build();

        FileEntity savedFile = fileRepository.save(file);

        Assertions.assertThat(savedFile).isNotNull();
        Assertions.assertThat(savedFile.getId()).isNotBlank();
    }

    @Test
    public void FileRepository_FindById_ReturnFile() {

        FileEntity file = FileEntity.builder()
                .title("Title of file")
                .description("Description of file")
                .fileType("fileType")
                .file(new byte[0])
                .build();

        FileEntity savedFile = fileRepository.save(file);
        Optional<FileEntity> fetchedFile = fileRepository.findById(savedFile.getId());

        Assertions.assertThat(fetchedFile.isPresent()).isEqualTo(true);
    }

    @Test
    public void FileRepository_GetAllFiles_ReturnFileList() {

        FileEntity fileOne = FileEntity.builder()
                .title("Title of file")
                .description("Description of file")
                .file(new byte[0])
                .fileType("fileType")
                .build();

        FileEntity fileTwo = FileEntity.builder()
                .title("Title of file")
                .description("Description of file")
                .file(new byte[0])
                .fileType("fileTye")
                .build();

        fileRepository.save(fileOne);
        fileRepository.save(fileTwo);

        List<FileEntity> adminGetAllFiles = fileRepository.adminGetAllFiles();
        List<FileEntity> userGetAllFiles = fileRepository.userGetAllFiles();

        Assertions.assertThat(adminGetAllFiles.size()).isEqualTo(2);
        Assertions.assertThat(userGetAllFiles.size()).isEqualTo(2);
    }

    @Test
    public void FileRepository_SearchForFile_ReturnFiles() {

        FileEntity file = FileEntity.builder()
                .title("Title of file")
                .description("Description of file")
                .file(new byte[0])
                .fileType("fileType")
                .build();
        FileEntity savedFile = fileRepository.save(file);

        List<FileEntity> adminFiles = fileRepository.adminSearchForFile(savedFile.getTitle());
        List<FileEntity> userFiles = fileRepository.userSearchForFile(savedFile.getTitle());

        Assertions.assertThat(adminFiles.size()).isEqualTo(1);
        Assertions.assertThat(userFiles.size()).isEqualTo(1);
    }

}
