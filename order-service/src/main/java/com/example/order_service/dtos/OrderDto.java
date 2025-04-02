package com.example.order_service.dtos;

public record OrderDto(String description, String price, boolean confirmationStatus) {
}
