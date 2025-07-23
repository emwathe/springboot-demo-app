package com.example.demo;

import com.example.demo.entity.Product;
import com.example.demo.repository.ProductRepository;
import com.example.demo.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    public ProductServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllProducts() {
        Product p1 = new Product(); p1.setId(1L); p1.setName("Pen"); p1.setPrice(2.5);
        Product p2 = new Product(); p2.setId(2L); p2.setName("Book"); p2.setPrice(10.0);
        when(productRepository.findAll()).thenReturn(Arrays.asList(p1, p2));
        List<Product> products = productService.getAllProducts();
        assertEquals(2, products.size());
    }

    @Test
    void testGetProductById() {
        Product p = new Product(); p.setId(1L); p.setName("Pen");
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));
        Optional<Product> found = productService.getProductById(1L);
        assertTrue(found.isPresent());
        assertEquals("Pen", found.get().getName());
    }

    @Test
    void testSaveProduct() {
        Product p = new Product(); p.setName("Pen");
        when(productRepository.save(p)).thenReturn(p);
        Product saved = productService.saveProduct(p);
        assertEquals("Pen", saved.getName());
    }

    @Test
    void testDeleteProduct() {
        productService.deleteProduct(1L);
        verify(productRepository, times(1)).deleteById(1L);
    }
}
