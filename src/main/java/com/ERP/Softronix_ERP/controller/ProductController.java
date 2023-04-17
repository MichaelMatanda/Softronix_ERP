package com.ERP.Softronix_ERP.controller;

import com.ERP.Softronix_ERP.model.Product;
import com.ERP.Softronix_ERP.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@CrossOrigin
public class ProductController  {
    @Autowired
    private ProductService productService;

    @PostMapping("/add")
    public String add(@RequestBody Product product) {
        productService.saveProduct(product);
        return "New product is added";
    }
    @GetMapping("/getAll")
        public List<Product> getAllProduct(){
            return productService.getAllProduct();
        }
    }

