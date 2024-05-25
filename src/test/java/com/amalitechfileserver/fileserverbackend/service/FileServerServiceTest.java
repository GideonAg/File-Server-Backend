package com.amalitechfileserver.fileserverbackend.service;

import com.amalitechfileserver.fileserverbackend.auth.SendMails;
import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.exception.FileNotFound;
import com.amalitechfileserver.fileserverbackend.repository.FileRepository;
import jakarta.mail.MessagingException;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.when;

@ExtendWith(value = MockitoExtension.class)
public class FileServerServiceTest {

    @Mock
    private FileRepository fileRepository;

    @Mock
    private SendMails sendMails;

    @InjectMocks
    private FileServerServiceImpl fileServerService;

    private FileEntity file;
    private FileEntity fileUpdatedDownload;

    @BeforeEach
    public void init() {
        file = FileEntity.builder()
                .title("Title of file")
                .description("Description of file")
                .file(new byte[0])
                .fileType("fileType")
                .build();

        fileUpdatedDownload = file = FileEntity.builder()
                .title("Title of file")
                .description("Description of file")
                .file(new byte[0])
                .fileType("fileType")
                .numberOfDownloads(1)
                .numberOfShares(0)
                .build();
    }

    @Test
    public void FileService_UploadFile_ReturnsString() throws Exception {

        when(fileRepository.save(Mockito.any(FileEntity.class))).thenReturn(file);

        MultipartFile multipartFile = Mockito.mock(MultipartFile.class);
        String response = fileServerService.uploadFile(multipartFile,
                "Title of file",
                "Description of file");

        Assertions.assertThat(response).isNotBlank();
    }

    @Test
    public void FileService_ShareFile_ReturnsString() throws MessagingException, FileNotFound {

        String fileId = UUID.randomUUID().toString();
        FileShareDto fileShareDto = FileShareDto.builder()
                        .receiverEmail("user@gmail.com")
                                .fileId(fileId)
                                        .build();

        when(fileRepository.findById(fileId)).thenReturn(Optional.of(file));

        String response = fileServerService.shareFile(fileShareDto);

        Assertions.assertThat(response).isNotBlank();
    }

    @Test
    public void FileService_DownloadFile_ReturnsString() throws FileNotFound {

        String fileId = UUID.randomUUID().toString();

        when(fileRepository.findById(fileId)).thenReturn(Optional.ofNullable(file));
        assert file != null;
        when(fileRepository.save(file)).thenReturn(fileUpdatedDownload);

        FileEntity downloadedFile = fileServerService.downloadFile(fileId);

        Assertions.assertThat(downloadedFile).isNotNull();
    }

    @Test
    public void FileService_GetAllFiles_ReturnsString() {
        List<FileEntity> userFiles = fileServerService.userGetAllFiles();
        List<FileEntity> adminFiles = fileServerService.adminGetAllFiles();

        Assertions.assertThat(userFiles.size()).isEqualTo(0);
        Assertions.assertThat(adminFiles.size()).isEqualTo(0);
    }

    @Test
    public void FileService_DeleteFileById_ReturnsString() throws FileNotFound {

        String fileId = UUID.randomUUID().toString();

        when(fileRepository.findById(fileId)).thenReturn(Optional.ofNullable(file));
        String response = fileServerService.deleteFileById(fileId);

        Assertions.assertThat(response.isBlank()).isEqualTo(false);
    }

    @Test
    public void FileService_SearchForFile_ReturnsString() {
        FileEntity file = FileEntity.builder()
                .title("Title of file")
                .description("Description of file")
                .build();

        List<FileEntity> adminFiles = fileServerService.adminSearchForFile(file.getTitle());
        List<FileEntity> userFiles = fileServerService.userSearchForFile(file.getTitle());

        Assertions.assertThat(adminFiles.size()).isEqualTo(0);
        Assertions.assertThat(userFiles.size()).isEqualTo(0);
    }
}
