package com.example.product_service.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variant {
    private String id;
    private String name;
    private String img;
}