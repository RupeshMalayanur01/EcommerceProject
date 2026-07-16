package com.example.productservice.services.sortingService;

import com.example.productservice.models.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class PriceLowToHighSorter implements Sorter {

    @Override
    public List<Product> sort(List<Product> products) {
        List<Product> sorted = new ArrayList<>(products);
        Comparator<Product> priceAsc = (p1, p2) -> p1.getPrice().compareTo(p2.getPrice());
        sorted.sort(priceAsc);
        return sorted;
    }
}
