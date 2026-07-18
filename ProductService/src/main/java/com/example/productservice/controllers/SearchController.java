package com.example.productservice.controllers;

import com.example.productservice.dtos.GetProductDto;
import com.example.productservice.dtos.search.FilterDto;
import com.example.productservice.dtos.search.SearchRequestDto;
import com.example.productservice.dtos.search.SearchResponseDto;
import com.example.productservice.dtos.search.SortingCriteria;
import com.example.productservice.exceptions.ProductNotExistsException;
import com.example.productservice.models.Product;
import com.example.productservice.services.SearchService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/search")
public class SearchController {
    private SearchService searchService;

    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @PostMapping("/")
    public SearchResponseDto search(@RequestBody SearchRequestDto searchRequest) throws ProductNotExistsException {
        SearchResponseDto response = new SearchResponseDto();
        Page<Product> productsPage =  searchService.search(
                searchRequest.getQuery(), searchRequest.getFilters(), searchRequest.getSortBy(), searchRequest.getPageNumber(), searchRequest.getPageSize());

        List<GetProductDto> getProductDtos = productsPage.getContent().stream()
                .map(GetProductDto::from)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(productsPage.getNumber(), productsPage.getSize(), productsPage.getSort());
        Page<GetProductDto> getProductDtoPage = new PageImpl<>(getProductDtos, pageable, productsPage.getTotalElements());

        response.setProductsPage(getProductDtoPage);

        return response;
    }

    @GetMapping("/byCategory")
    public SearchResponseDto simpleSearch(@RequestParam("query") String query,
                                          @RequestParam("category") Long categoryId,
                                          @RequestParam("pageNumber") int pageNumber, //Spring Data page numbers start at 0.
                                          @RequestParam("pageSize") int pageSize,
                                          @RequestParam("sortBy") String sortingAttribute
    ) throws ProductNotExistsException {

        SearchResponseDto response = new SearchResponseDto();
        Page<Product> productsPage =  searchService.simpleSearch(query, categoryId, pageNumber, pageSize, sortingAttribute);
        List<GetProductDto> getProductDtos = productsPage.getContent().stream()
                .map(GetProductDto::from)
                .collect(Collectors.toList());

        Pageable pageable = PageRequest.of(productsPage.getNumber(), productsPage.getSize(), productsPage.getSort());
        Page<GetProductDto> getProductDtoPage = new PageImpl<>(getProductDtos, pageable, productsPage.getTotalElements());

        response.setProductsPage(getProductDtoPage);
        return response;
    }
}
