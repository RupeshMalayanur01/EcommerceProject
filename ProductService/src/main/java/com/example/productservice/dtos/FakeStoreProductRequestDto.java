package com.example.productservice.dtos;

import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class FakeStoreProductRequestDto {
    private String title;
    private Double price;
    private String image;
    private String description;
    private String category;

    public Product toProduct() {
        Product product = new Product();
        product.setTitle(this.title);
        product.setDescription(this.description);
        product.setImageUrl(this.image);
        product.setPrice(this.price);
        
        if (this.category != null) {
            Category category = new Category();
            category.setName(this.category);
            product.setCategory(category);
        }

        return product;
    }

    public FakeStoreProductRequestDto fromProduct(Product product) {
        this.title = product.getTitle();
        this.description = product.getDescription();
        this.image = product.getImageUrl();
        this.price = product.getPrice();

        if (product.getCategory() != null) {
            this.category = product.getCategory().getName();
        }

        return this;
    }
}
