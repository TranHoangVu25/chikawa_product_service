package com.example.product_service.models;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Variant {
    private String id;      // Ví dụ: "2240300049660"
    private String name;    // Ví dụ: "Chiikawa"
    private String img;     // URL ảnh của biến thể
}
