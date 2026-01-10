package com.financial.ledger.config;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RootController {

    /**
     * Root endpoint to stabilize Railway routing
     * Railway treats services with root handlers as complete web applications
     */
    @GetMapping("/")
    public String root() {
        return "Financial Ledger Backend Running";
    }
}