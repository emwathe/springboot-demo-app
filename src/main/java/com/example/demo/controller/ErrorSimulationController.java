package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.exception.SimulationException;

@RestController
public class ErrorSimulationController {

    @GetMapping("/api/simulate-error")
    public String simulateError() {
        throw new SimulationException("Simulated application error for logging test.");
    }
}
