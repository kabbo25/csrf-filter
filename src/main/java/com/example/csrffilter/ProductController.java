package com.example.csrffilter;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.logging.Logger;

@Controller
@RequestMapping("/product")
public class ProductController {
    private final Logger logger = Logger.getLogger(ProductController.class.getName());

    @GetMapping("/add")
    public String showForm() {
        return "main";
    }

    @PostMapping("/add")
    public String add (@RequestParam String name){
        logger.info("Adding Product"+name);
        return "main";
    }
}
