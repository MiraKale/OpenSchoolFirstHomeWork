package com.example.supplierservice.model;

import com.example.supplierservice.enums.Evaluation;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "review")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;
    private String comment;
    @Column(nullable = false)
    private Evaluation evaluation;
    @Column(nullable = false)
    private LocalDateTime timestamp;
}
