package com.example.productservice.services;

import com.example.productservice.dtos.search.FilterDto;
import com.example.productservice.dtos.search.SortingCriteria;
import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Product;
import com.example.productservice.repositories.ProductRepository;
import com.example.productservice.services.filteringService.FilterFactory;
import com.example.productservice.services.sortingService.SorterFactory;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class SearchService {
    private ProductRepository productRepository;

    public SearchService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<Product> search(
            String query,
            List<FilterDto> filters,
            SortingCriteria sortingCriteria,
            int pageNumber, // 1    // 2
            int pageSize    // 5    // 5 -> (5 * (pageNumber - 1)) -> (pageNumber * pageSize) - 1
            //                           [5 -> 9]
    ) throws ProductNotExistsException {
        List<Product> products = productRepository
                .findByTitleContaining(query);

        for (FilterDto filterDto: filters) {
            products = FilterFactory.getFilterFromKey(
                    filterDto.getKey()
            ).apply(products, filterDto.getValues());
        }

        if(products.isEmpty()) {
            throw new ProductNotExistsException("No products found for the given search criteria.");
        }

        products = SorterFactory.getSorterByCriteria(sortingCriteria).sort(products);

        List<Product> productsOnPage = new ArrayList<>();

        for (int i = pageSize * (pageNumber - 1); i <= (pageSize * pageNumber) - 1; ++i) {
            if(i >= products.size()) break;
            productsOnPage.add(products.get(i));
        }

        Pageable pageable = PageRequest.of(pageNumber, pageSize); // Adjust page number and size as needed
        return new PageImpl<>(productsOnPage, pageable, products.size());
    }

    public Page<Product> simpleSearch(
            String query,
            Long categoryId,
            int pageNumber,
            int pageSize,
            String sortingAttribute
    ) {
        // select * from products
        // where title like "%query%"
        // and categoryID = {categoryId}
        // limit {pageSize} offset pageNumber * pageSize
        // orderBy sortingAttribute;
        Page<Product> products = productRepository
                .findAllByTitleContainingAndCategory_Id(
                        query, categoryId,
                        PageRequest.of(
                                pageNumber,
                                pageSize,
                                Sort.by(sortingAttribute).descending()));

        return products;
    }
}
