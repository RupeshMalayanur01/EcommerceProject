package com.example.productservice.services;

import com.example.productservice.dtos.FakeStoreProductRequestDto;
import com.example.productservice.dtos.FakeStoreProductResponseDto;
import com.example.productservice.exceptions.ExternalServiceException;
import com.example.productservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class ProductServiceFakeStoreImpl implements ProductService{
    private final RestTemplate restTemplate;
    @Autowired
    ProductServiceFakeStoreImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long id) {
        try {
            FakeStoreProductResponseDto productDto = restTemplate.getForObject(
                    "https://fakestoreapi.com/products/{id}",
                    FakeStoreProductResponseDto.class, id
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
            //Here we use FakeStoreProductResponseDto[] class instead of List<FakeStoreProductResponseDto> because RestTemplate doesn't support generic types directly
            FakeStoreProductResponseDto[] productsDto = restTemplate.getForObject("https://fakestoreapi.com/products/",
                    FakeStoreProductResponseDto[].class);
            List<Product> products = new ArrayList<>();
            if (productsDto == null) {
                return products;
            }
            for(FakeStoreProductResponseDto productDto : productsDto){
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
            FakeStoreProductRequestDto fakeStoreProductRequestDto = new FakeStoreProductRequestDto().fromProduct(product);
            String url = "https://fakestoreapi.com/products/" + id;
            ResponseEntity<FakeStoreProductResponseDto> response =
                    restTemplate.exchange(
                            url,
                            HttpMethod.PUT,
                            new HttpEntity<>(fakeStoreProductRequestDto),
                            FakeStoreProductResponseDto.class
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
            FakeStoreProductRequestDto fakeStoreProductDto = new FakeStoreProductRequestDto().fromProduct(product);
            FakeStoreProductResponseDto response = restTemplate.postForObject("https://fakestoreapi.com/products/", fakeStoreProductDto, FakeStoreProductResponseDto.class);
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

            FakeStoreProductRequestDto fakeStoreProductDto = new FakeStoreProductRequestDto();
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

            HttpEntity<FakeStoreProductRequestDto> entity =
                    new HttpEntity<>(fakeStoreProductDto, headers);

            ResponseEntity<FakeStoreProductResponseDto> response =
                    restTemplate.exchange(
                            "https://fakestoreapi.com/products/{id}",
                            HttpMethod.PATCH,
                            entity,
                            FakeStoreProductResponseDto.class,
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
