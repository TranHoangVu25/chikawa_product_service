package com.example.product_service.dto.request;

import com.example.product_service.Enums.Action;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ProductSendEvent implements Serializable {
     String id;
     String name;
     Double price;
     String status;
     List<String> categories;
     List<String> characters;
     List<String> images;
     Action action;
}

