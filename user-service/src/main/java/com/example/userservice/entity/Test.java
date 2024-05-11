package com.example.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
public class Test {

    @Id
    private Long id;
}
