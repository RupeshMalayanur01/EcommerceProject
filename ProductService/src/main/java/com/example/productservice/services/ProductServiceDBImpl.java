package com.example.productservice.services;

import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Category;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.CategoryRepository;
import com.example.productservice.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service("productServiceDBImpl")
public class ProductServiceDBImpl implements ProductService {
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    @Autowired
    public ProductServiceDBImpl(ProductRepository productRepository, CategoryRepository categoryRepository){
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
    }
    @Override
    public Product getSingleProduct(Long id) throws ProductNotExistsException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new ProductNotExistsException("Product with id: " + id + " doesn't exist.");
        }
        return productOptional.get();
    }

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product replaceProduct(Long id, Product product) throws ProductNotExistsException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new ProductNotExistsException("Product with id : " + id + " does not exist");
        }
        Optional<Category> categoryOptional = categoryRepository.findByName(product.getCategory().getName());
        if(categoryOptional.isEmpty()){
            categoryRepository.save(product.getCategory());
        }
        else{
            product.setCategory(categoryOptional.get());
        }
        //Set the ID to ensure we update the existing product instead of creating a new one
        product.setId(id);
        return productRepository.save(product);
    }

    @Override
    public void deleteProduct(Long id) {
        productRepository.deleteById(id);
    }

    @Override
    public Product addNewProduct(Product product) {
        Optional<Category> categoryOptional = categoryRepository.findByName(product.getCategory().getName());
        if(categoryOptional.isEmpty()){
            categoryRepository.save(product.getCategory());
        }
        else{
            //this line is needed if we skip the id in the request
            product.setCategory(categoryOptional.get());
        }
        return productRepository.save(product);
    }

    @Override
    public Product updateProduct(Long id, Product product) throws ProductNotExistsException {
        Optional<Product> productOptional = productRepository.findById(id);
        if(productOptional.isEmpty()){
            throw new ProductNotExistsException("Product with id : " + id + " does not exist");
        }
        Product originalProduct = productOptional.get();
        
        //Handle category update if provided
        if(product.getCategory() != null && product.getCategory().getName() != null) {
            Optional<Category> categoryOptional = categoryRepository.findByName(product.getCategory().getName());
            if(categoryOptional.isEmpty()){
                categoryRepository.save(product.getCategory());
                originalProduct.setCategory(product.getCategory());
            }
            else{
                originalProduct.setCategory(categoryOptional.get());
            }
        }
        
        if(product.getTitle() != null)
            originalProduct.setTitle(product.getTitle());
        if(product.getPrice() != null)
            originalProduct.setPrice(product.getPrice());
        if(product.getDescription() != null)
            originalProduct.setDescription(product.getDescription());
        if(product.getImageUrl() != null)
            originalProduct.setImageUrl(product.getImageUrl());
        return productRepository.save(originalProduct);
    }
}
