package app.bambushain.models.exception;

import lombok.*;

import java.io.Serializable;

@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BambooException extends Exception implements Serializable {
    private ErrorType errorType;
    private String entityType;
    private String message;

    public BambooException(Throwable throwable) {
        errorType = ErrorType.Unknown;
        message = throwable.getMessage();
        entityType = "";
    }
}
