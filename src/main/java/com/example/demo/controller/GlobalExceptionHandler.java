package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import com.example.demo.exception.SimulationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        logger.error("404 error: Page not found - {}", ex.getRequestURL());
        model.addAttribute("errorMessage", "The page you are looking for does not exist.");
        return "error/404";
    }

    @ExceptionHandler(SimulationException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleSimulationException(SimulationException ex, Model model) {
        logger.error("Simulation error occurred: {}", ex.getMessage());
        model.addAttribute("errorMessage", "A simulated error occurred: " + ex.getMessage());
        return "error/generic";
    }

}
