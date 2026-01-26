package com.example.productservice.controllers;

import com.example.productservice.dtos.ProductRequestDto;
import com.example.productservice.dtos.ProductResponseDto;
import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Product;
import com.example.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;

    @Autowired
    ProductController(ProductService productService){
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<ProductResponseDto>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<ProductResponseDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(new ProductResponseDto().fromProduct(product));
        }
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponseDto> getSingleProduct(@PathVariable("id") Long id) throws ProductNotExistsException {
        Product product = productService.getSingleProduct(id);
        if (product == null) {
            throw new ProductNotExistsException("Product with id " + id + " doesn't exist");
        }
        return ResponseEntity.ok(new ProductResponseDto().fromProduct(product));
    }

    @PostMapping()
    public ResponseEntity<ProductResponseDto> addNewProduct(@RequestBody ProductRequestDto productRequestDto){
        Product product = productService.addNewProduct(productRequestDto.toProduct());
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new ProductResponseDto().fromProduct(product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ProductResponseDto> updateProduct(@PathVariable("id") Long id, @RequestBody ProductRequestDto productRequestDto) throws ProductNotExistsException {
        Product product = productService.updateProduct(id, productRequestDto.toProduct());
        if (product == null) {
            throw new ProductNotExistsException("Product with id " + id + " doesn't exist");
        }
        return ResponseEntity.ok(new ProductResponseDto().fromProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponseDto> replaceProduct(@PathVariable("id") Long id, @RequestBody ProductRequestDto productRequestDto) throws ProductNotExistsException {
        Product product = productService.replaceProduct(id, productRequestDto.toProduct());
        if (product == null) {
            throw new ProductNotExistsException("Product with id " + id + " doesn't exist");
        }
        return ResponseEntity.ok(new ProductResponseDto().fromProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
