package com.ykrenz.file.upload.storage.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class UploadRequest {

    private MultipartFile file;

    private String crc;
}
