package com.finances.budgetmanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ViewController {



    @GetMapping("/")
    public String home() {
        return "home";  // szuka pliku home.html w katalogu templates
    }

    @GetMapping("/transactions")
    public String transactions() {
        return "transactions"; // szuka pliku transactions.html w templates
    }

    @GetMapping("/categories")
    public String categories() {
        return "categories"; // szuka pliku categories.html w templates
    }

    @GetMapping("/reports")
    public String reports() {
        return "reports"; // szuka pliku reports.html w templates
    }

    @GetMapping("/login")
    public String loginPage() {
        return "login"; // powinna wskazywać na widok Thymeleaf login.html
    }

}
