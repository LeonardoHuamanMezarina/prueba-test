package vallegrande.edu.pe.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vallegrande.edu.pe.model.Product;
import vallegrande.edu.pe.service.ProductService;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductControllerTest {

    private ProductController productController;

    @BeforeEach
    void setUp() {
        ProductService productService = new ProductService();
        productController = new ProductController(productService);
    }

    @Test
    @DisplayName("Debe obtener la lista de productos a través del controlador")
    void testGetProducts() {
        List<Product> products = productController.getProducts();
        assertNotNull(products);
        assertEquals(5, products.size());
    }
}
