package com.routine.domain.f_product.repository;


import com.routine.domain.f_product.dto.ProductDTO;
import com.routine.domain.f_product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
   // List<Product> findByCategory(String category);
    List<Product> findByCategoryOrTitleContaining(@Param("category")String category,@Param("title") String title);
}
