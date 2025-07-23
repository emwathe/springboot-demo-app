package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ErrorSimulationController {

    @GetMapping("/api/simulate-error")
    public String simulateError() {
        throw new RuntimeException("Simulated application error for logging test.");
    }
}
