package com.spirent.birdwatching.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

@Table("bird")
@Data
@EqualsAndHashCode(callSuper = false)
public class Bird extends BaseEntity {
    @Id
    private Long id;
    @NotBlank(message = "Bird name must not be empty.")
    private String name;
    private String color;
    @Positive(message = "Weight must be positive.")
    private Double weight;
    @Positive(message = "Height must be positive.")
    private Double height;
}
