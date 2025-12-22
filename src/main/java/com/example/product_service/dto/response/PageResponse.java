package com.example.product_service.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.List;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PageResponse<T> {

    private List<T> data;        // danh sách item
    private int page;            // page hiện tại (0-based)
    private int size;            // số phần tử / page
    private long totalElements;  // tổng số sản phẩm
    private int totalPages;      // tổng số page
    private boolean last;        // có phải page cuối không
}



