package com.ERP.Softronix_ERP.service;

import com.ERP.Softronix_ERP.model.Product;

import java.util.List;

public interface ProductService {
    public Product saveProduct(Product product);

    List<Product> getAllProduct();
}
