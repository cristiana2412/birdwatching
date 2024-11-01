package com.spirent.birdwatching.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.validator.constraints.Range;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDateTime;

@Table("sighting")
@Data
@EqualsAndHashCode(callSuper = false)
public class Sighting extends BaseEntity {
    @Id
    @Range(min = 1, message = "Sighting ID must be greater than 1.")
    @Schema(description = "Sighting's unique identifier.", example = "55540")
    private Long id;
    @Range(min = 1, message = "Bird ID must be greater than 1.")
    @Schema(description = "Bird's unique identifier.", example = "40000")
    private Long birdId;
    @NotBlank(message = "Bird location must not be empty.")
    @Size(min = 1, max = 80, message = "Field at path must be between {min} and {max} characters inclusive.")
    @Schema(description = "Bird's location.", example = "Wallkill River NWR--Liberty Marsh (NY)")
    private String location;
    @Schema(description = "A date-time without a time-zone in the ISO-8601 calendar system.", format = "'YYYY-MM-DD', or 'YYYY-MM-DD hh:mm'", example = "2007-12-03T10:15:30")
    private LocalDateTime dateTime;
}
