package com.amalitechfileserver.fileserverbackend.controller;

import com.amalitechfileserver.fileserverbackend.config.JwtService;
import com.amalitechfileserver.fileserverbackend.dto.DownloadedFile;
import com.amalitechfileserver.fileserverbackend.dto.FileShareDto;
import com.amalitechfileserver.fileserverbackend.entity.FileEntity;
import com.amalitechfileserver.fileserverbackend.service.FileServerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

@WebMvcTest(FileServerController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class FileControllerTest {

    @MockBean
    private FileServerService fileServerService;

    @MockBean
    private JwtService jwtService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void FileServer_ShareFile_ReturnString() throws Exception {
        FileShareDto fileShareDto = FileShareDto.builder()
                        .fileId("fileId")
                                .receiverEmail("user@gmail.com")
                                        .build();

        given(fileServerService
                .shareFile(fileShareDto)
        ).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        ResultActions response = mockMvc.perform(post("/file/share")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fileShareDto)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void FileServer_DownloadFile_ReturnString() throws Exception {
        String fileId = "fileId";
        DownloadedFile downloadedFile = DownloadedFile.builder()
                        .filename("fileName")
                                .fileType("image/jpeg")
                                        .fileByteArray(new byte[0])
                                                .build();

        when(fileServerService
                .downloadFile(fileId)
        ).thenReturn(downloadedFile);

        ResultActions response = mockMvc.perform(get("/file/download/" + fileId)
                .header("Content-Disposition", "attachment; filename=" + downloadedFile.filename())
                .contentType(downloadedFile.fileType())
                .content(downloadedFile.fileByteArray()));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void FileServer_GetAllFiles_ReturnString() throws Exception {
        List<FileEntity> fileEntityList = Collections.singletonList(mock(FileEntity.class));

        when(fileServerService
                .userGetAllFiles()
        ).thenReturn(fileEntityList);

        ResultActions response = mockMvc.perform(get("/file/user/all-files")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fileEntityList)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

    @Test
    public void FileServer_SearchForFile_ReturnString() throws Exception {
        List<FileEntity> fileEntityList = Collections.singletonList(mock(FileEntity.class));

        String fileName = "FileName";
        when(fileServerService
                .userSearchForFile(fileName)
        ).thenReturn(fileEntityList);

        ResultActions response = mockMvc.perform(get("/file/user/search-for-file/" + fileName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(fileEntityList)));

        response.andExpect(MockMvcResultMatchers.status().isOk());
    }

}
