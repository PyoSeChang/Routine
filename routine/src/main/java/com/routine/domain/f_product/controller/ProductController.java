package com.routine.domain.f_product.controller;



import com.routine.domain.f_product.dto.ProductDTO;
import com.routine.domain.f_product.model.CartItem;
import com.routine.domain.f_product.model.Product;
import com.routine.domain.f_product.service.CartService;
import com.routine.domain.f_product.service.ProductService;

import com.routine.domain.f_product.service.ProductServiceImpl;
import jakarta.servlet.http.HttpSession;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Log4j2
@RequestMapping("/product")
@Controller
@RequiredArgsConstructor
public class ProductController {
    private final ProductServiceImpl productServiceImpl;
    @Value("${file.upload.path}")
    private String uploadDir;
    private final ProductService productService;



    //추가 폼
    @GetMapping("/register")
    public void insert() {
    }

    //추가
    @PostMapping("/register")
    public String insert(ProductDTO productDTO) {
        log.info("productDTO: " + productDTO);
        MultipartFile file = productDTO.getImage();

        String fileName ="";
        if (file != null && !file.isEmpty()) {
            try {
               // String uploadDir = "D:\\KSJ\\upload"; // 저장 경로

                String uuid = UUID.randomUUID().toString();

                String filePath = uploadDir + "/" + uuid+"_"+file.getOriginalFilename();

                // 파일 저장
                file.transferTo(new File(filePath));
                fileName = uuid+"_"+file.getOriginalFilename();
                System.out.println("파일 저장 성공: " + filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        Product product = new Product();
//        product.setId(productDTO.getId());
//        product.setTitle(productDTO.getTitle());
//        product.setPrice(productDTO.getPrice());
//        product.setDescription(productDTO.getDescription());
//        product.setTags(productDTO.getTags());
//        product.setImage(fileName);
        productDTO.setImagePath(fileName);


        // 실제 데이터 저장 (예: DB)
         productService.insert(productDTO);


        return "redirect:/product/list";
    }

    @GetMapping("/list")
    public void list(@RequestParam(required = false) String category,
                     @RequestParam(required = false) String title,
                     Model model) {

        List<ProductDTO> products=productService.list(category,title);



        model.addAttribute("products", products);


    }


    @GetMapping("/view/{filename}")
    public ResponseEntity<Resource> viewFileGet(
            @PathVariable("filename") String filename){
        Resource resource = new FileSystemResource(
                uploadDir+File.separator+filename);
        String resourceName = resource.getFilename();
        HttpHeaders headers = new HttpHeaders();
        try{
            headers.add("Content-Type",
                    Files.probeContentType(resource.getFile().toPath()));
        }catch (Exception e){
            return ResponseEntity.internalServerError().build();
        }
        return ResponseEntity.ok().headers(headers).body(resource);
    }

    @GetMapping("/detail/{id}")
    public String view(@PathVariable("id")Long id, Model model) {
        ProductDTO product=productService.findById(id);
        model.addAttribute("product", product);
        return "product/view";
    }
    //수정
    @GetMapping("/update/{id}")
    public String update(@PathVariable("id")Long id, Model model) {
        ProductDTO product=productService.findById(id);
        model.addAttribute("product", product);
        return "product/update";


    }
    @PostMapping("/update")
    public String update(ProductDTO productDTO) {
        MultipartFile file = productDTO.getImage();

        String fileName ="";
        if (file != null && !file.isEmpty()) {
            try {
                // String uploadDir = "D:\\KSJ\\upload"; // 저장 경로

                String uuid = UUID.randomUUID().toString();

                String filePath = uploadDir + "/" + uuid+"_"+file.getOriginalFilename();

                // 파일 저장
                file.transferTo(new File(filePath));
                fileName = uuid+"_"+file.getOriginalFilename();
                System.out.println("파일 저장 성공: " + filePath);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        productDTO.setImagePath(fileName);

        productService.update(productDTO);
        return "redirect:/product/list";
    }



    @PostMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        boolean deleted = productServiceImpl.deleteProductIfSafe(id);
        if (deleted) {
            redirectAttributes.addFlashAttribute("msg", "상품이 삭제되었습니다.");
        } else {
            redirectAttributes.addFlashAttribute("error", "해당 상품은 장바구니에 있어 삭제할 수 없습니다.");
        }
        return "redirect:/product/list";
    }


}
