package com.routine.domain.f_product.dto;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public class UploadFileDTO {

    private List<MultipartFile> files;

    public List<MultipartFile> getFiles() {
        return files;
    }
    // files는 <input type="file" multiple> 태그에서 전송된 파일 담기


    public void setFiles(List<MultipartFile> files) {
        this.files = files;
    }
}
