package com.example.productservice.services.filteringService;

import com.example.productservice.models.Product;

import java.util.ArrayList;
import java.util.List;

public class RAMFilter implements Filter {
    @Override
    public List<Product> apply(List<Product> products, List<String> allowedValues) {
        List<Product> ans = new ArrayList<>();

        for (Product product: products) {
            for (String ramValue : allowedValues) {
                if (product.getDescription().contains(ramValue)) {
                    ans.add(product);
                    break;
                }
            }
        }

        return ans;
    }
}
