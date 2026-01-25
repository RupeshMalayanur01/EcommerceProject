package com.example.productservice.controllers;

import com.example.productservice.dtos.FakeStoreProductRequestDto;
import com.example.productservice.dtos.FakeStoreProductResponseDto;
import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Product;
import com.example.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
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
    ProductController(@Qualifier("fakeStoreProductService") ProductService productService){
        this.productService = productService;
    }

    @GetMapping()
    public ResponseEntity<List<FakeStoreProductResponseDto>> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<FakeStoreProductResponseDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(new FakeStoreProductResponseDto().fromProduct(product));
        }
        return ResponseEntity.ok(productDtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FakeStoreProductResponseDto> getSingleProduct(@PathVariable("id") Long id) throws ProductNotExistsException {
        Product product = productService.getSingleProduct(id);
        if (product == null) {
            throw new ProductNotExistsException("Product with id " + id + " doesn't exist");
        }
        return ResponseEntity.ok(new FakeStoreProductResponseDto().fromProduct(product));
    }

    @PostMapping()
    public ResponseEntity<FakeStoreProductResponseDto> addNewProduct(@RequestBody FakeStoreProductRequestDto fakeStoreProductRequestDto){
        Product product = productService.addNewProduct(fakeStoreProductRequestDto.toProduct());
        if (product == null) {
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(new FakeStoreProductResponseDto().fromProduct(product));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<FakeStoreProductResponseDto> updateProduct(@PathVariable("id") Long id, @RequestBody FakeStoreProductRequestDto fakeStoreProductRequestDto) throws ProductNotExistsException {
        Product product = productService.updateProduct(id, fakeStoreProductRequestDto.toProduct());
        if (product == null) {
            throw new ProductNotExistsException("Product with id " + id + " doesn't exist");
        }
        return ResponseEntity.ok(new FakeStoreProductResponseDto().fromProduct(product));
    }

    @PutMapping("/{id}")
    public ResponseEntity<FakeStoreProductResponseDto> replaceProduct(@PathVariable("id") Long id, @RequestBody FakeStoreProductRequestDto fakeStoreProductRequestDto) throws ProductNotExistsException {
        Product product = productService.replaceProduct(id, fakeStoreProductRequestDto.toProduct());
        if (product == null) {
            throw new ProductNotExistsException("Product with id " + id + " doesn't exist");
        }
        return ResponseEntity.ok(new FakeStoreProductResponseDto().fromProduct(product));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
        return ResponseEntity.ok().build();
    }

}
