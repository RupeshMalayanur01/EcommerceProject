package com.example.productservice.dtos;

import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDto {
    private Long id;
    private String title;
    private Double price;
    private String image;
    private String description;
    private String category;

    public Product toProduct() {
        Product product = new Product();
        product.setId(this.id);
        product.setTitle(this.title);
        product.setDescription(this.description);
        product.setImageUrl(this.image);
        
        Category category = new Category();
        category.setName(this.category);
        product.setCategory(category);
        
        product.setPrice(this.price);

        return product;
    }

    public ProductResponseDto fromProduct(Product product) {
        this.id = product.getId();
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
