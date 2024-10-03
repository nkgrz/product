package ru.buynest.product.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.buynest.product.api.request.SaveOrUpdateProductRequest;
import ru.buynest.product.api.response.ProductResponse;
import ru.buynest.product.service.ProductService;

import java.util.UUID;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody SaveOrUpdateProductRequest requestEntity) {
        ProductResponse productResponse = productService.createProduct(requestEntity);
        return new ResponseEntity<>(productResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{productId}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable UUID productId,
            @RequestBody SaveOrUpdateProductRequest requestEntity) {
        ProductResponse productResponse = productService.updateProduct(productId, requestEntity);
        return ResponseEntity.ok(productResponse);
    }

    @GetMapping("/{productId}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable UUID productId) {
        return ResponseEntity.ok(productService.getProductById(productId));
    }
}
