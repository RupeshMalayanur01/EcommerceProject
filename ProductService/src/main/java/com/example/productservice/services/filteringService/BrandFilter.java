package com.example.productservice.services.filteringService;

import com.example.productservice.models.Product;

import java.util.ArrayList;
import java.util.List;

public class BrandFilter implements Filter {
    @Override
    public List<Product> apply(List<Product> products, List<String> allowedValues) {
        List<Product> ans = new ArrayList<>();

        for (Product product: products) {
            for (String brandName : allowedValues) {
                if (product.getTitle().contains(brandName)) {
                    ans.add(product);
                    break;
                }
            }
        }

        return ans;
    }
}
