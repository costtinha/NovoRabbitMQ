package com.example.order_service.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "orders_table")
public class Order {
    @Id
    @GeneratedValue
    private int orderId;

    private String description;

    private String price;

    private boolean confirmationStatus;

    public Order(int orderId, String description, String price, boolean confirmationStatus) {
        this.orderId = orderId;
        this.description = description;
        this.price = price;
        this.confirmationStatus = confirmationStatus;
    }

    public Order(String description, String price, boolean confirmationStatus) {
        this.description = description;
        this.price = price;
        this.confirmationStatus = confirmationStatus;
    }

    public Order() {
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isConfirmationStatus() {
        return confirmationStatus;
    }

    public void setConfirmationStatus(boolean confirmationStatus) {
        this.confirmationStatus = confirmationStatus;
    }
}
