package com.ERP.Softronix_ERP.model;

import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;

@Data@Entity
public class Receipt implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false)
    private Long id;
}
