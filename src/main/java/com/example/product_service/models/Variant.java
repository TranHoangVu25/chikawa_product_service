package com.example.product_service.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variant {
    private String name;    // Ví dụ: "Chiikawa"
    private String img;     // URL ảnh của biến thể
}
