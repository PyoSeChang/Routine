package com.routine.domain.f_product.controller;


import com.routine.domain.f_product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;


@Controller
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;


}
