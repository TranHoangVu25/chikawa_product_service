package com.example.product_service.dto;

import com.example.product_service.models.Product;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.data.annotation.Id;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BestSellersDTO {
    @Id
    String id;
    String name;
    String slug;
    String description;
    List<Product> products;
}
