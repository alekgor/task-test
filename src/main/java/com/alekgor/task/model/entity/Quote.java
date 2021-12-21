package com.alekgor.task.model.entity;

import lombok.*;

import javax.persistence.*;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Quote {
    @Id
    @Column(name = "isin", nullable = false)
    private String isin;
    private Double bid;
    private Double ask;
}
