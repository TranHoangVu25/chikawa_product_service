package com.example.product_service.dto.request;

import java.util.List;

public class CartRequest {
    Integer user_id;
    List<CartItemRequest> cart_items;
}
