package com.example.productservice.services;

import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Product;

import java.util.List;

public interface ProductService {
    public Product getSingleProduct(Long id) throws ProductNotExistsException;
    public List<Product> getAllProducts();
    Product replaceProduct(Long id, Product product) throws ProductNotExistsException;

    public void deleteProduct(Long id);

    public Product addNewProduct(Product product);

    public Product updateProduct(Long id, Product product) throws ProductNotExistsException;
}
