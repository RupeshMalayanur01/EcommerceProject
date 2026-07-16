package com.example.productservice.services.sortingService;

import com.example.productservice.dtos.search.SortingCriteria;

public class SorterFactory {
    public static Sorter getSorterByCriteria(SortingCriteria sortingCriteria) {
        return switch (sortingCriteria) {
            case RELEVANCE -> null;
            case POPULARITY -> null;
            case PRICE_HIGH_TO_LOW -> new PriceHighToLowSorter();
            case PRICE_LOW_TO_HIGH -> new PriceLowToHighSorter();
            case RATING_HIGH_TO_LOW -> null;
            case RATING_LOW_TO_HIGH -> null;
            default -> null;
        };
    }
}
