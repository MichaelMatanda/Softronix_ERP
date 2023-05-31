package com.ERP.Softronix_ERP.repository;

import com.ERP.Softronix_ERP.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findUserByUsername(String username);
    User findUserByEmail(String email);
}
