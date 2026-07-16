package com.example.productservice.dtos;

import com.example.productservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetProductDto {
    private Long id;
    private String title;
    private String description;
    private double price;
    private String imageUrl;

    public static GetProductDto from(Product product) {
        GetProductDto getProductResponseDto = new GetProductDto();
        getProductResponseDto.setId(product.getId());
        getProductResponseDto.setDescription(product.getDescription());
        getProductResponseDto.setImageUrl(product.getImageUrl());
        getProductResponseDto.setPrice(product.getPrice());
        getProductResponseDto.setTitle(product.getTitle());

        return getProductResponseDto;
    }
}
