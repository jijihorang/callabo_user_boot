package com.myproject.callabo_user_boot.product.controller;

import com.myproject.callabo_user_boot.product.dto.ProductDetailDTO;
import com.myproject.callabo_user_boot.product.dto.ProductListDTO;
import com.myproject.callabo_user_boot.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api2/product")
@RequiredArgsConstructor
@Log4j2
public class ProductController {

    private final ProductService productService;

    // 상품 리스트
    @GetMapping("/list")
    public ResponseEntity<List<ProductListDTO>> getProductList(String creatorId){
        return ResponseEntity.ok(productService.getProductList(creatorId));
    }

    // 상품 상세 조회
    @GetMapping("/detail/{productNo}")
    public ResponseEntity<ProductDetailDTO> getProductDetail(@PathVariable("productNo") Long productNo){

        return ResponseEntity.ok(productService.readProductDetail(productNo));
    }
}
