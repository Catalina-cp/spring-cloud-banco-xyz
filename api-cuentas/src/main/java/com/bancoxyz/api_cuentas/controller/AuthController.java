package com.bancoxyz.api_cuentas.controller;

import com.bancoxyz.api_cuentas.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtUtil jwtUtil;

    private static final String API_KEY_VALIDA = "cuentas-secret-2026";

    @Autowired
    public AuthController(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/token")
    public ResponseEntity<Map<String, String>> generarToken(@RequestBody Map<String, String> body) {
        String apiKey = body.get("apiKey");

        if (apiKey == null || !apiKey.equals(API_KEY_VALIDA)) {
            return new ResponseEntity<>(Map.of("error", "API Key inválida"), HttpStatus.UNAUTHORIZED);
        }

        String token = jwtUtil.generarToken("api-cuentas-client");
        return new ResponseEntity<>(Map.of("token", token), HttpStatus.OK);
    }
}