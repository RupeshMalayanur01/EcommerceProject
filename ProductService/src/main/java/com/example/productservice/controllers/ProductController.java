package com.example.productservice.controllers;

import com.example.productservice.dtos.FakeStoreProductRequestDto;
import com.example.productservice.dtos.FakeStoreProductResponseDto;
import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Product;
import com.example.productservice.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {
    private ProductService productService;

    @Autowired
    ProductController(@Qualifier("fakeStoreProductService") ProductService productService){
        this.productService = productService;
    }

    @GetMapping()
    public List<FakeStoreProductResponseDto> getAllProducts(){
        List<Product> products = productService.getAllProducts();
        List<FakeStoreProductResponseDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            productDtos.add(new FakeStoreProductResponseDto().fromProduct(product));
        }
        return productDtos;
    }

    @GetMapping("/{id}")
    public FakeStoreProductResponseDto getSingleProduct(@PathVariable("id") Long id) throws ProductNotExistsException {
        Product product = productService.getSingleProduct(id);
        if (product == null) {
            throw new ProductNotExistsException("Product with id " + id + " doesn't exist");
        }
        return new FakeStoreProductResponseDto().fromProduct(product);
    }

    @PostMapping()
    public FakeStoreProductResponseDto addNewProduct(@RequestBody FakeStoreProductRequestDto fakeStoreProductRequestDto){
        Product product = productService.addNewProduct(fakeStoreProductRequestDto.toProduct());
        if (product == null) {
            return null;
        }
        return new FakeStoreProductResponseDto().fromProduct(product);
    }

    @PatchMapping("/{id}")
    public FakeStoreProductResponseDto updateProduct(@PathVariable("id") Long id, @RequestBody FakeStoreProductRequestDto fakeStoreProductRequestDto) throws ProductNotExistsException {
        Product product = productService.updateProduct(id, fakeStoreProductRequestDto.toProduct());
        if (product == null) {
            throw new ProductNotExistsException("Product with id " + id + " doesn't exist");
        }
        return new FakeStoreProductResponseDto().fromProduct(product);
    }

    @PutMapping("/{id}")
    public FakeStoreProductResponseDto replaceProduct(@PathVariable("id") Long id, @RequestBody FakeStoreProductRequestDto fakeStoreProductRequestDto) throws ProductNotExistsException {
        Product product = productService.replaceProduct(id, fakeStoreProductRequestDto.toProduct());
        if (product == null) {
            throw new ProductNotExistsException("Product with id " + id + " doesn't exist");
        }
        return new FakeStoreProductResponseDto().fromProduct(product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable("id") Long id){
        productService.deleteProduct(id);
    }

}
