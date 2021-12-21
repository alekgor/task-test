package com.alekgor.task.model.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EnergyLevel {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    @OneToOne
    @JoinColumn(name = "quote_isin")
    private Quote quote;
    private Double energyLevel;
}
