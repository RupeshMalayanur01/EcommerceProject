package com.example.productservice.services.filteringService;

public class FilterFactory {
    public static Filter getFilterFromKey(String key) {
        return switch (key) {
            case "ram" -> new RAMFilter();
            case "brand" -> new BrandFilter();
            default -> null;
        };
    }
}
