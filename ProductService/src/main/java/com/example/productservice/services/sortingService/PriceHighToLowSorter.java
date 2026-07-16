package com.example.productservice.services.sortingService;

import com.example.productservice.models.Product;

import java.util.ArrayList;
import java.util.List;

public class PriceHighToLowSorter implements Sorter {

    @Override
    public List<Product> sort(List<Product> products) {
        List<Product> sorted = new ArrayList<>(products);
        sorted.sort((p1, p2) -> p2.getPrice().compareTo(p1.getPrice()));
        return sorted;
    }
}
