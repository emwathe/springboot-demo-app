package com.example.demo.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.jdbc.BadSqlGrammarException;
import com.example.demo.exception.BasketException;

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

    @ExceptionHandler(BasketException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBasketException(BasketException ex, Model model) {
        logger.error("Basket error: {}", ex.getMessage());
        model.addAttribute("errorMessage", "Basket error: " + ex.getMessage());
        return "error/generic";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex, Model model) {
        logger.error("Data integrity violation: {}", ex.getMessage());
        model.addAttribute("errorMessage", "Database error: " + ex.getMessage());
        return "error/generic";
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleBadSqlGrammar(BadSqlGrammarException ex, Model model) {
        logger.error("SQL error: {}", ex.getMessage());
        model.addAttribute("errorMessage", "Database error: Invalid SQL statement");
        return "error/generic";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        model.addAttribute("errorMessage", "An unexpected error occurred. Please try again later.");
        return "error/generic";
    }
}
