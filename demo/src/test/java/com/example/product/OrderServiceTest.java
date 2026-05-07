package com.example.product;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    ProductRepository repository;

    @InjectMocks
    OrderService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateTotalPrice_NoDiscount() {

        Product product = new Product(1, "Laptop", 200);

        when(repository.findById(1)).thenReturn(product);

        double result = service.calculateTotalPrice(1, 2);

        assertEquals(400, result);

        verify(repository).findById(1);
    }

    @Test
    void testCalculateTotalPrice_WithDiscount() {

        Product product = new Product(1, "Laptop", 600);

        when(repository.findById(1)).thenReturn(product);

        double result = service.calculateTotalPrice(1, 2); // 1200 → discount

        assertEquals(1080, result);

        verify(repository).findById(1);
    }

    @Test
    void testCalculateTotalPrice_ProductNotFound() {

        when(repository.findById(1)).thenReturn(null);

        assertThrows(RuntimeException.class, () -> {
            service.calculateTotalPrice(1, 2);
        });

        verify(repository).findById(1);
    }

    @Test
    void testCalculateTotalPrice_WithZeroQuantity() {

        Product product = new Product(1, "Laptop", 200);

        when(repository.findById(1)).thenReturn(product);

        double result = service.calculateTotalPrice(1, 0);

        assertEquals(0, result);

        verify(repository).findById(1);
    }

    @Test
    void testCalculateTotalPrice_WithBoundaryCaseQuantity() {

        Product product = new Product(1, "Laptop", 500);

        when(repository.findById(1)).thenReturn(product);

        double result = service.calculateTotalPrice(1, 2);

        assertEquals(1000, result);

        verify(repository).findById(1);
    }

    @Test
    void testCalculateTotalPrice_WithNEGATIVEQuantity() {

        Product product = new Product(1, "Laptop", 200);

        when(repository.findById(1)).thenReturn(product);

        double result = service.calculateTotalPrice(1, -2);

        assertEquals(-400, result);

        verify(repository).findById(1);
    }
}