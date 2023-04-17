package com.ERP.Softronix_ERP.repository;

import com.ERP.Softronix_ERP.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {

}
