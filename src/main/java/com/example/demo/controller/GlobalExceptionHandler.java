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
    private static final String GENERIC_ERROR_VIEW = "error/generic";
    private static final String NOT_FOUND_VIEW = "error/404";

    @ExceptionHandler(NoHandlerFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleNotFound(NoHandlerFoundException ex, Model model) {
        logger.error("404 error: Page not found - {}", ex.getRequestURL());
        model.addAttribute("errorMessage", "The page you're looking for doesn't exist.");
        return NOT_FOUND_VIEW;
    }

    @ExceptionHandler(BasketException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public String handleBasketException(BasketException ex, Model model) {
        logger.error("Basket error: {}", ex.getMessage());
        model.addAttribute("errorMessage", "We couldn't find your basket. Please try again.");
        return GENERIC_ERROR_VIEW;
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleDataIntegrityViolation(DataIntegrityViolationException ex, Model model) {
        logger.error("Data integrity violation: {}", ex.getMessage());
        model.addAttribute("errorMessage", "There was a problem with your request. Please try again.");
        return GENERIC_ERROR_VIEW;
    }

    @ExceptionHandler(BadSqlGrammarException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleBadSqlGrammar(BadSqlGrammarException ex, Model model) {
        logger.error("SQL error: {}", ex.getMessage());
        model.addAttribute("errorMessage", "There was a problem with our database. Please try again later.");
        return GENERIC_ERROR_VIEW;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleGenericException(Exception ex, Model model) {
        logger.error("Unexpected error occurred:", ex);
        logger.error("Error message: {}", ex.getMessage());
        logger.error("Error class: {}", ex.getClass().getName());
        if (ex.getCause() != null) {
            logger.error("Cause: {}", ex.getCause().getMessage());
        }
        model.addAttribute("errorMessage", "Something went wrong. Please try again later.");
        return GENERIC_ERROR_VIEW;
    }
}
