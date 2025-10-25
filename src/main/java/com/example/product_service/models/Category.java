package com.example.product_service.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Category {
    private String name;
    private String slug;
}
