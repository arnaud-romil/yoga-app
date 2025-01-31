package com.openclassrooms.starterjwt.security.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;
import java.util.Date;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@SpringBootTest
class JwtUtilsTest {

    @Autowired
    private JwtUtils jwtUtils;

    private String token;

    @Value("${oc.app.jwtSecret}")
    private String jwtSecret;

    @BeforeEach
    void init() {
        token = Jwts.builder()
                .setSubject("yoga@studio.com")
                .setIssuedAt(Date.from(Instant.now()))
                .setExpiration(Date.from(Instant.now().plusSeconds(3600L)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
    }

    @Test
    void shouldGetUsernameFromJwtToken() {
        String username = jwtUtils.getUserNameFromJwtToken(token);
        assertEquals("yoga@studio.com", username);
    }

    @Test
    void shouldValidateJwtToken() {
        boolean result = jwtUtils.validateJwtToken(token);
        assertTrue(result);
    }

    @Test
    void shouldNotValidateExpiredJwtToken() {
        token = Jwts.builder()
                .setSubject("yoga@studio.com")
                .setIssuedAt(Date.from(Instant.now().minusSeconds(3600L)))
                .setExpiration(Date.from(Instant.now().minusSeconds(3600L)))
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact();
        boolean result = jwtUtils.validateJwtToken(token);
        assertFalse(result);
    }

    @Test
    void shouldNotValidateJwtTokenWithInvalidSignature() {
        boolean result = jwtUtils.validateJwtToken(token + "invalid");
        assertFalse(result);
    }

    @Test
    void shouldNotValidateJwtTokenWithNullValue() {
        boolean result = jwtUtils.validateJwtToken(null);
        assertFalse(result);
    }

    @Test
    void shouldNotValidateMalformedJwtToken() {
        boolean result = jwtUtils.validateJwtToken(token.substring(10));
        assertFalse(result);
    }

    @Test
    void shouldNotValidateUnsupportedJwtToken() {
        token = Jwts.builder()
                .setPayload("payload")
                .compact();
        boolean result = jwtUtils.validateJwtToken(token);
        assertFalse(result);
    }
}
