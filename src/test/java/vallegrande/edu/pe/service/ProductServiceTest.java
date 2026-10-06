package vallegrande.edu.pe.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vallegrande.edu.pe.model.Product;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    private ProductService productService;

    @BeforeEach
    void setUp() {
        productService = new ProductService();
    }

    @Test
    @DisplayName("Debe retornar lista de 5 productos")
    void testGetProducts() {
        List<Product> products = productService.getProducts();
        assertNotNull(products);
        assertEquals(5, products.size());
    }

    @Test
    @DisplayName("Debe procesar producto cuando el nombre es válido")
    void testProcessProductValid() {
        String result = productService.processProduct("Laptop");
        assertEquals("Producto procesado: Laptop", result);
    }

    @Test
    @DisplayName("Debe responder producto vacío cuando el nombre es vacío")
    void testProcessProductEmpty() {
        String result = productService.processProduct("");
        assertEquals("Producto vacío", result);
    }

    @Test
    @DisplayName("Debe responder no válido cuando el nombre es null")
    void testProcessProductNull() {
        String result = productService.processProduct(null);
        assertEquals("Producto no válido", result);
    }

    @Test
    @DisplayName("Debe validar producto correctamente cuando cumple condiciones")
    void testValidateProductTrue() {
        Product product = new Product(1, "Teclado", 50.0);
        assertTrue(productService.validateProduct(product));
    }

    @Test
    @DisplayName("Debe retornar false si el producto es nulo o inválido")
    void testValidateProductFalse() {
        assertFalse(productService.validateProduct(null));
        assertFalse(productService.validateProduct(new Product(2, "", 50.0)));
        assertFalse(productService.validateProduct(new Product(3, "Mouse", -10.0)));
    }
}
