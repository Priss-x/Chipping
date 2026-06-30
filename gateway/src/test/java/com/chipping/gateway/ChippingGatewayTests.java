package com.chipping.gateway;

import com.chipping.gateway.security.JwtUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class ChippingGatewayTests {

    @Test
    @DisplayName("CP01 - Generación Exitosa de Token para Chipping")
    void testGenerarTokenExitoso() {

        String token = JwtUtil.generateToken("priscila_dev", "ADMIN");

        assertNotNull(token);
        assertFalse(token.isBlank());
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    @DisplayName("CP02 - Validación de Firma Criptográfica Legítima")
    void testValidarTokenCorrecto() {

        String token = JwtUtil.generateToken("adminChipping", "ADMIN");

        assertTrue(JwtUtil.validateToken(token));
    }

    @Test
    @DisplayName("CP03 - Rechazo Absoluto de Token Manipulado")
    void testRechazarTokenInvalido() {

        String tokenCorrupto = JwtUtil.generateToken("adminChipping", "ADMIN") + "token_corrompido";

        assertFalse(JwtUtil.validateToken(tokenCorrupto));
    }

    @Test
    @DisplayName("CP04 - Extracción Correcta del Usuario desde el Token")
    void testExtraerUsername() {

        String token = JwtUtil.generateToken("adminChipping", "ADMIN");

        assertEquals("adminChipping", JwtUtil.extractUsername(token));
    }

    @Test
    @DisplayName("CP05 - Extracción Correcta del Rol desde el Token")
    void testExtraerRol() {

        String token = JwtUtil.generateToken("clienteChipping", "CLIENTE");

        assertEquals("CLIENTE", JwtUtil.extractRole(token));
    }
}