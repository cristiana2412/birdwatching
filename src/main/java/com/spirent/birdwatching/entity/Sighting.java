package com.spirent.birdwatching.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import java.time.LocalDateTime;

@Table("sighting")
@Data
@EqualsAndHashCode(callSuper = false)
public class Sighting extends BaseEntity {
    @Id
    private Long id;
    @NotBlank(message = "Bird ID must not be empty.")
    private Long birdId;
    @NotBlank(message = "Bird location must not be empty.")
    private String location;
    //    @Schema(description = "A date-time without a time-zone in the ISO-8601 calendar system.", format = "'YYYY-MM-DD', or 'YYYY-MM-DD hh:mm'", example = "2007-12-03T10:15:30")
    private LocalDateTime dateTime;
}
