package com.routine.domain.f_product.service;


import com.routine.domain.f_product.dto.ProductDTO;
import com.routine.domain.f_product.model.Product;
import com.routine.domain.f_product.repository.CartItemRepository;
import com.routine.domain.f_product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log4j2
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final CartItemRepository cartItemRepository;


    //상품을 데이터베이스에 추가
    @Override
    public void insert(ProductDTO productDTO) {
        productRepository.save(dtoToEntity(productDTO));

    }

    //모든 상품을 조회하여 리스트로 반환
    @Override
    public List<ProductDTO>  list(String category, String title) {
        List<Product> products =null;
        if (category == null && title == null ) {
            products = productRepository.findAll();
        }else {
            products = productRepository.findByCategoryOrTitleContaining(category,title);
        }

        List<ProductDTO> productDTOList=products.stream()
                .map(product -> EntityToDto(product))
                .collect(Collectors.toList());

        return productDTOList;
    }

    //특정 상품을 ID로 조회하여 반환
    @Override
    public ProductDTO findById(Long id) {
        log.info("Attempting to fetch product with ID: {}", id);
        Product product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return null;
        }
        return EntityToDto(product);

    }

    //기존 상품 정보 업데이트
    @Override
    public void update(ProductDTO productDTO) {
        log.info("productDTO 값은 "+ productDTO);
        Product oldProduct = productRepository.findById(productDTO.getId()).orElse(null);
        oldProduct.setPrice(productDTO.getPrice());
        oldProduct.setDescription(productDTO.getDescription());
        oldProduct.setCategory(productDTO.getCategory());
        if (productDTO.getImagePath() != null && !productDTO.getImagePath().isEmpty()) {
            oldProduct.setImage(productDTO.getImagePath());
        }
        oldProduct.setTitle(productDTO.getTitle());
        productRepository.save(oldProduct);
    }

    //특정 상품을 ID로 삭제
    @Override
    public void delete(Long id) {
        productRepository.deleteById(id);

    }

    public boolean isProductDeletable(Long productId) {
        int count = cartItemRepository.countByProductId((productId));
        return count == 0;
    }
    public boolean deleteProductIfSafe(Long productId) {
        if (isProductDeletable(productId)) {
            productRepository.deleteById(productId);
            return true;
        } else {
            return false;
        }
    }
}
