package app.bambushain.models.bamboo;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Event
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event implements Serializable {
    private Integer id = 0;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Boolean isPrivate;
}

