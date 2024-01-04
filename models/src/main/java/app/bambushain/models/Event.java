package app.bambushain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * Event
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Event {
    private Integer id;
    private String title;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private String color;
    private Boolean isPrivate;
}

