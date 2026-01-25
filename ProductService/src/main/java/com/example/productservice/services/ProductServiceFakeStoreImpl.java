package com.example.productservice.services;

import com.example.productservice.dtos.FakeStoreProductRequestDto;
import com.example.productservice.dtos.FakeStoreProductResponseDto;
import com.example.productservice.exceptions.ExternalServiceException;
import com.example.productservice.models.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.RequestCallback;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Service("fakeStoreProductService")
public class ProductServiceFakeStoreImpl implements ProductService{
    private RestTemplate restTemplate;
    @Autowired
    ProductServiceFakeStoreImpl(RestTemplate restTemplate){
        this.restTemplate = restTemplate;
    }

    @Override
    public Product getSingleProduct(Long id) {
        try {
            FakeStoreProductResponseDto productDto = restTemplate.getForObject(
                    "https://fakestoreapi.com/products/" + id,
                    FakeStoreProductResponseDto.class
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
            FakeStoreProductResponseDto[] responsedto = restTemplate.getForObject("https://fakestoreapi.com/products/",
                    FakeStoreProductResponseDto[].class);
            List<Product> response = new ArrayList<>();
            if (responsedto == null) {
                return response;
            }
            for(FakeStoreProductResponseDto productdto : responsedto){
                response.add(productdto.toProduct());
            }
            return response;
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to fetch products from external service", e);
        }
    }

    @Override
    public Product replaceProduct(Long id, Product product) {
        try {
            FakeStoreProductRequestDto fakeStoreProductDto = new FakeStoreProductRequestDto();
            fakeStoreProductDto.setTitle(product.getTitle());
            fakeStoreProductDto.setDescription(product.getDescription());
            fakeStoreProductDto.setPrice(product.getPrice());
            fakeStoreProductDto.setImage(product.getImageUrl());
            if (product.getCategory() != null) {
                fakeStoreProductDto.setCategory(product.getCategory().getName());
            }

            RequestCallback requestCallback = restTemplate.httpEntityCallback(fakeStoreProductDto, FakeStoreProductRequestDto.class);
            HttpMessageConverterExtractor<FakeStoreProductResponseDto> responseExtractor = new HttpMessageConverterExtractor<>(FakeStoreProductResponseDto.class, restTemplate.getMessageConverters());
            FakeStoreProductResponseDto response = restTemplate.execute("https://fakestoreapi.com/products/" + id, HttpMethod.PUT, requestCallback, responseExtractor);

            if (response == null) {
                return null;
            }
            return response.toProduct();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to replace product in external service", e);
        }
    }

    @Override
    public void deleteProduct(Long id) {
        try {
            restTemplate.delete("https://fakestoreapi.com/products/" + id);
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to delete product from external service", e);
        }
    }

    @Override
    public Product addNewProduct(Product product) {
        try {
            FakeStoreProductRequestDto fakeStoreProductDto = new FakeStoreProductRequestDto();
            fakeStoreProductDto.setTitle(product.getTitle());
            fakeStoreProductDto.setDescription(product.getDescription());
            fakeStoreProductDto.setPrice(product.getPrice());
            fakeStoreProductDto.setImage(product.getImageUrl());
            if (product.getCategory() != null) {
                fakeStoreProductDto.setCategory(product.getCategory().getName());
            }
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
            // Convert Product to RequestDto (only non-null fields will be sent)
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

            RequestCallback requestCallback = restTemplate.httpEntityCallback(fakeStoreProductDto, FakeStoreProductRequestDto.class);
            HttpMessageConverterExtractor<FakeStoreProductResponseDto> responseExtractor = new HttpMessageConverterExtractor<>(FakeStoreProductResponseDto.class, restTemplate.getMessageConverters());
            FakeStoreProductResponseDto response = restTemplate.execute("https://fakestoreapi.com/products/" + id, HttpMethod.PATCH, requestCallback, responseExtractor);

            if (response == null) {
                return null;
            }
            return response.toProduct();
        } catch (RestClientException e) {
            throw new ExternalServiceException("Failed to update product in external service", e);
        }
    }
}
