package com.routine.domain.f_product.dto;

import lombok.Builder;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
public class ProductDTO {
    private Long id;
    private String title;
    private String description;
    private int price;
    private String tags;

    // 업로드할 파일
    private MultipartFile image;
    // 이미지 이름 가져가기 위해서
    private String imagePath;
    // 카테고리 분류
    private String category;

}
