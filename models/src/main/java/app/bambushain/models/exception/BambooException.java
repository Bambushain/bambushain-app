package app.bambushain.models.exception;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

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
