package com.ERP.Softronix_ERP.service.Impl;

import com.ERP.Softronix_ERP.model.Product;
import com.ERP.Softronix_ERP.repository.ProductRepository;
import com.ERP.Softronix_ERP.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {
    @Autowired
    private ProductRepository productRepository;
    @Override
    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }
    @Override
    public List<Product> getAllProduct(){
        return productRepository.findAll();
    }

}
