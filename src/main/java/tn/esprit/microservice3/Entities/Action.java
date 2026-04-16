package tn.esprit.microservice3.Entities;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Action {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomAct;

    private String categoryAct;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal prixAct;
}
