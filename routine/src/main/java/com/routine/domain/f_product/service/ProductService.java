package com.routine.domain.f_product.service;

import com.routine.domain.f_product.dto.ProductDTO;
import com.routine.domain.f_product.model.Product;

import java.util.List;

public interface ProductService {


    //상품을 데이터베이스에 추가
    void insert (ProductDTO productDTO);

    //모든 상품을 조회하여 리스트로 반환
    public List<ProductDTO> list(String category,String title);

    //특정 상품을 ID로 조회하여 반환
    public ProductDTO findById(Long id);

    //기존 상품 정보 업데이트
    public void update(ProductDTO productDTO);

    //특정 상품을 ID로 삭제
    public void delete(Long id);

    //
    default Product dtoToEntity(ProductDTO dto) {
        Product product = Product.builder()
                .id(dto.getId())
                .category(dto.getCategory())
                .title(dto.getTitle())
                .description(dto.getDescription())
                .price(dto.getPrice())
                .tags(dto.getTags())
                .image(dto.getImagePath())
                .build();
        return product;
    }
    default ProductDTO EntityToDto(Product product){
        ProductDTO productDTO = ProductDTO.builder()
                .id(product.getId())
                .category(product.getCategory())
                .title(product.getTitle())
                .description(product.getDescription())
                .price(product.getPrice())
                .tags(product.getTags())
                .imagePath(product.getImage())
                .build();
        return productDTO;
    }






}
