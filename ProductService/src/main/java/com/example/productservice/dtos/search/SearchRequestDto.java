package com.example.productservice.dtos.search;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class SearchRequestDto {
    private String query;

    private List<FilterDto> filters;

    private SortingCriteria sortBy;

    private int pageNumber;

    private int pageSize;
}
