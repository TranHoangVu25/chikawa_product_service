package com.example.product_service.dto.request;

import com.example.product_service.Enums.Action;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductSearchEvent implements Serializable {
    private String id;
    private String name;
    private String description;
    private Double price;
    private List<String> categories;
    private List<String> characters;
    private Action action;
}

