package com.alekgor.task.model.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;


@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class History {
    @Id
    @GeneratedValue
    @Column(name = "id", nullable = false)
    private Long id;
    private String isin;
    private Double bid;
    private Double ask;
    private Double energyLevel;
    private Date date;
}
