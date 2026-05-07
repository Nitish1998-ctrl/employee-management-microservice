package com.example.product;

public class OrderService{

    private ProductRepository repository;

    public OrderService(ProductRepository repository) {
        this.repository = repository;
    }

    public double calculateTotalPrice(int productId, int quantity) {

        Product product = repository.findById(productId);

        if (product == null) {
            throw new RuntimeException("Product not found");
        }

        double total = product.getPrice() * quantity;

        // Apply 10% discount if total > 1000
        if (total > 1000) {
            total = total * 0.9;
        }

        return total;
    }
}
