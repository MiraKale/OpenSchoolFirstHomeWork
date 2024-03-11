package com.example.supplierservice.model;

import lombok.*;

@Setter
@Getter
@Builder
@ToString
public class Category {
    private Long id;
    private String name;
}
