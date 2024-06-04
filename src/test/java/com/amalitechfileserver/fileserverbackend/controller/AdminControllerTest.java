package com.amalitechfileserver.fileserverbackend.controller;

import com.amalitechfileserver.fileserverbackend.config.JwtService;
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
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Collections;
import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
public class AdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private FileServerService fileServerService;

    @MockBean
    private JwtService jwtService;

    @Test
    public void AdminController_UploadFile_ReturnString() throws Exception {

        String title = "FileName";
        String description = "Description";
        MockMultipartFile file = new MockMultipartFile(
                title, "file.txt", MediaType.TEXT_PLAIN_VALUE, "File content".getBytes()
        );

        String message = "File uploaded successfully";

        when(fileServerService
                .uploadFile(file, title, description)
        ).thenReturn(message);

        ResultActions response = mockMvc.perform(multipart("/file/upload")
                                .file("file", file.getBytes())
                                .param("title", title)
                                .param("description", description));

        response.andExpect(status().isCreated());
    }

    @Test
    public void AdminController_AdminSearchForFile_ReturnFiles() throws Exception {
        String fileName = "FileName";
        List<FileEntity> adminFiles = Collections.singletonList(mock(FileEntity.class));

        when(fileServerService
                .adminSearchForFile(fileName)
        ).thenReturn(adminFiles);

        ResultActions response = mockMvc.perform(get("/file/admin/search-for-file/" + fileName)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(adminFiles)));

        response.andExpect(status().isOk());
    }

    @Test
    public void AdminController_AdminGetAllFile_ReturnFiles() throws Exception {
        List<FileEntity> fileEntityList = Collections.singletonList(mock(FileEntity.class));

        when(fileServerService
                .adminGetAllFiles()
        ).thenReturn(fileEntityList);

        ResultActions response = mockMvc.perform(get("/file/admin/all-files")
                .contentType(MediaType.APPLICATION_JSON)
                .contentType(objectMapper.writeValueAsString(fileEntityList)));

        response.andExpect(status().isOk());
    }

    @Test
    public void AdminController_AdminDeleteFile_ReturnFiles() throws Exception {
        String fileId = "fileId";
        String message = "File deleted successfully";

        given(fileServerService
                .deleteFileById(fileId)
        ).willAnswer(invocationOnMock -> invocationOnMock.getArgument(0));

        ResultActions response = mockMvc.perform(delete("/file/delete/" + fileId)
                .contentType(MediaType.TEXT_PLAIN)
                .content(message));

        response.andExpect(status().isOk());
    }
}
