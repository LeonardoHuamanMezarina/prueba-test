package vallegrande.edu.pe.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import vallegrande.edu.pe.model.LoginRequest;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class AuthControllerTest {

    private AuthController authController;

    @BeforeEach
    void setUp() {
        authController = new AuthController();
    }

    @Test
    @DisplayName("Debe retornar éxito con credenciales válidas")
    void testLoginSuccess() {
        LoginRequest request = new LoginRequest();
        request.setUsername("admin");
        request.setPassword("123456");

        Map<String, Object> response = authController.login(request);

        assertNotNull(response);
        assertEquals(true, response.get("success"));
        assertEquals("Login correcto", response.get("message"));
        assertEquals("ABC123XYZ", response.get("token"));
    }

    @Test
    @DisplayName("Debe rechazar login con credenciales erróneas")
    void testLoginFailure() {
        LoginRequest request = new LoginRequest();
        request.setUsername("usuario");
        request.setPassword("wrongpass");

        Map<String, Object> response = authController.login(request);

        assertNotNull(response);
        assertEquals(false, response.get("success"));
        assertEquals("Credenciales incorrectas", response.get("message"));
        assertNull(response.get("token"));
    }
}
