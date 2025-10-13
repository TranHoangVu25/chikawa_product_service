package com.example.product_service.dto.request;

import com.example.product_service.models.Category;
import com.example.product_service.models.CharacterEntity;
import com.example.product_service.models.Variant;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProductRequest {
    private String url;

    private String name;

    private String description;

    private Double price;

    private String vendor;

    private List<String> images;

    private List<Variant> variants;

    private List<Category> categories;

    private List<CharacterEntity> characters;
}
