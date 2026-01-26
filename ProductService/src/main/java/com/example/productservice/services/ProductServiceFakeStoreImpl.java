package com.example.productservice.services;

import com.example.productservice.dtos.ProductRequestDto;
import com.example.productservice.dtos.ProductResponseDto;
import com.example.productservice.exceptions.ExternalServiceException;
import com.example.productservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service
@Profile("fakestore")
public class ProductServiceFakeStoreImpl implements ProductService{
    private final RestTemplate restTemplate;
    @Autowired
    ProductServiceFakeStoreImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long id) {
        try {
            ProductResponseDto productDto = restTemplate.getForObject(
                    "https://fakestoreapi.com/products/{id}",
                    ProductResponseDto.class, id
            );
            if (productDto == null) {
                return null;
            }
            return productDto.toProduct();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to fetch product from external service", e);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try {
            //Here we use ProductResponseDto[] class instead of List<ProductResponseDto> because RestTemplate doesn't support generic types directly
            ProductResponseDto[] productsDto = restTemplate.getForObject("https://fakestoreapi.com/products/",
                    ProductResponseDto[].class);
            List<Product> products = new ArrayList<>();
            if (productsDto == null) {
                return products;
            }
            for(ProductResponseDto productDto : productsDto){
                products.add(productDto.toProduct());
            }
            return products;
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to fetch products from external service", e);
        }
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        try {
            ProductRequestDto productRequestDto = new ProductRequestDto().fromProduct(product);
            String url = "https://fakestoreapi.com/products/" + id;
            ResponseEntity<ProductResponseDto> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PUT,
                            new HttpEntity<>(productRequestDto),
                            ProductResponseDto.class
                    );
            if(response.getBody() == null){
                return null;
            }
            return response.getBody().toProduct();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to replace product in external service", e);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            restTemplate.delete("https://fakestoreapi.com/products/{id}", id);
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to delete product from external service", e);
        }
    }

    @Override
    public Product addNewProduct(Product product) {
        try {
            ProductRequestDto fakeStoreProductDto = new ProductRequestDto().fromProduct(product);
            ProductResponseDto response = restTemplate.postForObject("https://fakestoreapi.com/products/", fakeStoreProductDto, ProductResponseDto.class);
            if (response == null) {
                return null;
            }
            return response.toProduct();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to create product in external service", e);
        }
    }

    @Override
    public Product updateProduct(Long id, Product product) {
        try {

            ProductRequestDto fakeStoreProductDto = new ProductRequestDto();
            if (product.getTitle() != null) {
                fakeStoreProductDto.setTitle(product.getTitle());
            }
            if (product.getDescription() != null) {
                fakeStoreProductDto.setDescription(product.getDescription());
            }
            if (product.getPrice() != null) {
                fakeStoreProductDto.setPrice(product.getPrice());
            }
            if (product.getImageUrl() != null) {
                fakeStoreProductDto.setImage(product.getImageUrl());
            }
            if (product.getCategory() != null && product.getCategory().getName() != null) {
                fakeStoreProductDto.setCategory(product.getCategory().getName());
            }

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<ProductRequestDto> entity =
                    new HttpEntity<>(fakeStoreProductDto, headers);

            ResponseEntity<ProductResponseDto> response =
                    restTemplate.exchange(
                            "https://fakestoreapi.com/products/{id}",
                            HttpMethod.PATCH,
                            entity,
                            ProductResponseDto.class,
                            id
                    );
            if(response.getBody() == null){
                return null;
            }
            return response.getBody().toProduct();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to update product in external service", e);
        }
    }
}
