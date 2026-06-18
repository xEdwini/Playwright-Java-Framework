package com.saucedemo.model;

public record CheckoutCustomer(String firstName, String lastName, String postalCode) {
    public static CheckoutCustomer validUkCustomer() {
        return new CheckoutCustomer("John", "Smith", "SW1A 1AA");
    }
}
