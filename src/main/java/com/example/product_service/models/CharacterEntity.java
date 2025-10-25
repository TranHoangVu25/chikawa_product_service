package com.example.product_service.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterEntity {
    private String name;
    private String slug;
}
