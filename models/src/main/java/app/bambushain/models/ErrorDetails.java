package app.bambushain.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * ErrorDetails
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorDetails {
    private String entityType;
    private ErrorType errorType;
    private String message;
}

